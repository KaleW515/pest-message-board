<template>
  <div class="board-container">
    <el-header class="board-header">
      <div class="board-header-title">留言板</div>
    </el-header>
    <div class="board-header-search">
      <el-input
        @keyup.enter.native="onGetComment"
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        v-model="searchField">
      </el-input>
      <el-switch
        class="board-header-search-switch"
        @change="onGetComment"
        v-model="seqValue"
        active-text="按点赞数排序"
        inactive-text="按时间排序">
      </el-switch>
    </div>
    <el-main>
      <div v-for="(item, key) in comments" :key="key">
        <el-card shadow="hover" class="board-message-card">
          <div slot="header">
            <div class="board-message-card-header">
              <el-avatar :size="50" :src="item.avatarUrl" class="board-message-card-ava"
                         @click.native="onOther(item, key)"></el-avatar>
              <span class="board-message-card-header-name">{{ item.fromUsername }}</span>
              <span class="board-message-card-header-date">{{ item.pubDate }}</span>
              <el-link @click="openInput(item, key)">回复</el-link>
              <el-link @click="onReply(item, key)">查看回复</el-link>
              <el-tooltip effect="dark" content="点个赞" placement="top-end">
                <i class="el-icon-top" @click="onLike(item, key)"></i>
              </el-tooltip>
              <span v-text="item.likeNum"></span>
              <el-tooltip effect="dark" content="踩一下" placement="top-end">
                <i class="el-icon-bottom" @click="onDislike(item, key)"></i>
              </el-tooltip>
              <span v-text="item.dislikeNum"></span>
            </div>
          </div>
          <div>
            <span v-text="item.content" class="board-message-card-body-comment"></span>
            <div class="board-message-card-body-reply" v-if="item.replyIsShow">
              <ul class="infinite-list" style="overflow: auto">
                <li class="infinite-list-item" v-for="(i, key) in item.reply" :key="key">
                  <el-avatar :size="30" :src="i.avatarUrl" @click.native="onOther(i, key)"
                             style="cursor: pointer"></el-avatar>
                  <span v-text="i.fromUsername"></span>
                  <span v-text="i.content"></span>
                  <el-link type="primary" v-if="i.fromUserName === username" class="delete-reply"
                           @click="onDeleteReply(i, key)">{{ '删除' }}
                  </el-link>
                </li>
              </ul>
            </div>
            <el-input
              :rows="5"
              placeholder="请输入回复内容"
              type="textarea"
              v-if="item.inputIsShow"
              v-model="replyContent"></el-input>
            <el-button icon="el-icon-check" v-if="item.inputIsShow"
                       class="board-message-card-body-button" @click="onInput(item)"></el-button>
          </div>
        </el-card>
      </div>
      <el-pagination
        class="board-message-pagination"
        background
        layout="prev, pager, next, jumper"
        :current-page.sync="currentPage"
        :total="total"
        @current-change="onGetComment"></el-pagination>
    </el-main>
  </div>
</template>

<script>
import { getComment, incLikeNum, incDislikeNum } from '@/api/comment'
import { postReply, getReply, deleteReply } from '@/api/reply'

export default {
  name: 'MessageBoardIndex',
  components: {},
  props: {},
  data () {
    return {
      username: '',
      searchField: '',
      seqValue: false,
      replyContent: '',
      currentPage: 1,
      comments: [{
        commentId: 1,
        avatarUrl: '',
        fromUsername: '',
        pubDate: '',
        likeNum: 0,
        dislikeNum: 0,
        content: '',
        replyIsShow: false,
        inputIsShow: false,
        reply: [{
          linkIsShow: true,
          fromUserName: '',
          replyId: 1
        }]
      }],
      total: 0
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
    this.onGetComment()
    this.username = localStorage.getItem('username')
  },
  methods: {
    onGetComment () {
      getComment({
        searchField: this.searchField,
        page: this.currentPage,
        seqValue: this.seqValue
      })
        .then(response => {
          this.comments = response.data.payload.comment
          this.total = response.data.payload.total
        })
        .catch(err => {
          console.log(err)
        })
    },
    onLike (item, key) {
      incLikeNum(item.commentId)
        .then(response => {
          if (response.data.msg === 'failed') {
            this.$message.error('你已经点过赞了')
          } else if (response.data.msg === 'success') {
            this.comments[key].likeNum += 1
            this.$message.success('点赞成功')
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    onDislike (item, key) {
      incDislikeNum(item.commentId)
        .then(response => {
          if (response.data.msg === 'failed') {
            this.$message.error('你已经点过踩了')
          } else if (response.data.msg === 'success') {
            this.comments[key].dislikeNum += 1
            this.$message.success('点踩成功')
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    openInput (item, key) {
      this.comments[key].inputIsShow = !this.comments[key].inputIsShow
    },
    onInput (item) {
      postReply({
        content: this.replyContent,
        toCommentId: item.commentId
      })
        .then(res => {
          if (res.data.msg === 'success') {
            this.$message.success('评论成功')
          } else {
            this.$message.error('评论失败')
          }
          this.replyContent = ''
          item.inputIsShow = false
        })
        .catch(err => {
          console.log(err)
        })
    },
    onReply (item, key) {
      getReply(item.commentId, 1)
        .then(res => {
          item.reply = res.data.payload.reply
          this.comments[key].replyIsShow = !this.comments[key].replyIsShow
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
    onOther (item, key) {
      const routeData = this.$router.resolve({
        path: '/other',
        query: {
          name: item.fromUsername
        }
      })
      window.open(routeData.href, '_blank')
    }
  }
}
</script>

<style scoped lang="less">
.board-header {
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-top: 50px;
  padding-bottom: 50px;

  .board-header-title {
    font-size: 40px;
    position: relative;
    text-align: center;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.board-header-search {
  margin-top: 15px;

  .board-header-search-switch {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}

.board-message-card {
  position: relative;
  margin-top: 30px;
}

.board-message-card-header {
  display: flex;
  justify-content: left;
  align-items: center;

  .board-message-card-header-name {
    margin-left: 20px;
  }

  .board-message-card-header-date {
    position: absolute;
    right: 10px;
  }

  .board-message-card-ava {
    cursor: pointer;
  }

  :nth-child(3) {
    font-size: 10px;
  }

  :nth-child(4) {
    position: absolute;
    right: 210px;
  }

  :nth-child(5) {
    position: absolute;
    right: 130px;
  }

  :nth-child(6) {
    margin-left: 40px;
    cursor: pointer;
  }

  :nth-child(8) {
    margin-left: 40px;
    cursor: pointer;
  }
}

.board-message-card-body-comment {
  display: flex;
  align-items: center;
  justify-content: center;
}

.board-message-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.board-message-card-body-button {
  position: relative;
  margin-left: 92%;
  margin-top: 10px;
}

.board-message-card-body-reply {
  height: auto;
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

</style>
