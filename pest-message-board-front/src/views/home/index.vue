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
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { getUserInfo } from '@/api/user'
import { getWSUrl } from '@/util/config'
import { mapMutations } from 'vuex'

export default {
  name: 'HomeIndex',
  components: {},
  props: {},
  data () {
    return {
      isSuperuser: false,
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
