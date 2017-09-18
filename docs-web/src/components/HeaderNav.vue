<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="dark">
      <b-nav-toggle target="nav-collapse"></b-nav-toggle>
      <b-navbar-brand href="/">文档管理</b-navbar-brand>
      <b-collapse is-nav id="nav-collapse">
        <b-nav is-nav-bar v-if="hasLogin">
          <b-nav-item to="/docs"><span class="fa fa-file-text-o"> 文档</span></b-nav-item>
          <b-nav-item to="/files"><span class="fa fa-file-word-o"> 文件</span></b-nav-item>
        </b-nav>
        <b-nav is-nav-bar class="ml-auto" v-if="hasLogin">
          <b-nav-item-dropdown right>
            <template slot="button-content">
              <em>{{username}}</em>
            </template>
            <b-dropdown-item href="#" @click="logout">退出登录</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-nav>
        <b-nav is-nav-bar class="ml-auto" v-else>
          <b-nav-item right to="/login">登录</b-nav-item>
          <b-nav-item right to="/register">注册账号</b-nav-item>
        </b-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>

<script>
  import constant from '../utils/constant'
  export default {
    props: {
      hasLogin: {
        type: Boolean,
        default: false
      },
      username: {
        type: String
      }
    },
    methods: {
      logout: function () {
        this.$cookies.remove(constant.user);
        this.$cookies.remove(constant.accessToken);
        this.$router.push('/login');
      }
    }
  }
</script>

<style>

</style>
