<template>
  <div class="publish-container">
    <el-header class="publish-header">
      <span>发布留言</span>
    </el-header>
    <el-main>
      <el-input
        :rows="10"
        placeholder="请输入回复内容,字数限制200字"
        type="textarea"
        id="input"
        v-model="comment">
      </el-input>
      <el-button @click="toogleDialogEmoji" class="emoji-button">😃</el-button>
      <VEmojiPicker
        v-show="showDialog"
        labelSearch="Search"
        lang="pt-BR"
        @select="onSelectEmoji"
      />
      <el-button icon="el-icon-check" type="primary" class="publish-button" @click="onPublish">提交</el-button>
    </el-main>
  </div>
</template>

<script>
import { publishComment } from '@/api/comment'
import { VEmojiPicker } from 'v-emoji-picker'

export default {
  name: 'PublishIndex',
  components: { VEmojiPicker },
  props: {},
  data () {
    return {
      comment: '',
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
    onPublish () {
      if (this.comment.length < 1) {
        this.$message.error('留言太短了')
      } else {
        publishComment({
          content: this.comment
        })
          .then(res => {
            if (res.data.msg === '添加成功') {
              this.$message.success('添加成功')
            } else {
              this.$message.error('添加失败')
            }
          })
          .catch(err => {
            console.log(err)
          })
      }
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
      this.comment = resultText
      console.log(this.comment)
    }
  }
}
</script>

<style scoped lang="less">
.publish-header {
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-top: 50px;
  padding-bottom: 50px;
  font-size: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.publish-button {
  position: absolute;
  right: 55px;
  margin-top: 20px;
}

.emoji-button {
  margin-top: 20px;
}
</style>
