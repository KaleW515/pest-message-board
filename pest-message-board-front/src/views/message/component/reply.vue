<template>
  <div class="reply-container">
    <span v-text="deliver.item.content" class="reply-container-comment"></span>
    <div class="reply-container-reply" v-if="deliver.item.replyIsShow" style="overflow: auto">
      <ul class="infinite-list">
        <li class="infinite-list-item" v-for="(item, key) in deliver.item.reply" :key="key">
          <div style="display: flex; align-items: center">
            <el-avatar :size="30" :src="item.avatarUrl" @click.native="onOther(item, key)"
                       style="cursor: pointer"></el-avatar>
            <span v-text="item.fromUsername"></span>
            <span>:</span>
            <span v-text="item.content"></span></div>
          <div>
            <el-link type="primary" v-if="item.fromUsername === username" class="delete-reply"
                     @click="onDeleteReply(item, key)">{{ '删除' }}
            </el-link>
          </div>
        </li>
      </ul>
    </div>
    <el-input
      id="input"
      :rows="5"
      placeholder="请输入回复内容"
      type="textarea"
      v-if="deliver.item.inputIsShow"
      v-model="replyContent"></el-input>
    <div class="message-container-button">
      <el-button @click="toogleDialogEmoji" v-if="deliver.item.inputIsShow" style="height: 50px">😃</el-button>
      <VEmojiPicker
        v-show="showDialog"
        labelSearch="Search"
        lang="pt-BR"
        @select="onSelectEmoji"
      />
      <el-button icon="el-icon-check" v-if="deliver.item.inputIsShow" @click="onInput(deliver.item, deliver.key)"
                 style="height: 50px"></el-button>
    </div>
  </div>
</template>

<script>
import { deleteReply, postReply } from '@/api/reply'
import { VEmojiPicker } from 'v-emoji-picker'

export default {
  name: 'ReplyIndex',
  components: { VEmojiPicker },
  props: ['deliver'],
  data () {
    return {
      replyContent: '',
      username: JSON.parse(localStorage.getItem('user')).username,
      showDialog: false
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
  },
  methods: {
    onOther (item, key) {
      const routeData = this.$router.resolve({
        path: '/other',
        query: {
          name: item.fromUsername
        }
      })
      window.open(routeData.href, '_blank')
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
    onInput (item, key) {
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
          this.showDialog = false
          item.inputIsShow = false
        })
        .catch(err => {
          console.log(err)
        })
    },
    toogleDialogEmoji () {
      this.showDialog = !this.showDialog
    },
    onSelectEmoji (emoji) {
      const input = document.getElementById('input')
      const startPos = input.selectionStart
      const endPos = input.selectionEnd
      const resultText = input.value.substring(0, startPos) + emoji.data + input.value.substring(endPos)
      input.value = resultText
      input.focus()
      input.selectionStart = startPos + emoji.data.length
      input.selectionEnd = startPos + emoji.data.length
      this.replyContent = resultText
    }
  }
}
</script>

<style scoped lang="less">
.reply-container-comment {
  display: flex;
  align-items: center;
  justify-content: center;
}

.infinite-list {
  height: auto;
  max-height: 200px;
  padding: 10px;
}

.infinite-list-item {
  list-style: none;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;

  :nth-child(2) {
    margin-left: 20px;
  }

  :nth-child(4) {
    margin-left: 20px;
  }
}

.message-container-button {
  margin-top: 5px;
  display: flex;
  justify-content: space-between;
}
</style>
