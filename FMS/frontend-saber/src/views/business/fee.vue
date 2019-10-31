<template>
<basic-container>
  <div>
    <div style="color:green">+应收合计(CNY):{{r_total}}</div>
    <div style="color:red">-应付合计(CNY):{{p_total}}</div>
    <div :style="r_total-p_total>0?'color:green':'color:red'">=毛利润(CNY):{{(r_total-p_total).toFixed(2)}}</div>
  </div>

  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad" :row-style="rowStyle">
    <template slot="menuLeft">
      <el-button v-if="role!='salesman'" type="danger" size="small" icon="el-icon-plus" @click="handlePayable">生成应付</el-button>
      <el-button v-if="role=='admin'||role=='finance'" type="danger" size="small" icon="el-icon-edit" @click="batchStatus(3)">审核</el-button>
      <el-button v-if="role=='admin'||role=='finance'" type="success" size="small" icon="el-icon-edit" @click="feeLockOrUnlock(true)">锁定</el-button>
      <el-button v-if="role=='admin'||role=='finance'" type="danger" size="small" icon="el-icon-edit" @click="feeLockOrUnlock(false)">解锁</el-button>
    </template>
    <template v-if="role=='admin'||role=='finance'" slot="menuLeft">
      <el-button type="warning" size="small" icon="el-icon-edit" @click="verifiedAll">审核全部</el-button>
    </template>
    <template slot-scope="scope" slot="codeForm">
      <el-autocomplete :disabled="scope.disabled" v-model="form.code" :fetch-suggestions="queryFeeSearchAsync" @select="costTypeSelect" placeholder="请输入费用代码"></el-autocomplete>
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
  remove,
  createPayable
} from "@/api/business/fee";
import {
  mapGetters
} from "vuex";
import {
  getDetailByCode
} from "@/api/dictionaries/costtype"
import {
  verifyAllFee
} from "@/api/business/airbusiness"
import {
  batchLockOrUnlock,
  batchStatus,
} from "@/api/financial/cancelafterverification"
import {
  getCostType,
} from "@/api/basicData/basicData"
export default {
  watch: {
    internalOrderNo: function(n, o) {
      this.refreshPage();
    },
  },
  created() {},
  props: {
    role: {
      required: false,
      default: "",
    },
    client: {
      required: false,
      defalult: "",
    },
    canEdit: {
      required: false,
      defalult: false,
    },
    internalOrderNo: {
      required: false,
      defalult: "",
    },
  },
  data() {
    return {
      r_total: 0,
      p_total: 0,
      exchange_rate_list: [],
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 9999,
        pageSizes: [9999],
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
        dialogClickModal: false,
        addBtn: true,
        align: "center",
        labelWidth: "120",
        selection: true,
        column: [{
            label: "收付款单位",
            prop: "settlementUnit",
            width: 200,
            type: "select",
            dicUrl: "/api/basicData/prclient?internalOrderNo=" + this.internalOrderNo,
            rules: [{
              required: true,
              message: "请选择结算单位",
              trigger: "blur"
            }]
          },
          {
            label: "应收/应付",
            prop: "type",
            type: "radio",
            width: 120,
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
          },
          {
            label: "费用代码",
            prop: "code",
            formslot: true,
            rules: [{
              required: true,
              message: "请输入费用代码",
              trigger: "blur"
            }]
          },
          {
            label: "费用简称",
            prop: "shortName",
            rules: [{
              required: true,
              message: "请输入费用简称",
              trigger: "blur"
            }]
          },
          {
            label: "费用英文名称",
            prop: "englishName",
            rules: [{
              required: true,
              message: "请输入费用英文名称",
              trigger: "blur"
            }]
          },
          {
            label: "费用中文名称",
            prop: "chineseName",
            rules: [{
              required: true,
              message: "请输入费用中文名称",
              trigger: "blur"
            }]
          },


          {
            label: "计费单位",
            prop: "chargeUnit",
            type: "select",
            dicData: [{
              label: "票",
              value: "票",
            }, {
              label: "箱数",
              value: "箱数",
            }, {
              label: "件数",
              value: "件数",
            }, {
              label: "重量",
              value: "重量",
            }, {
              label: "体积",
              value: "体积",
            }, {
              label: "计费重",
              value: "计费重",
            }],
            rules: [{
              required: true,
              message: "请选择计费单位",
              trigger: "blur"
            }]
          },
          {
            label: "数量",
            prop: "quantity",
            type: "number",
            maxRows: 10000,
            change: (d) => {
              if (d.value == "") return;
              this.form.amount = d.value * this.form.unitPrice;
            },
            minRows: 1,
            valueDefault: 1,
            rules: [{
              required: true,
              message: "请输入数量",
              trigger: "blur"
            }]
          },
          {
            label: "单价",
            prop: "unitPrice",
            type: "number",
            precision: 2,
            maxRows: 10000,
            minRows: 0,
            change: (d) => {
              if (d.value == "") return;
              this.form.amount = d.value * this.form.quantity;
            },
            rules: [{
              required: true,
              message: "请输入单价",
              trigger: "blur"
            }]
          },
          {
            label: "费用金额",
            prop: "amount",
            type: "number",
            disabled: true,
            minRows: 0,
            maxRows: 99999,
            precision: 2,
            valueDefault: 0,
            formatter: (row, value, label, column) => {
              return value.toFixed(2);
            },
            rules: [{
              required: true,
              message: "请输入费用金额",
              trigger: "blur"
            }]
          },
          {
            label: "费用币种",
            prop: "currency",
            type: "select",
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请选择费用币种",
              trigger: "blur"
            }]
          },
          {
            label: "已结算金额",
            prop: "settledAmount",
            width: 150,
            disabled: true,
            display: false,
            type: "number",
          },
          {
            label: "已创建账单金额",
            prop: "billCreatedAmount",
            width: 200,
            display: false,
            disabled: true,
            type: "number",
          },
          {
            label: "创建人",
            prop: "createUser",
            type: "select",
            disabled: true,
            addDisplay: false,
            dicUrl: "/api/basicData/user",
          }, {
            label: "创建时间",
            prop: "createTime",
            display: false,
            width: 200,
            disabled: true,
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
          },
          {
            label: "是否锁定",
            prop: "isLocked",
            display: false,
            disabled: true,
            type: "select",
            dicData: [{
              label: "是",
              value: true
            }, {
              label: "否",
              value: false
            }]
          },
          {
            label: "状态",
            prop: "feeStatus",
            type: "select",
            valueDefault: 1,
            disabled: true,
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
        ]
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(this.canEdit, false),
        viewBtn: this.vaildData(false, false),
        delBtn: this.vaildData(this.canEdit, false),
        editBtn: this.vaildData(this.canEdit, false)
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
    verifiedAll() {
      this.$confirm("确定要审核全部费用数据?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        .then(() => {
          return verifyAllFee(this.internalOrderNo);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!"
          });
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
        pageSizes: [9999],
        currentPage: 1,
        total: 0
      })
      this.onLoad(this.page);
    },

    rowSave(row, loading, done) {
      row.convertCurrency = null;
      row.internalOrderNo = this.internalOrderNo;
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
    operationJudge(row) {
      if (this.role != "admin" || this.role != "finance" && row.isLocked) {
        this.$message.warning("费用已锁定")
        return true;
      }
      if (this.role != "admin" || this.role != "finance" && row.feeStatus > 2) {
        this.$message.warning("费用已审核或费用结算中")
        return true;
      }
      return false;
    },
    rowUpdate(row, index, loading, done) {
      if (this.operationJudge(row)) {
        done();
        return;
      }
      row.convertCurrency = null;
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
      if (this.operationJudge(row)) return;
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
      if (type == "add") this.form = {
        settlementUnit: this.client
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
      getList(page.currentPage, page.pageSize, this.internalOrderNo, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        let p_total = 0;
        let r_total = 0;
        for (let item of this.data) {
          if (item.type == 0) r_total += item.convertAmount;
          else p_total += item.convertAmount;
        }
        this.p_total = p_total.toFixed(2);
        this.r_total = r_total.toFixed(2);
        this.loading = false;
        this.selectionClear();
      });
    }
  }
};
</script>

<style>
</style>
