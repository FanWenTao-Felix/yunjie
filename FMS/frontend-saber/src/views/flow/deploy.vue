<template>
  <basic-container>
    <avue-form :option="option" v-model="form" :upload-before="uploadBefore" :upload-after="uploadAfter"></avue-form>
  </basic-container>
</template>

<script>
  import {deployUpload} from "@/api/flow/flow";
  import {flowCategory} from "@/util/flow";

  export default {
    data() {
      return {
        form: {
          flowCategory: '',
          imgUrl: [],
          file: {},
        },
        option: {
          labelWidth: 120,
          menuBtn: false,
          column: [
            {
              label: '流程类型',
              prop: 'flowCategory',
              type: 'select',
              dicUrl: `/api/blade-system/dict/dictionary?code=flow`,
              props: {
                label: "dictValue",
                value: "dictKey"
              },
              rules: [
                {
                  required: true,
                  message: '请选择流程类型',
                  trigger: 'blur'
                }
              ]
            },
            {
              label: '附件上传',
              prop: 'imgUrl',
              type: 'upload',
              loadText: '附件上传中，请稍等',
              span: 24,
              propsHttp: {
                res: 'data'
              },
              tip: '请上传 bpmn20.xml 标准格式文件',
              action: '/api/blade-flow/manager/check-upload'
            },
          ]
        }
      }
    },
    methods: {
      uploadBefore(file, done) {
        this.$message.success('部署开始')
        this.file = file;
        done()
      },
      uploadAfter(res, done, loading) {
        if (!this.form.flowCategory) {
          this.$message.warning('清先选择流程类型');
          loading()
          return false;
        }
        if (res.success) {
          deployUpload(flowCategory(this.form.flowCategory), [this.file]).then(res => {
            const data = res.data;
            if (data.success) {
              this.$message.success('部署结束')
              done()
            } else {
              this.$message.error(data.msg);
              loading()
            }
          })
        } else {
          this.$message.warning('请上传 bpmn20.xml 标准格式文件');
          loading()
          return false;
        }
      },
    }
  }
</script>
