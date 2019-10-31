<template>
  <basic-container>
    <avue-form v-model="getdate"
               :option="optiondate">
    </avue-form>
    <div>
      <div><span style="color: #ff4d51">合计：+应收款{{r_total}}  -应付款{{p_total}}=毛利润{{r_total-p_total}}（分币种合计）</span></div>
    </div>
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
               @on-load="onLoad"
               @refresh-change="refreshChange">
      <template slot="menuLeft">
      </template>
      <template slot-scope="scope" slot="menu">

      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail} from "@/api/financial/receivablepayable.js";
  import {add, update, remove} from "@/api/statistics/profitebusiness";
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {
        r_total: 0,
        p_total: 0,
        getall:{
          payableAmount:0,
          receivableAmount:0,
          normalProfit:0,
        },
        getdate:{},
        form: {},
        query: {},
        loading: true,
        dialogVisible: false,
        page: {
          pageSize: 100,
          currentPage: 1,
          total: 0
        },
        selectionList: [],

        optiondate:{
          menuBtn:false,
          column:[
            {
              label: "日期",
              prop: "daterange",
              type: "daterange",
              startPlaceholder: '开始日期',
              endPlaceholder: '结束日期',
            },
          ],
        },


        option: {
          height:'auto',
          calcHeight: 350,
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          selection: true,
          addBtn:false,
          delBtn:false,
          editBtn:false,
          align: "center",
          column: [
            {
              label: "创建时间",
              prop: "createTime",
              width:"160",
            },
            {
              label: "委托方",
              prop: "client",
              dicUrl: "/api/basicData/tenant/clientdata",
              search:true,
              type:'select',
              width:'200',
            },
            {
              label: "订单号",
              prop: "internalOrderNo",
              search:true,
              dicUrl:"/api/business/salesmanagement/selectorder",
              props: {
                label: "internalOrderNo",
                value: "internalOrderNo"
              },
              type:'tree',
              width:'230',
            },
            {
              label: "操作员",
              prop: "operator",
              type: "select",
              width: 190,
              dicUrl: "/api/basicData/alloperator",
              search:true,
              hide: false,
            },

            {
              label: "提单号",
              prop: "mainOrderNo",
              width:'120',
              type: "tree",
              dicUrl: "/api/basicData/mainOrderNo",
              search:true,
            },

            {
              label: "业务员",
              prop: "salesman",
              type: "select",
              dicUrl: "/api/basicData/tenant/alluser",
              search:true,
              hide: true,
            },
            /*{
              label: "币种",
              prop: "receiveMap",
              width: 150,
              formatter: (r, v, l, c) => {
                let data = [];
                for (let key in v) data.push(key + ":" + v[key]);
                return data.join(" \n");
              },
            },*/
            {
              label: "应收金额",
              prop: "receiveMap",
              width: 150,
              formatter: (r, v) => {
                let data = [];
                for (let key in v) data.push(key + ":" + v[key]);
                return data.join(" \n");
              },
            },
            {
              label: "应付金额",
              prop: "payMap",
              width: 150,
              formatter: (r, v) => {
                let data = [];
                for (let key in v) data.push(key + ":" + v[key]);
                return data.join(" \n");
              },
            },
            {
              label: "利润",
              prop: "profitMap",
              width: 150,
              formatter: (r, v) => {
                let data = [];
                for (let key in v) data.push(key + ":" + v[key]);
                return data.join(" \n");
              },
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
          addBtn: this.vaildData(this.permission.salesmanagement_add, false),
          viewBtn: this.vaildData(this.permission.salesmanagement_view, false),
          delBtn: this.vaildData(this.permission.salesmanagement_delete, false),
          editBtn: this.vaildData(this.permission.salesmanagement_edit, false)
        };
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      },

    },
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
            alert(row.id);
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



      p(s){return s < 10 ? '0' + s : s},
      searchChange(params) {
        this.query = params;

        const arr=this.getdate.daterange;
        var begindate;
        var enddate;
        if(arr!=null) {
          const d = new Date(arr[0]);
          const resDate = d.getFullYear() + '-' + this.p((d.getMonth() + 1)) + '-' + this.p(d.getDate());
          const resTime = this.p(d.getHours()) + ':' + this.p(d.getMinutes()) + ':' + this.p(d.getSeconds());
          const d1 = new Date(arr[1]);
          const resDate1 = d1.getFullYear() + '-' + this.p((d1.getMonth() + 1)) + '-' + this.p(d1.getDate());
          const resTime1 = this.p(d1.getHours()) + ':' + this.p(d1.getMinutes()) + ':' + this.p(d1.getSeconds());
          begindate = resDate + " " + resTime;
          enddate = resDate1 + " " + resTime1;
        }else {
          console.log("");
        }
        this.onLoad(this.page, params, begindate, enddate);
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
      onLoad(page, params = {}, begindate,enddate) {
        this.loading = true;
        getList(page.currentPage, page.pageSize, Object.assign(params, this.query), begindate,enddate).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });


        let p_total = 0;
        let r_total = 0;
        for (let item of this.data) {
          r_total += item.receiveMap.CNY;
          p_total += item.payMap.CNY;
          //console.log(item.payMap.CNY)
        }
        this.p_total = p_total.toFixed(2);
        this.r_total = r_total.toFixed(2);


        var payableAmount=0;
        var receivableAmount=0;
        var normalProfit=0;
        this.data.forEach(function (value) {
          payableAmount=payableAmount+value.payableAmount;
          receivableAmount=receivableAmount+value.receivableAmount;
          normalProfit=normalProfit+value.normalProfit;
        },0);
        /*this.getall.payableAmount=payableAmount;
        this.getall.receivableAmount=receivableAmount;
        this.getall.normalProfit=normalProfit;*/
      },
      refreshChange(){
        this.onLoad(this.page);
      },

    },
  };
</script>

<style>
</style>


