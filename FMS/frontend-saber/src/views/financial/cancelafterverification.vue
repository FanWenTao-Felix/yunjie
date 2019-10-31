<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad" :row-style="rowStyle">
    <template slot-scope="scope" slot="codeForm">
      <el-autocomplete :disabled="scope.disabled" v-model="form.code" :fetch-suggestions="queryFeeSearchAsync" @select="costTypeSelect" placeholder="请输入费用代码"></el-autocomplete>
    </template>
    <template slot="menuLeft">
      <el-button type="primary" @click="batchStatus(3)" size="small">审核通过</el-button>
      <el-button type="danger" size="small" @click="batchStatus(0)">审核不通过</el-button>
      <el-button type="primary" size="small" @click="feeLockOrUnlock(true)">锁定</el-button>
      <el-button type="primary" size="small" @click="feeLockOrUnlock(false)">解锁</el-button>
      <el-button type="danger" size="small" @click="billData(0,0)">创建应收账单</el-button>
      <el-button type="danger" size="small" @click="billData(1,0)">创建应付账单</el-button>
      <el-button type="danger" size="small" @click="billData(0,1)">加入应收账单</el-button>
      <el-button type="danger" size="small" @click="billData(1,1)">加入应付账单</el-button>
    </template>
    <template slot="unSettledAmount" slot-scope="scope">
      <div>{{scope.row.amount-scope.row.settledAmount}}</div>
    </template>

    <!--
    <template slot="inputSettledAmount" slot-scope="scope">
      <div style="display:flex;">
        <el-input-number :disabled="scope.row.feeStatus==6||scope.row.feeStatus<3||scope.row.isLocked" v-model="scope.row.inputSettledAmount" :precision="2" :step="100" :min="0" :max="9999"></el-input-number>
        <el-button :disabled="scope.row.feeStatus==6||scope.row.feeStatus<3||scope.row.isLocked" type="normal" size="small" @click="settledAmountChange(scope.row)">修改</el-button>
      </div>
    </template>
    -->

  </avue-crud>
  <el-dialog top="2vh" title="创建账单" :visible.sync="bill_show" width="95%" :show-close="false" :close-on-click-modal="false">
    <div style="display:flex;">
      <div style="width:40%;">
        <avue-form ref="bill_form" v-model="bill_form" :option="bill_option" @reset-change="billCancel" @submit="billSubmit"></avue-form>
      </div>
      <div style="width:60%;">
        <avue-crud style="width:100%;margin-top:-50px" :data="bill_detail_list" :option="bill_detail_option"></avue-crud>
      </div>
    </div>
  </el-dialog>

  <el-dialog top="2vh" title="选择账单" :visible.sync="bill_detail_show" width="98%" :show-close="false" :close-on-click-modal="false">
    <div style="display:flex;">
      <div style="width:100%;">
        <bill v-if="bill_detail_show" :bill_can_operate="false" ref="bill" />
      </div>
    </div>
    <div style="display:flex">
      <span style="flex:1"></span>
      <el-button type="primary" @click="billDetailConfirm">确定</el-button>
      <el-button @click="billDetailCancel">取消</el-button>
    </div>
  </el-dialog>


  <el-dialog top="2vh" title="账单明细" :visible.sync="modify_bill_show" width="98%" :show-close="false" :close-on-click-modal="false">
    <div style="display:flex;">
      <div style="width:100%;">
        <billdetail v-if="modify_bill_show" :bill_amount="bill_form.amount" :bill_status="0" :bill_id="bill_id" ref="billdetail" />
      </div>
    </div>
    <div style="display:flex">
      <span style="flex:1"></span>
      <el-button type="primary" @click="modifyBillConfirm">保存修改</el-button>
      <el-button @click="modifyBillCancel">取消</el-button>
    </div>
  </el-dialog>



</basic-container>
</template>

<script>
import {
  getList,
  batchStatus,
  modifySettledAmount,
  batchLockOrUnlock
} from "@/api/financial/cancelafterverification";
import {
  createBillData,
  joinBill,
  update as billUpdate
} from "@/api/reconciliationstatement/bill"
import {
  mapGetters
} from "vuex";
import {
  getDetailByCode
} from "@/api/dictionaries/costtype"
import {
  getCostType,
} from "@/api/basicData/basicData"

import bill from "../reconciliationstatement/bill.vue"
import billdetail from "../reconciliationstatement/billdetail.vue"

export default {
  created() {},
  data() {
    return {
      modify_bill_show: false,
      bill_form: {},
      bill_detail_show: false,
      bill_detail_list: [],
      bill_detail_option: {
        menu: false,
        refreshBtn: false,
        addBtn: false,
        viewBtn: false,
        columnBtn: false,
        column: [{
            label: "收付款单位",
            prop: "settlementUnit",
            width: 200,
            type: "select",
            dicUrl: "/api/basicData/tenant/clientdata",
            rules: [{
              required: true,
              message: "请选择结算单位",
              trigger: "blur"
            }]
          }, {
            label: "应收/应付",
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
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请选择类型",
              trigger: "blur"
            }]
          }, {
            label: "工作号",
            prop: "internalOrderNo",
            width: 250,
            addDisplay: false,
            disabled: true,
            rules: [{
              required: false,
              message: "请输入工作号",
              trigger: "blur"
            }]
          },
          {
            label: "费用名称",
            value: "shortName",
          },
          {
            label: "币种",
            prop: "currency",
          },
        ]
      },
      bill_option: {
        emptyText: "取消",
        column: [{
            label: "结算单位",
            prop: "settlementUnit",
            type: "select",
            disabled: true,
            dicUrl: "/api/basicData/tenant/clientdata",
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
              message: "请输入账单金额",
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
      bill_show: false,
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
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        viewBtn: true,
        addBtn: true,
        align: "center",
        labelWidth: "120",
        menu: false,
        selection: true,
        dialogClickModal: false,
        column: [{
            label: "收付款单位",
            prop: "settlementUnit",
            search: true,
            width: 200,
            type: "select",
            dicUrl: "/api/basicData/tenant/clientdata",
            rules: [{
              required: true,
              message: "请选择结算单位",
              trigger: "blur"
            }]
          }, {
            label: "应收/应付",
            prop: "type",
            type: "radio",
            search: true,
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
              required: true,
              message: "请输入费用币种",
              trigger: "blur"
            }]
          }, {
            label: "工作号",
            prop: "internalOrderNo",
            width: 250,
            addDisplay: false,
            search: true,
            disabled: true,
            rules: [{
              required: false,
              message: "请输入工作号",
              trigger: "blur"
            }]
          },
          {
            label: "创建人",
            prop: "createUser",
            type: "select",
            dicUrl: "/api/basicData/user",
            disabled: true,
          }, {
            label: "费用名称",
            value: "shortName",
          },
          {
            label: "币种",
            prop: "currency",
          },
          {
            label: "应结算金额",
            prop: "amount",
            type: "number",
            disabled: true,
            width: 100,
            minRows: 0,
            maxRows: 99999,
            precision: 2,
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入费用金额",
              trigger: "blur"
            }]
          }, {
            label: "未结算金额",
            prop: "unSettledAmount",
            width: 150,
            disabled: true,
            slot: true,
          },
          {
            label: "已创建账单金额",
            prop: "billCreatedAmount",
            type: "number",
          },
          {
            label: "状态",
            prop: "feeStatus",
            search: true,
            type: "select",
            disabled: true,
            valueDefault: 0,
            dicData: [{
              label: "审核不通过",
              value: 0,
            }, {
              label: "未审核",
              value: 1
            }, {
              label: "审核中",
              value: 2
            }, {
              label: "审核通过",
              value: 3
            }, {
              label: "结算中",
              value: 4
            }],
            rules: [{
              required: true,
              message: "请选择状态",
              trigger: "blur"
            }]
          }, {
            label: "是否锁定",
            prop: "isLocked",
            disabled: true,
            type: "select",
            dicData: [{
              label: "锁定",
              value: true
            }, {
              label: "未锁定",
              value: false
            }],
          }
        ]
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(false, false),
        viewBtn: this.vaildData(false, false),
        delBtn: this.vaildData(false, false),
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
  components: {
    bill,
    billdetail
  },
  methods: {
    modifyBillConfirm() {
      this.modify_bill_show = false;
      let data = this.$refs.billdetail.bill_form;
      billUpdate(data).then(res => {
        if (res.data.code != 200) {
          this.$message.error("保存失败");
        } else {
          this.$message.success("保存成功");
          this.modify_bill_show = false;
        }
      }).catch(res => {
        this.$message.error("保存失败");
      })
    },
    modifyBillCancel() {
      this.modify_bill_show = false;
    },
    billDetailCancel() {
      this.bill_detail_ids = "";
      this.bill_detail_list = {};
      this.bill_detail_show = false;
    },
    billDetailConfirm() {
      let billSelectionList = this.$refs.bill.selectionList;
      if (billSelectionList.length != 1) {
        this.$message.error("只能选择一个账单");
        return;
      }
      let bill = billSelectionList[0];
      if (bill.settlementUnit != this.bill_detail_list[0].settlementUnit) {
        this.$message.error("请选择同一结算单位");
        return;
      }
      joinBill(bill.id, this.bill_detail_ids).then(res => {
        this.billDetailCancel();
        if (res.data.code != 200) {
          this.$message.error("加入账单失败");
        } else {
          this.$message.success("加入账单成功");
          this.bill_id = bill.id;
          this.modify_bill_show = true;
        }
      }).catch(res => {
        this.billDetailCancel();
      })
    },
    billCancel() {
      this.bill_detail_ids = "";
      this.bill_detail_list = [];
      this.bill_form = {};
      this.bill_show = false;
    },
    billSubmit(form, done) {
      createBillData(form, this.bill_detail_ids).then(res => {
        done()
        if (res.data.code != 200) this.$message.error("创建失败")
        else {
          this.$message.success("创建成功");
          this.bill_id = res.data.data;
          this.modify_bill_show = true;
        }
        this.billCancel();
      }).catch(res => {
        done()
        this.billCancel();
      })
    },
    billData(rp, type) {
      // rp 0 应收  1 应付
      // type 0 创建账单  1加入账单
      if ((rp != 0 && rp != 1) || (type != 0 && type != 1)) {
        throw "错误账单参数";
      };
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      /**
      let routeUrl = this.$router.resolve({
        path: "/financial/cancelafterverification",
      });
      window.open(routeUrl.href, '_blank');
       **/
      let list = this.selectionList;
      let checkUnit = list[0].settlementUnit;
      for (let item of list) {
        if (item.settlementUnit != checkUnit) {
          this.$message.error("只能选择相同的结算单位");
          this.billDetailCancel();
          this.billCancel();
          return;
        } else if (item.type != rp) {
          this.$message.error("只能选择相同的结算类型");
          this.billDetailCancel();
          this.billCancel();
          return;
        }
        checkUnit = item.settlementUnit;
      }
      this.bill_detail_list = list;
      this.bill_detail_ids = this.ids;
      this.$refs.crud.toggleSelection();
      if (type == 0) {
        this.bill_form.settlementUnit = checkUnit;
        this.bill_form.type = rp;
        this.bill_form.currency = list[0].currency;
        this.bill_show = true;
      } else if (type == 1) this.bill_detail_show = true;
    },
    settledAmountChange(data) {
      if (!data.inputSettledAmount) {
        this.$message.error("请输入正确结算金额");
        return;
      }
      modifySettledAmount(data.id, data.inputSettledAmount).then(res => {
        if (res.data.code != 200) data.settledAmount = 0;
        this.$message.success("修改成功")
        this.onLoad(this.page);
      }).catch(res => {
        data.settledAmount = 0;
      })
    },
    feeLockOrUnlock(s) {
      let title = "确定将选择数据" + (s ? "锁定" : "解锁") + "?";
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      this.$confirm(title, {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return batchLockOrUnlock(this.ids, s);
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
    batchStatus(status) {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      this.$confirm("确定将选择数据审核?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return batchStatus(this.ids, status);
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
    rowStyle({
      row,
      column,
      rowIndex
    }) {
      if (row.isLocked) {
        return {
          backgroundColor: '#BBB',
          color: '#000'
        }
      } else if (row.type == 0) {
        return {
          backgroundColor: 'LightGreen',
          color: '#000'
        }
      } else if (row.type == 1) {
        return {
          backgroundColor: 'Crimson',
          color: '#000'
        }
      }

    },
    costTypeSelect(item) {
      getDetailByCode(item.value).then(res => {
        if (res.data.code != 200 || !res.data.success) return;
        let data = res.data.data;
        this.form.shortName = data.costName;
        this.form.englishName = data.costEnname;
        this.form.chineseName = data.costAllname;
        this.form.currency = data.costDefault;
      })
    },
    queryFeeSearchAsync(q, cb) {
      getCostType(q).then(res => {
        if (res.status != 200) return cb([]);
        let data = res.data;
        cb(data);
      })
    },
    refreshPage() {
      this.$set(this, "page", {
        pageSize: 9999,
        currentPage: 1,
        total: 0
      })
      this.onLoad(this.page);
    },
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
      /**
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
          **/
    },
    handlePayable() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      this.$confirm("确定要生成对应的应付数据?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return createPayable(this.ids);
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

    },
    beforeOpen(done, type) {
      /**
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.id).then(res => {
          this.form = res.data.data;
        });
      }**/
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
