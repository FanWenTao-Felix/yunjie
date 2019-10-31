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
               @row-click="more"
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

        <el-button type="primary"
                   size="small"
                   icon="el-icon-download"
                   plain
                   @click="dialogVisible=true">导出
        </el-button>


        <el-dialog
          title="选择订单导出条件"
          :visible.sync="dialogVisible"
          width="30%"
          open="dialogload()">
          <!--:before-close="handleClose"-->
          <template>
            <el-radio v-model="radio" label="1">空运出口AIR</el-radio><br><br>
            <el-radio v-model="radio" label="2">海运整柜FCL</el-radio><br><br>
            <el-radio v-model="radio" label="3">海运拼箱LCL</el-radio><br><br>
          </template>
          <template>
            <div class="block">
              <br><span class="demonstration">当前搜索条件＋时间范围（每次最长3个月)</span>
              <el-date-picker
                v-model="getdate"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
              </el-date-picker>
            </div>
          </template>

          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <a :href="choosesele()"
               @click="dialogVisible = false"
               class="el-button"
               download="AirPort.xlsx">确定
            </a>
          </span>
        </el-dialog>


      </template>
      <template slot-scope="scope" slot="menu">

        <el-popover
          placement="right"
          width="auto"
          class="popover"
          trigger="click">
          <div>订单号：{{gridData.internalOrderNo}}</div><br>

          <div>业务员：{{gridData.salesman}}</div><br>
          <div>操作员：{{gridData.operator}}</div><br>

          <div>件重体(委托)：{{gridData.inAmount}} BAGS、{{gridData.inWeight}} KGS、{{gridData.inVolumn}} CBM</div><br>
          <div>件重体(订舱)：{{gridData.customsDeclarationAmount}} BAGS、{{gridData.customsDeclarationWeight}}
            KGS、{{gridData.inAmount}} CBM</div><br>
          <div>件重体(实际)：{{gridData.goodsAmount}} BAGS、{{gridData.goodsGrossWeight}} KGS、{{gridData.goodsVolumn}}
            CBM</div><br>

          <div>运输条款：{{gridData.transportClause}}</div><br>
          <div>航空代理：{{gridData.fbAgent}}</div><br>
          <div>MAWB：{{gridData.mainOrderNo}}</div><br>
          <div>HAWB：{{gridData.subOrderNo}}</div><br>
          <div>备注：{{gridData.bz}}</div>

            <el-button slot="reference"
                       icon="el-icon-circle-plus"
                       type="text"
                       size="small">更多
          </el-button>
        </el-popover>


      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove,airdetail,seadetail} from "@/api/business/salesmanagement";
  import {mapGetters} from "vuex";

  let DIC= [
    {value: 'AIR', label: 'AIR'},
    { value: 'FCL', label: 'FCL'},
    {value: 'LCL', label: 'LCL'}
  ]

  export default {
    data() {
      return {
        form: {},
        query: {},
        loading: true,
        dialogVisible: false,
        radio: '1',//默认选择
        download:'',
        gridData:
          {
            internalOrderNo: '',
            salesman: '',
            operator: '',

            customsDeclarationWeight: '',
            customsDeclarationAmount: '',
            inVolumn: '',

            inWeight: '',
            inAmount: '',

            goodsAmount: '',
            goodsGrossWeight: '',
            goodsVolumn: '',

            transportClause: '',
            fbAgent: '',
            mainOrderNo: '',
            subOrderNo: '',
            bz: '',
          },
        getdate:'',



        page: {
          pageSize: 50,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        option: {


          printBtn:true,
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
              label: "订单号",
              prop: "internalOrderNo",
              width:'300',
              search:true,
              type:'tree',
              dicUrl: "/api/business/salesmanagement/selectorder",
              props: {
                label: "internalOrderNo",
                value: "internalOrderNo"
              },
              rules: [{
                required: true,
                message: "请输入订单号",
                trigger: "blur"
              }]
            },
            {
              label: "类型",
              prop: "tranType",
              search:true,
              type:'tree',
              dicData: DIC,
              rules: [{
                required: true,
                message: "请输入类型",
                trigger: "blur"
              }]
            },
            {
              label: "货物名称",
              prop: "goodsChineseName",
              width:'150',
              rules: [{
                required: true,
                message: "请输入货物名称",
                trigger: "blur"
              }]
            },
            {
              label: "委托方",
              prop: "client",
              width:'250',
              search:true,
              type:'tree',
              dicUrl: "/api/basicData/tenant/clientdata",

              rules: [{
                required: true,
                message: "请输入委托方",
                trigger: "blur"
              }]
            },
            {
              label: "提/运单号",
              prop: "mainOrderNo",
              width:'200',
              search:true,
              rules: [{
                required: true,
                message: "请输入提/运单号",
                trigger: "blur"
              }]
            },
            {
              label: "始发港",
              prop: "loadingPort",
              width:'150',
              rules: [{
                required: true,
                message: "请输入始发港",
                trigger: "blur"
              }]
            },
            {
              label: "中转港",
              prop: "fbDestinationPort",
              width:'150',
              rules: [{
                required: true,
                message: "请输入中转港",
                trigger: "blur"
              }]
            },
            {
              label: "目的地",
              prop: "destinationPort",
              width:'150',
              rules: [{
                required: true,
                message: "请输入目的地",
                trigger: "blur"
              }]
            },
            {
              label: "出发时间",
              prop: "fbLaunchTime",
              type: "date",
              format: "yyyy-MM-dd",
              valueFormat: "yyyy-MM-dd",
              width:'150',
              rules: [{
                required: true,
                message: "请输入出发时间",
                trigger: "blur"
              }]
            },
            {
              label: "到达时间",
              prop: "sbArrivalTime",
              type: "date",
              format: "yyyy-MM-dd",
              valueFormat: "yyyy-MM-dd",
              width:'150',
              rules: [{
                required: true,
                message: "到达时间",
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
          airdetail();
          seadetail();
        });
      },
      refreshChange(){
        this.onLoad(this.page);
      },


      more(row){
        this.gridData.internalOrderNo=row.internalOrderNo;
        this.gridData.salesman=row.salesman;
        this.gridData.operator=row.operator;

        /*报关*/
        this.gridData.customsDeclarationWeight=row.customsDeclarationWeight;
        this.gridData.customsDeclarationAmount=row.customsDeclarationAmount;
        this.gridData.inVolumn=row.inVolumn;

        /*委托*/
        this.gridData.inWeight=row.inWeight;
        this.gridData.inAmount=row.inAmount;
        this.gridData.inVolumn=row.inVolumn;

        /*实际*/
        this.gridData.goodsAmount=row.goodsAmount;
        this.gridData.goodsGrossWeight=row.goodsGrossWeight;
        this.gridData.goodsVolumn=row.goodsVolumn;

        this.gridData.transportClause=row.transportClause;
        this.gridData.fbAgent=row.fbAgent;
        this.gridData.mainOrderNo=row.mainOrderNo;
        this.gridData.subOrderNo=row.subOrderNo;
        this.gridData.bz=row.bz;
      },

      choosesele(){
        function checkTime(i){
          if(i<10){
            i = '0'+i
          }
          return i
        }
        var get=this.getdate;
        for (var i=0;i<get.length;i++){
          var time=get[i];
          var date;
          date=new Date(time);
          var dateTime = date.getFullYear()+'-'+checkTime(date.getMonth()+1)+'-'+checkTime(date.getDate());
          if(i==0){
            var begintime=dateTime;
          }else{
            var endtime=dateTime;
          }
        }
        var type=this.radio;
return '/api/business/salesmanagement/findbyselect?begintime='+begintime+'&endtime='+endtime+'&type='+type;
      },
      },
  };
</script>

<style>
</style>
