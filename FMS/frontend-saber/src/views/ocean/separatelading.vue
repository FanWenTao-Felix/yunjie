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
                   v-if="permission.separatelading_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/ocean/separatelading";
  import {getDetail as getDetails} from "@/api/ocean/seabusiness";
  import {getDetail as getClientData ,getDetail as getClientDatas} from "@/api/client/clientdata";
  import {mapGetters} from "vuex";

  export default {
      props: ["internalOrderNo","businessId","billId"],
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
              }],
                hide:true,
                editDisplay: false,
                viewDisplay: false,
                addDisplay: false,
            },
            {
              label: "工作单号",
                editDisabled: true,
                addDisabled: true,
              prop: "internalOrderNo",
              rules: [{
                required: true,
                message: "请输入工作单号",
                trigger: "blur"
              }]
            },
            {
              label: "分单号",
              prop: "subOrderNo",
              rules: [{
                required: true,
                message: "请输入分单号",
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
                  }, {
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
              label: "货物名称",
              prop: "cargoName"
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
              label: "供货人",
              prop: "supplier",
            },
            {
              label: "放货人代理",
              prop: "consigneeAgent",
            },
            {
              label: "箱量描述",
              prop: "cartonQuantityDescribe",
            },
            {
              label: "箱量",
              prop: "cartonQuantity",
            },
            {
              label: "柜号",
              prop: "containerNumber",
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
            {
              label: "备注",
              prop: "comment",
            },
            {
              label: "主单id",
              prop: "billLadingId",
                hide:true,
                editDisplay: false,
                viewDisplay: false,
                addDisplay: false,
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
      //props: ["businessId"],
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
         // alert(this.billId);
          if(["add"].includes(type)){
              getDetails(this.businessId).then(res => {
                  let data = res.data.data;
                  this.form.cargoName = data.cargoName;
                  this.form.description = data.description;
                  this.form.shippingMark = data.shippingMark;
                  this.form.numberUnits =  data.numberUnits;
                  this.form.roughWeight = data.roughWeight;
                  this.form.volume = data.volume;
                /*  this.form.nameShipper = data.nameShipper;
                  this.form.shipperAddress = data.shipperAddress;
                  this.form.consignee =  data.consignee;
                  this.form.consigneeShipper = data.consigneeShipper;*/
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
                  this.form.billLadingId=this.billId;
                //  alert(data.quantity+"*"+data.cabinetType);
                  this.form.cartonQuantity=data.cabinetType;
                  this.form.goodsPackage=data.goodsPackage;

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
        });
      }
    }
  };
</script>

<style>
</style>
