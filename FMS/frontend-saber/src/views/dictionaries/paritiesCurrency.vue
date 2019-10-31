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
        <div>报表汇率：</div>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.parities_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/dictionaries/paritiesCurrency";
  import {mapGetters} from "vuex";

  export default {
      created: function() {},
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
           /* align: "center",
            dialogTop: 25,
            labelWidth: 150,
            dialogHeight: 600,
            dialogWidth: "40%",*/
          column: [
          /*  {
              label: "编号",
              prop: "id",
              rules: [{
                required: true,
                message: "请输入",
                trigger: "blur"
              }]
            },*/
            {
              label: "原币种",
              prop: "originalCurrency",
                type: 'tree',
                search:true,
                dicData:[
                    { label: "RMB", value: "RMB"},
                    { label: "USD", value: "USD"},
                    { label: "HKD", value: "HKD"},
                    { label: "EU", value: "EU"},
                ],
              rules: [{
                required: true,
                message: "请输入原币种",
                trigger: "blur"
              }]
            },
            {
              label: "宿币种",
              prop: "nightMoney",
                type: 'tree',
                dicData:[
                    { label: "RMB", value: "RMB"},
                    { label: "USD", value: "USD"},
                    { label: "HKD", value: "HKD"},
                    { label: "EU", value: "EU"},
                ],
              rules: [{
                required: true,
                message: "请输入宿币种",
                trigger: "blur"
              }]
            },
            {
              label: "基数",
              prop: "cardinalNumber",
              rules: [{
                required: true,
                message: "请输入基数",
                trigger: "blur"
              }]
            },
            {
              label: "汇率",
              prop: "parities",
              rules: [{
                required: true,
                message: "请输入汇率",
                trigger: "blur"
              }]
            },
            {
              label: "有效开始日期",
              prop: "validStart",
                type: "date",
                search:true,
                format: "yyyy-MM-dd hh:mm:ss",
                valueFormat: "yyyy-MM-dd hh:mm:ss",
              rules: [{
                required: true,
                message: "请输入有效开始日期",
                trigger: "blur"
              }]
            },
            {
              label: "有效结束日期",
              prop: "effectiveEnd",
                type: "date",
               search:true,
                format: "yyyy-MM-dd hh:mm:ss",
                valueFormat: "yyyy-MM-dd hh:mm:ss",
              rules: [{
                required: true,
                message: "请输入有效结束日期",
                trigger: "blur"
              }]
            },
              {
                  label: "是否显示",
                  prop: "isshow",
                  type: "radio",
                  hide: true,
                  dicData:[
                      { label: "是", value: "1"},
                      { label: "否", value: "2"},
                  ],
                  rules: [{
                      required: true,
                      trigger: "blur"
                  }]
              },
              {
                  label: "汇率类型",
                  prop: "paritiesType",
                  readonly:true,
                  valueDefault:"标准汇率"
              },
              {
                  label: "默认设置",
                  prop: "defaultSetting",
                  type: "radio",
                  hide: true,
                  dicData:[
                      { label: "是", value: "1"},
                      { label: "否", value: "2"},
                  ],
                  rules: [{
                      required: true,
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
          addBtn: this.vaildData(this.permission.parities_add,true),
          viewBtn: this.vaildData(this.permission.parities_view,true),
          delBtn: this.vaildData(this.permission.parities_delete,true),
          editBtn: this.vaildData(this.permission.parities_edit,true)
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
