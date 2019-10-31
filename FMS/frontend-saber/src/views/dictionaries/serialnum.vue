<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.serialnum_delete" @click="handleDelete">删 除
      </el-button>
    </template>
  </avue-crud>
</basic-container>
</template>

<script>
import {
  getList,
  getDetail,
  add,
  update,
  remove
} from "@/api/dictionaries/serialnum";
import {
  mapGetters
} from "vuex";
const operationList = [{
  label: "未确认",
  value: 0
}, {
  label: "已确认",
  value: 1
}]
export default {
  data() {
    return {
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 3,
        currentPage: 1,
        total: 0,
        pageSizes: [3],
      },
      selectionList: [],
      option: {
        tip: false,
        border: true,
        viewBtn: true,
        align: "center",
        columnBtn: false,
        menu: false,
        column: [{
            label: "前缀",
            prop: "prefix",
            rules: [{
              required: true,
              message: "请输入前缀",
              trigger: "blur"
            }]
          },
          {
            label: "四位年份",
            prop: "fourDigitYear",
            rules: [{
              required: false,
              message: "请输入四位年份",
              trigger: "blur"
            }]
          },
          {
            label: "两位年份",
            prop: "twoDigitYear",
            rules: [{
              required: false,
              message: "请输入两位年份",
              trigger: "blur"
            }]
          },
          {
            label: "一位年份",
            prop: "oneDigitYear",
            rules: [{
              required: false,
              message: "请输入一位年份",
              trigger: "blur"
            }]
          },
          {
            label: "月份",
            prop: "month",
            rules: [{
              required: false,
              message: "请输入月份",
              trigger: "blur"
            }]
          },
          {
            label: "日期",
            prop: "day",
            rules: [{
              required: false,
              message: "请输入日期",
              trigger: "blur"
            }]
          },
          {
            label: "当前值",
            prop: "currentValue",
            rules: [{
              required: true,
              message: "请输入当前值",
              trigger: "blur"
            }]
          },
          {
            label: "归零值",
            prop: "zeroingValue",
            rules: [{
              required: true,
              message: "请输入归零值",
              trigger: "blur"
            }]
          },
          {
            label: "操作",
            prop: "operation",
            type: "select",
            dicData: operationList,
            disabled: true,
            rules: [{
              required: false,
              message: "请输入操作",
              trigger: "blur"
            }]
          },
        ]
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(this.permission.serialnum_add, false),
        viewBtn: this.vaildData(this.permission.serialnum_view, false),
        delBtn: this.vaildData(this.permission.serialnum_delete, false),
        editBtn: this.vaildData(this.permission.serialnum_edit, false)
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
      getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
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
