<template>
  <div class="message-container">
    <el-header class="message-container-header">
      <span>留言板</span>
    </el-header>
    <div class="message-search">
      <el-input
        @keyup.enter.native="onGetComment"
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        v-model="searchField">
      </el-input>
      <el-switch
        class="message-search-switch"
        @change="onGetComment"
        v-model="seqValue"
        active-text="按点赞数排序"
        inactive-text="按时间排序">
      </el-switch>
    </div>
    <el-main class="message-main">
      <div v-for="(item, key) in comments" :key="key">
        <el-card shadow="hover" class="message-card">
          <comment :deliver="{item: item, key: key}" @openComment="openComment" @openInput="openInput"></comment>
          <reply :deliver="{item:item, key:key}"></reply>
        </el-card>
      </div>
      <el-pagination
        class="message-pagination"
        background
        layout="prev, pager, next, jumper"
        :current-page.sync="currentPage"
        :total="total"
        @current-change="onGetComment"></el-pagination>
    </el-main>
  </div>
</template>

<script>
import { getComment } from '@/api/comment'
import Reply from '@/views/message/component/reply'
import Comment from '@/views/message/component/comment'

export default {
  name: 'MessageBoardIndex',
  components: {
    Reply,
    Comment
  },
  props: {},
  data () {
    return {
      username: '',
      searchField: '',
      seqValue: false,
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
    openComment (key) {
      this.comments[key].replyIsShow = !this.comments[key].replyIsShow
    },
    openInput (key) {
      this.comments[key].inputIsShow = !this.comments[key].inputIsShow
    }
  }
}
</script>

<style scoped lang="less">
.message-container {
  position: relative;
}

.message-main {
  position: absolute;
  right: 0;
  left: 0;
}

.message-container-header {
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-top: 50px;
  padding-bottom: 50px;
  font-size: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.message-search {
  margin-top: 15px;

  .message-search-switch {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}

.message-card {
  position: relative;
  margin-top: 30px;
}

.message-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.message-card-body-button {
  position: relative;
  margin-left: 92%;
  margin-top: 10px;
}

</style>
