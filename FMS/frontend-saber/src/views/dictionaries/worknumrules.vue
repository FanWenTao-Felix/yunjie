<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :before-open="beforeOpen" :permission="permissionList" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.worknumrules_delete" @click="handleDelete">删 除
      </el-button>
    </template>
    <template slot="rule" slot-scope="scope">
      <div>{{scope.row.rule}}</div>
    </template>
  </avue-crud>
  <serialnum />
</basic-container>
</template>

<script>
import {
  getList,
  getDetail,
  add,
  update,
  remove
} from "@/api/dictionaries/worknumrules";
import {
  getWorkNumRulesUser
} from "@/api/basicData/basicData.js"
import {
  mapGetters
} from "vuex";
import {
  copyObject
} from "@/common/util.js";
import serialnum from "./serialnum.vue";
const digit_of_year_list = [{
  label: "四位年",
  value: 4
}, {
  label: "两位年",
  value: 2
}, {
  label: "一位年",
  value: 1
}, {
  label: "不用年",
  value: 0
}]
const purposeList = [{
  label: "空运",
  value: 0
}, {
  label: "海运",
  value: 1
}]
const zeroingTypeList = [{
  label: "不归零",
  value: 0,
}, {
  label: "按日归零",
  value: 1,
}, {
  label: "按月归零",
  value: 2,
}, {
  label: "按年归零",
  value: 3,
}]
const trueAndFalse = [{
  label: '否',
  value: false,
}, {
  label: '是',
  value: true,
}];
export default {

  created: function() {},
  data() {
    return {
      form: {},
      query: {},
      loading: true,
      page: {
        pageSize: 5,
        pageSizes: [5],
        currentPage: 1,
        total: 0
      },
      selectionList: [],
      option: {
        height: 318,
        calcHeight: 350,
        tip: false,
        border: true,
        index: false,
        viewBtn: true,
        align: "center",
        dialogTop: 5,
        labelWidth: 150,
        dialogHeight: 725,
        dialogWidth: "80%",
        menuWidth: 200,
        dialogClickModal: false,
        column: [{
            label: "前缀",
            prop: "prefix",
            span: 24,
            width: 150,
            change: (d) => {
              if (d.value == this.form[d.column.prop]) return;
              this.renderRule(d.value, d.column.prop);
            },
            rules: [{
              required: true,
              message: "请输入前缀",
              trigger: "blur"
            }]
          },
          {
            label: "规则年份位数",
            prop: "digitOfYear",
            type: "select",
            hide: true,
            change: (d) => {
              if (d.value == this.form[d.column.prop]) return;
              this.renderRule(d.value, d.column.prop);
            },
            dicData: digit_of_year_list,
            valueDefault: 4,
            span: 10,
            rules: [{
              required: true,
              message: "请输入生成规则选择用的年份显示位数",
              trigger: "blur"
            }]
          },
          {
            label: "规则包含月份",
            prop: "ruleMonth",
            type: "radio",
            change: (d) => {
              if (d.value == this.form[d.column.prop]) return;
              this.renderRule(d.value, d.column.prop);
            },
            hide: true,
            span: 6,
            dicData: trueAndFalse,
            valueDefault: true,
            rules: [{
              required: true,
              message: "请输入生成规则包含月份",
              trigger: "blur"
            }]
          },
          {
            label: "规则包含日",
            prop: "ruleDay",
            type: "radio",
            change: (d) => {
              if (d.value == this.form[d.column.prop]) return;
              this.renderRule(d.value, d.column.prop);
            },
            hide: true,
            span: 6,
            dicData: trueAndFalse,
            valueDefault: true,
            rules: [{
              required: true,
              message: "请输入生成规则包含日",
              trigger: "blur"
            }]
          },
          {
            label: "规则包含目的港",
            prop: "rulePortDestination",
            span: 8,
            hide: true,
            type: "radio",
            valueDefault: true,
            change: (d) => {
              if (d.value == this.form[d.column.prop]) return;
              this.renderRule(d.value, d.column.prop);
            },
            dicData: trueAndFalse,
            rules: [{
              required: true,
              message: "请输入生成规则包含目的港",
              trigger: "blur"
            }]
          },
          {
            label: "流水号位数",
            prop: "serialNumDigit",
            type: "number",
            hide: true,
            change: (d) => {
              if (d.value == this.form[d.column.prop]) return;
              this.renderRule(d.value, d.column.prop);
            },
            span: 9,
            valueDefault: 2,
            minRows: 2,
            maxRows: 10,
            rules: [{
              required: true,
              message: "请输入生成规则的流水号位数",
              trigger: "blur"
            }]
          },
          {
            label: "生成规则",
            prop: "rule",
            disabled: true,
            slot: true,
            width: 320,
            span: 24,
            rules: [{
              required: false,
              message: "请输入生成规则",
              trigger: "blur"
            }]
          },
          {
            label: "用途",
            prop: "purpose",
            type: "select",
            dicData: purposeList,
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入用途",
              trigger: "blur"
            }]
          },
          {
            label: "当前流水号",
            prop: "currentSerialNum",
            disabled: true,
            valueDefault: 0,
            width: 150,
            minRows: 0,
            type: "number",
            maxRows: 99999,
            rules: [{
              required: true,
              message: "请输入当前流水号",
              trigger: "blur"
            }]
          },
          {
            label: "归零方式",
            prop: "zeroingType",
            type: "select",
            dicData: zeroingTypeList,
            valueDefault: 0,
            rules: [{
              required: true,
              message: "请输入归零方式",
              trigger: "blur"
            }]
          },

          {
            label: "使用人员",
            prop: "users",
            type: 'tree',
            width: 400,
            multiple: true,
            selectAll: true,
            dicUrl: "/api/basicData/user",
            rules: [{
              required: true,
              message: "请输入使用人员",
              trigger: "blur"
            }]
          }, {
            label: "备注",
            prop: "remarks",
            rules: [{
              required: false,
              message: "请输入备注",
              trigger: "blur"
            }]
          },

        ]
      },
      data: []
    };
  },
  components: {
    serialnum
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(this.permission.worknumrules_add, false),
        viewBtn: this.vaildData(this.permission.worknumrules_view, false),
        delBtn: this.vaildData(this.permission.worknumrules_delete, false),
        editBtn: this.vaildData(this.permission.worknumrules_edit, false)
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
    renderRule(data, prop) {
      let d = this.form;
      d[prop] = data;
      let s = this.form.prefix;
      if (d.digitOfYear > 0) {
        s += "<" + ("Y".repeat(d.digitOfYear)) + ">"
      }
      if (d.ruleMonth) {
        s += "<MM>";
      }
      if (d.ruleDay) {
        s += "<DD>";
      }
      if (d.rulePortDestination) {
        s += "<POD>";
      }
      s += "<" + ("#".repeat(d.serialNumDigit) + ">");
      d.rule = s;
    },
    arrayToString: function(a) {
      let str = "";
      for (let i = 0; i < a.length; i++) {
        str += a[i];
        if (i >= a.length - 1) break;
        str += ","
      }
      return str;
    },
    rowSave(row, loading, done) {
      row.users = this.arrayToString(row.users);
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
      row.users = this.arrayToString(row.users);
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
      if (typeof(this.form.users) == "string" && this.form.users) this.form.users = this.form.users.split(",")
      //默认人员全选
      if (type == "add") getWorkNumRulesUser().then(res => {
        if (res.status != 200) return;
        let data = res.data;
        if (data.length <= 1) return;
        let list = [];
        for (let item of data) list.push(item.value);
        this.form.users = list.join(",");
      })
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.id).then(res => {
          this.form = res.data.data;
          if (this.form.users) this.form.users = this.form.users.split(",");
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
        for (let i = 0; i < data.records.length; i++) {
          data.records[i].users = data.records[i].users.split(",");
        }
        this.loading = false;
        this.selectionClear();
      });
    }
  }
};
</script>

<style>
</style>
