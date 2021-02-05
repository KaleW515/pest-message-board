<template>
  <div class="other-container">
    <el-header class="other-header">
      <div class="other-header-title">
        <span>个人信息</span>
      </div>
    </el-header>
    <el-main class="other-body">
      <img v-if="userInfo.avatarUuid" :src="userInfo.avatarUuid" alt="" class="other-body-avatar">
      <el-divider></el-divider>
      <div class="other-body-item">
        <span>姓名</span>
        <span>{{ userInfo.username }}</span>
      </div>
      <el-divider></el-divider>
      <div class="other-body-item">
        <span>签名</span>
        <span>{{ userInfo.signature }}</span>
      </div>
    </el-main>
  </div>
</template>

<script>
import { getInfoByName } from '@/api/user'

export default {
  name: 'OtherIndex',
  components: {},
  props: {},
  data () {
    return {
      userInfo: {
        avatarUuid: ''
      }
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
    getInfoByName(this.$route.query.name)
      .then(res => {
        this.userInfo = res.data.payload
      })
      .catch(err => {
        console.log(err)
      })
  },
  methods: {}
}
</script>

<style scoped lang="less">
.other-header {
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-top: 50px;
  padding-bottom: 50px;

  .other-header-title {
    font-size: 40px;
    position: relative;
    text-align: center;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.other-body {
  position: relative;
  width: 90%;
  height: auto;
  left: 50%;
  transform: translate(-50%, 0%);
  border-radius: 10px;
  line-height: 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-top: 20px;

  .other-body-avatar {
    position: relative;
    width: 200px;
    height: 200px;
    margin-top: 10px;
    left: 50%;
    transform: translate(-50%, 0);
  }
}
.other-body-item {
  :nth-child(1) {
    position: relative;
    left: 30px;
  }

  :nth-child(2) {
    position: fixed;
    left: 50%;
    transform: translate(-50%, 0);
    outline: none;
  }
}

</style>
