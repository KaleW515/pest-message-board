import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    newReply: 0,
    newLike: 0,
    newDislike: 0
  },
  mutations: {
    setNewReply (state, msg) {
      state.newReply = msg.newReply
    },
    setNewLike (state, msg) {
      state.newLike = msg.newLike
    },
    setNewDislike (state, msg) {
      state.newDislike = msg.newDislike
    },
    noticeInit (state, msg) {
      state.newReply = msg.newReply
      state.newLike = msg.newLike
      state.newDislike = msg.newDislike
    }
  }
})

export default store
