<template>
<basic-container>
  <avue-crud :option="option" :table-loading="loading" :data="data" :page="page" :permission="permissionList" :before-open="beforeOpen" v-model="form" ref="crud" @row-update="rowUpdate" @row-save="rowSave" @row-del="rowDel" @search-change="searchChange"
    @search-reset="searchReset" @selection-change="selectionChange" @current-change="currentChange" @size-change="sizeChange" @on-load="onLoad">
    <template slot="menuLeft">
      <el-button type="danger" size="small" icon="el-icon-delete" plain v-if="permission.clientdata_delete" @click="handleDelete">删 除
      </el-button>
      <el-button type="primary" icon="el-icon-download" @click="getTemplate" size="small">获取导入模板 </el-button>
      <el-button type="primary" icon="el-icon-download" @click="batchExport" size="small">导出 </el-button>
      <el-col :span="7.5">
        <el-upload :headers="uploadHeaders" class="upload-demo" :on-progress="batchImportProgress" action="/api/client/clientdata/batchImport" :limit="1" :before-upload="beforeBatchImport" :on-success="batchImportSuccess" :file-list="fileList">
          <el-button icon="el-icon-upload" type="primary" size="small">导入
          </el-button>
        </el-upload>
      </el-col>
    </template>
    <template slot-scope="scope" slot="menu">
      <el-button @click="openLinkman(scope.row)" icon="el-icon-edit" type="text" size="small">联系人管理</el-button>
    </template>

  </avue-crud>
  <el-dialog :close-on-click-modal="false" title="联系人管理" :visible.sync="linkman_show" width="90%" height="100px" top="4vh">
    <linkman :clientdata_id="clientdata_id" />
  </el-dialog>
</basic-container>
</template>

<script>
import linkman from "./linkman.vue"
import {
  getList,
  getDetail,
  add,
  update,
  remove
} from "@/api/client/clientdata";
import {
  mapGetters
} from "vuex";
import {
  getCountry
} from "@/common/country.js";
import {
  arrayToString
} from "@/common/util.js"
import {
  neccessaryHeaders
} from "@/router/axios.js";
const trueAndFalse = [{
  label: '否',
  value: false,
}, {
  label: '是',
  value: true,
}];
const paytimeSpan = [{
  label: "半月结",
  value: 0,
}, {
  label: "月结",
  value: 1,
}]
const paytimeType = [{
  label: '到港日期',
  value: 0,
}, {
  label: '离港日期',
  value: 1,
}]
const clientSource = [{
  label: '网上推荐',
  value: '0',
}];
const clientGroup = [{
  label: '第一组',
  value: 'clientGroup',
}];
const clientType = [{
  label: "客户",
  value: 0,
}, {
  label: "供应商",
  value: 1,
}, {
  label: "客户兼供应商",
  value: 2,
}];
const businessType = [];
const businessRange = [{
  label: "电商",
  value: "电商"
}];
export default {
  created: function() {},
  data() {
    return {
      clientdata_id: "",
      linkman_show: false,
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
        height: 'auto',
        calcHeight: 350,
        tip: false,
        border: true,
        viewBtn: true,
        align: "center",
        labelWidth: 120,
        dialogClickModal: false,
        column: [{
            label: "客户简称",
            prop: "shortName",
            search: true,
            rules: [{
              required: true,
              message: "请输入客户简称",
              trigger: "blur"
            }]
          },
          {
            label: "全称",
            prop: "fullName",
            search: true,
            rules: [{
              required: true,
              message: "请输入全称",
              trigger: "blur"
            }]
          },
          {
            label: "英文名",
            prop: "englishName",
            search: true,
            rules: [{
              required: false,
              message: "请输入英文名",
              trigger: "blur"
            }]
          }, {
            label: "联系人",
            prop: "linkman",
          },
          {
            label: "联系电话",
            prop: "contactNumber",
            rules: [{
              required: true,
              message: "请输入联系电话",
              trigger: "blur"
            }]
          },
          {
            label: "传真机",
            prop: "faxNumber",
            hide: true,
            rules: [{
              required: false,
              message: "请输入传真机",
              trigger: "blur"
            }]
          }, {
            label: "客户类型",
            prop: "clientType",
            type: "select",
            dicData: clientType,
            rules: [{
              required: true,
              message: "请选择客户类型",
              trigger: "blur"
            }]
          },
          {
            label: "公司类型",
            prop: "companyType",
            type: 'tree',
            multiple: true,
            dicUrl: '/api/basicData/companyType',
            rules: [{
              required: true,
              message: "请选择公司类型",
              trigger: "blur"
            }]
          }, {
            label: "中文地址",
            prop: "chineseAddress",
            rules: [{
              required: false,
              message: "请输入中文地址",
              trigger: "blur"
            }]
          },
          {
            label: "英文地址",
            prop: "englishAddress",
            hide: true,
            rules: [{
              required: false,
              message: "请输入英文地址",
              trigger: "blur"
            }]
          }, {
            label: "手机",
            prop: "phone",
            rules: [{
              required: false,
              message: "请输入手机",
              trigger: "blur"
            }]
          },

          {
            label: "邮政编码",
            prop: "postalCode",
            hide: true,
            rules: [{
              required: false,
              message: "请输入邮政编码",
              trigger: "blur"
            }]
          },

          {
            label: "网站地址",
            prop: "webAddress",
            hide: true,
            rules: [{
              required: false,
              message: "请输入网站地址",
              trigger: "blur"
            }]
          },
          {
            label: "E_Mail",
            prop: "email",
            hide: true,
            rules: [{
              required: false,
              message: "请输入E_Mail",
              trigger: "blur"
            }]
          },
          {
            label: "备注",
            prop: "remark",
            hide: true,
            rules: [{
              required: false,
              message: "请输入备注",
              trigger: "blur"
            }]
          },

          {
            label: "国家",
            prop: "country",
            type: "select",
            dicData: getCountry(),
            span: 12,
            rules: [{
              required: false,
              message: "请输入国家",
              trigger: "false"
            }]
          },
          {
            label: "所属地区",
            prop: "district",
            rules: [{
              required: false,
              message: "请输入所属地区",
              trigger: "blur"
            }]
          },
          {
            label: "所属城市",
            prop: "city",
            rules: [{
              required: false,
              message: "请输入所属城市",
              trigger: "blur"
            }]
          },
          /** {
                      label: "拖欠限额(数目)",
                      prop: "limitedArrearsAmount",
                      width: 150,
                      hide: true,
                      labelWidth: 150,
                      rules: [{
                        required: false,
                        message: "请输入拖欠限额中的数目",
                        trigger: "blur"
                      }]
                    },
                    {
                      label: "拖欠限额(货币)",
                      prop: "limitedArrearsCurrency",
                      labelWidth: 150,
                      width: 150,
                      hide: true,
                      type: "select",
                      dicUrl: "/api/basicData/currency",
                      rules: [{
                        required: true,
                        message: "请输入拖欠限额中的货币",
                        trigger: "blur"
                      }]
                    },
                    {
                      label: "付款期限(日期类型)",
                      width: 150,
                      labelWidth: 150,
                      prop: "paytimeType",
                      hide: true,

                      type: "select",
                      dicData: paytimeType,
                      rules: [{
                        required: true,
                        message: "请输入付款期限中的日期类型，是出仓日期还是入仓日期等等",
                        trigger: "blur"
                      }]
                    },
                    {
                      label: "付款期限(时间跨度)",
                      prop: "paytimeSpan",
                      type: "select",
                      width: 150,
                      labelWidth: 150,
                      dicData: paytimeSpan,
                      hide: true,
                      rules: [{
                        required: true,
                        message: "请输入付款期限中的时间跨度，是日结还是月结等等",
                        trigger: "blur"
                      }]
                    },
                    {
                      label: "付款期限(天数)",
                      prop: "paytimeDay",
                      width: 150,
                      hide: true,

                      labelWidth: 150,
                      rules: [{
                        required: true,
                        message: "请输入付款期限中的天数",
                        trigger: "blur"
                      }]
                    },
          **/
          {
            label: "单位代码",
            prop: "unitCode",
            hide: true,

            rules: [{
              required: false,
              message: "请输入单位代码",
              trigger: "blur"
            }]
          },
          {
            label: "商业范围",
            prop: "businessRange",
            hide: true,

            type: "select",
            dicData: businessRange,
            rules: [{
              required: false,
              message: "请输入商业范围",
              trigger: "blur"
            }]
          },
          {
            label: "信誉等级",
            prop: "creditLevel",
            hide: true,

            rules: [{
              required: false,
              message: "请输入信誉等级",
              trigger: "blur"
            }]
          },
          {
            label: "审核状态",
            prop: "examineStatus",
            hide: true,

            disabled: true,
            rules: [{
              required: false,
              message: "请输入审核状态",
              trigger: "blur"
            }]
          },
          {
            label: "业务类型",
            prop: "businessType",
            hide: true,

            type: "select",
            dicData: businessType,
            rules: [{
              required: false,
              message: "请输入业务类型",
              trigger: "blur"
            }]
          },
          {
            label: "客户来源",
            prop: "clientSource",
            type: "select",
            dicData: clientSource,
            hide: true,

            rules: [{
              required: false,
              message: "请输入客户来源",
              trigger: "blur"
            }]
          },
          {
            label: "客户分组",
            prop: "clientGroup",
            type: "select",
            dicData: clientGroup,
            hide: true,

            rules: [{
              required: false,
              message: "请输入客户分组",
              trigger: "blur"
            }]
          },

          {
            label: "财务核算代码",
            prop: "financialAccountingCode",
            width: 150,
            hide: true,

            rules: [{
              required: false,
              message: "请输入财务核算代码",
              trigger: "blur"
            }]
          },
          {
            label: "应收客户",
            prop: "receivableClient",
            span: 6,
            hide: true,
            type: "switch",
            dicData: trueAndFalse,
            rules: [{
              required: false,
              message: "请输入应收客户",
              trigger: "blur"
            }]
          },
          {
            label: "应付客户",
            prop: "payableClient",
            span: 6,
            type: "switch",
            dicData: trueAndFalse,
            hide: true,

            rules: [{
              required: false,
              message: "请输入应付客户",
              trigger: "blur"
            }]
          },
          {
            label: "共享资料",
            prop: "dataSharing",
            hide: true,

            span: 6,
            type: "switch",
            dicData: trueAndFalse,
            rules: [{
              required: false,
              message: "请输入共享资料",
              trigger: "blur"
            }]
          },
          {
            label: "重点关注",
            prop: "focusClient",
            span: 6,
            type: "switch",
            hide: true,

            dicData: trueAndFalse,
            rules: [{
              required: false,
              message: "请输入重点关注",
              trigger: "blur"
            }]
          },
          {
            label: "无价值客户",
            prop: "worthlessClient",
            width: 100,
            span: 6,
            type: "switch",
            hide: true,

            dicData: trueAndFalse,
            rules: [{
              required: false,
              message: "请输入无价值客户",
              trigger: "blur"
            }]
          },
          {
            label: "直客",
            prop: "directCnee",
            span: 6,
            hide: true,

            type: "switch",
            dicData: trueAndFalse,
            rules: [{
              required: false,
              message: "请输入直客",
              trigger: "blur"
            }]
          },
          {
            label: "国外有分部",
            prop: "foreignBranch",
            width: 100,
            span: 12,
            type: "switch",
            hide: true,

            dicData: trueAndFalse,
            rules: [{
              required: false,
              message: "请输入国外有分部",
              trigger: "blur"
            }]
          },
          {
            label: "含税",
            prop: "includingTax",
            span: 6,
            type: "switch",
            dicData: trueAndFalse,
            hide: true,

            rules: [{
              required: false,
              message: "请输入含税",
              trigger: "blur"
            }]
          },
          {
            label: "税率(%)",
            span: 12,
            prop: "taxRate",
            hide: true,

            rules: [{
              required: false,
              message: "请输入税率",
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
        addBtn: this.vaildData(this.permission.clientdata_add, false),
        viewBtn: this.vaildData(this.permission.clientdata_view, false),
        delBtn: this.vaildData(this.permission.clientdata_delete, false),
        editBtn: this.vaildData(this.permission.clientdata_edit, false)
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
  components: {
    linkman
  },
  methods: {
    openLinkman(data) {
      this.clientdata_id = data.id;
      this.linkman_show = true;
    },
    getTemplate() {
      var downloadElement = document.createElement('a');
      var href = "api/client/clientdata/importTemplate";
      downloadElement.href = href;
      downloadElement.click();
    },
    batchExport: function() {
      var downloadElement = document.createElement('a');
      var href = "/api/client/clientdata/batchExport";
      downloadElement.href = href;
      downloadElement.click();
    },
    batchImportProgress: function(event, file, fileList) {
      console.log(fileList);
    },
    beforeBatchImport: function(file) {
      const isText = file.type === 'application/vnd.ms-excel'
      const isTextComputer = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      return (isText || isTextComputer)
    },
    batchImportSuccess: function(response, file, fileList) {
      console.log(fileList);
      if (response.code == 200 && response.success) {
        this.$message.success("导入成功")
        this.onLoad(this.page);
      } else {
        this.$message.error("导入失败")
      }
    },
    rowSave(row, loading, done) {
      row.companyType = arrayToString(row.companyType);
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
      row.companyType = arrayToString(row.companyType);
      console.log(row.companyType);
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
