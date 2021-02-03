<template>
  <div class="board-container">
    <el-header class="board-header">
      <div class="board-header-title">留言板</div>
    </el-header>
    <div class="board-header-search">
      <el-input
        @keyup.enter.native="onSearchComments()"
        placeholder="请输入内容"
        prefix-icon="el-icon-search"
        v-model="searchField">
      </el-input>
      <el-switch
        class="board-header-search-switch"
        @change="init()"
        v-model="valueOpen"
        active-text="按点赞数排序"
        inactive-text="按时间排序">
      </el-switch>
    </div>
    <el-main>
      <div v-for="(item, key) in messages" :key="key">
        <el-card shadow="hover" class="board-message-card">
          <div slot="header">
            <div @click="onPersonInfo(item)">
              <el-avatar :size="50" :src="item.avaUrl" class="board-message-card-ava"></el-avatar>
            </div>
          </div>
        </el-card>
      </div>
    </el-main>
  </div>
</template>

<script>
export default {
  name: 'MessageBoardIndex',
  components: {},
  props: {},
  data () {
    return {
      searchField: '',
      valueOpen: '',
      messages: []
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
  },
  methods: {
    onSearchComments () {
    },
    onPersonInfo (item) {
    }
  }
}
</script>

<style scoped lang="less">
.board-header {
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding-top: 50px;
  padding-bottom: 50px;

  .board-header-title {
    font-size: 40px;
    position: relative;
    text-align: center;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.board-header-search {
  margin-top: 15px;

  .board-header-search-switch {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>
