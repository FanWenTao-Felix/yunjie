<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.file_delete" @click="handleDelete">删 除
      </el-button>
    </template>
    <template slot-scope="scope" slot="menu">
      <el-button :disabled="downloadDisabled" type="text" size="small" icon="el-icon-edit" @click="downloadFile(scope.row)">下载文件
      </el-button>
    </template>
    <template slot-scope="scope" slot="fileForm">
      <el-input id="uploadFile" v-model="form.file" placeholder="请输入内容" type="file"></el-input>
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
  remove
} from "@/api/business/businessfile";
import {
  mapGetters
} from "vuex";

export default {
  data() {
    return {
      downloadDisabled: false,
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
        dialogClickModal: false,
        column: [{
            label: "内部单号",
            prop: "internalOrderNo",
            addDisplay: false,
            disabled: true,
            rules: [{
              required: true,
              message: "请输入内部单号",
              trigger: "blur"
            }]
          },
          {
            label: "文件名",
            prop: "name",
            addDisplay: false,
            disabled: true,
            rules: [{
              required: true,
              message: "请输入文件名",
              trigger: "blur"
            }]
          },
          {
            label: "文件路径",
            prop: "path",
            disabled: true,
            addDisplay: false,
            rules: [{
              required: false,
              message: "请输入文件路径",
              trigger: "blur"
            }]
          },
          {
            label: "文件类型",
            prop: "type",
            type: "select",
            disabled: true,
            dicData: [{
              label: "空运文件",
              value: 0
            }, {
              label: "海运文件",
              value: 1
            }],
            rules: [{
              required: true,
              message: "请输入文件类型(空运文件，海运文件)",
              trigger: "blur"
            }]
          },
          {
            label: "文件描述",
            prop: "description",
            rules: [{
              required: false,
              message: "请输入文件描述",
              trigger: "blur"
            }]
          },
          {
            label: "上传文件",
            prop: "file",
            formslot: true,
            hide: true,
            editDisplay: false,
            viewDisplay: false,
            rules: [{
              required: false,
              message: "请上传文件",
              trigger: "blur"
            }]
          }
        ]
      },
      data: []
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(true, false),
        viewBtn: this.vaildData(true, false),
        delBtn: this.vaildData(true, false),
        editBtn: this.vaildData(true, false)
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
  watch: {
    internalOrderNo: function(o, n) {
      this.refreshPage();
    }
  },
  created() {
    console.log(this.internalOrderNo)
  },
  props: ["internalOrderNo", "file_type"],
  methods: {
    downloadFile(data) {
      this.downloadDisabled = true;
      var downloadElement = document.createElement('a');
      var href = "/api/business/businessfile/getfile?internalOrderNo=" + data.internalOrderNo + "&name=" + data.name + "&path=" + data.path;
      downloadElement.href = href;
      downloadElement.click();
      let that = this;
      setTimeout(() => {
        that.downloadDisabled = false;
      }, 800)
    },
    fileChange(data) {
      console.log("ok", data);
    },
    refreshPage() {
      this.$set(this, "page", {
        pageSize: 10,
        currentPage: 1,
        total: 0
      })
      this.onLoad(this.page);
    },
    rowSave(row, loading, done) {
      var files = document.getElementById("uploadFile").files;
      if (files.length <= 0) {
        this.$message.error("请选择文件");
        done();
        return;
      }
      var data = new FormData();
      data.append("file", files[0]);
      data.append("internalOrderNo", this.internalOrderNo);
      data.append("description", row.description || "");
      data.append("name", row.name);
      data.append("type", row.type);
      add(data).then(() => {
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
      if (type == "add") this.$set(this, "form", {
        description: "",
        type: 0
      });
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
      getList(page.currentPage, page.pageSize, this.internalOrderNo, Object.assign(params, this.query)).then(res => {
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
