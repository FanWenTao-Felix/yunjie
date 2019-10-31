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
                   v-if="permission.additionfee_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/ocean/additionfee";
  import {mapGetters} from "vuex";
  var DIC = {
      feeName: [{
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
      ],
      units: [{
          label: '票',
          value: '票',
      }, {
          label: '箱',
          value: '箱'
      }]

  }
  export default {
      watch: {
          businessId: function(o, n) {
              this.refreshPage();
          }
      },
      created() {},
      props: ["businessId"],
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
          tip: false,
          border: true,
          index: true,
          viewBtn: true,
          selection: true,
          column: [
            {
              label: "主键",
              prop: "id",
              rules: [{
                required: true,
                message: "请输入主键",
                trigger: "blur"
              }],
                hide:true,
                editDisplay: false,
                viewDisplay: false,
                addDisplay: false,
            },
              {
                  label: "业务订单id",
                  prop: "businessId",
                  hide:true,
                  editDisplay: false,
                  viewDisplay: false,
                  addDisplay: false,
                 // valueDefault: this.businessId
              },
            {
              label: "附加费名称",
              prop: "feeName",
                type: "select",
                dicData: DIC.feeName,
              rules: [{
                required: true,
                message: "请输入附加费名称",
                trigger: "blur"
              }]
            },
            {
              label: "单位",
              prop: "units",
                type: "select",
                dicData: DIC.units,
              rules: [{
                required: true,
                message: "请输入单位",
                trigger: "blur"
              }]
            },
            {
              label: "柜型",
              prop: "cabinetType",
              rules: [{
                required: true,
                message: "请输入柜型",
                trigger: "blur"
              }]
            },
            {
              label: "20",
              prop: "twenty",
            /*  rules: [{
                required: true,
                message: "请输入20",
                trigger: "blur"
              }],*/
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            },
            {
              label: "40",
              prop: "forty",
            /*  rules: [{
                required: true,
                message: "请输入40",
                trigger: "blur"
              }],*/
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            },
            {
              label: "40H",
              prop: "fortyH",
             /* rules: [{
                required: true,
                message: "请输入40H",
                trigger: "blur"
              }],*/
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            },
            {
              label: "45H",
              prop: "fortyFive",
              /*rules: [{
                required: true,
                message: "请输入45H",
                trigger: "blur"
              }],*/
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            },
            {
              label: "单票价格",
              prop: "singlePrice",
             /* rules: [{
                required: true,
                message: "请输入单票价格",
                trigger: "blur"
              }],*/
                type: "number",
                maxRows: 9999,
                minRows: 0,
                precision: 2,
                valueDefault: 0,
            },
            {
              label: "币种",
              prop: "currency",
                type: "select",
                dicUrl: "/api/basicData/currency",
             rules: [{
                required: true,
                message: "请输入币种",
                trigger: "blur"
              }]
            },
              {
                  label: "应收/应付",
                  prop: "type",
                  type: "radio",
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
                      message: "请输入应收/应付",
                      trigger: "blur"
                  }]
              },
              {
                  label: "结算单位",
                  prop: "settlementUnit",
                  type: "select",
                  dicUrl: "/api/basicData/supplier",
                  rules: [{
                      required: true,
                      message: "请选择结算单位",
                      trigger: "blur"
                  }]
              },
              {
                  label: "状态",
                  prop: "status",
                  type: "select",
                  disabled: true,
                  valueDefault: 0,
                  dicData: [{
                      label: "审核不通过",
                      value: -1,
                  }, {
                      label: "未审核",
                      value: 0
                  }, {
                      label: "审核中",
                      value: 1
                  }, {
                      label: "审核通过",
                      value: 2
                  }, {
                      label: "未核销",
                      value: 3
                  }, {
                      label: "核销中",
                      value: 4
                  }, {
                      label: "已核销",
                      value: 5
                  }],
                  rules: [{
                      required: true,
                      message: "请选择状态",
                      trigger: "blur"
                  }]
              },
            {
              label: "备注",
              prop: "remarks",
            /*  rules: [{
                required: true,
                message: "请输入备注",
                trigger: "blur"
              }]*/
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
          row.businessId = this.businessId;
         // alert(row.businessId);
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
        refreshPage() {
            this.$set(this, "page", {
                pageSize: 9999,
                pageSizes: [9999],
                currentPage: 1,
                total: 0
            })
            this.onLoad(this.page);
        },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
          //alert("ss")
        this.loading = true;
        getList(page.currentPage, page.pageSize, this.businessId,Object.assign(params, this.query)).then(res => {
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
