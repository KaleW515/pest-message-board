<template>
  <div class="user-admin-container">
    <el-table :data="tableData" class="user-table" height="80%">
      <el-table-column v-for="(item, key) in titles" :key="key" :label="item.name" :prop="item.value"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="onFree(scope.$index, scope.row)">封禁
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="onUnfree(scope.$index, scope.row)">解封
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { freezeUser, unfreezeUser, getAllUsers } from '@/api/admin'

export default {
  name: 'UserAdminIndex',
  components: {},
  props: {},
  data () {
    return {
      tableData: [],
      titles: [
        {
          name: '用户号',
          value: 'userId'
        }, {
          name: '姓名',
          value: 'username'
        }, {
          name: '密码',
          value: 'password'
        }, {
          name: '签名',
          value: 'signature'
        }, {
          name: '管理员',
          value: 'isSuperuser'
        }, {
          name: '加入日期',
          value: 'dateJoined'
        }]
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
    this.onGetUsers()
  },
  methods: {
    onUnfree (index, row) {
      unfreezeUser(row.userId)
        .then(res => {
          if (res.data.msg === '解冻成功') {
            this.$message.success(res.data.msg)
          } else {
            this.$message.error(res.data.msg)
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    onFree (index, row) {
      freezeUser(row.userId)
        .then(res => {
          if (res.data.msg === '冻结成功') {
            this.$message.success(res.data.msg)
          } else {
            this.$message.error(res.data.msg)
          }
        })
        .catch(err => {
          console.log(err)
        })
    },
    onGetUsers () {
      getAllUsers()
        .then(res => {
          this.tableData = res.data.payload.users
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>

<style scoped lang="less">
.user-admin-container {
  height: 100%;
}
.user-table {
  width: 100%;
  height: 100%;
}
</style>
