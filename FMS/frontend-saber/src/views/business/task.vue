<template>
<basic-container>
  <avue-crud :option="option" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange" @search-reset="searchReset"
    @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.task_delete" @click="handleDelete">删 除
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
  remove
} from "@/api/business/task";
import {
  mapGetters
} from "vuex";

export default {
  data() {
    return {
      form: {},
      query: {},
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      option: {
        tip: false,
        border: true,
        viewBtn: true,
        align: "center",
        column: [{
            label: "工作号",
            prop: "internalOrderNo",
            disabled: true,
            display: false,
            rules: [{
              required: true,
              message: "请输入内部订单号",
              trigger: "blur"
            }]
          }, {
            label: "作业名称",
            prop: "taskName",
            type: "select",
            dicUrl: "/api/basicData/taskname",
            rules: [{
              required: true,
              message: "请输入作业名称",
              trigger: "blur"
            }]
          },
          {
            label: "服务商",
            prop: "providerId",
            type: "select",
            dicUrl: "/api/basicData/supplier",
            rules: [{
              required: true,
              message: "请输入服务商(关联client_data_id)",
              trigger: "blur"
            }]
          },
          {
            label: "地点",
            prop: "location",
            rules: [{
              required: false,
              message: "请输入地点",
              trigger: "blur"
            }]
          },

          {
            label: "开始时间",
            prop: "beginTime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            type: "datetime",
            rules: [{
              required: false,
              message: "请输入开始时间",
              trigger: "blur"
            }]
          },
          {
            label: "完成时间",
            prop: "finishTime",
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            type: "datetime",
            rules: [{
              required: false,
              message: "请输入完成时间",
              trigger: "blur"
            }]
          },
          {
            label: "项目要求",
            prop: "requirement",
            type: "textarea",
            rules: [{
              required: false,
              message: "请输入项目要求",
              trigger: "blur"
            }]
          },
          {
            label: "项目反馈",
            prop: "feedback",
            type: "textarea",
            rules: [{
              required: false,
              message: "请输入项目反馈",
              trigger: "blur"
            }]
          },
          {
            label: "是否归档",
            prop: "isArchive",
            valueDefault: false,
            type: "radio",
            dicData: [{
              label: "是",
              value: true
            }, {
              label: "否",
              value: false
            }],
            rules: [{
              required: true,
              message: "请输入归档",
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
        addBtn: this.vaildData(true, false),
        viewBtn: this.vaildData(false, false),
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
  props: ["internalOrderNo"],
  methods: {
    rowSave(row, loading, done) {
      row.internalOrderNo = this.internalOrderNo;
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
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    onLoad(page, params = {}) {
      params = {
        internalOrderNo: this.internalOrderNo
      }
      getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
      });
    }
  }
};
</script>

<style>
</style>
