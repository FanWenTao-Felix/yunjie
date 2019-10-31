<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">

      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.seawhole_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template  slot-scope="scope" slot="menu">
        <el-button @click="openFee(scope.row)" icon="el-icon-edit" type="text" size="small">费用管理</el-button>
        <el-button @click="openFile(scope.row)"  icon="el-icon-edit" type="text" size="small">文件管理</el-button>
        <el-button @click="openLading(scope.row)" icon="el-icon-edit" type="text" size="small">提单信息</el-button>
        <el-button @click="openIframe(scope.row,0)" icon="el-icon-edit" type="text" size="small">交接单</el-button>
        <el-button @click="openIframe(scope.row,1)" icon="el-icon-edit" type="text" size="small">对账单</el-button>
       <!-- <el-button @click="openIframe(scope.row,2)" icon="el-icon-edit" type="text" size="small">提货单</el-button>-->
    </template>


      <template slot-scope="scope" slot="portLoadingForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.portLoading"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>
      <template slot-scope="scope" slot="polForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.pol"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>
      <template slot-scope="scope" slot="transitShipmentForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.transitShipment"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>
      <template slot-scope="scope" slot="unloadForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.unload"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>
      <template slot-scope="scope" slot="destinationPortForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.destinationPort"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>



    </avue-crud>

    <el-dialog title="费用管理" :visible.sync="fee_show"  width="90%" top="4vh" :modal="false" :modal-append-to-body="false">
      <seafee  :internalOrderNo="fee_internal_order_no" :canEdit="true" />
    </el-dialog>
    <el-dialog title="文件管理" :visible.sync="file_show" width="90%" top="4vh" :modal="false" :modal-append-to-body="false">
      <file :internalOrderNo="file_internal_order_no"  :file_type="1" />
    </el-dialog>
    <el-dialog title="提单信息" :visible.sync="lading_show" width="90%" top="4vh" :modal="false" :modal-append-to-body="false">
      <lading :internalOrderNo="lading_internal_order_no"  :businessId="business_id" :id="seaid" :type="1"/>
    </el-dialog>
    <el-dialog :title="iframe_title" :visible.sync="iframe_show" width="90%" top="4vh" :modal="false" :modal-append-to-body="false">
      <iframe style="width:100%;height:700px;" :src="iframe_url"></iframe>
    </el-dialog>

  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/ocean/seawhole";
  import {getSeaWorkrules} from "@/api/basicData/basicData";
  import file from "./businessfile.vue";
  //import seafee from "../business/fee.vue";
  import seafee from "./seafee.vue";
  import lading from "./billoflading.vue";
  import {getSeaport
  } from "@/api/basicData/basicData"
  //import lading from "./billoflading.vue";

  import {mapGetters} from "vuex";

  export default {
      components: {
          file,
          lading,
          seafee
      },
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
          excel_viewer_url: "http://view.officeapps.live.com/op/view.aspx?src=",
          models:true,
          iframe_url:"",
          iframe_title:"",
          iframe_show:false,
          file_show:false,
          file_internal_order_no:"",
          lading_show:false,
          seaid:"",
          lading_internal_order_no :"",
          business_id:"",
          business_idfee:"",
          fee_show:false,
          fee_internal_order_no:"",
          order_no:"",
        selectionList: [],
        option: {
            dialogWidth: "90%",
            dialogHeight: 700,
            dialogClickModal: false,
            dialogTop: 20,
            labelWidth: 150,
          height:'auto',
          calcHeight: 350,
          tip: false,
          border: true,
          index: true,
          viewBtn: true,
          selection: true,
          column: [
            {
              label: "",
              prop: "id",
              rules: [{
                required: true,
                message: "请输入",
                trigger: "blur"
              }],
                hide:true,
                editDisplay: false,
                viewDisplay: false,
                addDisplay: false,
            },
              {
                  label: "工作单号",
                  prop: "internalOrderNo",
                  editDisabled: true,
                  addDisplay: false,
                 // type: "select",
                  width: 200,
                  //dicUrl: "/api/basicData/seaworkrules",
                  rules: [{
                      required: true,
                      message: "请输入工作单号",
                      trigger: "blur"
                  }]
              },
              {
                  label: "",
                  prop: "businessId",
                  hide:true,
                  editDisplay: false,
                  viewDisplay: false,
                  addDisplay: false,
                  valueDefault: this.businessId
              },
            {
              label: "起运港",
              prop: "portLoading", width: 200,
                formslot: true,
                //type: "select",
               // dicUrl: "/api/basicData/seaportOne",
              rules: [{
                required: true,
                message: "请输入起运港",
                trigger: "blur"
              }]
            },
            {
              label: "起运港ETD",
              prop: "portEtd", width: 200,
                type: "datetime",
                valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                message: "请输入起运港ETD",
                trigger: "blur"
              }]
            },
            {
              label: "船名2/航次2",
              prop: "shipsNameTwo", width: 200,
             /* rules: [{
                required: true,
                message: "请输入船名2",
                trigger: "blur"
              }]*/
            },
            {
              label: "CY截关时间",
              prop: "cyClosingDate", width: 200,
                type: "datetime",
                valueFormat: "yyyy-MM-dd HH:mm:ss",
              /*rules: [{
                required: true,
                message: "请输入CY截关时间",
                trigger: "blur"
              }]*/
            },
            {
              label: "截关SI",
              prop: "closingSl",
                type: "datetime", width: 200,
                valueFormat: "yyyy-MM-dd HH:mm:ss",
              /*rules: [{
                required: true,
                message: "请输入截关SI",
                trigger: "blur"
              }]*/
            },
            {
              label: "预计出发时间",
              prop: "scheduledTime", width: 200,
                type: "datetime",
                valueFormat: "yyyy-MM-dd HH:mm:ss",
            /*  rules: [{
                required: true,
                message: "请输入预计出发时间",
                trigger: "blur"
              }]*/
            },
            {
              label: "装货港POL",
              prop: "pol",
                formslot: true,
                //type: "select",
                width: 200,
               // dicUrl: "/api/basicData/seaportOne",
              rules: [{
                required: true,
                message: "请输入装货港POL",
                trigger: "blur"
              }]
            },
            {
              label: "装货港ETD",
              prop: "proEtd",
                type: "datetime", width: 200,
                valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入装货港ETD",
                trigger: "blur"
              }]
            },
            {
              label: "船名/航次",
              prop: "shipsName", width: 200,
            /*  rules: [{
                required: true,
                message: "请输入船名",
                trigger: "blur"
              }]*/
            },
            {
              label: "装货港码头",
              prop: "loadingDock", width: 200,
              /*rules: [{
                required: true,
                message: "请输入装货港码头",
                trigger: "blur"
              }]*/
            },
            {
              label: "实际离港时间",
              prop: "departureAirport",
                type: "datetime",
                valueFormat: "yyyy-MM-dd HH:mm:ss",
            /*  rules: [{
                required: true,
                message: "请输入实际离港时间",
                trigger: "blur"
              }]*/
            },
            {
              label: "中转港",
              prop: "transitShipment", width: 200,
                formslot: true,
              //  type: "select",
               // dicUrl: "/api/basicData/seaportOne",
             /* rules: [{
                required: true,
                message: "请输入中转港",
                trigger: "blur"
              }]*/
            },
            {
              label: "卸货港",
              prop: "unload", width: 200,
                formslot: true,
               /* type: "select",
                dicUrl: "/api/basicData/seaportOne",*/
              rules: [{
                required: true,
                message: "请输入卸货港",
                trigger: "blur"
              }]
            },
            {
              label: "ETA",
              prop: "eta", width: 200,
                type: "datetime",
                valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入ETA",
                trigger: "blur"
              }]
            },
            {
              label: "卸货码头",
              prop: "unloadWharf", width: 200,
            /*  rules: [{
                required: true,
                message: "请输入卸货码头",
                trigger: "blur"
              }]*/
            },
            {
              label: "实际到港时间",
              prop: "arrivalDat",
                type: "datetime", width: 200,
                valueFormat: "yyyy-MM-dd HH:mm:ss",
            /*  rules: [{
                required: true,
                message: "请输入实际到港时间",
                trigger: "blur"
              }]*/
            },
            {
              label: "目的港",
              prop: "destinationPort", width: 200,
                formslot: true,
              /*  type: "select",
                dicUrl: "/api/basicData/seaportOne",*/
              rules: [{
                required: true,
                message: "请输入目的港",
                trigger: "blur"
              }]
            },
            {
              label: "订舱代理",
              prop: "bookingAgent", width: 200,
             /* rules: [{
                required: true,
                message: "请输入订舱代理",
                trigger: "blur"
              }]*/
            },
            {
              label: "船东",
              prop: "shipowner", width: 200,
            /*  rules: [{
                required: true,
                message: "请输入船东",
                trigger: "blur"
              }]*/
            },
            {
              label: "订舱号",
              prop: "shipping", width: 200,
           /*   rules: [{
                required: true,
                message: "请输入订舱号",
                trigger: "blur"
              }]*/
            },
            {
              label: "主单号",
              prop: "munualFolio", width: 200,
               // valueDefault: this.munualFolio,
            /*  rules: [{
                required: true,
                message: "请输入主单号",
                trigger: "blur"
              }]*/
            },
            {
              label: "分单号",
              prop: "houseBill", width: 200,
                //valueDefault: this.houseBill,
            /*  rules: [{
                required: true,
                message: "请输入分单号",
                trigger: "blur"
              }]*/
            },
            {
              label: "运输要求", width: 200,
              prop: "transportationRequire",
                span: 24,
            },
            {
              label: "运输反馈",
              prop: "feedback", width: 200,
                span: 24,
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
          businessId: function() {
              this.$set(this, "page", {
                  pageSize: 10,
                  currentPage: 1,
                  total: 0
              })
              this.onLoad(this.page);
          }
      },
      props: ["businessId"],
    methods: {
        queryPortSearchAsync(q, cb) {
            getSeaport(q).then(res => {
                if (res.status != 200) return cb([]);
                let data = res.data;
                cb(data);
            })
        },
        openLading(data){
            this.lading_show=true;
            this.seaid=data.id;
            this.business_id=data.businessId;
            this.lading_internal_order_no = data.internalOrderNo;
        },
        openFee(data){
            //this.business_idfee=data.id;
            this.fee_internal_order_no=data.internalOrderNo;
            this.fee_show=true;
        },

        openIframe(data,type) {
            this.iframe_url = "";
           let src = "http://" + document.domain
            this.iframe = ""
            this.iframe_title = "";
            //let src;
            if(type==0){
                this.iframe_title = "交接单";
                src+="/api/ocean/seabusiness/deliveryReceipt?id="+ data.id+"&type=1";
                // src= "/api/ocean/seabusiness/deliveryReceipt?id=" + data.id+"&type=1";
            }
            if(type==1){
                this.iframe_title = "海运对账单";
                src+="/api/ocean/seabusiness/statement?id="+ data.id+"&type=1";
                //src= "/api/ocean/seabusiness/statement?id=" + data.id+"&type=1";
            }
            src+= "&client=" + new Date();
            this.iframe_url = this.excel_viewer_url + encodeURIComponent(src);
             // this.iframe_url = src;
            this.iframe_show = true;
        },

        openFile(data) {
            this.file_internal_order_no = data.internalOrderNo;
            this.file_show = true;
        },
      rowSave(row, loading, done) {
          row.businessId=this.businessId;
         row.internalOrderNo=this.order_no;
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
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
          getSeaWorkrules().then(res => {
              this.order_no=res.data[0].value;
           //   alert(this.order_no);
          });
        this.loading = true;
        getList(page.currentPage, page.pageSize,this.businessId, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
            if(data.total>=1){
                let o = this.option;
                this.$set(o,"addBtn", false);
            }
            if(data.total==0){
                let o = this.option;
                this.$set(o,"addBtn", true);
            }
        });
      }
    }
  };
</script>

<style>
</style>
