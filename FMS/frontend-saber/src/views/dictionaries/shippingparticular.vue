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
                   v-if="permission.shippingparticular_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getDetail, add, update, remove,getRelativeList} from "@/api/dictionaries/shippingparticular";
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
              label: "起运港",
              prop: "originPort",
              rules: [{
                required: true,
                message: "请输入起运港",
                trigger: "blur"
              }],
                search:true,
                type: "select",
                dicUrl: "/api/basicData/airportOne",
            },
            {
              label: "目的港",
              prop: "destinationPort",
              rules: [{
                required: true,
                message: "请输入目的港",
                trigger: "blur"
              }],
                search:true,
                dicUrl: "/api/basicData/airportOne",
                type: "select",
            },
            {
              label: "截关/开船",
              prop: "stopOpen",
              rules: [{
                required: true,
                message: "请输入截关/开船",
                trigger: "blur"
              }]
            },
            {
              label: "船程",
              prop: "boatRide",
            /*  rules: [{
                required: true,
                message: "请输入船程",
                trigger: "blur"
              }]*/
            },
            {
              label: "中转",
              prop: "transfer",
            /*  rules: [{
                required: true,
                message: "请输入中转",
                trigger: "blur"
              }]*/
            },
            {
              label: "GP20",
              prop: "gpTwenty",
             /* rules: [{
                required: true,
                message: "请输入GP20",
                trigger: "blur"
              }]*/
            },
            {
              label: "GP40",
              prop: "gpForty",
             /* rules: [{
                required: true,
                message: "请输入GP40",
                trigger: "blur"
              }]*/
            },
            {
              label: "HC40",
              prop: "hcForty",
             /* rules: [{
                required: true,
                message: "请输入HC40",
                trigger: "blur"
              }]*/
            },
            {
              label: "HC45",
              prop: "hcFortyFive",
            /*  rules: [{
                required: true,
                message: "请输入HC45",
                trigger: "blur"
              }]*/
            },
            {
              label: "运价备注",
              prop: "shippingNote",
             /* rules: [{
                required: true,
                message: "请输入运价备注",
                trigger: "blur"
              }]*/
            },
            {
              label: "有效期",
              prop: "periodValidity",
              rules: [{
                required: true,
                message: "请输入有效期",
                trigger: "blur"
              }]
            },
            {
              label: "船司id",
              prop: "lineId",
              rules: [{
                required: true,
                message: "请输入船司id",
                trigger: "blur"
              }],
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
      props: ['shippingLine_id'],
      watch: {
          shippingLine_id: function(n) {
               //alert(n);
              if (n == "") this.data = {};
              this.refreshPage();
          }
      },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          addBtn: this.vaildData(this.permission.shippingparticular_add, true),
          viewBtn: this.vaildData(this.permission.shippingparticular_view, true),
          delBtn: this.vaildData(this.permission.shippingparticular_delete, true),
          editBtn: this.vaildData(this.permission.shippingparticular_edit, true)
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
          row.lineId = this.shippingLine_id;
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
        //  alert("ssssss"+this.shippingLine_id);
          if (this.shippingLine_id == "" || !this.shippingLine_id) return;
          this.loading = true;
          getRelativeList(page.currentPage, page.pageSize, this.shippingLine_id,Object.assign(params, this.query)).then(res => {
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
