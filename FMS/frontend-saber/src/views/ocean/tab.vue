<template>
<basic-container>
  <avue-tabs :option="option" @change="handleChange"></avue-tabs>
 <!-- <span v-if="type.prop==='tab1'"><seawhole :businessId="this.businessId"/></span>-->
  <span v-if="type.prop==='tab1'"><file :internalOrderNo="this.internalOrderNo"/></span>
  <span v-else-if="type.prop==='tab2'"><seellTab/>
   <!-- <el-button @click="openIframe()" icon="el-icon-edit" type="text" size="small">打印交接单</el-button>-->
   <!-- <el-button type="primary" size="small" plain>
     <a class='download' :href='url' title="打印交接单">打印交接单</a>
    </el-button>-->
    <!--<el-button type="primary" size="small" plain>
          <a class='download' :href='url'  title="下载">表价模板</a>
    </el-button>-->
    <!-- <iframe style="width:100%;height:700px;" :src="iframe_url"></iframe>-->
  </span>

</basic-container>
</template>

<script>
    import file from "./businessfile.vue";
    export default{
    props: ["internalOrderNo","businessId"],
  data() {
    return {
        type:{},
        url:'api/dictionaries/shippingline/download',
        //iframe_url: "api/ocean/seabusiness/deliveryReceipt",
        iframe_show:false,
        iframe_title:"",
        excel_viewer_url: "http://view.officeapps.live.com/op/view.aspx?src=",
        option:{
            column: [{
                icon:'el-icon-info',
                label: '文件管理',
                prop: 'tab1',
            }, {
                icon:'el-icon-warning',
                label: '套打交接单',
                prop: 'tab2',
            }]
        },
    };
  },
    created(){
        this.type = this.option.column[0];
    },
    components: {
        file,
        seellTab
    },
  computed: {

  },
  methods: {
      handleChange(column) {
          this.type = column
      },
      openIframe() {
          this.iframe_url = "";
          this.iframe_title = "交接单";
        //  let src = "http://" + document.domain
             alert(this.businessId);
           let src= "api/ocean/seabusiness/deliveryReceipt?id=1";
           this.iframe_url = src;
          alert(this.iframe_url);
          // src += "&client=" + new Date();
          //this.iframe_url = this.excel_viewer_url + encodeURIComponent(src);
          this.iframe_show = true;
      },
  }
};
</script>

<style>
</style>
