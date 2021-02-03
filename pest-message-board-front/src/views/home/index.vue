<template>
  <div class="home-container">
    <el-container class="home-container-container">
      <el-aside class="home-container-aside">
        <span class="home-container-title">疫情信息平台</span>
        <el-menu
          router
          default-active="1-1"
          :default-openeds="['1', '2','3']"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-news"></i>
              <span>关于疫情</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/home/pest">最新信息</el-menu-item>
              <el-menu-item index="/home/message-board">留言板</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>个人主页</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/home/userinfo">我的主页</el-menu-item>
              <el-menu-item index="/home/publish">发布留言</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'HomeIndex',
  components: {},
  props: {},
  data () {
    return {}
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
  },
  methods: {}
}
</script>

<style scoped lang="less">
.home-container {
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;

  .home-container-container {
    height: 100%;

    .home-container-aside {
      width: 250px;
      height: 100%;
      background-color: #545c64;
    }
  }
}

.home-container-title {
  color: white;
  font-size: 30px;
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-bottom: 70px;
}
</style>
