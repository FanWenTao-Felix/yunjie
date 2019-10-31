<template>
  <basic-container>
    <div>
      <div><span style="color: #ff4d51">汇总：柜量：{{getall.goodsAmount}}TEU；重量：{{getall.goodsGrossWeight}}KGS；体积：{{getall.goodsVolumn}}CBM；</span></div>
    </div>
    <div class="block">
      <br><span class="demonstration">日期：</span>
      <el-date-picker
        :picker-options="pickerOptions2"
        v-model="getdate"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期">
      </el-date-picker>
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

        <el-button @click="openDetail(scope.row)"
                   type="text"
                   size="small">票
        </el-button>

      </template>
    </avue-crud>

    <el-dialog title="详情"
               :visible.sync="detail_show"
               width="80%"
               top="4vh"
               align="center">
      <avue-crud v-if="detail_show"
                 :option="optiondetail"
                 :table-loading="loading"
                 :data="datedetail"
                 v-model="form"
                 ref="crud"
                 @on-load="onLoaddetail">
      </avue-crud>

    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, getallpiao, clientdetail, clientgetnum, airdetail, seadetail} from "@/api/statistics/clientbusiness";
  import {mapGetters} from "vuex";

  let DIC= [
    {value: 'AIR', label: 'AIR'},
    { value: 'FLC', label: 'FCL'},
    {value: 'LCL', label: 'LCL'}
  ];

  export default {
    data() {
      return {
        pickerOptions2: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },

        detail_type: 0,
        detail_show: false,
        detail_client: "",
        getall:{
          goodsAmount:0,
          goodsVolumn:0,
          goodsGrossWeight:0,
        },
        getdate:'',
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




        optiondetail:{
          menu:false,
          height:'auto',
          tip: false,
          border: true,
          index: false,
          viewBtn: false,
          addBtn:false,
          delBtn:false,
          editBtn:false,
          align: "center",
          column: [
            {
              label: "账单状态",
              prop: "billStatus",
              type: "select",
              disabled: true,
              display: false,
              dicData: [{
                label: "未归档",
                value: 0
              }, {
                label: "已归档",
                value: 1
              }],
              /*renderHeader: (h, {column}) => {
                return h('div',[
                  h('span', column.label),
                  h('span', "（总单数："+this.data.length+"票）"),
                ])
              },*/
            },
            {
              label: "订单号",
              prop: "internalOrderNo",
              props: {
                label: "internalOrderNo",
                value: "internalOrderNo"
              },
              dicUrl: "/api/business/salesmanagement/selectorder",
            },
            {
              label: "提单号",
              prop: "mainOrderNo",
              dicUrl: "/api/basicData/mainOrderNo",
            },
            {
              label: "地点/国家",
              prop: "client",
              dicUrl: "/api/basicData/tenant/clientdata",
            },
            {
              label: "时间",
              prop: "createTime",
            },
          ]
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
              label: "客户",
              prop: "client",
              dicUrl: "/api/basicData/tenant/clientdata",
              search:true,
              type:'select',
              width:'200',
              renderHeader: (h, {column}) => {
                return h('div',[
                  h('span', column.label),
                  h('span', "（总单数："+this.piao+"票）"),
                ])
              },
            },
            {
              label: "创建月份",
              prop: "createTime",
            },
            {
              label: "类型",
              prop: "tranType",
              search:true,
              type:'select',
              dicData: DIC,
            },
            {
              label: "航空/海运公司",
              prop: "code",
              type: "select",
              width: 150,
              dicUrl: "/api/basicData/companyType",
              search:true,
              hide: true,
            },
            {
              label: "业务员",
              prop: "salesman",
              type: "select",
              dicUrl: "/api/basicData/tenant/alluser",
              search:true,
              hide: true,
            },
            {
              label: "操作员",
              prop: "operator",
              type: "select",
              width: 150,
              dicUrl: "/api/basicData/tenant/alluser",
              search:true,
              hide: true,
            },
            {
              label: "提单号",
              prop: "mainOrderNo",
              /*type: "tree",
              dicUrl: "/api/basicData/mainOrderNo",
              props: {
                label: "mainOrderNo",
                value: "mainOrderNo"
              },*/
              search:true,
              hide: false,
            },
          ]
        },
        data: [],
        piao:[],
        datedetail:[],
        allpiao:[],
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


      openDetail(data) {
        this.detail_type = data.businessType;
        this.detail_client = data.client;
        this.detail_show = true;
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
        var begindate;
        var enddate;
        var goodsAmount=0;
        var goodsGrossWeight=0;
        var goodsVolumn=0;

        function checkTime(i){
          if(i<10){
            i = '0'+i
          }
          return i
        }
        var get=this.getdate;
        if(get!=null){
          for (var i=0;i<get.length;i++){
            var time=get[i];
            var date;
            date=new Date(time);
            var dateTime = date.getFullYear()+'-'+checkTime(date.getMonth()+1)+'-'+checkTime(date.getDate());
            if(i==0){
              begindate=dateTime;
            }else{
              enddate=dateTime;
            }
          }
          this.data.forEach(function (value) {
            goodsAmount=goodsAmount+value.goodsAmount;
            goodsGrossWeight=goodsGrossWeight+value.goodsGrossWeight;
            goodsVolumn=goodsVolumn+value.goodsVolumn;
          },0);
          this.getall.goodsAmount=goodsAmount;
          this.getall.goodsGrossWeight=goodsGrossWeight;
          this.getall.goodsVolumn=goodsVolumn;
          this.onLoad(this.page, params,begindate,enddate);
        }else {
          console.log("date in null")
        }
      },





      selectionChange(list) {
        this.selectionList = list;
      },
      selectionClear() {
        this.selectionList = [];
        //this.$refs.crud.toggleSelection();
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
          //this.selectionClear();
        });
      },
      refreshChange(){
        this.onLoad(this.page);
      },
      onLoaddetail(page, params = {}) {
        var num=this.detail_client;
        this.loading = true;
        clientdetail(page.currentPage, page.pageSize, Object.assign(params, this.query),num).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.datedetail = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },



    },
    mounted() {
      airdetail();
      seadetail();

      let date = new Date();
      let year = date.getFullYear().toString();
      let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1).toString() : (date.getMonth() + 1).toString();
      let da = date.getDate() < 10 ? '0' + date.getDate().toString() : date.getDate().toString();
      let beg = year + '-' + month + '-01';
      let end = year + '-' + month + '-' + da;
      this.getdate = [beg, end];

      getallpiao().then(res=>{
        const data = res.data.data;
        this.piao=data;
      });
      clientgetnum().then(res=>{
        const data = res.data.data;
        this.allpiao=data;
        //console.log(data);
      });

    }
  };
</script>

<style>
</style>


