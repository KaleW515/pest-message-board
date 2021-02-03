<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginFormRules" label-width="80px" class="login-form">
      <span class="login-title">欢迎登录</span>
      <el-form-item label="账号" prop="username">
        <el-input type="text" placeholder="请输入账号" v-model="loginForm.username" auto-complete="off"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="loginForm.password" auto-complete="off"/>
      </el-form-item>
      <el-form-item label="验证码" prop="code" @keyup.enter.native="onSubmit('loginForm')">
        <el-row>
          <el-col :span="12">
            <el-input type="text" placeholder="请输入验证码" v-model="loginForm.code" auto-complete="off"/>
          </el-col>
          <el-col :span="12" style="height: 30px;">
            <img :src=captchaUrl alt="点击换一张" id="id_captcha" @click="onCaptcha">
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item class="login-form-button">
        <el-button type="primary" @click="onSubmit('loginForm')"
                   v-loading.fullscreen.lock="fullscreenLoading">提交
        </el-button>
        <el-button @click="resetForm('loginForm')">重置</el-button>
        <el-button type="primary" @click="$router.push('/register')">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getCaptcha, login } from '@/api/user'
import { getHash } from '@/util/tool'

export default {
  name: 'LoginIndex',
  components: {},
  props: {},
  data () {
    return {
      captchaUrl: '',
      loginForm: {
        username: '',
        password: '',
        code: ''
      },
      fullscreenLoading: false,
      loginFormRules: {
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
        ],
        code: [
          {
            required: true,
            message: '验证码不可为空',
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
    this.onCaptcha()
  },
  methods: {
    async onSubmit (formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.openFullScreen()
          this.loginForm.password = getHash(this.loginForm.password)
          login(this.loginForm)
            .then((response) => {
              if (response.data.msg !== '登录成功') {
                this.$message.error(response.data.msg)
                this.fullscreenLoading = false
                this.onCaptcha()
              } else {
                localStorage.setItem('Authorization', response.data.payload)
                this.fullscreenLoading = false
                this.$message({
                  message: '恭喜你，登录成功',
                  type: 'success'
                })
                this.$router.push('/home')
              }
            })
            .catch((error) => {
              console.log(error)
            })
        } else {
          return false
        }
      })
    },
    onCaptcha () {
      getCaptcha()
        .then(response => {
          const blob = new Blob([response.data], { type: 'image/jpeg' })
          this.captchaUrl = window.URL.createObjectURL(blob)
        })
        .catch(err => {
          console.log(err)
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
.login-container {
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

.login-form {
  position: fixed;
  width: 600px;
  height: 450px;
  padding: 30px;
  border: 1px solid #DCDFE6;
  border-radius: 5px;
  box-shadow: 0 0 2px #b1b4ba;

  .login-title {
    text-align: center;
    font-size: 40px;
    display: flex;
    justify-content: center;
    margin-top: 30px;
    margin-bottom: 50px;
  }

  .login-form-button {
    margin-bottom: 0;
    margin-top: 60px;
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
