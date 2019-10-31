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
                   v-if="permission.topmenu_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-button size="small"
                   icon="el-icon-delete"
                   @click="handleMenuSetting"
                   v-if="permission.topmenu_setting"
                   plain>菜单配置
        </el-button>
      </template>
      <template slot-scope="{row}"
                slot="source">
        <div style="text-align:center">
          <i :class="row.source"></i>
        </div>
      </template>
    </avue-crud>
    <el-dialog title="下级菜单配置"
               :visible.sync="box"
               width="345px">

      <el-tree :data="menuGrantList"
               show-checkbox
               node-key="id"
               ref="treeMenu"
               :default-checked-keys="menuTreeObj"
               :props="props">
      </el-tree>

      <span slot="footer"
            class="dialog-footer">
        <el-button @click="box = false">取 消</el-button>
        <el-button type="primary"
                   @click="submit">确 定</el-button>
      </span>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove, grant, grantTree, getTopTree} from "@/api/system/topmenu";
  import {mapGetters} from "vuex";
  import iconList from "@/config/iconList";

  export default {
    data() {
      return {
        form: {},
        box: false,
        query: {},
        loading: true,
        props: {
          label: "title",
          value: "key"
        },
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        selectionList: [],
        menuGrantList: [],
        menuTreeObj: [],
        option: {
          height:'auto',
          calcHeight:350,
          tip: false,
          border: true,
          index: true,
          viewBtn: true,
          selection: true,
          dialogWidth: 200,
          dialogHeight: 200,
          column: [
            {
              label: "菜单名",
              prop: "name",
              search: true,
              rules: [{
                required: true,
                message: "请输入菜单名",
                trigger: "blur"
              }]
            },
            {
              label: "菜单图标",
              prop: "source",
              type: "icon-select",
              slot: true,
              iconList: iconList,
              rules: [
                {
                  required: true,
                  message: "请输入菜单图标",
                  trigger: "click"
                }
              ]
            },
            {
              label: "菜单编号",
              prop: "code",
              search: true,
              rules: [{
                required: true,
                message: "请输入菜单编号",
                trigger: "blur"
              }]
            },
            {
              label: "菜单排序",
              prop: "sort",
              type: "number",
              rules: [{
                required: true,
                message: "请输入菜单排序",
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
          addBtn: this.vaildData(this.permission.topmenu_add, false),
          viewBtn: this.vaildData(this.permission.topmenu_view, false),
          delBtn: this.vaildData(this.permission.topmenu_delete, false),
          editBtn: this.vaildData(this.permission.topmenu_edit, false)
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
      submit() {
        const menuList = this.$refs.treeMenu.getCheckedKeys().join(",");
        grant(this.ids, menuList).then(() => {
          this.box = false;
          this.$message({
            type: "success",
            message: "操作成功!"
          });
          this.onLoad(this.page);
        });
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
      handleMenuSetting() {
        if (this.selectionList.length !== 1) {
          this.$message.warning("只能选择一条数据");
          return;
        }
        this.menuTreeObj = [];
        grantTree()
          .then(res => {
            this.menuGrantList = res.data.data.menu;
            getTopTree(this.ids).then(res => {
              this.menuTreeObj = res.data.data.menu;
              this.box = true;
            });
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
