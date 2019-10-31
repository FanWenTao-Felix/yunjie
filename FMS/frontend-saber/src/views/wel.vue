<template>
  <div>
    <div class="wel__header">
      <div class="wel__info">
        <img :src="userInfo.avatar"
             alt=""
             class="wel__info-img">
        <div class="wel__info-content">
          <div class="wel__info-title">
           {{$t('wel.info')}}
          </div>
          <div class="wel__info-subtitle">
            {{$t('wel.dept')}}
          </div>
        </div>
      </div>

    </div>
    <basic-container>
     <!-- <avue-crud :option="option1" :data="data" @on-load="onLoad" >
      </avue-crud>-->
    </basic-container>

  </div>
</template>

<script>
  import {mapGetters} from "vuex";
  import {getStart} from "@/api/business/airbusiness";

  export default {
    name: "wel",
    data() {
      return {
        data: []
      };
    },
    computed: {
      ...mapGetters(["userInfo"]),
      option1() {
        return {
            tip: false,
            border: true,
            viewBtn: true,
            addBtn:false,
            align: "center",
            columnBtn: false,
            menu: false,
            column: [
                {
                    label: this.$t("wel.table.rw"),
                    prop: "rw"
                },
                {
                    label: this.$t("wel.table.nr"),
                    prop: "nr",
                },
                {
                    label: this.$t("wel.table.sj"),
                    prop: "sj"
                },
                {
                    label: this.$t("wel.table.yw"),
                    prop: "yw"
                },{
                    label: this.$t("wel.table.wt"),
                    prop: "wt"
                }
            ]
        };
      },
    },
    created() {
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getStart().then(res => {
                const data = res.data.data;
                alert(data);
              /*  const data = res.data.data;
                this.page.total = data.total;
                this.data = data.records;*/
                this.loading = false;
               // this.selectionClear();
            });
        }
    }
  };
</script>

<style scoped="scoped" lang="scss">
  .wel {
    &__header {
      padding: 25px 40px;
      border-bottom: 1px solid #e8e8e8;
      background-color: #fff;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    &__info {
      display: flex;
      align-items: center;

      &-img {
        border-radius: 72px;
        display: block;
        width: 72px;
        height: 72px;

        img {
          width: 100%;
          height: 100%;
          display: block;
        }
      }

      &-content {
        position: relative;
        margin-left: 24px;
        color: rgba(0, 0, 0, 0.45);
        line-height: 22px;
      }

      &-title {
        font-size: 20px;
        line-height: 28px;
        font-weight: 500;
        color: rgba(0, 0, 0, 0.85);
        margin-bottom: 12px;
      }

      &-subtitle {
        position: relative;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.45);
        line-height: 22px;
      }
    }

    &__extra {
      &-item {
        position: relative;
        padding: 0 32px;
        display: inline-block;

        &:last-child {
          &::after {
            display: none;
          }
        }

        &:after {
          background-color: #e8e8e8;
          position: absolute;
          top: 30px;
          right: 0;
          width: 1px;
          height: 40px;
          content: "";
        }
      }

      &-title {
        color: rgba(0, 0, 0, 0.45);
        font-size: 14px;
        line-height: 22px;
        margin-bottom: 4px;
      }

      &-subtitle {
        color: rgba(0, 0, 0, 0.85);
        font-size: 30px;
        line-height: 38px;
        margin: 0;

        span {
          color: rgba(0, 0, 0, 0.45);
          font-size: 20px;
        }
      }
    }
  }
</style>
