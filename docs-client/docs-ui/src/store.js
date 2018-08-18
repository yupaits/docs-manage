import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    user: {},
    currentKeys: []
  },
  getters: {
    user(state) {
      return state.user;
    },
    currentKeys(state) {
      return state.currentKeys;
    }
  },
  mutations: {
    setUserInfo(state, user) {
      state.user = user;
    },
    removeUserInfo(state) {
      state.user = {};
    },
    setCurrentKeys: (state, keys) => {
      state.currentKeys = keys;
    }
  },
  actions: {
    setUserInfo: ({commit}, user) => {
      commit('setUserInfo', user);
    },
    removeUserInfo: ({commit}) => {
      commit('removeUserInfo');
    },
    setCurrentKeys: ({commit}, keys) => {
      commit('setCurrentKeys', keys);
    }
  }
});

export default store