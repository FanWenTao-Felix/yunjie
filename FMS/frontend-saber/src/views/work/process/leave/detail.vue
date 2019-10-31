<template>
  <basic-container>
    <el-form ref="form" :model="form" label-width="80px">
      <el-row type="flex" class="row-bg" justify="end">
        <el-form-item>
          <el-button @click="handleCancel">关闭</el-button>
        </el-form-item>
      </el-row>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>审批信息</span>
        </div>
        <el-form-item label="申请人">
          <el-input :disabled="true" v-model="form.flow.assigneeName"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间">
              <el-input :disabled="true" v-model="form.startTime"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-input :disabled="true" v-model="form.endTime"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="请假理由">
          <el-input :disabled="true" type="textarea" v-model="form.reason"></el-input>
        </el-form-item>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>流程信息</span>
        </div>
        <el-row type="flex" class="row-bg">
          <avue-timeline pending time :time-width="200">
            <avue-timeline-item v-bind:key="flow.id" v-for="flow in flowList">
              <div slot="time">
                <p>{{flow.createTime}}</p>
              </div>
              <div slot="content">
                <p>{{flow.assigneeName}} 在 [{{flow.createTime}}] 开始处理 [{{flow.historyActivityName}}] 环节</p>
                <p v-if="flow.historyActivityDurationTime!==''">任务历时 [{{flow.historyActivityDurationTime}}]</p>
                <p v-if="flow.comment!==''">批复意见: [{{flow.comment}}]</p>
                <p v-if="flow.endTime!==''">结束时间: [{{flow.endTime}}]</p>
              </div>
            </avue-timeline-item>
          </avue-timeline>
        </el-row>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>流程跟踪</span>
        </div>
        <el-row type="flex" class="row-bg">
          <img :src="src" alt="design">
        </el-row>
      </el-card>
    </el-form>
  </basic-container>
</template>

<script>
  import {historyFlowList, leaveDetail} from "@/api/work/process";

  export default {
    data() {
      return {
        businessId: '',
        processInstanceId: '',
        src: '',
        flowList: [],
        form: {
          flow:{
            assigneeName:'',
          },
          startTime: '',
          endTime: '',
          reason: '',
        }
      }
    },
    created() {
      this.init();
    },
    methods: {
      init() {
        this.processInstanceId = this.$route.params.processInstanceId;
        this.businessId = this.$route.params.businessId;
        this.src = `/api/blade-flow/process/diagram-view?processInstanceId=${this.$route.params.processInstanceId}`;
        historyFlowList(this.processInstanceId).then(res => {
          const data = res.data;
          if (data.success) {
            this.flowList = data.data;
          }
        })
        leaveDetail(this.businessId).then(res => {
          const data = res.data;
          if (data.success) {
            this.form = data.data;
          }
        })
      },
      handleCancel() {
        this.$router.$avueRouter.closeTag();
        this.$router.push({path: `/work/start`});
      }
    }
  }
</script>
