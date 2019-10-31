<template>
<basic-container>
  <avue-crud :option="option" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange" @search-reset="searchReset"
    @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.clientlinkman_delete" @click="handleDelete">删 除
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
} from "@/api/linkman/clientlinkman";
import {
  mapGetters
} from "vuex";

export default {
  data() {
    return {
      form: {},
      query: {},
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      option: {
        tip: false,
        border: true,
        viewBtn: true,
        dialogClickModal: false,
        align: "center",
        column: [{
            label: "cid",
            prop: "clientDataId",
            disabled: true,
          }, {
            label: "类型",
            prop: "type",
            type: "select",
            dicData: [{
              label: "发货人",
              value: 0
            }, {
              label: "收货人",
              value: 1
            }, {
              label: "通知人",
              value: 2
            }],
            rules: [{
              required: true,
              message: "请选择类型",
              trigger: "blur"
            }]
          }, {
            label: "联系人",
            prop: "linkman",
            rules: [{
              required: true,
              message: "请输入联系人",
              trigger: "blur"
            }]
          },
          {
            label: "地址",
            prop: "address",
            rules: [{
              required: true,
              message: "请输入地址",
              trigger: "blur"
            }]
          },
          {
            label: "联系电话",
            prop: "phone",
            rules: [{
              required: true,
              message: "请输入联系电话",
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
        addBtn: this.vaildData(true, false),
        viewBtn: this.vaildData(true, false),
        delBtn: this.vaildData(true, false),
        editBtn: this.vaildData(true, false)
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
  watch: {
    clientdata_id: function(n, o) {
      this.refreshPage();
    }
  },

  props: ["clientdata_id"],
  methods: {
    refreshPage() {
      this.$set(this, "page", {
        pageSize: 10,
        currentPage: 1,
        total: 0
      })
      this.onLoad(this.page);
    },
    rowSave(row, loading, done) {
      row.clientDataId = this.clientdata_id;
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
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    onLoad(page, params = {}) {
      getList(page.currentPage, page.pageSize, this.clientdata_id, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
      });
    }
  }
};
</script>

<style>
</style>
