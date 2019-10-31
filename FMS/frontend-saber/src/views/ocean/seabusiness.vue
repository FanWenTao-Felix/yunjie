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
                   v-if="permission.seabusiness_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template v-if="role!=1" slot-scope="scope" slot="menu">
        <el-button @click="openOperation(scope.row)" icon="el-icon-edit" type="text" size="small">订单操作</el-button>
      </template>

      <template slot-scope="scope" slot="portLoadingForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.portLoading"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>
      <template slot-scope="scope" slot="destinationForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.destination"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>

      <template slot-scope="scope" slot="transshipmentForm">
        <el-autocomplete :disabled="scope.disabled" v-model="form.transshipment"
                         :fetch-suggestions="queryPortSearchAsync" placeholder="请输入港口"></el-autocomplete>
      </template>


    </avue-crud>
  <template v-if="operation_booking_type==1">
    <el-dialog  title="订单操作" :visible.sync="operation_show" width="90%" top="4vh">
       <seawhole :businessId="business_id"/>
    </el-dialog>
    </template>
  <template v-if="operation_booking_type==2">
    <el-dialog  title="订单操作" :visible.sync="operation_show" width="90%" top="4vh">
         <seaspell :businessId="business_id"/>
      </el-dialog>
 </template>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/ocean/seabusiness";
/*  import {getSeaWorkrules} from "@/api/basicData/basicData";*/
  import {
      getDetail as getClientData
  } from "@/api/client/clientdata";
  import {
      judgeUser,getSeaport
  } from "@/api/basicData/basicData"
  import seawhole from "./seawhole.vue"
  import seaspell from "./seaspell.vue"
  import {mapGetters} from "vuex";
  var DIC = {
      bookingType: [{
          label: '整箱',
          value: '1',
      }, {
          label: '拼箱',
          value: '2'
      }],
      billWay: [{
          label: '整本提单',
          value: '整本提单',
      }, {
          label: '电放提单',
          value: '电放提单'
      }, {
          label: '海运单',
          value: '海运单'
      }],
      cabinetType: [{
          label: '20',
          value: '20',
      }, {
          label: '40',
          value: '40'
      }, {
          label: '40H',
          value: '40H'
      }, {
          label: '45H',
          value: '45H'
      }
      ],
  /*    feeName: [{
          label: '文件费(DOC)',
          value: '文件费(DOC)',
      }, {
          label: '设备交接单费(EIR)',
          value: '设备交接单费(EIR)'
      },
          {
              label: '国际船舶和港口保安费(ISPS)',
              value: '国际船舶和港口保安费(ISPS)'
          },
          {
              label: '低硫附加费(LSS)',
              value: '低硫附加费(LSS)'
          },
          {
              label: '封条费(SEAL)',
              value: '封条费(SEAL)'
          },
          {
              label: '码头费(THC)',
              value: '码头费(THC)'
          },
          {
              label: 'AMS省报费(AMS)',
              value: 'AMS省报费(AMS)'
          },
          {
              label: '低硫燃料附加费(CLS)',
              value: '低硫燃料附加费(CLS)'
          },
          {
              label: '设备交接单费(EIR)',
              value: '设备交接单费(EIR)'
          },
          {
              label: '欧盟成本附加费(ENS)',
              value: '欧盟成本附加费(ENS)'
          },
          {
              label: '燃油成本附加费(FCR)',
              value: '燃油成本附加费(FCR)'
          },
          {
              label: '华南码头费(FCR)',
              value: '华南码头费(FCR)'
          },
          {
              label: '货币贬值附加费(CAF)',
              value: '货币贬值附加费(CAF)'
          },
          {
              label: '燃油附加费(BAF)',
              value: '燃油附加费(BAF)'
          },
          {
              label: '起运港码头附加费(ORC)',
              value: '起运港码头附加费(ORC)'
          },
          {
              label: '目的港提货费(DDC)',
              value: '目的港提货费(DDC)'
          },
          {
              label: '码头操作（吊柜）费(THC)',
              value: '码头操作（吊柜）费(THC)'
          },
          {
              label: '货币贬值附加费(CAF)',
              value: '货币贬值附加费(CAF)'
          },
          {
              label: '旺季附加费(PSS)',
              value: '旺季附加费(PSS)'
          },
      ],*/
  }

  export default {
      components: {
          seawhole,
          seaspell
      },
      created() {
          judgeUser().then(res => {
              if (res.data.code != 200 && res.data.success) return;
              let data = res.data.data;
              this.role = data;
              let o = this.option;
              let c = o.column;
            /*  if (data == 0) {
              } else */
            if (data == 1) { //业务员
                  this.$set(o, "addBtn", true);
              } else if (data == 2) { //操作员
                  this.$set(o, "addBtn", true);
                  this.$set(o, "delBtn", true);
              }
          })
      },
    data() {
      return {
          operation_show:false,
          fee_show:false,
          business_idfee:"",
          business_id:"",
          fee_internal_order_no:"",
          operation_booking_type:"",
          role: -1, //角色身份  0，admin 1 业务员 2操作员
          bookingType:"",
          consignors:"",
          order_no:"",
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
              label: "订舱类型",
              prop: "bookingType",
                width: 150,
                search:true,
                type: "select",
                dicData: DIC.bookingType,
                valueDefault:'1'
            },
            {
              label: "提单方式",
                width: 150,
              prop: "billWay",  type: "select",
                dicData: DIC.billWay,
                valueDefault:'整本提单',
            },
            {
              label: "业务员",
              prop: "salesman",
                width: 150,
                editDisabled: true,
                type: "select",
                dicUrl: "/api/basicData//tenant/alluser",
              rules: [{
                required: true,
                message: "请输入业务员",
                trigger: "blur"
              }]
            },
            {
              label: "操作员",
              prop: "operating",
                width: 150,
                editDisabled: true,
                type: "select",
                dicUrl: "/api/basicData//tenant/alluser",
              rules: [{
                required: true,
                message: "请输入操作员",
                trigger: "blur"
              }]
            },
            {
              label: "创建日期",
              prop: "creationTime",
                type: "datetime",
                width: 150,
                valueFormat: "yyyy-MM-dd HH:mm:ss",
              rules: [{
                required: true,
                message: "请输入创建日期",
                trigger: "blur"
              }]
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
                  },{
                      label: "CFS-CFS",
                      value: "CFS-CFS"
                  },{
                      label: "CFS-DOOR",
                      value: "CFS-DOOR"
                  },{
                      label: "DOOR-CFS",
                      value: "DOOR-CFS"
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
              label: "委托人",
              prop: "consignor",
                type: "select",
                hide:true,
                drag: true,
                multiple: true,
                width: 150,
                dicUrl: "/api/basicData/tenant/clientdata",
              rules: [{
                required: true,
                message: "请输入委托人",
                trigger: "blur"
              }]
            },
            {
              label: "发货人",
              prop: "nameShipper",
              type: "select",
                width: 150,
                dicUrl: "/api/basicData/tenant/clientdata",
                change: (d) => {
                    if (d.value == "" || d.value == this.form[d.column.prop]) return;
                    getClientData(d.value).then(res => {
                        if (res.data.code == 200) {
                            let data = res.data.data;
                            this.form.shipperAddress = data.englishAddress;
                        }
                    })},
            rules: [{
                required: true,
                message: "请输入发货人",
                trigger: "blur"
              }]
            },
            {
              label: "发货人地址",
              prop: "shipperAddress",
                width: 500,
             /* rules: [{
                required: true,
                message: "请输入发货人地址",
                trigger: "blur"
              }]*/
            },
            {
              label: "收货人",
              prop: "consignee",
                type: "select",
                width: 150,
                dicUrl: "/api/basicData/tenant/clientdata",
                change: (d) => {
                    if (d.value == "" || d.value == this.form[d.column.prop]) return;
                    getClientData(d.value).then(res => {
                        if (res.data.code == 200) {
                            let data = res.data.data;
                            this.form.consigneeShipper = data.englishAddress;
                        }
                    })},
              rules: [{
                required: true,
                message: "请输入收货人",
                trigger: "blur"
              }]
            },
            {
              label: "收货人地址",
              prop: "consigneeShipper",
                width: 500,
            /*  rules: [{
                required: true,
                message: "请输入收货人地址",
                trigger: "blur"
              }]*/
            },
            {
              label: "通知人",
              prop: "notifier",
                width: 150,
            /*  rules: [{
                required: true,
                message: "请输入通知人",
                trigger: "blur"
              }]*/
            },
          /*  {
              label: "起运港",
              prop: "portLoading",
              //  search:true,
                width: 150,
                formslot: true,
              //  type: "select",
             //   dicUrl: "/api/basicData/seaportOne",
              rules: [{
                required: true,
                message: "请输入起运港",
                trigger: "blur"
              }]
            },
            {
              label: "目的港",
              prop: "destination",
                width: 150,
                formslot: true,
             //   search:true,
              ///  type: "select",
              //  dicUrl: "/api/basicData/seaportOne",
              rules: [{
                required: true,
                message: "请输入目的港",
                trigger: "blur"
              }]
            },
            {
              label: "中转港",
              prop: "transshipment",
                width: 150,
                formslot: true,
               // search:true,
              //  type: "select",
             //   dicUrl: "/api/basicData/seaportOne",
             /!* rules: [{
                required: true,
                message: "请输入中转港",
                trigger: "blur"
              }]*!/
            },
            {
              label: "船东",
              prop: "shipowner",
                search:true,
                width: 150,
              rules: [{
                required: true,
                message: "请输入船东",
                trigger: "blur"
              }]
            },*/
            {
              label: "货物名称",
              prop: "cargoName",
                width: 150,
              rules: [{
                required: true,
                message: "请输入货物名称",
                trigger: "blur"
              }]
            },
            {
              label: "货物中文名称",
              prop: "cargoChinese",
                width: 150,
           /*   rules: [{
                required: true,
                message: "请输入货物中文名称",
                trigger: "blur"
              }]*/
            },
              {
                  label: "货好时间",
                  prop: "goodsTime",
                  width: 150,
                  type: "datetime",
                  valueFormat: "yyyy-MM-dd HH:mm:ss",
              },
            {
              label: "船期",
              prop: "sailSchedule",
                width: 150,
             /* rules: [{
                required: true,
                message: "请输入船期",
                trigger: "blur"
              }]*/
            },
            {
              label: "货物描述",
              prop: "description",
                width: 150,
              /*rules: [{
                required: true,
                message: "请输入货物描述",
                trigger: "blur"
              }]*/
            },
            {
              label: "货物备注",
              prop: "cargoRemark",
                width: 150,
             /* rules: [{
                required: true,
                message: "请输入货物备注",
                trigger: "blur"
              }]*/
            },
            {
              label: "货物件数",
              prop: "numberUnits",
                width: 150,
                type: "number",
                maxRows: 9999,
                minRows: 0,
              //  precision: 2,
                valueDefault: 0,
            /*  rules: [{
                required: true,
                message: "请输入货物件数(包装)",
                trigger: "blur"
              }]*/
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
                width: 150,
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            /*  rules: [{
                required: true,
                message: "请输入货物毛重",
                trigger: "blur"
              }]*/
            },
            {
              label: "货物体积",
              prop: "volume",
                width: 150,
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
             /* rules: [{
                required: true,
                message: "请输入货物体积",
                trigger: "blur"
              }]*/
            },
            {
              label: "货值",
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
              prop: "value",
            /*  rules: [{
                required: true,
                message: "请输入货值",
                trigger: "blur"
              }]*/
            },
              {
                  label: "出货条款",
                  prop: "shipmentClause",
                  width: 150,
                  type:"radio",
                  dicData: [{
                      label: "双清",
                      value: "双清"
                  }, {
                      label: "DDP",
                      value: "DDP"
                  },{
                      label: "CIF",
                      value: "CIF"
                  }, {
                      label: "FOB",
                      value: "FOB"
                  },{
                      label: "其他",
                      value: "其他"
                  }],
                 /* mock:{
                      type:'dicData'
                  },*/
              },
              {
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
            {
                  label: "出货要求",
                  prop: "deliveryRequirements",
                  width: 150,
                type: "checkbox",
                dicData: [{
                    label: "需要买保险",
                    value: "需要买保险"
                }, {
                    label: "需要产地证",
                    value: "需要产地证"
                },{
                    label: "需要申请目的港14天",
                    value: "需要申请目的港14天"
                }, {
                    label: "需要申请目的港21天",
                    value: "需要申请目的港21天"
                }]
              },
            /*  {
                  label: "费用名称",
                  prop: "feeName",
                  width: 150,
                  type: "select",
                  dicData: DIC.feeName,
                  rules: [{
                      required: true,
                      message: "请输入费用名称",
                      trigger: "blur"
                  }]
              },*/
            {
              label: "销售价",
              prop: "salesPrice",
                width: 150,
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            },
              {
                  label: "币种",
                  prop: "currency",
                  width: 150,
                  type: "select",
                  dicUrl: "/api/basicData/currency",
                /*  rules: [{
                      required: true,
                      message: "请输入币种",
                      trigger: "blur"
                  }]*/
              },
              {
              label: "柜型",
              prop: "cabinetType",
                  width: 150,
                  type: "select",
                  dicData: DIC.cabinetType,
            },
            {
              label: "柜量",
              prop: "quantity",
                type: "number",
              /*  maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,*/
                width: 150,},
              {
                  label: "业务状态",
                  prop: "businessState",
                  type:"select",
                  width: 150,
                  search:true,
                  dicData: [{
                      label: "草稿",
                      value: "草稿"
                  }, {
                      label: "未审核",
                      value: "未审核"
                  },{
                      label: "审核中",
                      value: "审核中"
                  }, {
                      label: "已审核",
                      value: "已审核"
                  },{
                      label: "已完成",
                      value: "已完成"
                  },{
                      label: "已拒绝",
                      value: "已拒绝"
                  }, {
                      label: "已取消",
                      value: "已取消"
                  }],
                },
              {
                  label: "唛头",
                  prop: "shippingMark",
                  width: 500,
                  span: 24,
              }, /*{
              label: "报关行信息",
                  width: 500,
              prop: "customsBroker",
                span: 24,
            }, {
              label: "拖车行信息",
                  width: 500,
              prop: "trailerCompany",
                span: 24,
            },*/
              {
                  label: "供货人",
                  width: 500,
                  prop: "supplier",
                  span: 24,
              },
              {
                  label: "放货人代理",
                  width: 500,
                  prop: "consigneeAgent",
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
    methods: {
        queryPortSearchAsync(q, cb) {
            getSeaport(q).then(res => {
                if (res.status != 200) return cb([]);
                let data = res.data;
                cb(data);
            })
        },
        openOperation(data) {
            this.operation_show=true;
            this.business_id=data.id;
            this.operation_booking_type=data.bookingType;
        },
        arrayToString: function(a) {
            let str = "";
            for (let i = 0; i < a.length; i++) {
                str += a[i];
                if (i >= a.length - 1) break;
                str += ","
            }
            return str;
        },
        rowSave(row, loading, done) {
            row.deliveryRequirements = this.arrayToString(row.deliveryRequirements);
            row.consignor = this.arrayToString(row.consignor);
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
          row.deliveryRequirements = this.arrayToString(row.deliveryRequirements);
          row.consignor = this.arrayToString(row.consignor);
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
              let a=this.form.deliveryRequirements.split(",");
              this.form.deliveryRequirements=a;
              let b=this.form.consignor.split(",");
              this.form.consignor=b;
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
