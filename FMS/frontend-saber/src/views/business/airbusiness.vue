<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.airbusiness_delete" @click="handleDelete">删 除
      </el-button>
    </template>
    <template slot-scope="scope" slot="otherFeeListForm">
      <div>
        <el-button icon="el-icon-edit" @click="otherFeeOperation(scope.row,0)" type="text" size="small">增加</el-button>
        <el-button icon="el-icon-edit" @click="otherFeeOperation(scope.row,1)" type="text" size="small">减少</el-button>
        <div v-for="(item,index) in form.otherFeeList" style="display:flex;align-item:center">
          <el-tag>费用代码</el-tag>
          <el-input style="width:200px;" minlength="2" maxlength="3" v-model="item.code" :key="item.code" placeholder="请输入费用代码"></el-input>
          <el-tag>费用金额</el-tag>
          <el-input type="number" v-model.number="item.fee" :key="item.code+item.fee"></el-input>
          <!--
          <el-input style="width:200px;" minlength="2" maxlength="3" v-model="form.otherFeeList[index].code" placeholder="请输入费用代码"></el-input>
          <el-input-number type="number" maxlength="7" v-model.number="form.otherFeeList[index].fee" :precision="2" :step="1" :max="9999" :min="0"></el-input-number>
          -->
        </div>
      </div>
    </template>


    <template slot-scope="scope" slot="sizeListForm">
      <div>
        <el-button icon="el-icon-edit" @click="sizeOperation(scope.row,0)" type="text" size="small">增加</el-button>
        <el-button icon="el-icon-edit" @click="sizeOperation(scope.row,1)" type="text" size="small">减少</el-button>
        <div v-for="(item,index) in form.sizeList" style="display:flex;align-item:center">
          <el-tag>长:</el-tag>
          <el-input @change="sizeListChange" style="width:200px;" type="number" v-model="item.l" :key="index+'l'" placeholder="请输入货物长"></el-input>
          <el-tag>宽:</el-tag>
          <el-input @change="sizeListChange" style="width:200px;" type="number" v-model="item.w" :key="index+'w'" placeholder="请输入货物宽"></el-input>
          <el-tag>高:</el-tag>
          <el-input @change="sizeListChange" style="width:200px;" type="number" v-model="item.h" :key="index+'h'" placeholder="请输入货物高"></el-input>
          <el-tag>件数</el-tag>
          <el-input @change="sizeListChange" style="width:200px;" type="number" v-model="item.a" :key="index+'a'" placeholder="请输入货物件数"></el-input>
        </div>
      </div>
    </template>


    <template slot-scope="scope" slot="mainOrderNoForm">
      <el-input :disabled="scope.disabled" v-if="scope.row.isMain" v-model="form.mainOrderNo" placeholder="请输入主单号"></el-input>
      <el-autocomplete :disabled="scope.disabled" v-else v-model="form.mainOrderNo" :fetch-suggestions="queryMainOrderSearchAsync" placeholder="请输入主单号"></el-autocomplete>
    </template>
    <template v-if="role=='operator'||role=='admin'" slot-scope="scope" slot="menu">
      <el-button @click="openFee(scope.row)" icon="el-icon-edit" type="text" size="small">费用管理</el-button>
      <el-button @click="openTask(scope.row)" icon="el-icon-edit" type="text" size="small">作业项目</el-button>
      <!--
      <el-button v-if="!scope.row.isMain" @click="openMain(scope.row)" icon="el-icon-edit" type="text" size="small">查看主单</el-button>
      <el-button v-if="scope.row.isMain" @click="openSub(scope.row)" icon="el-icon-edit" type="text" size="small">查看分单</el-button>
      -->
      <el-button @click="openFile(scope.row)" icon="el-icon-edit" type="text" size="small">文件管理</el-button>
      <el-button @click="openIframe(scope.row,0)" icon="el-icon-edit" type="text" size="small">交接单</el-button>
      <el-button @click="openIframe(scope.row,1)" icon="el-icon-edit" type="text" size="small">提单</el-button>
      <el-button @click="openIframe(scope.row,2)" icon="el-icon-edit" type="text" size="small">提单(展示)</el-button>
      <el-button @click="openIframe(scope.row,3)" icon="el-icon-edit" type="text" size="small">对账单</el-button>
    </template>



    <template slot-scope="scope" slot="consignerForm">
      <div>
        <el-tag type="success">填写格式: 联系人###手机###地址 </el-tag>
        <el-autocomplete style="width:100%;" type="textarea" :disabled="scope.disabled" v-model="form.consigner" :fetch-suggestions="queryConsignerSearchSync" placeholder="联系人###手机###地址"></el-autocomplete>
      </div>
    </template>
    <template slot-scope="scope" slot="consigneeForm">
      <div>
        <el-tag type="success">填写格式: 联系人###手机###地址 </el-tag>
        <el-autocomplete style="width:100%" type="textarea" :disabled="scope.disabled" v-model="form.consignee" :fetch-suggestions="queryConsigneeSearchSync" placeholder="联系人###手机###地址"></el-autocomplete>
      </div>
    </template>
    <template slot-scope="scope" slot="notifierForm">
      <div>
        <el-tag type="success">填写格式: 联系人###手机###地址 </el-tag>
        <el-autocomplete style="width:100%;" type="textarea" :disabled="scope.disabled" v-model="form.notifier" :fetch-suggestions="queryNotifierSearchSync" placeholder="联系人###手机###地址"></el-autocomplete>
      </div>
    </template>

    <template slot-scope="scope" slot="loadingPortForm">
      <el-autocomplete @change.native="loadingPortChange" :disabled="scope.disabled" v-model="form.loadingPort" @select="handleSelectLoadingAirport" :fetch-suggestions="queryPortSearchAsync" placeholder="请输入起运港港口"></el-autocomplete>
    </template>
    <template slot-scope="scope" slot="destinationPortForm">
      <el-autocomplete @change.native="destinationPortChange" :disabled="scope.disabled" v-model="form.destinationPort" @select="handleSelectDestinationAirport" :fetch-suggestions="queryPortSearchAsync" placeholder="请输入目的港港口"></el-autocomplete>
    </template>
    <template slot-scope="scope" slot="fbDestinationPortForm">
      <el-autocomplete :disabled="scope.disabled" v-model="form.fbDestinationPort" :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
    </template>
    <template slot-scope="scope" slot="sbDestinationPortForm">
      <el-autocomplete :disabled="scope.disabled" v-model="form.sbDestinationPort" :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
    </template>
  </avue-crud>
  <el-dialog :close-on-click-modal="false" :title="iframe_title" :visible.sync="iframe_show" width="90%" top="4vh" align="center">
    <iframe style="width:100%;height:700px;" :src="iframe_url"></iframe>
  </el-dialog>
  <el-dialog :close-on-click-modal="false" title="文件管理" :visible.sync="file_show" width="90%" top="4vh">
    <businessfile :internalOrderNo="file_internal_order_no" :file_type="0" />
  </el-dialog>
  <el-dialog :close-on-click-modal="false" title="费用管理" :visible.sync="fee_show" width="90%" top="4vh">
    <fee v-if="fee_show" :role="role" :client="order_client" :internalOrderNo="fee_internal_order_no" :canEdit="true" />
  </el-dialog>
  <el-dialog :close-on-click-modal="false" title="作业项目管理" :visible.sync="task_show" width="90%" top="4vh">
    <task v-if="task_show" :internalOrderNo="task_internal_order_no" />
  </el-dialog>
  <el-dialog :close-on-click-modal=" false" title="主单信息" :visible.sync="main_show" width="90%" top="4vh">
    <avue-form :option="mainOrderOption" v-model="mainOrderForm"></avue-form>
  </el-dialog>
  <el-dialog :close-on-click-modal="false" title="分单信息" :visible.sync="sub_show" width="90%" top="4vh">
    <airsub :main_order_no="air_main_order_no" :column="option.column" :mainOrderPermission="mainOrderPermission" />
  </el-dialog>
  <el-dialog :center="true" :show-close="false" :close-on-click-modal="false" title="选择账单收款银行" :visible.sync="bankaccount_show" width="30%" top="20vh">
    <avue-form ref="bankaccount_form" v-model="bankaccount_form" :option="bankaccount_option" @reset-change="bankAccountCancel" @submit="bankAccountSubmit"></avue-form>
  </el-dialog>
</basic-container>
</template>

<script>
import {
  getList as getLinkmanList
} from "@/api/linkman/clientlinkman"
import {
  getDetailByAirCode
} from "@/api/dictionaries/air"
import {
  getList,
  getDetail,
  add,
  update,
  remove,
  getMainOrderDetail
} from "@/api/business/airbusiness";
import {
  getDetail as getClientData
} from "@/api/client/clientdata";

import {
  judgeUser,
  getMainOrderNoData,
  getCurrency,
  getAirport,
} from "@/api/basicData/basicData"
import {
  mapGetters
} from "vuex";
import fee from "./fee.vue"
import businessfile from "./businessfile.vue"
import airsub from "./airsub.vue"
import task from "./task.vue"
const flightStatusList = [{
  label: "无状态",
  value: 0
}, {
  label: "拉货中",
  value: 1
}, {
  label: "已起飞",
  value: 2
}, {
  label: "已到达",
  value: 3
}]
const payTypeList = [{
  label: "预付",
  value: 0
}, {
  label: "到付",
  value: 1
}]
const freightRateLevelList = [{
  label: "M",
  value: "M"
}, {
  label: "N",
  value: "N"
}, {
  label: "Q",
  value: "Q"
}, {
  label: "C",
  value: "C"
}, {
  label: "R",
  value: "R"
}, {
  label: "S",
  value: "S"
}, {
  label: "U",
  value: "U"
}, {
  label: "E",
  value: "E"
}, {
  label: "X",
  value: "X"
}, {
  label: "Y",
  value: "Y"
}]
const businessTypeList = [{
  label: "出口",
  value: 0,
}, {
  label: "进口",
  value: 1
}]
const basePermission = ["internalOrderNo", "mainOrderNo", "subOrderNo", "salesman", "operator", "isMain", "loadingPort", "destinationPort", "loadingPortFullName", "destinationPortFullName", "client",
  "consigner", "consignee", "notifier", "goodsChineseName", "goodsEnglishName", "goodsDescription", "measurement", "sizeList", "goodsAmount",
  "goodsPackage",
  "goodsGrossWeight", "goodsLength",
  "goodsWidth", "goodsHeight",
  "goodsVolumn",
  "goodsChargeableWeight", "chargePrice",
  "chargeCurrency"
];
const operatorExtraPermission = ["cargoRate", "mark", "inWeight", "inChargeableWeight", "inAmount", "inVolumn", "customsDeclarationWeight", "customsDeclarationAmount", "cargoTerminalWeight", "cargoTerminalVolumn", "declaredValueForCarriage",
  "declaredValueForCustoms", "amountOfInsurance"
];
const mainOrderPermission = ["carrierId", "ladingBillFeeCurrency", "airFeePayType", "otherFeeList", "otherFeePayType", "bookingAgentId", "bookingStatus", "bookingDate",
  "airLine", "voyage", "flight", "flightTime",
  "publishFare",
  "documentaryOffDate",
  "fbDestinationPort",
  "fbLaunchTime",
  "fbArrivalTime", "fbStatus",
  "sbDestinationPort",
  "sbLaunchTime",
  "sbArrivalTime", "sbStatus"
];
const admin = "admin";
const salesman = "salesman";
const operator = "operator";
const finance = "finance";
export default {
  components: {
    fee,
    businessfile,
    airsub,
    task,
  },
  created() {
    this.mainOrderPermission = mainOrderPermission;
    judgeUser().then(res => {
      if (res.data.code != 200 && res.data.success) return;
      let data = res.data.data;
      this.role = data;
      let o = this.option;
      let c = o.column;
      this.$set(o, "viewBtn", true);
      this.$set(o, "editBtn", true);
      if (data == admin || data == salesman || data == operator) this.$set(o, "addBtn", true);
    })
  },
  data() {
    return {
      task_show: false,
      task_internal_order_no: "",
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
      bankaccount_show: false,
      popup_open_type: "",
      consignee_list: [],
      consigner_list: [],
      notifier_list: [],
      air_main_order_no: "",
      excel_viewer_url: "http://view.officeapps.live.com/op/view.aspx?src=",
      iframe_title: "",
      iframe_show: false,
      iframe_url: "",
      main_show: false,
      sub_show: false,
      fee_show: false,
      file_show: false,
      file_internal_order_no: "",
      fee_internal_order_no: "",
      role: -1, //判断角色身份
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      mainOrderForm: {},
      mainOrderOption: {
        submitBtn: false,
        emptyBtn: false,
        align: "center",
        column: [],
      },
      option: {
        dialogWidth: "90%",
        dialogHeight: 700,
        dialogClickModal: false,
        dialogTop: 20,
        labelWidth: 150,
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        viewBtn: false,
        addBtn: false,
        editBtn: false,
        deleteBtn: false,
        align: "center",
        column: [{
            label: "作业项目",
            prop: "task",
            type: "select",
            dicUrl: "/api/basicData/taskname",
            display: false,
            disabled: true,
            hide: true,
            search: true,
          }, {
            label: "作业时间",
            prop: "taskTime",
            search: true,
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            display: false,
            hide: true,
            disabled: true,
          }, {
            label: "工作号",
            prop: "internalOrderNo",
            editDisabled: true,
            width: 250,
            type: "select",
            dicUrl: "/api/basicData/airworkrules",
            rules: [{
              required: true,
              message: "请输入工作号",
              trigger: "blur"
            }]
          },
          {
            label: "分单/主单",
            prop: "isMain",
            width: 130,
            search: true,
            type: "switch",
            change: (d) => {
              this.changePopupData(d.value);
            },
            dicData: [{
              label: "分单",
              value: false
            }, {
              label: "主单",
              value: true
            }],
            valueDefault: false,
            rules: [{
              required: true,
              message: "请选择是否主单",
              trigger: "blur"
            }]
          },
          {
            label: "主单号",
            prop: "mainOrderNo",
            formslot: true,
            search: true,
            width: 200,
            rules: [{
              required: false,
              message: "请输入主单号",
              trigger: "blur"
            }]
          },
          {
            label: "分单号",
            prop: "subOrderNo",
            width: 250,
            rules: [{
              required: false,
              message: "请输入分单号",
              trigger: "blur"
            }]
          },
          {
            label: "业务员",
            prop: "salesman",
            type: "select",
            dicUrl: "/api/basicData/salesman",
            editDisabled: true,
            addDisplay: false,
            width: 150,
            rules: [{
              required: true,
              message: "请选择业务员",
              trigger: "blur"
            }]
          },
          {
            label: "操作员",
            prop: "operator",
            editDisabled: true,
            type: "select",
            width: 150,
            dicUrl: "/api/basicData/operator",
            rules: [{
              required: true,
              message: "请选择操作员",
              trigger: "blur"
            }]
          },
          {
            label: "航班",
            prop: "flight",
            hide: true,
            rules: [{
              required: false,
              message: "请输入航班",
              trigger: "blur"
            }]
          }, {
            label: "航班时间",
            prop: "flightTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入航班",
              trigger: "blur"
            }]
          }, {
            label: "委托人",
            prop: "client",
            width: 130,
            type: "select",
            dicUrl: "/api/basicData/clientdata",
            rules: [{
              required: true,
              message: "请选择委托人",
              trigger: "blur"
            }]
          },
          {
            label: "发货人信息",
            prop: "consigner",
            width: 150,
            hide: true,
            formslot: true,
            formatter: (r, v, l, c) => {
              return this.formatterInfo(v);
            },
            span: 12,
            rules: [{
              required: true,
              message: "请输入发货人信息",
              trigger: "blur"
            }]
          },
          {
            label: "收货人信息",
            prop: "consignee",
            formslot: true,
            hide: true,
            width: 150,
            span: 12,
            formatter: (r, v, l, c) => {
              return this.formatterInfo(v);
            },
            rules: [{
              required: true,
              message: "请输入收货人信息",
              trigger: "blur"
            }]
          },
          {
            label: "通知人信息",
            prop: "notifier",
            formslot: true,
            width: 150,
            formatter: (r, v, l, c) => {
              return this.formatterInfo(v);
            },
            rules: [{
              required: false,
              message: "请输入通知人信息",
              trigger: "blur"
            }]
          }, {
            label: "货物中文名称",
            prop: "goodsChineseName",
            width: 150,
            type: "textarea",
            rules: [{
              required: true,
              message: "请输入货物中文名称",
              trigger: "blur"
            }]
          },
          {
            label: "货物英文名",
            prop: "goodsEnglishName",
            type: "textarea",
            width: 150,
            rules: [{
              required: true,
              message: "请输入货物英文名",
              trigger: "blur"
            }]
          },
          {
            label: "起运港",
            prop: "loadingPort",
            formslot: true,
            rules: [{
              required: true,
              message: "请输入起运港",
              trigger: "blur"
            }]
          },
          {
            label: "起运港全称",
            prop: "loadingPortFullName",
            hide: true,
            rules: [{
              required: true,
              message: "请输入起运港全称",
              trigger: "blur"
            }]
          },
          {
            label: "目的港",
            prop: "destinationPort",
            formslot: true,
            rules: [{
              required: true,
              message: "请输入目的港",
              trigger: "blur"
            }]
          },
          {
            label: "目的港全称",
            prop: "destinationPortFullName",
            hide: true,
            rules: [{
              required: true,
              message: "请输入目的港全称",
              trigger: "blur"
            }]
          },
          /**
          {
            label: "业务状态",
            prop: "businessStatus",
            type: "select",
            search: true,
            hide: true,
            display: false,
            dicData: [{
                label: "草稿",
                value: 0
              }, {
                label: "未审核",
                value: 1
              }, {
                label: "审核中",
                value: 2
              }, {
                label: "已审核",
                value: 3
              }, {
                label: "已完成",
                value: 4
              }, {
                label: "已拒绝",
                value: 5
              },
              {
                label: "已取消",
                value: 6
              }
            ],
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请选择业务状态",
              trigger: "blur"
            }]
          },
          **/

          {
            label: "唛头",
            prop: "mark",
            hide: true,
            type: "textarea",
            rules: [{
              required: false,
              message: "请输入唛头",
              trigger: "blur"
            }]
          },

          {
            label: "货物备注",
            prop: "goodsDescription",
            type: "textarea",
            width: 150,
            rules: [{
              required: false,
              message: "请输入货物备注",
              trigger: "blur"
            }]
          },
          {
            label: "货物尺寸描述",
            prop: "measurement",
            width: 150,
            type: "textarea",
            hide: true,
            rules: [{
              required: false,
              message: "请输入货物尺寸描述",
              trigger: "blur"
            }]
          },
          {
            label: "货物包装",
            prop: "goodsPackage",
            type: "select",
            dicUrl: "/api/basicData/package",
            span: 6,
            rules: [{
              required: false,
              message: "请输入货物包装",
              trigger: "blur"
            }]
          },
          {
            label: "尺寸详情",
            prop: "sizeList",
            formslot: true,
            hide: true,
            span: 24,
          },
          {
            label: "件数",
            prop: "goodsAmount",
            type: "number",
            span: 6,
            change: (d) => {
              if (d.value == "" || d.value == this.form[d.column.prop]) return;
              let n = (this.form.goodsVolumn || 0) * d.value / 6000;
              this.form.goodsChargeableWeight = n > (this.form.goodsGrossWeight || 0) ? n : (this.form.goodsGrossWeight || 0);
            },
            maxRows: 999999,
            labelWidth: 60,
            minRows: 0,
            valueDefault: 1,
            rules: [{
              required: true,
              message: "请输入货物件数",
              trigger: "blur"
            }]
          },
          {
            label: "货物毛重",
            prop: "goodsGrossWeight",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            change: (d) => {
              if (d.value == "" || d.value == this.form[d.column.prop]) return;
              let n = (this.form.goodsAmount || 0) * (this.form.goodsVolumn || 0) / 6000;
              this.form.goodsChargeableWeight = n > d.value ? n : d.value;
            },
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入货物毛重",
              trigger: "blur"
            }]
          },
          {
            label: "货物体积(cbm)",
            prop: "goodsVolumn",
            type: "number",
            change: (d) => {
              if (d.value == "" || d.value == this.form[d.column.prop]) return;
              let n = (this.form.goodsAmount || 0) * d.value / 6000;
              this.form.goodsChargeableWeight = n > (this.form.goodsGrossWeight || 0) ? n : (this.form.goodsGrossWeight || 0);
            },
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入货物体积(cbm)",
              trigger: "blur"
            }]
          },
          {
            label: "货物收费重量",
            prop: "goodsChargeableWeight",
            type: "number",
            width: 130,
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入货物收费重量",
              trigger: "blur"
            }]
          },

          {
            label: "入货件数",
            prop: "inAmount",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            change: (d) => {
              if (d.value == "" || d.value == this.form[d.column.prop]) return;
              let n = (this.form.inVolumn || 0) * d.value / 6000;
              this.form.inChargeableWeight = n > (this.form.inWeight || 0) ? n : (this.form.inWeight || 0);
            },
            valueDefault: 1,
            hide: true,
            rules: [{
              required: true,
              message: "请输入入货件数",
              trigger: "blur"
            }]
          },
          {
            label: "入货重量",
            prop: "inWeight",
            type: "number",
            change: (d) => {
              if (d.value == "" || d.value == this.form[d.column.prop]) return;
              let n = (this.form.inAmount || 0) * (this.form.inVolumn || 0) / 6000;
              this.form.inChargeableWeight = n > d.value ? n : d.value;
            },
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: true,
              message: "请输入入货重量",
              trigger: "blur"
            }]
          },
          {
            label: "入货体积(cbm)",
            prop: "inVolumn",
            type: "number",
            maxRows: 999999,
            change: (d) => {
              if (d.value == "" || d.value == this.form[d.column.prop]) return;
              let n = (this.form.inAmount || 0) * d.value / 6000;
              this.form.inChargeableWeight = n > (this.form.inWeight || 0) ? n : (this.form.inWeight || 0);
            },
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: true,
              message: "请输入入货体积(cbm)",
              trigger: "blur"
            }]
          },
          {
            label: "入货收费重量",
            prop: "inChargeableWeight",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: true,
              message: "请输入入货收费重量",
              trigger: "blur"
            }]
          },
          {
            label: "报关件数",
            prop: "customsDeclarationAmount",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            valueDefault: 1,
            hide: true,
            rules: [{
              required: false,
              message: "请输入报关件数",
              trigger: "blur"
            }]
          },
          {
            label: "报关重量",
            prop: "customsDeclarationWeight",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请输入报关重量",
              trigger: "blur"
            }]
          },
          {
            label: "货站重量",
            prop: "cargoTerminalWeight",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请输入货站重量",
              trigger: "blur"
            }]
          },
          {
            label: "货站体积(cbm)",
            prop: "cargoTerminalVolumn",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请输入货站体积(cbm)",
              trigger: "blur"
            }]
          },
          {
            label: "收费价格(空运费)/kg",
            prop: "chargePrice",
            type: "number",
            width: 180,
            labelWidth: "160",
            span: 6,
            precision: 2,
            maxRows: 999999,
            minRows: 0,
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入收费价格(空运费)/kg",
              trigger: "blur"
            }]
          },
          {
            label: "收费货币",
            prop: "chargeCurrency",
            type: "select",
            valueDefault: "CNY",
            span: 6,
            dicUrl: "/api/basicData/currency",
            rules: [{
              required: true,
              message: "请输入收费货币",
              trigger: "blur"
            }]
          },
          {
            label: "运价等级",
            prop: "cargoRate",
            type: "select",
            dicData: freightRateLevelList,
            rules: [{
              required: true,
              message: "请输入运价等级",
              trigger: "blur"
            }]
          },
          {
            label: "运输声明价值",
            prop: "declaredValueForCarriage",
            hide: true,
            rules: [{
              required: false,
              message: "请输入运输声明价值",
              trigger: "blur"
            }]
          }, {
            label: "海关声明价值",
            prop: "declaredValueForCustoms",
            hide: true,
            rules: [{
              required: false,
              message: "请输入海关声明价值",
              trigger: "blur"
            }]
          },
          {
            label: "保险金额",
            prop: "amountOfInsurance",
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请输入保险金额",
              trigger: "blur"
            }]
          },
          {
            label: "承运人",
            prop: "carrierId",
            hide: true,
            type: "select",
            dicUrl: "/api/basicData/aircompany",
            rules: [{
              required: true,
              message: "请输入承运人id",
              trigger: "blur"
            }]
          },
          /**
          {
            label: "业务类型(进出口)",
            prop: "businessType",
            type: "select",
            dicData: [{
              label: "出口",
              value: 0
            }, {
              label: "进口",
              value: 1
            }],
            valueDefault: 0,
            hide: true,
            rules: [{
              required: true,
              message: "请输入业务类型(进出口)",
              trigger: "blur"
            }]
          }, **/
          {
            label: "提单费用货币币种",
            prop: "ladingBillFeeCurrency",
            type: "select",
            dicUrl: "/api/basicData/currency",
            valueDefault: "CNY",
            hide: true,
            rules: [{
              required: false,
              message: "请选择提单费用货币币种",
              trigger: "blur"
            }]
          },
          {
            label: "空运费支付类型",
            prop: "airFeePayType",
            type: "radio",
            dicData: payTypeList,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请选择空运费支付类型",
              trigger: "blur"
            }]
          }, {
            label: "其他费用",
            prop: "otherFeeList",
            formslot: true,
            hide: true,
            rules: [{
              required: false,
              message: "请选择空运其他费用",
              trigger: "blur"
            }]
          }, {
            label: "其他费支付类型",
            prop: "otherFeePayType",
            type: "radio",
            dicData: payTypeList,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请选择空运费支付类型",
              trigger: "blur"
            }]
          },
          /**{
            label: "进仓代理",
            prop: "warehousingAgentId",
            type: "select",
            width: 150,
            dicUrl: "/api/basicData/supplier",
            hide: true,
            rules: [{
              required: false,
              message: "请选择进仓代理",
              trigger: "blur"
            }]
          },
          **/
          /**
          {
            label: "航司代理",
            prop: "airlineCompanyAgentId",
            type: "select",
            width: 150,
            dicUrl: "/api/basicData/aircompanyagent",
            hide: true,
            rules: [{
              required: false,
              message: "请选择航司代理",
              trigger: "blur"
            }]
          },**/
          {
            label: "订舱代理",
            prop: "bookingAgentId",
            type: "select",
            width: 150,
            dicUrl: "/api/basicData/supplier",
            hide: true,
            rules: [{
              required: false,
              message: "请选择订舱代理",
              trigger: "blur"
            }]
          }, {
            label: "订舱状态",
            prop: "bookingStatus",
            type: "select",
            dicData: [{
              label: "未订舱",
              value: 0
            }, {
              label: "已订舱",
              value: 1
            }],
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请输入订舱状态",
              trigger: "blur"
            }]
          },
          {
            label: "订舱日期",
            prop: "bookingDate",
            type: "date",
            valueFormat: "yyyy-MM-dd",
            hide: true,
            rules: [{
              required: false,
              message: "请输入订舱日期",
              trigger: "blur"
            }]
          },
          /**  就是通知人
          {
            label: "另行通知",
            prop: "handlingInformation",
            hide: true,
            type: "textarea",
            rules: [{
              required: false,
              message: "请输入另行通知",
              trigger: "blur"
            }]
          },
          **/
          {
            label: "航线",
            prop: "airLine",
            hide: true,
            rules: [{
              required: false,
              message: "请输入航线",
              trigger: "blur"
            }]
          }, {
            label: "航程",
            prop: "voyage",
            hide: true,
            rules: [{
              required: false,
              message: "请输入航程",
              trigger: "blur"
            }]
          },

          {
            label: "公布运价",
            prop: "publishFare",
            hide: true,
            type: "number",
            maxRows: 999999,
            minRows: 0,
            precision: 2,
            valueDefault: 0,
            rules: [{
              required: false,
              message: "请输入公布运价",
              trigger: "blur"
            }]
          },
          {
            label: "截单时间",
            prop: "documentaryOffDate",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入截单时间",
              trigger: "blur"
            }]
          },
          /**
          {
            label: "截关时间",
            prop: "closingTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入截关时间",
              trigger: "blur"
            }]
          },
          {
            label: "截货时间",
            prop: "cargoTerminalTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入截货时间",
              trigger: "blur"
            }]
          },
          **/
          {
            label: "一程目的港",
            prop: "fbDestinationPort",
            formslot: true,
            hide: true,
            rules: [{
              required: false,
              message: "请输入一程目的港",
              trigger: "blur"
            }]
          },
          {
            label: "一程起飞时间",
            prop: "fbLaunchTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入一程起飞时间",
              trigger: "blur"
            }]
          },
          {
            label: "一程到达时间",
            prop: "fbArrivalTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入一程到达时间",
              trigger: "blur"
            }]
          },
          {
            label: "一程状态",
            prop: "fbStatus",
            type: "select",
            dicData: flightStatusList,
            valueDefault: 1,
            hide: true,
            rules: [{
              required: false,
              message: "请输入一程状态",
              trigger: "blur"
            }]
          },
          {
            label: "二程目的港",
            prop: "sbDestinationPort",
            formslot: true,
            hide: true,
            rules: [{
              required: false,
              message: "请输入二程目的港",
              trigger: "blur"
            }]
          },
          {
            label: "二程起飞时间",
            prop: "sbLaunchTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入二程起飞时间",
              trigger: "blur"
            }]
          },
          {
            label: "二程到达时间",
            prop: "sbArrivalTime",
            type: "datetime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            hide: true,
            rules: [{
              required: false,
              message: "请输入二程到达时间",
              trigger: "blur"
            }]
          },
          {
            label: "二程状态",
            prop: "sbStatus",
            type: "select",
            dicData: flightStatusList,
            valueDefault: 0,
            hide: true,
            rules: [{
              required: false,
              message: "请输入二程状态",
              trigger: "blur"
            }]
          },
        ],
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(true, false),
        viewBtn: this.vaildData(false, false),
        delBtn: this.vaildData(this.permission.airbusiness_delete, false),
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
    'form.client'(n, o) {
      if (!this.form.client) return;
      getLinkmanList(1, 9999, this.form.client, {}).then(res => {
        if (!res.data.success) return;
        let list = res.data.data.records;
        this.consignee_list = [];
        this.consigner_list = [];
        this.notifier_list = [];
        for (let item of list) {
          let d = item.linkman + "###" + item.phone + "###" + item.address;
          let d1 = {
            label: d,
            value: d,
          };
          if (item.type == 0) this.consigner_list.push(d1);
          else if (item.type == 1) this.consignee_list.push(d1);
          else if (item.type == 2) this.notifier_list.push(d1);
        }
      })
    }
  },
  methods: {
    recountWeight() {
      let sizeList = this.form.sizeList;
      let a_total = 0;
      let v_total = 0;
      for (let item of sizeList) {
        a_total += Number(item.a);
        v_total += Number(item.l) * Number(item.w) * Number(item.h);
      }
      this.form.inVolumn = v_total;
      this.form.goodsVolumn = v_total;
      this.form.cargoTerminalVolumn = v_total;
      this.form.inAmount = a_total;
      this.form.goodsAmount = a_total;
    },
    sizeListChange(c, o) {
      this.recountWeight()
    },
    loadingPortChange(data) {
      let value = data.path[0].value.toUpperCase();
      this.form.loadingPort = value;
      this.handleSelectAirport({
        label: value,
        value: value
      }, 0);
    },
    destinationPortChange(data) {
      let value = data.path[0].value.toUpperCase();
      this.form.destinationPort = value;
      this.handleSelectAirport({
        label: value,
        value: value
      }, 1);
    },
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
      this.iframe_src += "&bankAccountIds=" + form.bankAccountId;
      this.iframe_url = this.excel_viewer_url + encodeURIComponent(this.iframe_src);
      this.bankaccount_show = false;
      this.iframe_show = true;
      done();
    },
    formatterInfo(v) {
      let data = v ? v : "";
      data = data.split("###");
      let value = "";
      if (data.length > 0) value += "联系人: " + data[0] + " \n ";
      if (data.length > 1) value += "联系电话: " + data[1] + " \n ";
      if (data.length > 2) value += "联系地址: " + data[2];
      return value;
    },
    createFilter(obj, queryString) {
      return obj.filter(item => {
        return !item.value.toUpperCase().indexOf(queryString.toUpperCase());
      })
    },
    queryConsigneeSearchSync(q, cb) {
      cb(this.createFilter(this.consignee_list, q));
    },
    queryConsignerSearchSync(q, cb) {
      cb(this.createFilter(this.consigner_list, q));
    },
    queryNotifierSearchSync(q, cb) {
      cb(this.createFilter(this.notifier_list, q));
    },
    handleSelectLoadingAirport(item) {
      this.handleSelectAirport(item, 0);
    },
    handleSelectDestinationAirport(item) {
      this.handleSelectAirport(item, 1);
    },
    handleSelectAirport(item, type) {
      getDetailByAirCode(item.value).then(res => {
        if (res.data.code != 200) return;
        let data = res.data.data;
        data.airEncityname = data.airEncityname ? data.airEncityname.toUpperCase() : "";
        if (type == 0) this.form.loadingPortFullName = data.airEncityname;
        else this.form.destinationPortFullName = data.airEncityname;
      })
    },
    sizeOperation(data, type) {
      if (!this.form.sizeList) this.form.sizeList = [];
      let recount = false;
      let l = this.form.sizeList.length;
      if (type == 0 && l < 20) {
        recount = true;
        this.form.sizeList.push({
          l: 1,
          w: 1,
          h: 1,
          a: 1,
        })
      } else if (type == 1 && l > 0) {
        this.form.sizeList.splice(-1, 1);
        recount = true;
      } else if (l >= 20 && type == 0) this.$message.warning("最多只能添加20条");
      else if (l <= 0 && type == 1) this.$message.warning("不能再少了");
      console.log(recount);
      if (recount) this.recountWeight();
    },
    otherFeeOperation(data, type) {
      if (!this.form.otherFeeList) this.form.otherFeeList = [];
      let l = this.form.otherFeeList.length;
      if (type == 0 && l < 6) { //+
        this.form.otherFeeList.push({
          code: "",
          fee: 0,
        });
      } else if (type == 1 && l > 0) { //-
        this.form.otherFeeList.splice(-1, 1);
      } else if (l >= 20 && type == 0) this.$message.warning("最多只能添加20条");
      else if (l <= 0 && type == 1) this.$message.warning("不能再少了");
    },
    handleRowDBLClick(row, event) {
      this.$refs.crud.rowEdit(row, row.$index);
    },
    openFile(data) {
      this.file_internal_order_no = data.internalOrderNo;
      this.file_show = true;
    },
    queryPortSearchAsync(q, cb) {
      getAirport(q).then(res => {
        if (res.status != 200) return cb([]);
        let data = res.data;
        cb(data);
      })
    },
    openSub(data) {
      this.air_main_order_no = data.mainOrderNo;
      this.sub_show = true;
    },
    openMain(data) {
      let m = this.mainOrderOption;
      let c = m.column;
      if (c.length == 0) {
        let list = JSON.parse(JSON.stringify(this.option.column));
        for (let i = 0; i < list.length; i++) {
          let hasPermission = false;
          for (let j = 0; j < mainOrderPermission.length; j++) {
            if (list[i].prop == mainOrderPermission[j]) hasPermission = true;
          }
          if (!hasPermission) list[i].display = false;
          if (list[i].prop == "loadingPort" || list[i].prop == "destinationPort") list[i].display = true;
          list[i].disabled = true;
          list[i].formslot = false;
          list[i].labelWidth = 150;
        }
        this.$set(m, "column", list);
      }
      getMainOrderDetail(data.mainOrderNo).then(res => {
        if (res.data.code != 200 || !res.data.success) return;
        this.mainOrderForm = res.data.data;
        if (!this.mainOrderForm.id) {
          this.$message.error("没有找到主单");
          return;
        }
        this.main_show = true;
      }).catch(res => {
        this.$message.error("查看主单失败");
      })
    },
    queryMainOrderSearchAsync(q, cb) {
      getMainOrderNoData(q).then(res => {
        if (res.status != 200) return cb([]);
        let data = res.data;
        cb(data);
      })
    },
    //根据是否主单来改变弹框显示
    changePopupData(trueAndFalse) {
      let o = this.option;
      let c = o.column;
      let isEdit = this.popup_open_type == "edit" ? true : false;

      let permission = basePermission;
      //主单 操作员
      if (isEdit && trueAndFalse && (this.role == operator || this.role == admin)) {
        permission = permission.concat(operatorExtraPermission);
        permission = permission.concat(mainOrderPermission);
      }
      for (let i = 0; i < c.length; i++) {
        let find = false;
        for (let j = 0; j < permission.length; j++) {
          if (c[i].prop == permission[j]) {
            find = true;
            break;
          }
        }
        if (this.role == operator && ((c[i].prop == "mainOrderNo") || c[i].prop == "subOrderNo")) find = false;
        this.$set(c[i], "addDisplay", find)
        this.$set(c[i], "viewDisplay", find)
        this.$set(c[i], "editDisplay", find)
      }
    },
    openIframe(data, type) {
      this.iframe_url = "";
      let src = "http://" + document.domain
      this.iframe = ""
      this.iframe_title = "";
      src += "/api/business/airbusiness/";
      if (type == 0) {
        this.iframe_title = "交接单";
        src += "deliveryReceipt";
      } else if (type == 1) {
        this.iframe_title = "空运提单(打印)";
        src += "waybill";
      } else if (type == 2) {
        this.iframe_title = "空运提单(展示)";
        src += "waybillshow";
      } else if (type == 3) {
        this.iframe_title = "空运对账单";
        src += "statement";
      }
      src += "?id=" + data.id + "&printer=" + this.$store.getters.userInfo.user_name;
      src += "&client=" + new Date().getTime();
      if (type == 3) {
        this.bankaccount_show = true;
        this.iframe_src = src;
        return;
      }
      this.iframe_url = this.excel_viewer_url + encodeURIComponent(src);
      this.iframe_show = true;
    },
    openTask(data) {
      this.task_internal_order_no = data.internalOrderNo;
      this.task_show = true;
    },
    openFee(data) {
      this.fee_internal_order_no = data.internalOrderNo;
      this.order_client = data.client;
      this.fee_show = true;
    },
    otherFeeListModify(data) {
      let otherFeeList = data.otherFeeList;
      let result = "";
      for (let i = 0; i < otherFeeList.length; i++) {
        let item = otherFeeList[i]
        if (!item.code || item.code == "") continue;
        result += item.code + ":" + item.fee;
        item.fee;
        if (i < otherFeeList.length - 1) result += ",";
      }
      data.otherFeeList = result;
    },
    sizeListModify(data) {
      let sl = data.sizeList;
      let result = "";
      for (let i = 0; i < sl.length; i++) {
        let item = sl[i];
        result += item.l + "x" + item.w + "x" + item.h + "x" + item.a;
        if (i < sl.length - 1) result += ",";
      }
      data.sizeList = result;
    },
    rowSave(row, loading, done) {
      if (row.otherFeePayType != 0 && row.otherFeePayType != 1) row.otherFeePayType = 0;
      if (row.airFeePayType != 0 && row.airFeePayType != 1) row.airFeePayType = 0;
      row.ladingBillFeeCurrency = "CNY";
      this.sizeListModify(row);
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
      this.otherFeeListModify(row);
      this.sizeListModify(row);
      console.log(row);
      if (row.otherFeePayType != 0 && row.otherFeePayType != 1) row.otherFeePayType = 0;
      if (row.airFeePayType != 0 && row.airFeePayType != 1) row.airFeePayType = 0;
      if (row.ladingBillFeeCurrency.length != 3) row.ladingBillFeeCurrency = "CNY";
      row.businessStatus = null;
      row.businessType = null;
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
      this.popup_open_type = type;
      this.changePopupData(this.form.isMain);
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.id).then(res => {
          let data = res.data.data;
          this.otherFeeListHandleToArray(data)
          this.sizeListHandleToArray(data)
          this.form = data;
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
    sizeListHandleToArray(data) {
      console.log(data.sizeList, "12312")
      let sl = data.sizeList;
      if (!sl || sl == "" || sl == " ") sl = [];
      else sl = sl.split(",");
      let list = [];
      for (let i = 0; i < sl.length; i++) {
        let item = sl[i];
        item = item.split("x");
        item = {
          l: item[0],
          w: item[1],
          h: item[2],
          a: item[3],
        }
        list.push(item);
      }
      data.sizeList = list;
      console.log(data.sizeList)
      console.log("ok")
    },
    otherFeeListHandleToArray(data) {
      let otherFeeList = data.otherFeeList;
      if (otherFeeList.length == 0) otherFeeList = [];
      else otherFeeList = otherFeeList.split(",");
      data.otherFeeList = otherFeeList;
      let list = otherFeeList;
      for (let j = 0; j < list.length; j++) {
        let item = list[j].split(":");
        item[1] = Number(item[1]);
        if (typeof item[1] != "number") item[1] = 0;
        list[j] = {
          code: item[0],
          fee: Number(item[1]),
        }
      }
    },
    onLoad(page, params = {}) {
      this.loading = true;
      getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        for (let i = i; i < this.data.length; i++) {
          this.otherFeeListHandleToArray(this.data[i]);
          this.sizeListHandleToArray(this.data[i]);
        }
        this.loading = false;
        this.selectionClear();
      });
    }
  }
};
</script>

<style>
.el-textarea__inner {
  height: 120px;
}
</style>
