<template>
  <div class="publish-container">
    <el-header class="publish-header">
      <div class="publish-header-title">发布留言</div>
    </el-header>
    <el-main>
      <el-input
        :rows="18"
        placeholder="请输入回复内容,字数限制200字"
        type="textarea"
        v-model="comment">
      </el-input>
      <el-button icon="el-icon-check" type="primary" class="publish-button" @click="onPublish">提交</el-button>
    </el-main>
  </div>
</template>

<script>
import { publishComment } from '@/api/comment'

export default {
  name: 'PublishIndex',
  components: {},
  props: {},
  data () {
    return {
      comment: ''
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

  .publish-header-title {
    font-size: 40px;
    position: relative;
    text-align: center;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.publish-button {
  position: relative;
  left: 92%;
  margin-top: 20px;
}
</style>
