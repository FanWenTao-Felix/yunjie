<template>
  <basic-container>

    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form" ref="crud"
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

        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain v-if="permission.air_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-upload class="upload-demo inline-block margin-right-10"
                   action="/api/dictionaries/air/uploadfile"
                   ref="upload"
                   :on-preview="handlePreview"
                   :on-remove="handleRemove"
                   :before-remove="beforeRemove"
                   :before-upload="beforeUpload"
                   :on-success="handleSuccess"
                   :on-error="handleError"
                   :auto-upload="true"
                   :limit="1"
                   :on-exceed="handleExceed"
                   :file-list="fileList">
          <el-button size="small"
                     type="primary">导入excel
          </el-button>
        </el-upload>
        <el-button type="primary" size="small" plain>
          <a class='download'
             :href='url'
             download="airport.xlsx"
             title="下载">下载模板
          </a>
        </el-button>
      </template>
    </avue-crud>

  </basic-container>
</template>

<script>
  import {
    getList,
    getDetail,
    add,
    update,
    remove,
    download
  } from "@/api/dictionaries/air";
  import {
    mapGetters
  } from "vuex";

  export default {
    data() {
      return {
        url: '/api/dictionaries/air/download',
        fileList: [],
        form: {},
        query: {},
        loading: true,
        page: {
          pageSize: 100,
          currentPage: 1,
          total: 0
        },
        selectionList: [], //被选择的数据条数集合
        option: {
          excelBtn: true,
          height: 'auto',
          calcHeight: 350,
          tip: false,
          border: true,
          index: false,
          viewBtn: true,
          selection: true,
          search: true,
          align: "center",
          column: [
            {
              label: "洲",
              prop: "airContinent",
              rules: [{
                required: true,
                message: "请输入洲名",
                trigger: "blur"
              }]
            },
            {
              label: "国家/地区",
              prop: "airCountryname",
              search: true,
              /*dicUrl: "/api/dictionaries/air/select",
              type: 'tree',
              props: {
                label: "airCountryname",
                value: "airCountryname"
              },*/
              rules: [{
                required: true,
                message: "请输入国家/地区",
                trigger: "blur"
              }]
            },
            {
              label: "城市英文名",
              prop: "airEncityname",
              search: true,
             /* type:"tree",
              dicUrl: "/api/dictionaries/air/select",
              props: {
                label: "airEncityname",
                value: "airEncityname"
              },*/
              rules: [{
                required: true,
                message: "城市英文名",
                trigger: "blur"
              }]
            },
            {
              label: "城市中文名",
              prop: "airChcityname",
              search: true,
              /* type:"tree",
               dicUrl: "/api/dictionaries/air/select",
               props: {
                 label: "airChcityname",
                 value: "airChcityname"
               },*/
              rules: [{
                required: true,
                message: "城市中文名",
                trigger: "blur"
              }]
            },
            {
              label: "三字代码",
              prop: "airCode",
              search: true,
              /*type:"tree",
              dicUrl: "/api/dictionaries/air/select",
              props: {
                label: "airCode",
                value: "airCode"
              },*/
              rules: [{
                required: true,
                message: "请输入机场代码",
                trigger: "blur"
              }]
            },
            {
              label: "机场名",
              prop: "fullName",
              rules: [{
                required: true,
                message: "请输入全称",
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
          addBtn: this.vaildData(this.permission.air_add),
          viewBtn: this.vaildData(this.permission.air_view),
          delBtn: this.vaildData(this.permission.air_delete),
          editBtn: this.vaildData(this.permission.air_edit)
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
      handleGet() {
        if (this.selectionList.length == 0) {
          this.$message.warning("至少选择一条数据导出~");
        } else {
          this.$confirm("确定将选择数据导出?", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(() => {
              return download();
            })
            .then(() => {
              this.onLoad(this.page);
              this.$message({
                type: "success",
                message: "导出成功!"
              });
            }), error => {
            this.$message({
              type: "error",
              message: "导出失败!"
            });
            console.log(error);
          };
        }

      },


      handleRemove(file, fileList) { //删除上传的文件触发
        console.log(file, fileList);
      },

      handlePreview(file) { //点击上传的文件触发
        console.log(file);
      },

      handleExceed(files, fileList) {
        this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
      },

      beforeRemove(file) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      },

      beforeUpload(file) {
        const Xls = file.name.split('.');
        if (Xls[1] === 'xls' || Xls[1] === 'xlsx') {
          return file;
        } else {
          this.$message.error('上传文件只能是 xls/xlsx 格式')
          this.beforeRemove(file);
          return false
        }
      },
      handleSuccess() {
        this.$notify.success({
          title: '成功',
          message: '文件上传成功'
        });
        this.$refs.upload.clearFiles();
        this.onLoad(this.page);
      },
      reset() {
        document.getElementsByClassName('.el-upload-list el-upload-list--text').innerHTMl = "";
      },
      handleError() {
        this.$notify.error({
          title: '错误',
          message: '文件上传失败'
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
      },
      refreshChange() {
        this.onLoad(this.page);
      }
    }
  };
</script>

<style>
  .inline-block {
    display: inline-block;
  }
</style>
