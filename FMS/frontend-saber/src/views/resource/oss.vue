<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               :before-open="beforeOpen"
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
                   v-if="permission.oss_delete"
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text"
                   icon="el-icon-check"
                   size="small"
                   v-if="permission.oss_enable"
                   plain
                   class="none-border"
                   @click.stop="handleEnable(scope.row)">启用
        </el-button>
      </template>
      <template slot-scope="{row}"
                slot="status">
        <el-tag>{{row.statusName}}</el-tag>
      </template>
      <template slot-scope="{row}"
                slot="category">
        <el-tag>{{row.categoryName}}</el-tag>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, enable} from "@/api/resource/oss";
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
          calcHeight:350,
          tip: false,
          border: true,
          index: true,
          viewBtn: true,
          selection: true,
          labelWidth: 100,
          column: [
            {
              label: "分类",
              type: "select",
              span: 24,
              width: 100,
              row: true,
              dicUrl: "/api/blade-system/dict/dictionary?code=oss",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              slot: true,
              prop: "category",
              search: true,
              rules: [{
                required: true,
                message: "请选择分类",
                trigger: "blur"
              }]
            },
            {
              label: "资源地址",
              prop: "endpoint",
              span: 24,
              search: true,
              rules: [{
                required: true,
                message: "请输入资源地址",
                trigger: "blur"
              }]
            },
            {
              label: "空间名",
              prop: "bucketName",
              span: 24,
              width: 120,
              rules: [{
                required: true,
                message: "请输入空间名",
                trigger: "blur"
              }]
            },
            {
              label: "accessKey",
              prop: "accessKey",
              span: 24,
              search: true,
              rules: [{
                required: true,
                message: "请输入accessKey",
                trigger: "blur"
              }]
            },
            {
              label: "secretKey",
              prop: "secretKey",
              span: 24,
              rules: [{
                required: true,
                message: "请输入secretKey",
                trigger: "blur"
              }]
            },
            {
              label: "是否启用",
              prop: "status",
              span: 24,
              width: 80,
              slot: true,
              addDisplay: false,
              editDisplay: false,
              viewDisplay: false,
            },
            {
              label: "备注",
              prop: "remark",
              span: 24,
              hide: true,
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
          addBtn: this.vaildData(this.permission.oss_add),
          viewBtn: this.vaildData(this.permission.oss_view),
          delBtn: this.vaildData(this.permission.oss_delete),
          editBtn: this.vaildData(this.permission.oss_edit)
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
      handleEnable(row) {
        this.$confirm("是否确定启用这条配置?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return enable(row.id);
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
  .none-border {
    border: 0;
    background-color: transparent !important;
  }
</style>
