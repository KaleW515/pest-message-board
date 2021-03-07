<template>
  <div slot="header">
    <div class="message-card-header">
      <el-avatar :size="50" :src="deliver.item.avatarUrl" class="message-card-ava"
                 @click.native="onOther(deliver.item, deliver.key)"></el-avatar>
      <span class="message-card-header-name">{{ deliver.item.fromUsername }}</span>
      <span class="message-card-header-date">{{ deliver.item.pubDate }}</span>
      <el-link @click="openInput(deliver.item, deliver.key)">回复</el-link>
      <el-link @click="onReply(deliver.item, deliver.key)">查看回复</el-link>
      <el-tooltip effect="dark" content="点个赞" placement="top-end">
        <i class="el-icon-top" @click="onLike(deliver.item, deliver.key)"></i>
      </el-tooltip>
      <span v-text="deliver.item.likeNum"></span>
      <el-tooltip effect="dark" content="踩一下" placement="top-end">
        <i class="el-icon-bottom" @click="onDislike(deliver.item, deliver.key)"></i>
      </el-tooltip>
      <span v-text="deliver.item.dislikeNum"></span>
    </div>
  </div>
</template>

<script>
import { incDislikeNum, incLikeNum } from '@/api/comment'
import { getReply } from '@/api/reply'

export default {
  name: 'CommentIndex',
  components: {},
  props: ['deliver'],
  data () {
    return {
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
  },
  methods: {
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
      this.$emit('openInput', key)
    },
    onReply (item, key) {
      getReply(item.commentId, 1)
        .then(res => {
          item.reply = res.data.payload.reply
          this.$emit('openComment', key)
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

.message-card-header {
  display: flex;
  justify-content: left;
  align-items: center;

  .message-card-header-name {
    margin-left: 20px;
  }

  .message-card-header-date {
    position: absolute;
    right: 10px;
  }

  .message-card-ava {
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
</style>
