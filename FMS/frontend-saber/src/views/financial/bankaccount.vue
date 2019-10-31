<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.bankaccount_delete" @click="handleDelete">删 除
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
} from "@/api/financial/bankaccount";
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
        labelWidth: "120",
        dialogClickModal: false,
        column: [{
            label: "账号",
            prop: "accountNumber",
            search: true,
            width: 250,
            rules: [{
              required: true,
              message: "请输入账号",
              trigger: "blur"
            }]
          },
          {
            label: "户名",
            prop: "accountName",
            width: 250,
            search: true,
            rules: [{
              required: true,
              message: "请输入户名",
              trigger: "blur"
            }]
          },
          {
            label: "开户银行",
            prop: "bankName",
            width: 250,
            search: true,
            rules: [{
              required: true,
              message: "请输入开户银行",
              trigger: "blur"
            }]
          },
          {
            label: "货币",
            prop: "currency",
            type: "select",
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请输入货币",
              trigger: "blur"
            }]
          },
          {
            label: "企业签章",
            prop: "enterpriseSignature",
            width: 150,
            rules: [{
              required: false,
              message: "请输入企业签章",
              trigger: "blur"
            }]
          },
          {
            label: "工商登记号",
            prop: "commercialRegistrationNo",
            width: 150,
            rules: [{
              required: false,
              message: "请输入工商登记号",
              trigger: "blur"
            }]
          },
          {
            label: "银行电话",
            width: 150,
            prop: "bankPhone",
            rules: [{
              required: false,
              message: "请输入银行电话",
              trigger: "blur"
            }]
          },
          {
            label: "税务登记号",
            prop: "taxRegistrationNo",
            width: 150,
            rules: [{
              required: false,
              message: "请输入税务登记号",
              trigger: "blur"
            }]
          },
          {
            label: "财务电话",
            prop: "financialPhone",
            width: 150,
            rules: [{
              required: false,
              message: "请输入财务电话",
              trigger: "blur"
            }]
          },
          {
            label: "地址",
            prop: "address",
            type: "textarea",
            width: 150,
            rules: [{
              required: false,
              message: "请输入地址",
              trigger: "blur"
            }]
          },
          {
            label: "swift code",
            prop: "swiftCode",
            width: 150,
            rules: [{
              required: false,
              message: "请输入swift code",
              trigger: "blur"
            }]
          },
          {
            label: "是否可用",
            prop: "disabled",
            type: "radio",
            valueDefault: false,
            dicData: [{
              label: "可用",
              value: true,
            }, {
              label: "不可用",
              value: false
            }],
            rules: [{
              required: true,
              message: "请输入是否可用",
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
