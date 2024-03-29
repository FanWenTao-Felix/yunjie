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
      <template slot-scope="scope" slot="menu">
        <el-button type="text"
                   size="small"
                   v-if="permission.work_send_detail"
                   plain
                   class="none-border"
                   @click.stop="handleDetail(scope.row)">详情
        </el-button>
        <el-button type="text"
                   size="small"
                   v-if="permission.work_send_follow"
                   plain
                   class="none-border"
                   @click.stop="handleImage(scope.row,scope.index)">跟踪
        </el-button>
      </template>
      <template slot-scope="{row}"
                slot="processDefinitionVersion">
        <el-tag>v{{row.processDefinitionVersion}}</el-tag>
      </template>
      <template slot-scope="{row}"
                slot="processIsFinished">
        <el-tag>{{row.processIsFinished==='finished' ? '已完成' : '未完成'}}</el-tag>
      </template>
    </avue-crud>
    <el-dialog title="流程图"
               :visible.sync="flowBox"
               :fullscreen="true">
      <iframe
        :src=flowUrl
        width="100%"
        height="700"
        title="流程图"
        frameBorder="no"
        border="0"
        marginWidth="0"
        marginHeight="0"
        scrolling="no"
        allowTransparency="yes">
      </iframe>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="flowBox = false">关 闭</el-button>
      </span>
    </el-dialog>
  </basic-container>
</template>

<script>
  import {mapGetters} from "vuex";
  import {sendList} from "@/api/work/work";
  import {flowCategory,flowRoute} from "@/util/flow";

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
        flowBox: false,
        flowUrl: '',
        workBox: false,
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
              label: "流程分类",
              type: "select",
              row: true,
              dicUrl: "/api/blade-system/dict/dictionary?code=flow",
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              slot: true,
              prop: "category",
              search: true,
              hide: true,
              width: 100,
            },
            {
              label: '流程名称',
              prop: 'processDefinitionName',
            },
            {
              label: '当前步骤',
              prop: 'taskName',
            },
            {
              label: '流程版本',
              prop: 'processDefinitionVersion',
              slot: true,
              width: 80,
            },
            {
              label: '流程进度',
              prop: 'processIsFinished',
              slot: true,
              width: 80,
            },
            {
              label: '申请时间',
              prop: 'createTime',
              width: 165,
            },
          ]
        },
        data: []
      };
    },
    computed: {
      ...mapGetters(["permission", "flowRoutes"]),
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.id);
        });
        return ids.join(",");
      },
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
      handleDetail(row) {
        this.$router.push({ path: `/work/process/${flowRoute(this.flowRoutes, row.category)}/detail/${row.processInstanceId}/${row.businessId}` });
      },
      handleImage(row) {
        this.flowUrl = `/api/blade-flow/process/diagram-view?processInstanceId=${row.processInstanceId}`;
        this.flowBox = true;
      },
      currentChange(currentPage){
        this.page.currentPage = currentPage;
      },
      sizeChange(pageSize){
        this.page.pageSize = pageSize;
      },
      onLoad(page, params = {}) {
        const values = {
          ...params,
          category: (params.category) ? flowCategory(params.category) : null
        }
        this.loading = true;
        sendList(page.currentPage, page.pageSize, Object.assign(values, this.query)).then(res => {
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
