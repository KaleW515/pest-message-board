<template>
  <div class="ReviseContainer">
    <el-dialog title="修改密码" :visible="deliver" append-to-body @close="closeDialog">
      <el-form ref="reviseForm" :model="reviseForm" :rules="rules" label-width="80px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input type="password" placeholder="请输入原始密码" v-model="reviseForm.oldPassword" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" placeholder="请输入新密码" v-model="reviseForm.newPassword" auto-complete="off"/>
        </el-form-item>
        <el-form-item label="重复密码" prop="checkNewPassword">
          <el-input type="password" placeholder="请重复新密码" v-model="reviseForm.checkNewPassword"
                    auto-complete="off"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     @click="onRevisePassword('reviseForm')">提交
          </el-button>
          <el-button @click="resetForm('reviseForm')">重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { revisePassword } from '@/api/user'
import { getHash } from '@/util/tool'

export default {
  name: 'ReviseIndex',
  components: {},
  props: ['deliver'],
  data () {
    const validatePwd = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.reviseForm.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      reviseForm: {
        oldPassword: '',
        newPassword: '',
        checkNewPassword: ''
      },
      rules: {
        oldPassword: [
          {
            required: true,
            message: '请输入原密码',
            trigger: 'blur'
          }
        ],
        newPassword: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          }
        ],
        checkNewPassword: [
          {
            required: true,
            message: '请重复新密码',
            trigger: 'blur'
          },
          {
            validator: validatePwd,
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
    onRevisePassword (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.reviseForm.newPassword = getHash(this.reviseForm.newPassword)
          this.reviseForm.oldPassword = getHash(this.reviseForm.oldPassword)
          revisePassword(this.reviseForm)
            .then(res => {
              if (res.data.msg === '更新成功') {
                this.$message.success(res.data.msg)
              } else {
                this.$message.error(res.data.msg)
              }
              this.resetForm(formName)
              localStorage.clear()
              this.$router.push('/login')
            })
            .catch(err => {
              console.log(err)
            })
        } else {
          this.dialogVisible = false
          return false
        }
      })
    },
    closeDialog () {
      this.$emit('closeDia')
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped lang="less"></style>
