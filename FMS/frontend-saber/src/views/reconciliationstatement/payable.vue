<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @search-change="searchChange" @search-reset="searchReset" @selection-change="selectionChange"
    @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot-scope="scope" slot="menu">
      <el-button @click="openDetail(scope.row)" type="text" size="small">查看详情</el-button>
    </template>
  </avue-crud>
  <el-dialog title="费用详情" :visible.sync="detail_show" width="90%" top="4vh" align="center">
    <div v-if="detail_type==0">
      <fee v-if="detail_show" :internalOrderNo="detail_internalOrderNo" />
    </div>
    <div>
      <additionfee />
    </div>
  </el-dialog>
</basic-container>
</template>

<script>
import {
  getList,
  getDetail,
} from "@/api/financial/receivablepayable.js";
import {
  mapGetters
} from "vuex";
import fee from "../business/fee.vue"
import additionfee from "../ocean/additionfee.vue"
export default {
  data() {
    return {
      detail_type: 0,
      detail_show: false,
      detail_internalOrderNo: "",
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
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        viewBtn: true,
        labelWidth: "120",
        menuWidth: 100,
        dialogClickModal: false,
        align: "center",
        column: [{
          label: "工作号",
          prop: "internalOrderNo",
          width: 250,
        }, {
          label: "主单号",
          prop: "mainOrderNo",
          width: 200,
        }, {
          label: "操作人",
          prop: "operator",
          type: "select",
          search: true,
          width: 120,
          dicUrl: "/api/basicData//tenant/alluser",
        }, {
          label: "业务员",
          prop: "salesman",
          type: "select",
          search: true,
          width: 120,
          dicUrl: "/api/basicData//tenant/alluser",
        }, {
          label: "委托人",
          prop: "client",
          search: true,
          width: 300,
          type: "select",
          dicUrl: "/api/basicData/tenant/clientdata",
        }, {
          label: "应付金额",
          prop: "payableAmount",
          type: "number",
          precision: 2,
        }, {
          label: "实付金额",
          prop: "actualPayableAmount",
          type: "number",
          precision: 2,
        }, {
          label: "未付金额",
          prop: "unPayableAmount",
          type: "number",
          precision: 2,
        }, {
          label: "合计币种",
          prop: "currency",
        }, {
          label: "创建时间",
          prop: "createTime",
          formatter: (r, v, l, c) => {
            return v.split(" ")[0];
          },
          width: 150,
        }, ]
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(false, false),
        viewBtn: this.vaildData(false, false),
        delBtn: this.vaildData(false, false),
        editBtn: this.vaildData(false, false)
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
  components: {
    fee
  },
  methods: {
    openDetail(data) {
      this.detail_type = data.businessType;
      this.detail_internalOrderNo = data.internalOrderNo;
      this.detail_show = true;
    },
    beforeOpen(done, type) {
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.internalOrderNo).then(res => {
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
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
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
