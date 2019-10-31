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
                   v-if="permission.billoflading_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template  slot-scope="scope" slot="menu">
        <el-button @click="openSeparate(scope.row)" icon="el-icon-edit" type="text" size="small">分单/并单信息</el-button>
        <el-button @click="openIframe(scope.row)" icon="el-icon-edit" type="text" size="small">提货单</el-button>
      </template>
    </avue-crud>
    <el-dialog title="分单/并单信息" :visible.sync="lading_show" width="90%" top="4vh" :modal="false" :modal-append-to-body="false">
     <separate :internalOrderNo="lading_internal_order_no" :billId="bill_id" :businessId="business_ids"/>
    </el-dialog>

    <el-dialog :title="iframe_title" :visible.sync="iframe_show" width="90%" top="4vh" :modal="false" :modal-append-to-body="false">
      <iframe style="width:100%;height:700px;" :src="iframe_url"></iframe>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/ocean/billoflading";
  import {getDetail as getDetails} from "@/api/ocean/seabusiness";
  import {getDetail as getClientData,getDetail as getClientDatas} from "@/api/client/clientdata";

  import separate from "./separatelading.vue";
  import {mapGetters} from "vuex";

  export default {
      components: {
          separate
      },
      props: ["internalOrderNo","businessId","id","type"],
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
          lading_show:false,
          bill_id:"",
          lading_internal_order_no:"",
          business_ids:"",
         selectionList: [],
          iframe_title:"",
          iframe_url:"",
          excel_viewer_url: "http://view.officeapps.live.com/op/view.aspx?src=",
          iframe_show:false,
        option: {
          height:'auto',
          calcHeight: 350,
            dialogClickModal: false,
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
              }],hide:true,
                editDisplay: false,
                viewDisplay: false,
                addDisplay: false,
            },
            {
              label: "工作单号",
              prop: "internalOrderNo",
                editDisabled: true,
                addDisabled: true,
              rules: [{
                required: true,
                message: "请输入工作单号",
                trigger: "blur"
              }]
            },
            {
              label: "货物名称",
              prop: "cargoName",
              rules: [{
                required: true,
                message: "请输入货物名称",
                trigger: "blur"
              }]
            },
            {
              label: "货物描述",
              prop: "description",

            },
            {
              label: "唛头",
              prop: "shippingMark",

            },
              {
                  label: "交接方式",
                  prop: "connectWay",
                  type:"select",
                  dicData: [{
                      label: "CY-CY",
                      value: "CY-CY"
                  }, {
                      label: "CY-DOOR",
                      value: "CY-DOOR"
                  },{
                      label: "CY-LO",
                      value: "CY-LO"
                  }, {
                      label: "DOOR-CY",
                      value: "DOOR-CY"
                  },{
                      label: "DOOR-DOOR",
                      value: "DOOR-DOOR"
                  }],
                  rules: [{
                      required: true,
                      message: "请输入交接方式",
                      trigger: "blur"
                  }]
              },
              {
                  label: "运费支付方式",
                  prop: "freightPayWay",
                  type:"select",
                  dicData: [{
                      label: "FREIGHT COLLECT",
                      value: "FREIGHT COLLECT"
                  }, {
                      label: "FREIGHT PREPAID",
                      value: "FREIGHT PREPAID"
                  }],
                  rules: [{
                      required: true,
                      message: "请输入运费支付方式",
                      trigger: "blur"
                  }]
              },
              {
                  label: "放货方式",
                  prop: "releaseCargoWay",
                  type:"select",
                  dicData: [{
                      label: "House B/L",
                      value: "House B/L"
                  }, {
                      label: "Master B/L",
                      value: "Master B/L"
                  }, {
                      label: "Seaway Bill",
                      value: "Seaway Bill"
                      },
                      {
                          label: "Telex Release",
                          value: "Telex Release"
                      }],
                 rules: [{
                      required: true,
                      message: "请输入放货方式",
                      trigger: "blur"
                  }]
              },
            {
              label: "货物件数",
              prop: "numberUnits",
            },
              {
                  label: "货物包装",
                  prop: "goodsPackage",
                  type: "select",
                  dicUrl: "/api/basicData/package",
              },
            {
              label: "货物毛重",
              prop: "roughWeight",
            },
            {
              label: "货物体积",
              prop: "volume",
            },
              {
                  label: "发货人",
                  prop: "nameShipper",
                  type: "select",
                  width: 150,
                  dicUrl: "/api/basicData/clientdata",
                  change: (d) => {
                      if (d.value == "" || d.value == this.form[d.column.prop]) return;
                      getClientData(d.value).then(res => {
                          if (res.data.code == 200) {
                              let data = res.data.data;
                              this.form.shipperAddress = data.englishAddress;
                          }
                      })},
              },
            {
              label: "发货人地址",
              prop: "shipperAddress",
            },
            {
              label: "收货人",
              prop: "consignee",
                type: "select",
                dicUrl: "/api/basicData/clientdata",
                change: (d) => {
                    if (d.value == "" || d.value == this.form[d.column.prop]) return;
                    getClientData(d.value).then(res => {
                        if (res.data.code == 200) {
                            let data = res.data.data;
                            this.form.shipperAddress = data.englishAddress;
                        }
                    })},
            },
            {
              label: "收货人地址",
              prop: "consigneeShipper",
            },
            {
              label: "通知人",
              prop: "notifier",
            },
            {
              label: "箱量描述",
              prop: "cartonQuantity",
            },
            {
              label: "货量描述",
              prop: "cargoQuantity",
            },
            {
              label: "正本长数",
              prop: "original",
            },
            {
              label: "签发地点",
              prop: "signSite",
            },
            {
              label: "签发日期",
              prop: "signTime",
                type: "datetime",
                valueFormat: "yyyy-MM-dd HH:mm:ss",
            },
            {
              label: "签发人",
              prop: "signName",
            },
            {
              label: "提单附页",
              prop: "follower",
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
          internalOrderNo: function() {
              this.$set(this, "page", {
                  pageSize: 10,
                  currentPage: 1,
                  total: 0
              })
              this.onLoad(this.page);
          }
      },
    methods: {
        openIframe() {
            this.iframe_url = "";
            let src = "http://" + document.domain
            this.iframe = ""
            //  this.iframe_title = "";
            this.iframe_title = "海运提货单";
            src+="/api/ocean/seabusiness/billLading?id="+ this.id+"&type="+this.type;
            src+="&client=" + new Date();
            this.iframe_url = this.excel_viewer_url + encodeURIComponent(src);
            // this.iframe_url = src;
            //  this.iframe_url = this.excel_viewer_url + src;
            this.iframe_show = true;
            //let downloadElement = document.createElement('a');
            //downloadElement.href = this.iframe_url;
            //downloadElement.click();
        },

        openSeparate(data){
               this.lading_show=true,
               this.bill_id=data.id;
               this.lading_internal_order_no = data.internalOrderNo;
              this.business_ids=this.businessId;
        },
      rowSave(row, loading, done) {
          row.internalOrderNo=this.internalOrderNo;
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
          if(["add"].includes(type)){
              getDetails(this.businessId).then(res => {
                  let data = res.data.data;
                  this.form.cargoName = data.cargoName;
                  this.form.description = data.description;
                  this.form.shippingMark = data.shippingMark;
                  this.form.numberUnits =  data.numberUnits;
                  this.form.roughWeight = data.roughWeight;
                  this.form.volume = data.volume;
                 // alert(data.nameShipper);
                     let id=data.nameShipper;
                  getClientData(id).then(res => {
                      let datas = res.data.data;
                      //alert(JSON.stringify(datas));
                      this.form.nameShipper = datas.id;
                      this.form.shipperAddress=datas.chineseAddress;
                  })
                  let consignee=data.consignee;
                  getClientDatas(consignee).then(ress => {
                      let datas = ress.data.data;
                     // alert(datas.consignee);
                    // alert(JSON.stringify(datas));
                     // shortName
                      this.form.consignee = datas.id;
                      this.form.consigneeShipper=datas.chineseAddress;
                  })
                  this.form.notifier = data.notifier;
                  this.form.internalOrderNo = this.internalOrderNo;
                  this.form.supplier = data.supplier;
                  this.form.consigneeAgent = data.consigneeAgent;
                  this.form.connectWay = data.connectWay;
                  this.form.freightPayWay =  data.freightPayWay;
                  this.form.releaseCargoWay = data.releaseCargoWay;
                  this.form.goodsPackage=data.goodsPackage;
                 // let q=data.quantity;
                  this.form.cartonQuantity=data.cabinetType;

              });
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
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        getList(page.currentPage, page.pageSize,this.internalOrderNo, Object.assign(params, this.query)).then(res => {
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
