<template>
<div>
  <basic-container>
    <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
      @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
      <template v-if="bill_can_operate" slot="menuLeft">
        <el-button type="success" size="small" icon="el-icon-edit" @click="archiveBusiness">归 档</el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text" size="small" icon="el-icon-edit" @click="editBill(scope.row)">账单明细</el-button>
        <el-button v-if="scope.row.billStatus==0" type="text" size="small" icon="el-icon-edit" @click="settleBusiness(scope.row)">确认结算</el-button>
        <el-button type="text" size="small" icon="el-icon-edit" @click="exportBill(scope.row)">导出账单</el-button>
      </template>
    </avue-crud>
  </basic-container>
  <el-dialog top="2vh" title="账单明细" :visible.sync="modify_bill_show" width="80%" :close-on-click-modal="false">
    <div style="width:100%;">
      <billdetail v-if="modify_bill_show" :bill="modify_bill" :bill_status="modify_bill_status" :bill_amount="modify_bill_amount" :bill_id="modify_bill_id" :bill_show="false" :bill_type="modify_bill_type" />
    </div>
  </el-dialog>
  <el-dialog top="2vh" :modal-append-to-body="false" :show-close="false" title="确认结算" :visible.sync="settle_show" width="80%" :close-on-click-modal="false">
    <avue-form ref="settle_form" v-model="settle_form" :option="settle_option" @reset-change="settleCancel" @submit="settleSubmit">
    </avue-form>
  </el-dialog>
  <el-dialog :center="true" :show-close="false" :close-on-click-modal="false" title="选择账单收款银行" :visible.sync="bankaccount_show" width="30%" top="20vh">
    <avue-form ref="bankaccount_form" v-model="bankaccount_form" :option="bankaccount_option" @reset-change="bankAccountCancel" @submit="bankAccountSubmit"></avue-form>
  </el-dialog>
  <el-dialog :close-on-click-modal="false" title="导出账单" :visible.sync="iframe_show" width="90%" top="4vh" align="center">
    <iframe style="width:100%;height:700px;" :src="iframe_url"></iframe>
  </el-dialog>
</div>
</template>

<script>
import {
  getList,
  getDetail,
  add,
  update,
  remove,
  archiveBill,
  settleBill,
  addToBill,
} from "@/api/reconciliationstatement/bill";
import {
  mapGetters
} from "vuex";
import billdetail from "./billdetail.vue"
export default {
  data() {
    return {
      bankaccount_option: {
        submitText: "生成对账单",
        emptyText: "取消",
        align: "center",
        column: [{
          label: "银行账号",
          prop: "bankAccountId",
          span: 24,
          type: "tree",
          dicUrl: "/api/basicData/bankaccount",
        }]
      },
      bankaccount_form: {

      },
      iframe_show: false,
      iframe_url: "",
      bankaccount_show: false,
      excel_viewer_url: "http://view.officeapps.live.com/op/view.aspx?src=",
      settle_show: false,
      settle_form: {},
      settle_option: {},
      form: {},
      query: {},
      modify_bill_show: false,
      modify_bill_id: "",
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      option: {
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        viewBtn: true,
        addBtn: this.bill_can_operate,
        selection: true,
        refreshBtn: false,
        columnBtn: false,
        align: "center",
        menu: this.bill_can_operate,
        column: [{
            label: "结算单位",
            prop: "settlementUnit",
            type: "select",
            search: true,
            editDisplay: false,
            dicUrl: "/api/basicData/tenant/clientdata",
            rules: [{
              required: true,
              message: "请选择结算单位",
              trigger: "blur"
            }]
          },
          {
            label: "账单状态",
            prop: "billStatus",
            type: "select",
            disabled: true,
            search: true,
            display: false,
            valueDefault: 0,
            dicData: [{
              label: "未归档",
              value: 0
            }, {
              label: "已结算",
              value: 1
            }, {
              label: "已归档",
              value: 2
            }]
          },
          {
            label: "币种",
            prop: "currency",
            type: "select",
            search: true,
            editDisplay: false,
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请选择币种",
              trigger: "blur"
            }]
          },
          {
            label: "账单金额",
            prop: "amount",
            type: "number",
            minRows: 0,
            maxRows: 999999,
            precision: 2,
            rules: [{
              required: true,
              message: "请输入到账金额",
              trigger: "blur"
            }]
          },
          {
            label: "实际收付款币种",
            prop: "finishedCurrency",
            type: "select",
            editDisplay: false,
            addDisplay: false,
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请选择币种",
              trigger: "blur"
            }]
          },
          {
            label: "实际收付款金额",
            prop: "finishedAmount",
            editDisplay: false,
            addDisplay: false,
            type: "number",
            minRows: 0,
            maxRows: 999999,
            precision: 2,
            rules: [{
              required: true,
              message: "请输入到账金额",
              trigger: "blur"
            }]
          },
          {
            label: "到账日期",
            prop: "gainedDate",
            valueFormat: "yyyy-MM-dd",
            type: "date",
            rules: [{
              required: false,
              message: "请输入到账日期",
              trigger: "blur"
            }]
          },
          {
            label: "凭证号",
            prop: "voucherNo",
            rules: [{
              required: false,
              message: "请输入凭证号",
              trigger: "blur"
            }]
          },
          {
            label: "水单号",
            prop: "serialNo",
            rules: [{
              required: false,
              message: "请输入水单号",
              trigger: "blur"
            }]
          },
          {
            label: "结算方式",
            prop: "settlementType",
            type: "select",
            dicData: [{
              label: "银行转账",
              value: "银行转账"
            }, {
              label: "现金",
              value: "现金"
            }, {
              label: "支票",
              value: "支票"
            }, {
              label: "支付宝",
              value: "支付宝"
            }, {
              label: "微信",
              value: "微信"
            }],
            rules: [{
              required: false,
              message: "请输入结算方式",
              trigger: "blur"
            }]
          },
          {
            label: "银行账号",
            prop: "bankAccountId",
            type: "select",
            dicUrl: "/api/basicData/bankaccount",
            rules: [{
              required: false,
              message: "请输入银行账号id",
              trigger: "blur"
            }]
          },
          /**
          {
            label: "银行账号",
            prop: "bankAccount",
            disabled: true,
            rules: [{
              required: false,
              message: "请输入银行账号",
              trigger: "blur"
            }]
          },
          **/
          {
            label: "类型收款付款",
            prop: "type",
            search: true,
            editDisabled: true,
            type: "radio",
            width: 100,
            dicData: [{
              label: "应收",
              value: 0
            }, {
              label: "应付",
              value: 1
            }],
            valueDefault: 0,
            rules: [{
              required: false,
              message: "请输入类型收款付款",
              trigger: "blur"
            }]
          },
          {
            label: "开票状态",
            prop: "invoiceStatus",
            type: "radio",
            dicData: [{
              label: "未申请未确认",
              value: 0
            }, {
              label: "已申请未确认",
              value: 1
            }, {
              label: "未申请已确认",
              value: 2
            }, {
              label: "已申请已确认",
              value: 3
            }],
            valueDefault: 0,
            rules: [{
              required: false,
              message: "请输入开票状态",
              trigger: "blur"
            }]
          },
          {
            label: "开票类型",
            prop: "invoiceType",
            type: "select",
            dicData: [{
              label: "增值税普通发票",
              value: "增值税普通发票"
            }, {
              label: "第三方开票",
              value: "第三方开票"
            }, {
              label: "Debit Note",
              value: "Debit Note"
            }, {
              label: "增值税专用发票",
              value: "增值税专用发票"
            }, {
              label: "不开发票",
              value: "不开发票"
            }, {
              label: "Credit Note",
              value: "Credit Note"
            }]
          },
          {
            label: "开票号码",
            prop: "invoiceNo",
            rules: [{
              required: false,
              message: "请输入开票号码",
              trigger: "blur"
            }]
          },
          {
            label: "发票日期",
            prop: "invoiceDate",
            valueFormat: "yyyy-MM-dd",
            type: "date",
            rules: [{
              required: false,
              message: "请输入发票日期",
              trigger: "blur"
            }]
          },
          {
            label: "备注",
            prop: "remark",
            rules: [{
              required: false,
              message: "请输入备注",
              trigger: "blur"
            }]
          },
          {
            label: "账单状态(是否确认)",
            labelWidth: 200,
            prop: "isConfirm",
            type: "radio",
            dicData: [{
              label: "是",
              value: true
            }, {
              label: "否",
              value: false
            }],
            valueDefault: false,
            rules: [{
              required: false,
              message: "请输入账单状态(是否确认)",
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
        viewBtn: this.vaildData(this.permission.bill_view, false),
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
  props: {
    bill_can_operate: {
      default: true,
    },
  },
  components: {
    billdetail
  },
  methods: {
    bankAccountCancel(form) {
      form = {};
      this.bankaccount_show = false;
    },
    bankAccountSubmit(form, done) {
      if (!form.bankAccountId) {
        this.$message.warning("请选择收款银行")
        done();
        return;
      }
      let src = "http://" + document.domain + "/api/reconciliationstatement/bill/statement?bankAccountIds=" + form.bankAccountId + "&id=" + this.export_bill.id + "&time=" + new Date().getTime();
      this.iframe_url = this.excel_viewer_url + encodeURIComponent(src);
      this.bankaccount_show = false;
      this.iframe_show = true;
      done();
    },
    exportBill(data) {
      this.export_bill = data;
      this.bankaccount_show = true;
    },
    settleCancel() {
      this.form = {};
      this.settle_show = false;
    },
    settleSubmit(form, done) {
      form.invoiceCurrency = null;
      this.$confirm("确定将账单结算?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return settleBill(form);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          form = {};
          this.settle_show = false;
          done();
        })
    },
    settleBusiness(data) {
      let settle_option = {
        emptyText: "取消",
        submitText: "确认结算",
        column: JSON.parse(JSON.stringify(this.option.column)),
      }
      let dl = ["settlementUnit", "currency", "type"];
      for (let v of settle_option.column) {
        for (let d of dl) {
          if (v.prop == d) v.disabled = true;
        }
      }
      this.settle_form = data;
      this.settle_option = settle_option;
      this.settle_show = true;
    },
    editBill(data) {
      this.modify_bill_id = data.id;
      this.modify_bill_type = data.type;
      this.modify_bill_amount = data.amount;
      this.modify_bill_status = data.billStatus;
      this.modify_bill = data;
      this.modify_bill_show = true;
    },
    rowSave(row, loading, done) {
      row.billStatus = 0;
      row.finishedCurrency = row.currency;
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
    archiveBusiness() {
      if (this.selectionList.length != 1) {
        this.$message.warning("请选择一条数据");
        return;
      }
      this.$confirm("确定将账单相关订单归档?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return archiveBill(this.ids);
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
