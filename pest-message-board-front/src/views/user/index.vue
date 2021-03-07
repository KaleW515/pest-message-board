<template>
  <div class="user-container">
    <el-header class="user-header">
      <div class="user-header-title">
        <span>个人信息</span>
        <el-link class="user-out" @click="logout">退出</el-link>
      </div>
    </el-header>
    <el-main class="user-main">
      <div class="user-body">
        <el-tooltip content="点击此处上传头像,为不影响视觉效果,请尽量上传宽高比一致的图像" placement="bottom" effect="light">
          <el-upload
            class="user-body-avatar"
            :show-file-list="false"
            :action="uploadUrl"
            :headers="headers">
            <img v-if="userInfo.avatarUuid" :src="userInfo.avatarUuid"
                 style="width: 200px; height: 200px; margin-top: 10px" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-tooltip>
        <el-link class="user-revise" @click="dialogVisible = true">修改密码</el-link>
        <el-divider></el-divider>
        <div class="user-body-item">
          <span>姓名</span>
          <span>{{ userInfo.username }}</span>
        </div>
        <el-divider></el-divider>
        <div class="user-body-item">
          <span>签名</span>
          <el-tooltip content="点击此处修改签名" placement="bottom" effect="light">
            <span @click="openUpdateSignature">{{ userInfo.signature }}</span>
          </el-tooltip>
        </div>
        <el-divider></el-divider>
        <div class="user-body-msg">
          <el-badge :value="newLikeNum" :max="99" class="item">
            <el-button size="small" @click="openComment">新的赞</el-button>
          </el-badge>
          <el-badge :value="newReplyNum" :max="99" class="item">
            <el-button size="small" @click="openComment">新的回复</el-button>
          </el-badge>
          <el-badge :value="newDislikeNum" :max="99" class="item">
            <el-button size="small" @click="openComment">新的踩</el-button>
          </el-badge>
        </div>
        <div v-for="(item, key) in reply" :key="key">
          <el-card v-if="commentIsShow" class="user-body-card">
            <div slot="header" class="user-body-card-header">
              <el-avatar :size="50" :src="item.avatarUrl" class="user-body-card-avatar"></el-avatar>
              <span class="user-body-card-name">{{ item.fromUsername }}</span>
              <span class="user-body-card-date">{{ item.pubDate }}</span>
              <el-link @click="onReply(item, key)">查看回复</el-link>
              <i class="el-icon-top"></i>
              <span class="likeNum" v-text="item.likeNum"></span>
              <i class="el-icon-bottom"></i>
              <span class="disLikeNum" v-text="item.dislikeNum"></span>
            </div>
            <div>
              <span v-text="item.content" class="user-body-card-comment"></span>
              <div class="user-body-card-reply" v-if="item.replyIsShow">
                <ul class="infinite-list" style="overflow: auto">
                  <li class="infinite-list-item" v-for="(i, key) in item.reply" :key="key">
                    <el-avatar :size="30" :src="i.avatarUrl"></el-avatar>
                    <span v-text="i.fromUsername"></span>
                    <span v-text="i.content"></span>
                    <el-link type="primary" class="delete-reply"
                             @click="onDeleteReply(i, key)">{{ '删除' }}
                    </el-link>
                  </li>
                </ul>
              </div>
            </div>
          </el-card>
        </div>
        <el-pagination
          style="position: relative; text-align: center; padding-bottom: 15px"
          v-if="commentIsShow"
          background
          layout="prev, pager, next, jumper"
          :current-page.sync="currentPage"
          :total=total
          page-size=5
          @current-change="openComment(false)">
        </el-pagination>
      </div>
      <revise :deliver="dialogVisible" @closeDia="close"></revise>
    </el-main>
  </div>
</template>

<script>
import { getUserInfo, updateSignature } from '@/api/user'
import { getCommentByUserId } from '@/api/comment'
import request from '@/util/request'
import { deleteReply, getReply } from '@/api/reply'
import Revise from '@/views/user/component/revise'
import { mapMutations } from 'vuex'

export default {
  name: 'UserIndex',
  components: {
    Revise
  },
  props: {},
  data () {
    return {
      userInfo: {
        avatarUuid: '',
        signature: '',
        username: '',
        userId: 0
      },
      dialogVisible: false,
      currentPage: 1,
      total: 0,
      uploadUrl: request.defaults.baseURL + '/attachment',
      headers: {
        Authorization: localStorage.getItem('Authorization')
      },
      newLikeNum: '',
      newDislikeNum: '',
      newReplyNum: '',
      commentIsShow: false,
      reply: []
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
    getUserInfo()
      .then(res => {
        this.userInfo = res.data.payload
      })
      .catch(err => {
        console.log(err)
      })
  },
  methods: {
    ...mapMutations(['noticeInit']),
    openUpdateSignature () {
      this.$prompt('请输入新的签名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '不超过50字',
        inputPattern: /^.{1,50}$/,
        inputErrorMessage: '签名格式不正确'
      })
        .then(({ value }) => {
          this.onUpdateSignature(value)
        })
        .catch(err => {
          console.log(err)
        })
    },
    onUpdateSignature (value) {
      updateSignature({
        signature: value
      })
        .then(res => {
          this.userInfo.signature = value
          this.$message.success('修改成功')
        })
        .catch(err => {
          console.log(err)
        })
    },
    openComment (open) {
      if (open) {
        this.commentIsShow = !this.commentIsShow
      }
      this.noticeInit({
        newReply: 0,
        newDislike: 0,
        newLike: 0
      })
      getCommentByUserId({
        userId: this.userInfo.userId,
        page: this.currentPage
      })
        .then(res => {
          this.reply = res.data.payload.comment
          this.total = res.data.payload.total
        })
        .catch(err => {
          console.log(err)
        })
    },
    onReply (item, key) {
      getReply(item.commentId, 1)
        .then(res => {
          item.reply = res.data.payload.reply
          item.replyIsShow = !item.replyIsShow
        })
        .catch(err => {
          console.log(err)
        })
    },
    onDeleteReply (item, key) {
      deleteReply(item.replyId)
        .then(res => {
          if (res.data.msg === 'success') {
            this.$message.success('删除成功')
          } else {
            this.$message.error(res.data.msg)
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    logout () {
      localStorage.clear()
      this.$router.push('/login')
    },
    close () {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped lang="less">
.user-container {
  position: relative;
}

.user-main {
  position: absolute;
  right: 0;
  left: 0;
}

.user-header {
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-top: 50px;
  padding-bottom: 50px;

  .user-header-title {
    font-size: 40px;
    position: relative;
    text-align: center;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.user-body {
  position: relative;
  width: 90%;
  height: auto;
  left: 50%;
  transform: translate(-50%, 0%);
  border-radius: 10px;
  line-height: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 10px;

  .user-body-avatar {
    position: relative;
    width: 200px;
    height: 200px;
    left: 50%;
    transform: translate(-50%, 0);
  }
}

.user-body-item {
  :nth-child(1) {
    position: relative;
    left: 30px;
  }

  :nth-child(2) {
    position: fixed;
    left: 50%;
    transform: translate(-50%, 0);
    cursor: pointer;
    outline: none;
  }
}

.user-body-msg {
  display: flex;
  justify-content: space-evenly;
  padding-bottom: 20px;
}

.user-body-card {
  margin-bottom: 30px;
}

.user-body-card-header {
  display: flex;
  justify-content: left;
  align-items: center;

  .user-body-card-name {
    margin-left: 20px;
  }

  .user-body-card-date {
    position: absolute;
    right: 10px;
  }

  :nth-child(4) {
    position: absolute;
    right: 160px;
  }

  :nth-child(5) {
    margin-left: 10px;
  }

  :nth-child(7) {
    margin-left: 20px;
  }
}

.user-body-card-comment {
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-body-card-reply {
  height: auto;
  width: 100%;
  position: relative;
  overflow: auto;
  display: flex;
  box-shadow: 0 -1px 1px 0 rgba(0, 0, 0, 0.1)
}

.infinite-list-item {
  list-style: none;
  margin-bottom: 10px;
  display: flex;
  align-items: center;

  :nth-child(2) {
    margin-left: 20px;
  }
}

.delete-reply {
  position: absolute;
  right: 0;
}

.user-out {
  position: absolute;
  right: 10px;
}

.user-revise {
  position: absolute;
  top: 20px;
  right: 20px;
}
</style>
