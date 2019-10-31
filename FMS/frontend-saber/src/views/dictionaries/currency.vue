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
                   v-if="permission.currency_delete"
                   @click="handleDelete">删 除
        </el-button>

        <el-col :span="7.5">
          <el-button type="primary" size="small"  plain>
            <a class='download' :href='url'  download="海运港管理.xlsx"  title="下载">下载模板</a></el-button></el-col>
        <el-col :span="8.1"> <el-upload  class="upload-demo" ref="upload" action="/api/dictionaries/currency/toLeadCurrency"
                    :on-success="handleSuccess"
                    :on-error="handleError"
                    :file-list="fileList"
                     multiple
                    :before-upload="beforeAvatarUpload">
          <el-button icon="el-icon-upload" type="primary" size="small" >导入</el-button>
        </el-upload>
        </el-col>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
  import {getList, getDetail, add, update, remove} from "@/api/dictionaries/currency";
  import {mapGetters} from "vuex";
  export default {

    data() {
      return {
        form: {},
        query: {},
        loading: true,
          url:'api/dictionaries/currency/download',
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
          fileList: [],
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
            /*{
              label: "编号",
              prop: "id",
               // hide: true,
              rules: [{
                required: true,
                message: "请输入",
                trigger: "blur"
              }]
            },*/
            {
              label: "货币编号",
              prop: "currencyCode",
                search:true,
              rules: [{
                required: true,
                message: "请输入货币编号",
                trigger: "blur"
              }]
            },
            {
              label: "货币名称",
              prop: "currencyName",
                search:true,
              rules: [{
                required: true,
                message: "请输入货币名称",
                trigger: "blur"
              }]
            },
            {
              label: "英文名称",
              prop: "englishName",
              rules: [{
                required: true,
                message: "请输入英文名称",
                trigger: "blur"
              }]
            },
            {
              label: "货币符号",
              prop: "currencySymbol",
              rules: [{
                required: true,
                message: "请输入货币符号",
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
          addBtn: this.vaildData(this.permission.currency_add),
          viewBtn: this.vaildData(this.permission.currency_view),
          delBtn: this.vaildData(this.permission.currency_delete),
          editBtn: this.vaildData(this.permission.currency_edit)
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
       /* handleGet:function(obj){
           // window.location.href = 'api/dictionaries/currency/download';
          //  obj.href='api/dictionaries/currency/download';
        },*/
        // 上传前对文件的判断
        beforeAvatarUpload(file) {
           // alert(file.name);
            const extension = file.name.split(".")[1] === "xls";
            const extension2 = file.name.split(".")[1] === "xlsx";
            if (!extension && !extension2) {
                alert("上传文件只能是 xls、xlsx格式!");
            }
            return extension || extension2;
        },
        handleSuccess : function(response) {
            if (response.code == 200 && response.success) {
                this.$message.success("导入成功")
                this.onLoad(this.page);
            } else {
                this.$message.error("导入失败!")
                this.onLoad(this.page);
            }
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
