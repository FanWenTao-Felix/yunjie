<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.airbusiness_delete" @click="handleDelete">删 除
      </el-button>
    </template>
  </avue-crud>
</basic-container>
</template>

<script>
import {
  getSubList,
  getDetail,
  add,
  update,
  remove
} from "@/api/business/airbusiness";
import {
  mapGetters
} from "vuex";

export default {
  data() {
    return {
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      option: {
        align: "center",
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        viewBtn: true,
        column: []
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(this.permission.airbusiness_add, false),
        viewBtn: this.vaildData(true, false),
        delBtn: this.vaildData(this.permission.airbusiness_delete, false),
        editBtn: this.vaildData(this.permission.airbusiness_edit, false)
      };
    },
    ids() {
      let ids = [];
      this.selectionList.forEach(ele => {
        ids.push(ele.id);
      });
      return ids.join(",");
    }
  },
  created() {
    for (let item of this.column) {
      item.hide = false;
      item.formslot = false;
      for (let permission of this.mainOrderPermission) {
        if (item.prop == permission) {
          item.viewDisplay = false;
          item.addDisplay = false;
          item.editDisplay = false;
          item.hide = true;
        }
      }
      item.search = false;
    }
    this.$set(this.option, "column", this.column);
  },
  watch: {
    main_order_no: function(o, n) {
      this.refreshPage();
    }
  },
  props: ["main_order_no", "column", "mainOrderPermission"],
  methods: {
    rowSave(row, loading, done) {
      add(row).then(() => {
        loading();
        this.onLoad(this.page);
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      }, error => {
        done();
        console.log(error);
      });
    },
    rowUpdate(row, index, loading, done) {
      update(row).then(() => {
        loading();
        this.onLoad(this.page);
        this.$message({
          type: "success",
          message: "操作成功!"
        });
      }, error => {
        done();
        console.log(error);
      });
    },
    rowDel(row) {
      this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return remove(row.id);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
        });
    },
    handleDelete() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return remove(this.ids);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.$refs.crud.toggleSelection();
        });
    },
    beforeOpen(done, type) {
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.id).then(res => {
          this.form = res.data.data;
        });
      }
      done();
    },
    searchReset() {
      this.query = {};
      this.onLoad(this.page);
    },
    searchChange(params) {
      this.query = params;
      this.onLoad(this.page, params);
    },
    selectionChange(list) {
      this.selectionList = list;
    },
    selectionClear() {
      this.selectionList = [];
      this.$refs.crud.toggleSelection();
    },
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    onLoad(page, params = {}) {
      this.loading = true;
      getSubList(page.currentPage, page.pageSize, this.main_order_no, false, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        this.loading = false;
        this.selectionClear();
      });
    }
  }
};
</script>

<style>
</style>
