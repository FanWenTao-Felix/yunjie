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
                   v-if="permission.airfreight_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import { getDetail, add, update, remove,getRelativeList} from "@/api/dictionaries/airfreight";
  import {mapGetters} from "vuex";

  export default {
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
              label: "地区",
              prop: "region",
              rules: [{
                required: true,
                message: "请输入地区",
                trigger: "blur"
              }]
            },
            {
              label: "起运港口",
              prop: "originPort",
                type: "select",
                dicUrl: "/api/basicData/airportOne",
              rules: [{
                required: true,
                message: "请输入起运港口",
                trigger: "blur"
              }]
            },
            {
              label: "到达港口",
              prop: "destinationPort",
                type: "select",
                dicUrl: "/api/basicData/airportOne",
              rules: [{
                required: true,
                message: "请输入到达港口",
                trigger: "blur"
              }]
            },
            {
              label: "最大值",
              prop: "leastValue",
                valueDefault: 0,
              /*rules: [{
                required: true,
                message: "请输入最大值",
                trigger: "blur"
              }]*/
            },
            {
              label: "最小值",
              prop: "normalValue",
                valueDefault: 0,
             /* rules: [{
                required: true,
                message: "请输入最小值",
                trigger: "blur"
              }]*/
            },
            {
              label: "+45KG",
              prop: "fortyFive",
                valueDefault: 0,
            /*  rules: [{
                required: true,
                message: "请输入+45KGS",
                trigger: "blur"
              }]*/
            },
            {
              label: "+100KG",
              prop: "oneHundred",
                valueDefault: 0,
             /* rules: [{
                required: true,
                message: "请输入+100KGS",
                trigger: "blur"
              }]*/
            },
            {
              label: "+300KG",
              prop: "threeHundred",
                valueDefault: 0,
             /* rules: [{
                required: true,
                message: "请输入+300KGS",
                trigger: "blur"
              }]*/
            },
            {
              label: "+500KG",
              prop: "fiveHundred",
                valueDefault: 0,
              /*rules: [{
                required: true,
                message: "请输入+500KGS",
                trigger: "blur"
              }]*/
            },
            {
              label: "+1000KG",
              prop: "oneThousand",
                valueDefault: 0,
              /*rules: [{
                required: true,
                message: "请输入+1000KGS",
                trigger: "blur"
              }]*/
            },
            {
              label: "ROUTING",
              prop: "routing",
            /*  rules: [{
                required: true,
                message: "请输入ROUTING",
                trigger: "blur"
              }]*/
            },
            {
              label: "2nd",
              prop: "twoNd",
            /*  rules: [{
                required: true,
                message: "请输入2nd",
                trigger: "blur"
              }]*/
            },
            {
              label: "3th",
              prop: "threeTh",
            /*  rules: [{
                required: true,
                message: "请输入3th",
                trigger: "blur"
              }]*/
            },
            {
              label: "时效",
              prop: "aging",
              rules: [{
                required: true,
                message: "请输入时效",
                trigger: "blur"
              }]
            },
            {
              label: "备注",
              prop: "remark",
             /* rules: [{
                required: true,
                message: "请输入备注",
                trigger: "blur"
              }],*/
            },
          ]
        },
        data: []
      };
    },
      props: ['aitfreight_id'],
      watch: {
          aitfreight_id: function(n) {
              // alert(n);
              if (n == "") this.data = {};
              this.refreshPage();
          }
      },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.airfreight_add, true),
          viewBtn: this.vaildData(this.permission.airfreight_view, true),
          delBtn: this.vaildData(this.permission.airfreight_delete, true),
          editBtn: this.vaildData(this.permission.airfreight_edit, true)
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
        refreshPage() {
            this.$set(this, "page", {
                pageSize: 10,
                currentPage: 1,
                total: 0
            })
            this.onLoad(this.page, {})
        },
      rowSave(row, loading, done) {
          row.freigthInfoId = this.aitfreight_id;
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
           //alert(this.aitfreight_id);
            if (this.aitfreight_id == "" || !this.aitfreight_id) return;
        this.loading = true;
          getRelativeList(page.currentPage, page.pageSize, this.aitfreight_id,Object.assign(params, this.query)).then(res => {
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
