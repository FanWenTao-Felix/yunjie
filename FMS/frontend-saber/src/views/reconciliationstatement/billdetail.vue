<template>
<div>
  <div class="b_div0">
    <span v-if="bill_status==2" style="color:red">已归档</span>
    <span v-else-if="bill_status==1" style="color:red">已结算</span>
    <span v-else style="color:#333">未归档</span>
    <span style="color:green">账单总金额({{bill.currency}}):{{billAmountTotal.toFixed(2)}}</span>
  </div>
  <basic-container>
    <avue-form v-if="bill_show" ref="bill_form" v-model="bill_form" :option="bill_option"></avue-form>
    <avue-crud style="margin-top:-20px;" :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel"
      @search-change="searchChange" @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
      <template slot="menuLeft">
        <el-button v-if="bill.billStatus==0" type="danger" size="small" icon="el-icon-delete" @click="handleDelete">撤销结算</el-button>
      </template>
    </avue-crud>
  </basic-container>

  <div v-if="bill.billStatus==0" style="width:100%">
    <div>请选择需要结算的款项</div>
    <el-button type="success" size="small" icon="el-icon-edit" @click="addSettlement">添加结算</el-button>
    <el-button type="danger" size="small" icon="el-icon-edit" @click="verifyFee">审核费用</el-button>
    <!--
    <el-button type="warning" size="small" icon="el-icon-edit" @click="lockFee">费用锁定</el-button>
    <el-button type="warning" size="small" icon="el-icon-edit" @click="lockFee">费用解锁</el-button>
    -->
    <el-table ref="multipleTable" :data="feeList" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column prop="type" label="项目" width="50">
        <template slot-scope="scope">
          <div>{{scope.row.type==0?"应收":"应付"}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="internalOrderNo" label="工作号" width="180">
      </el-table-column>
      <el-table-column prop="chineseName" label="费用名称">
      </el-table-column>
      <el-table-column prop="amount" label="结算金额">
      </el-table-column>
      <el-table-column prop="feeStatus" label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.feeStatus==0" type="danger">审核不通过</el-tag>
          <el-tag v-else-if="scope.row.feeStatus==1" type="info">未审核</el-tag>
          <el-tag v-else-if="scope.row.feeStatus==2">审核中</el-tag>
          <el-tag v-else-if="scope.row.feeStatus==3" type="success">审核通过</el-tag>
          <el-tag v-else-if="scope.row.feeStatus==4" type="warning">结算中</el-tag>
          <el-tag v-else-if="scope.row.feeStatus==5" type="warning">核销完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="settledAmount" label="已结算金额">
      </el-table-column>
      <el-table-column prop="billCreatedAmount" label="已创建账单金额">
      </el-table-column>
      <el-table-column prop="currency" label="币种">
      </el-table-column>
      <el-table-column prop="currentBillAmount" label="本次账单金额">
        <template slot-scope="scope">
          <el-input v-model="scope.row.currentBillAmount"></el-input>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="说明">
        <template slot-scope="scope">
          <el-input v-model="scope.row.description"></el-input>
        </template>
      </el-table-column>
    </el-table>
  </div>

</div>
</template>

<script>
import {
  batchLockOrUnlock,
  batchStatus,
} from "@/api/financial/cancelafterverification"
import {
  getBillFee
} from "@/api/business/fee"
import {
  getList,
  getDetail,
  add,
  update,
  remove
} from "@/api/reconciliationstatement/billdetail";
import {
  getDetail as getBillDetail,
  addToBill,
} from "@/api/reconciliationstatement/bill"
import {
  mapGetters
} from "vuex";

export default {
  data() {
    return {
      multipleSelection: [],
      feeList: [],
      billAmountTotal: 0,
      bill_option: {
        emptyBtn: false,
        submitBtn: false,
        labelWidth: 150,
        column: [{
            label: "结算单位",
            prop: "settlementUnit",
            type: "select",
            dicUrl: "/api/basicData/tenant/clientdata",
            disabled: true,
            rules: [{
              required: true,
              message: "请选择结算单位",
              trigger: "blur"
            }]
          },
          {
            label: "币种",
            prop: "currency",
            type: "select",
            disabled: true,
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请选择币种",
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
            label: "账单金额",
            prop: "amount",
            type: "number",
            minRows: 0,
            maxRows: 999999,
            precision: 2,
            rules: [{
              required: false,
              message: "请输入到账金额",
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
            type: "radio",
            width: 100,
            dicData: [{
              label: "应收",
              value: 0
            }, {
              label: "应付",
              value: 1
            }],
            disabled: true,
            valueDefault: 0,
            rules: [{
              required: true,
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
      bill_form: {},
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 999,
        pageSizes: [999],
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
        columnBtn: false,
        refreshBtn: false,
        selection: true,
        addBtn: this.bill_status == 0,
        editBtn: this.bill_status == 0,
        menu: false,
        align: "center",
        column: [{
            label: "账单id",
            prop: "billId",
            disabled: true,
            editDisplay: false,
            rules: [{
              required: true,
              message: "请输入账单id",
              trigger: "blur"
            }]
          },
          {
            label: "内部单号",
            prop: "internalOrderNo",
            disabled: true,
            addDisplay: false,
            editDisplay: false,
            rules: [{
              required: true,
              message: "请输入内部单号",
              trigger: "blur"
            }]
          },
          {
            label: "关联的费用id",
            disabled: true,
            editDisplay: false,
            addDisplay: false,
            prop: "feeId",
            rules: [{
              required: true,
              message: "请输入关联的费用id",
              trigger: "blur"
            }]
          },
          {
            label: "类型收款付款",
            prop: "type",
            disabled: true,
            editDisplay: false,
            labelWidth: 150,
            type: "radio",
            dicData: [{
              label: "应收",
              value: 0
            }, {
              label: "应付",
              value: 1
            }]
          },
          {
            label: "费用名称",
            prop: "feeName",
            editDisplay: false,
            editDisabled: true,
            rules: [{
              required: true,
              message: "请输入费用名称",
              trigger: "blur"
            }]
          }, {
            label: "结算币种",
            prop: "currency",
            editDisabled: true,
            editDisplay: false,
            type: "select",
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请选择结算币种",
              trigger: "blur"
            }]
          },
          {
            label: "结算金额",
            prop: "amount",
            type: "number",
            minRows: 0,
            maxRows: 99999,
            precision: 2,
            rules: [{
              required: true,
              message: "请输入结算金额",
              trigger: "blur"
            }]
          },
          {
            label: "转换币种",
            prop: "convertCurrency",
            editDisplay: false,
            addDisplay: false,
            rules: [{
              required: true,
              message: "请输入转换币种",
              trigger: "blur"
            }]
          },
          {
            label: "转换金额",
            prop: "convertAmount",
            editDisplay: false,
            type: "number",
            disabled: true,
            addDisplay: false,
            minRows: 0,
            maxRows: 99999,
            precision: 2,
            rules: [{
              required: true,
              message: "请输入转换金额",
              trigger: "blur"
            }]
          },
          {
            label: "说明",
            prop: "description",
            rules: [{
              required: false,
              message: "请输入说明",
              trigger: "blur"
            }]
          },
          {
            label: "创建人",
            prop: "createUser",
            type: "select",
            dicUrl: "/api/basicData/tenant/alluser",
          }
        ]
      },
      data: []
    };
  },
  props: ["bill_id", "bill_show", "bill_type", "bill_amount", "bill_status", "bill"],
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(false, false),
        viewBtn: this.vaildData(this.permission.billdetail_view, false),
        delBtn: this.vaildData(true, false),
        editBtn: this.vaildData(false, false)
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
  mounted() {
    if (!this.bill_show) return;
    getBillDetail(this.bill_id).then(res => {
      if (res.data.code != 200) {
        this.$message.error("没有找到账单");
        return;
      }
      this.bill_form = res.data.data;
    }).catch(res => {

    })
  },
  methods: {
    verifyFee() {
      if (this.multipleSelection.length <= 0) {
        this.$message.warning("请选择一条数据");
        return;
      }
      let idList = [];
      for (let item of this.multipleSelection) {
        idList.push(item.id);
      }
      idList = idList.join(",");
      this.$confirm("确定将选择数据审核?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return batchStatus(idList, 3);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.$refs.multipleTable.clearSelection();
          this.onLoad(this.page);
        });

    },
    addSettlement() {
      if (this.multipleSelection.length <= 0) {
        this.$message.warning("请选择一条数据");
        return;
      }
      let b = {
        id: this.bill.id,
        settlementUnit: this.bill.settlementUnit,
      }
      let f = this.multipleSelection;
      for (let item of f) item.convertCurrency = "CNY";
      this.$confirm("确定将账单相关费用结算?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return addToBill(f, b);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.$refs.multipleTable.clearSelection();
          this.onLoad(this.page);
        });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    rowSave(row, loading, done) {
      row.billStatus = 0;
      row.convertCurrency = row.currency;
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
      row.billStatus = 0;
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
      if (type == "add") {
        this.form = {
          billId: this.bill_id,
          type: this.bill_type,
        }
      }
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
      getList(page.currentPage, page.pageSize, this.bill_id, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        let billAmountTotal = 0;
        for (let item of this.data) {
          billAmountTotal += item.amount;
        }
        this.billAmountTotal = billAmountTotal;
        this.loading = false;
        this.selectionClear();
      });
      if (this.bill.billStatus != 0) return;
      this.feeList = [];
      getBillFee({
        type: this.bill.type,
        settlementUnit: this.bill.settlementUnit,
      }).then(res => {
        if (res.data.code != 200) return;
        this.feeList = res.data.data;
      })
    }
  }
};
</script>

<style>
.b_div0 {
  width: 100%;
  height: 100px;
}

.b_div0 span {
  font-size: 30px;
  margin-left: 15px;
}
</style>
