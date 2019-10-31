<template>
  <div class="top-menu">
    <el-menu :default-active="activeIndex"
             mode="horizontal"
             text-color="#333">
      <el-menu-item index="0"
                    @click.native="openMenu(itemHome)"
                    key="0">
        <template slot="title">
          <i :class="itemHome.source"></i>
          <span>{{generateTitle(itemHome)}}</span>
        </template>
       <!-- <template slot="title">
          <i :class="basics.source"></i>
          <span>{{generateTitle(basics)}}</span>
        </template>
        <template slot="title">
          <i :class="client.source"></i>
          <span>{{generateTitle(client)}}</span>
        </template>
        <template slot="title">
          <i :class="business.source"></i>
          <span>{{generateTitle(business)}}</span>
        </template>-->
      </el-menu-item>
      <template v-for="(item,index) in items">
        <el-menu-item :index="item.id+''"
                      @click.native="openMenu(item)"
                      :key="index">
          <template slot="title">
            <i :class="item.source" style="padding-right: 5px;"></i>
            <span>{{generateTitle(item)}}</span>
          </template>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "top-menu",
  data() {
    return {
      itemHome: {
        name: '首页',
        source: 'el-icon-menu',
      },
       /* basics: {
            name: '基础数据',
            source: 'el-icon-menu',
        },
        client: {
            name: '客户管理',
            source: 'el-icon-menu',
        },
        business: {
            name: '业务管理',
            source: 'el-icon-menu',
        },*/
      activeIndex: "0",
      items: [],
    };
  },
  created() {
    this.getMenu();
  },
  computed: {
    ...mapGetters(["tagCurrent", "menu"])
  },
  methods: {
    getMenu() {
      this.$store.dispatch("GetTopMenu").then(res => {
        this.items = res;
      });
    },
    generateTitle(item) {
      return this.$router.$avueRouter.generateTitle(
        item.name,
        (item.meta || {}).i18n
      );
    },
    openMenu(item) {
      this.$store.dispatch("GetMenu", item.id).then(data => {
        if (data.length !== 0) {
          this.$router.$avueRouter.formatRoutes(data, true);
        }
        let itemActive,
          childItemActive = 0;
        if (item.path) {
          itemActive = item;
        } else {
          if (this.menu[childItemActive].length == 0) {
            itemActive = this.menu[childItemActive];
          } else {
            itemActive = this.menu[childItemActive].children[childItemActive];
          }
        }
        this.$router.push({
          path: this.$router.$avueRouter.getPath({
            name: itemActive.label,
            src: itemActive.path,
            i18n: itemActive.meta.i18n
          })
        });
      });
    }
  }
};
</script>
