<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" ref="crud" v-model="form" :page="page" :permission="permissionList" @row-del="rowDel" @row-update="rowUpdate" @row-save="rowSave" @search-change="searchChange" @search-reset="searchReset"
    @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" v-if="permission.tenant_delete" plain @click="handleDelete">删 除
      </el-button>
    </template>
  </avue-crud>
</basic-container>
</template>

<script>
import {
  getList,
  remove,
  update,
  add
} from "@/api/system/tenant";
import {
  mapGetters
} from "vuex";

export default {
  data() {
    return {
      form: {},
      selectionList: [],
      query: {},
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      option: {
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        index: true,
        selection: true,
        viewBtn: true,
        dialogTop: 10,
        align: "center",
        dialogWidth: "80%",
        dialogHeight: 700,
        labelWidth: 150,
        column: [{
            label: "租户ID",
            prop: "tenantId",
            search: true,
            addDisplay: false,
            editDisplay: false,
            span: 12,
            rules: [{
              required: true,
              message: "请输入租户ID",
              trigger: "blur"
            }]
          },
          {
            label: "租户名称",
            prop: "tenantName",
            width: 150,
            search: true,
            span: 12,
            rules: [{
              required: true,
              message: "请输入参数名称",
              trigger: "blur"
            }]
          }, {
            label: "公司中文名",
            prop: "companyChineseName",
            type: "textarea",
            width: 250,
            search: true,
            span: 12,
            rules: [{
              required: true,
              message: "请输入公司中文名",
              trigger: "blur"
            }]
          }, {
            label: "公司英文名",
            prop: "companyEnglishName",
            width: 250,
            search: true,
            span: 12,
            rules: [{
              required: true,
              message: "请输入公司英文名",
              trigger: "blur"
            }]
          },
          {
            label: "联系电话",
            prop: "contactNumber",
            span: 12,
          },
          {
            label: "联系地址",
            prop: "address",
            width: 250,
            span: 12,
            minRows: 6,
            type: "textarea",
          }, {
            label: "传真机号码",
            prop: "fax",
            width: 150,
            span: 12,
          }, {
            label: "email",
            prop: "email",
            width: 150,
            span: 12,
          }, {
            label: "联系人",
            prop: "linkman",
            search: true,
            span: 12,
            rules: [{
              required: false,
              message: "请输入联系人",
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
        addBtn: this.vaildData(this.permission.tenant_add, false),
        viewBtn: this.vaildData(this.permission.tenant_view, false),
        delBtn: this.vaildData(this.permission.tenant_delete, false),
        editBtn: this.vaildData(this.permission.tenant_edit, false)
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
