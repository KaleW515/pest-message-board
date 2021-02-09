<template>
  <div class="register-container">
    <el-form ref="registerForm" :model="registerForm" :rules="registerFormRules" label-width="80px"
             class="register-form">
      <span class="register-title">欢迎注册</span>
      <el-form-item label="账号" prop="username">
        <el-input type="text" placeholder="请输入账号" v-model="registerForm.username" auto-complete="off"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="registerForm.password" auto-complete="off"/>
      </el-form-item>
      <el-form-item class="register-form-button">
        <el-button type="primary" @click="onSubmit('registerForm')"
                   v-loading.fullscreen.lock="fullscreenLoading">提交
        </el-button>
        <el-button @click="resetForm('registerForm')">重置</el-button>
        <el-button type="primary" @click="$router.push('/login')">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getHash } from '@/util/tool'
import { register } from '@/api/user'

export default {
  name: 'RegisterIndex',
  components: {},
  props: {},
  data () {
    return {
      registerForm: {
        username: '',
        password: ''
      },
      fullscreenLoading: false,
      registerFormRules: {
        username: [
          {
            required: true,
            message: '用户名不可为空',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '密码不可为空',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
  },
  methods: {
    onSubmit (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.openFullScreen()
          this.registerForm.password = getHash(this.registerForm.password)
          register(this.registerForm)
            .then(res => {
              if (res.data.msg === '注册成功') {
                this.$message.success(res.data.msg)
                this.fullscreenLoading = false
                this.$router.push('/login')
              } else {
                this.$message.error(res.data.msg)
                this.fullscreenLoading = false
              }
            })
            .catch(err => {
              console.log(err)
            })
        } else {
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    openFullScreen () {
      this.fullscreenLoading = true
      setTimeout(() => {
        this.fullscreenLoading = false
      }, 20000)
    }
  }
}
</script>

<style scoped lang="less">
.register-container {
  position: fixed;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.register-form {
  position: fixed;
  width: 600px;
  height: 450px;
  padding: 30px;
  line-height: 100px;
  border: 1px solid #DCDFE6;
  border-radius: 5px;
  box-shadow: 0 0 2px #b1b4ba;

  .register-title {
    text-align: center;
    font-size: 40px;
    display: flex;
    justify-content: center;
    margin-top: 30px;
    margin-bottom: 50px;
  }

  .register-form-button {
    margin-bottom: 0;
    margin-top: 82.5px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: -80px;
  }
}

.el-form-item {
  margin-bottom: 30px;

  .el-button {
    padding-left: 30px;
    padding-right: 30px;
    margin-left: 40px;
    margin-right: 40px;
  }
}
</style>
