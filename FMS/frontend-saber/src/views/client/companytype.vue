<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template v-if="false" slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.companytype_delete" @click="handleDelete">删 除
      </el-button>
      <el-col :span="7.5">
        <el-upload :headers="uploadHeaders" class="upload-demo" :on-progress="batchImportProgress" action="/api/client/companytype/batchImport" :limit="1" :before-upload="beforeBatchImport" :on-success="batchImportSuccess" :file-list="fileList">
          <el-button icon="el-icon-upload" type="primary" size="small">导入
          </el-button>
        </el-upload>
      </el-col>
    </template>

    <template slot="search">
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
} from "@/api/client/companytype";
import {
  mapGetters
} from "vuex";
import {
  neccessaryHeaders
} from "@/router/axios.js";
export default {
  data() {
    return {
      fileList: [],
      form: {},
      searchForm: {},
      uploadHeaders: neccessaryHeaders,
      query: {},
      loading: true,
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      option: {
        searchBtn: false,
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        refreshBtn: false,
        columnBtn: false,
        menu: false,
        dialogClickModal: false,
        column: [{
            label: "类型代码",
            prop: "code",
            rules: [{
              required: true,
              message: "请输入类型代码",
              trigger: "blur"
            }]
          },
          {
            label: "类型名称",
            prop: "name",
            rules: [{
              required: true,
              message: "请输入类型名称",
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
  methods: {
    batchImportProgress: function(event, file) {

    },
    beforeBatchImport: function(file) {
      const isText = file.type === 'application/vnd.ms-excel'
      const isTextComputer = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      return (isText || isTextComputer)
    },
    batchImportSuccess: function(response, file, fileList) {
      if (response.code == 200 && response.success) {
        this.$message.success("导入成功")
        this.onLoad(this.page);
      } else {
        this.$message.error("导入失败")
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
      this.page.currentPage = 1;
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
