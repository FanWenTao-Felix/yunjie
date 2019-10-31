<template>
  <basic-container>
    <avue-crud :option="option"
               :table-loading="loading"
               :data="data"
               :page="page"
               :permission="permissionList"
               :before-open="beforeOpen"
               v-model="form"
               ref="crud"
               @row-update="rowUpdate"
               @row-save="rowSave"
               @row-del="rowDel"
               @search-change="searchChange"
               @search-reset="searchReset"
               @selection-change="selectionChange"
               @current-change="currentChange"
               @size-change="sizeChange"
               @on-load="onLoad">
      <template slot="menuLeft">
        <el-button type="danger"
                   size="small"
                   icon="el-icon-delete"
                   plain
                   v-if="permission.airfreightinfo_delete"
                   @click="handleDelete">删 除
        </el-button>
        <el-col :span="7.5"><el-button type="primary" size="small" plain>
          <a class='download' :href='url'  download="航线.xlsx"  title="下载">航线模板</a></el-button>
        </el-col>
        <el-col :span="7.5"><el-button type="primary" size="small" plain>
        <a class='download' :href='priceUrl'  download="表价信息.xlsx"  title="下载">表价模板</a></el-button>
        </el-col>
        <el-col :span="8.1">
          <el-upload  action="/api/dictionaries/airfreightinfo/toLead"
                      :on-success="handleSuccess"
                      :on-error="handleError"
                      :file-list="fileList"
                      multiple
                      :before-upload="beforeAvatarUpload">
            <el-button icon="el-icon-upload" type="primary" size="small">导入</el-button>
          </el-upload>
        </el-col>
      </template>
      <template slot-scope="scope" slot="menu">
        <el-button icon="el-icon-edit" @click="management(scope.row)" type="text" size="small">表价信息</el-button>
      </template>
    </avue-crud>
    <el-dialog top="2vh" title="操作价表" :visible.sync="freight_show" width="80%" :before-close="fee_close">
      <ait :aitfreight_id="freight_id" />
    </el-dialog>
  </basic-container>
</template>

<script>
    import ait from "./airfreight.vue";
    import {getList, getDetail, add, update, remove} from "@/api/dictionaries/airfreightinfo";
    import {mapGetters} from "vuex";
    var DIC = {
        state: [{
            label: '正常',
            value: '正常',
        }, {
            label: '不正常',
            value: '不正常'
        }]
    }
    export default {
        data() {
            return {
                freight_id:"",
                freight_show: false,
                url:'api/dictionaries/airfreightinfo/download',
                priceUrl:'api/dictionaries/airfreight/download',
                form: {},
                query: {},
                loading: true,
                page: {
                    pageSize: 10,
                    currentPage: 1,
                    total: 0
                },
                selectionList: [],
                fileList: [],
                option: {
                    height:'auto',
                    calcHeight: 350,
                    tip: false,
                    border: true,
                    index: true,
                    viewBtn: true,
                    selection: true,
                    column: [
                        {
                            label: "",
                            prop: "id",
                            rules: [{
                                required: true,
                                message: "请输入",
                                trigger: "blur"
                            }],
                            hide:true,
                            editDisplay: false,
                            viewDisplay: false,
                            addDisplay: false,
                        },
                        {
                            label: "航线编号",
                            prop: "number",
                            search:true,
                            rules: [{
                                required: true,
                                message: "请输入编号",
                                trigger: "blur"
                            }]
                        },
                        {
                            label: "航线简称",
                            prop: "airLine",
                            search:true,
                            rules: [{
                                required: true,
                                message: "请输入航线简称",
                                trigger: "blur"
                            }]
                        },
                        {
                            label: "起运港",
                            prop: "originPort",
                            search:true,
                            type: "select",
                            dicUrl: "/api/basicData/airportOne",
                            rules: [{
                                required: true,
                                message: "请输入起运港",
                                trigger: "blur"
                            }]
                        },
                        {
                            label: "航班号",
                            prop: "flightNumber",
                            rules: [{
                                required: true,
                                message: "请输入航班号",
                                trigger: "blur"
                            }],
                        },
                        {
                            label: "机型",
                            prop: "type",
                            rules: [{
                                required: true,
                                message: "请输入机型",
                                trigger: "blur"
                            }],
                        },
                        {
                            label: "周期",
                            prop: "period",
                            rules: [{
                                required: true,
                                message: "请输入周期",
                                trigger: "blur"
                            }],
                        },
                        {
                            label: "载量",
                            prop: "capacity",
                            rules: [{
                                required: true,
                                message: "请输入载量",
                                trigger: "blur"
                            }],
                        },
                        {
                            label: "币种",
                            prop: "currency",
                            rules: [{
                                required: true,
                                message: "请输入币种",
                                trigger: "blur"
                            }],
                        //search:true,
                         dicUrl: "/api/basicData/currency",
                         type: "select"
                        },
                        {
                            label: "杂费备注",
                            prop: "sundryGoods",
                            /*rules: [{
                                required: true,
                                message: "请输入杂费备注",
                                trigger: "blur"
                            }]*/
                        },
                        {
                            label: "备注",
                            prop: "remark",
                            viewDisplay: false,
                           /* rules: [{
                                required: true,
                                message: "请输入备注",
                                trigger: "blur"
                            }]*/
                        },
                        {
                            label: "优势",
                            prop: "advantage",
                           /* rules: [{
                                required: true,
                                message: "请输入优势",
                                trigger: "blur"
                            }]*/
                        },
                        {
                            label: "状态",
                            prop: "state",
                            type: "radio",
                            dicData: DIC.state,
                            mock:{
                                type:'dic'
                            },
                            valueDefault:'正常',
                           /* rules: [{
                                required: true,
                                message: "请输入状态",
                                trigger: "blur"
                            }]*/
                        },
                        {
                            label: "创建时间",
                            prop: "creationTime",
                            type: "date",width: 200,
                           // search:true,
                            format: "yyyy-MM-dd hh:mm:ss",
                            valueFormat: "yyyy-MM-dd hh:mm:ss",
                            rules: [{
                                required: true,
                                message: "请输入创建时间",
                                trigger: "blur"
                            }],
                           // editDisplay: false,
                            viewDisplay: false,
                            //addDisplay: false,
                        },
                        {
                            label: "更新时间",
                            prop: "turnoverTime",width: 200,
                            type: "date",
                           // search:true,
                            format: "yyyy-MM-dd hh:mm:ss",
                            valueFormat: "yyyy-MM-dd hh:mm:ss",
                            rules: [{
                                required: true,
                                message: "请输入更新时间",
                                trigger: "blur"
                            }],
                           // editDisplay: false,
                            viewDisplay: false,
                           // addDisplay: false,
                        },
                    ]
                },
                data: []
            };
        },
        components: {
            ait
        },
        computed: {
            ...mapGetters(["permission"]),
            permissionList() {
                return {
                    addBtn: this.vaildData(this.permission.airfreightinfo_add, false),
                    viewBtn: this.vaildData(this.permission.airfreightinfo_view, false),
                    delBtn: this.vaildData(this.permission.airfreightinfo_delete, false),
                    editBtn: this.vaildData(this.permission.airfreightinfo_edit, false)
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
                this.onLoad(this.page, params);
            },
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.crud.toggleSelection();
            },
            currentChange(currentPage){
                this.page.currentPage = currentPage;
            },
            sizeChange(pageSize){
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
            },
            fee_close() {
                this.freight_show = false;
                this.freight_id = "";
            },
            management(data) {
               // alert(data.id);
                this.freight_show = true;
                this.freight_id = data.id;
            },
            // 上传前对文件的判断
            beforeAvatarUpload(file) {
                // alert(file.name);
                const extension = file.name.split(".")[1] === "xls";
                const extension2 = file.name.split(".")[1] === "xlsx";
                if (!extension && !extension2) {
                    alert("导入文件只能是 xls、xlsx格式!");
                }
                return extension || extension2;
            },
            handleSuccess : function(response) {
                if (response.code == 200 && response.success) {
                    this.$message.success("导入成功")
                    this.onLoad(this.page);
                } else {
                    this.$message.error("导入失败!请检查文件是否正确")
                    this.onLoad(this.page);
                }
            }

        },

    };
</script>

<style>
</style>
