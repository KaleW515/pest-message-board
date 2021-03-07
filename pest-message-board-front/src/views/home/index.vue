<template>
  <el-container class="home-container">
    <el-header class="home-container-header">
      <span class="home-container-header-title">
        <i :class="{
            'el-icon-s-fold': isCollapse,
            'el-icon-s-unfold': !isCollapse
          }" @click="isCollapse = !isCollapse">留言板</i>
      </span>
      <el-dropdown style="display: flex; align-items: center">
        <el-avatar size="medium" :src="circleUrl" style="margin-right: 10px"></el-avatar>
        <div class="info-title">
          <span>{{ username }}</span>
          <i class="el-icon-arrow-down"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>设置</el-dropdown-item>
            <el-dropdown-item @click.native="onLogout">退出</el-dropdown-item>
          </el-dropdown-menu>
        </div>
      </el-dropdown>
    </el-header>
    <el-container>
      <el-aside width="auto">
        <el-menu
          router
          class="home-container-aside"
          :collapse="isCollapse"
          default-active="1-1"
          :default-openeds="['1','2','3']"
          text-color="#fff"
          background-color="#3f3f3f"
          active-text-color="white">
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
              <el-menu-item index="/home/user">我的主页</el-menu-item>
              <el-menu-item index="/home/publish">发布留言</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="3" v-if="isSuperuser">
            <template slot="title">
              <i class="el-icon-menu"></i>
              <span>人员管理</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/admin/user">用户管理</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main class="home-container-main">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { getUserInfo, logout } from '@/api/user'
import { mapMutations } from 'vuex'
import { getWSUrl } from '@/util/config'

export default {
  name: 'app',
  components: {},
  props: {},
  data () {
    return {
      circleUrl: JSON.parse(localStorage.getItem('user')).avatarUuid,
      username: JSON.parse(localStorage.getItem('user')).username,
      isSuperuser: JSON.parse(localStorage.getItem('user')).isSuperuser,
      isCollapse: false,
      lockReconnect: false,
      timeout: 10 * 1000,
      timeoutObj: null,
      serverTimeoutObj: null,
      timeOutNum: null,
      socket: ''
    }
  },
  computed: {},
  watch: {
    '$store.state.newReply' (val) {
      if (val !== 0) {
        this.$notify({
          title: '消息提示',
          message: '刚刚有人回复了你',
          duration: 0
        })
      }
    },
    '$store.state.newLike' (val) {
      if (val !== 0) {
        this.$notify({
          title: '消息提示',
          message: '刚刚有人赞了你',
          duration: 0
        })
      }
    },
    '$store.state.newDislike' (val) {
      if (val !== 0) {
        this.$notify({
          title: '消息提示',
          message: '刚刚有人踩了你',
          duration: 0
        })
      }
    }
  },
  created () {
  },
  mounted () {
    getUserInfo()
      .then(res => {
        localStorage.setItem('username', res.data.payload.username)
        localStorage.setItem('userId', res.data.payload.userId)
        if (res.data.payload.isSuperuser === 1) {
          this.isSuperuser = true
        }
        this.initSocket(localStorage.getItem('userId').toString())
      })
      .catch(err => {
        console.log(err)
      })
  },
  methods: {
    onLogout () {
      this.$confirm('是否退出?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        logout()
          .then(res => {
            if (res.data.msg === 'success') {
              this.$message.success('退出成功')
              localStorage.removeItem('user')
              localStorage.removeItem('Authorization')
              this.$router.push('/login')
            } else {
              this.$message.error('退出失败')
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        })
      })
    },
    ...mapMutations(['setNewReply', 'setNewLike', 'setNewDislike']),
    initSocket (userId) {
      this.socket = new WebSocket(getWSUrl(userId))
      this.socket.onopen = this.open
      this.socket.onerror = this.error
      this.socket.onmessage = this.message
      this.socket.onclose = this.close
    },
    open: function () {
      console.log('open')
      this.start()
    },
    error: function () {
      console.log('error')
      this.reConnect()
    },

    message: function (msg) {
      console.log(typeof (msg) === 'undefined')
      if (typeof (msg) !== 'undefined') {
        console.log(msg)
        const res = JSON.parse(msg.data)
        if (res.newReply !== this.$store.state.newReply) {
          this.setNewReply({ newReply: res.newReply })
        }
        if (res.newLike !== this.$store.state.newLike) {
          this.setNewLike({ newLike: res.newLike })
        }
        if (res.newDislike !== this.$store.state.newDislike) {
          this.setNewDislike({ newDislike: res.newDislike })
        }
        this.reset()
      }
    },
    reConnect: function () {
      console.log('reconnect')
      const that = this
      if (that.lockReconnect) {
        return
      }
      that.lockReconnect = true
      that.timeOutNum = setTimeout(function () {
        that.initSocket(localStorage.getItem('userId'))
        that.lockReconnect = false
      }, 5000)
    },
    reset () {
      clearTimeout(this.timeoutObj)
      clearTimeout(this.serverTimeoutObj)
      this.start()
    },
    close: function () {
      console.log('close')
      this.reConnect()
    },
    start () {
      const self = this
      self.timeoutObj && clearTimeout(self.timeoutObj)
      self.serverTimeoutObj && clearTimeout(self.serverTimeoutObj)
      self.timeoutObj = setTimeout(function () {
        if (self.socket.readyState === 1) {
          self.socket.send('heartBeat')
        } else {
          self.reConnect()
        }
        self.serverTimeoutObj = setTimeout(function () {
          self.socket.close()
        }, self.timeout)
      }, self.timeout)
    }
  }
}
</script>

<style scoped lang="less">
.home-container {
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;

  .home-container-header {
    display: flex;
    align-items: center;
    background-color: rgba(43, 43, 43, 0.9);
    color: white;
    border-bottom: 1px solid black;
    justify-content: space-between;
  }

  .home-container-header-title {
    font-size: 20px;
  }

  .home-container-aside:not(.el-menu--collapse) {
    width: 250px;
    height: 100%;
    background-color: #3f3f3f;
  }

  .home-container-aside {
    width: 70px;
    height: 100%;
    background-color: #3f3f3f;
  }
}

.info-title {
  color: white;
  display: inline-block;
}

</style>
