<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               ref="crud"
               v-model="form"
               :page="page"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="primary"
                   size="small"
                   icon="el-icon-circle-plus"
                   v-if="permission.flow_model_create"
                   plain
                   @click="handleCreate">创 建
        </el-button>
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   v-if="permission.flow_model_delete"
                   plain
                   @click="handleDelete">删 除
        </el-button>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button type="text"
                   size="small"
                   v-if="permission.flow_model_update"
                   plain
                   class="none-border"
                   @click.stop="handleUpdate(scope.row,scope.index)">配置
        </el-button>
        <el-button type="text"
                   size="small"
                   v-if="permission.flow_model_deploy"
                   plain
                   class="none-border"
                   @click.stop="handleDeploy(scope.row,scope.index)">部署
        </el-button>
        <el-button type="text"
                   size="small"
                   v-if="permission.flow_model_download"
                   plain
                   class="none-border"
                   @click.stop="handleDownload(scope.row,scope.index)">下载
        </el-button>
        <el-button type="text"
                   size="small"
                   v-if="permission.flow_model_delete"
                   plain
                   class="none-border"
                   @click.stop="handleSlotDelete(scope.row,scope.index)">删除
        </el-button>
      </template>
      <template slot-scope="{row}"
                slot="version">
        <el-tag>v{{row.version}}</el-tag>
      </template>
    </avue-crud>
    <el-dialog title="流程配置"
               :visible.sync="flowBox"
               :fullscreen="true">
      <iframe
        :src=flowUrl
        width="100%"
        height="700"
        title="流程设计器"
        frameBorder="no"
        border="0"
        marginWidth="0"
        marginHeight="0"
        scrolling="no"
        allowTransparency="yes">
      </iframe>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="flowBox = false">取 消</el-button>
        <el-button type="primary"
                   @click="handleRefresh">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog title="流程部署"
               :visible.sync="deployBox"
               width="20%">
      <el-form :model="form"
               ref="form"
               label-width="80px">
        <el-form-item label="流程类型">
          <el-select v-model="categoryValue" placeholder="请选择" value="">
            <el-option
              v-for="item in category"
              :key="item.dictKey"
              :label="item.dictValue"
              :value="item.dictKey">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="deployBox = false">取 消</el-button>
        <el-button type="primary"
                   @click="handleDoDeploy">确 定</el-button>
      </span>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import website from '@/config/website';
  import {getDictionary} from "@/api/system/dict";
  import {modelList, removeModel, deployModel} from "@/api/flow/flow";
  import {flowCategory} from "@/util/flow";

  export default {
    data() {
      return {
        form: {},
        selectionId: '',
        selectionList: [],
        query: {},
        loading: true,
        page: {
          pageSize: 10,
          currentPage: 1,
          total: 0
        },
        deployBox: false,
        flowBox: false,
        flowUrl: '',
        category: [],
        categoryValue: '',
        option: {
          height:'auto',
          calcHeight:350,
          tip: false,
          border: true,
          index: true,
          selection: true,
          editBtn: false,
          addBtn: false,
          viewBtn: false,
          delBtn: false,
          dialogWidth: 300,
          dialogHeight: 400,
          menuWidth: 150,
          column: [
            {
              label: '模型主键',
              prop: 'id',
            },
            {
              label: '模型标识',
              prop: 'modelKey',
              search: true,
              width: 150,
            },
            {
              label: '模型名称',
              prop: 'name',
              search: true,
              width: 150,
            },
            {
              label: '流程版本',
              prop: 'version',
              slot: true,
              width: 80,
            },
            {
              label: '创建时间',
              prop: 'created',
              width: 165,
            },
            {
              label: '更新时间',
              prop: 'lastUpdated',
              width: 165,
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      }
    },
    methods: {
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
            return removeModel(this.ids);
          })
          .then(() => {
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.crud.toggleSelection();
            this.onLoad(this.page);
          });
      },
      handleCreate() {
        this.flowUrl = `${website.flowDesignUrl}/index.html`;
        this.flowBox = true;
      },
      handleUpdate(row) {
        this.flowUrl = `${website.flowDesignUrl}/index.html#/editor/${row.id}`;
        this.flowBox = true;
      },
      handleDeploy(row) {
        this.deployBox = true;
        this.selectionId = row.id;
      },
      handleDoDeploy() {
        if (!this.categoryValue) {
          this.$message({
            type: "warn",
            message: "请先选择流程类型!"
          });
          return;
        }
        deployModel({modelId: this.selectionId, category: flowCategory(this.categoryValue)}).then(res => {
          const data = res.data;
          if (data.success) {
            this.$message({
              type: "success",
              message: data.msg
            });
            this.deployBox = false;
          } else {
            this.$message({
              type: "warn",
              message: data.msg
            });
          }
        })
      },
      handleDownload(row) {
        window.open(`${website.flowDesignUrl}/app/rest/models/${row.id}/bpmn20`);
      },
      handleSlotDelete(row) {
        this.$confirm("确定将选择数据删除?", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            return removeModel(row.id);
          })
          .then(() => {
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.crud.toggleSelection();
            this.onLoad(this.page);
          });
      },
      handleRefresh() {
        this.flowBox = false;
        this.onLoad(this.page);
      },
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        this.loading = true;
        modelList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
        getDictionary({code: 'flow'}).then(res => {
          this.category = res.data.data;
        })
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
