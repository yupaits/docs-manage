<template>
  <div>
    <div>
      <a-layout class="layout">
        <a-layout-header class="layout-header">
          <div class="logo">文档管理</div>
          <div class="opt">
            <span class="header-user">{{$store.getters.user.username}}</span>
            <a-button type="dashed" size="small" @click="logout">注销</a-button>
          </div>
          <a-menu theme="dark" mode="horizontal" :defaultSelectedKeys="['/docs']" :selectedKeys="$store.getters.currentKeys" style="line-height: 64px">
            <a-menu-item key="/docs" @click.native="$router.push('/docs')">文档</a-menu-item>
            <a-menu-item key="/cates" @click.native="$router.push('/cates')">分类</a-menu-item>
          </a-menu>
        </a-layout-header>
        <a-layout-content class="layout-content-container">
          <div class="layout-content">
            <router-view></router-view>
          </div>
        </a-layout-content>
        <a-layout-footer :style="{ textAlign: 'center' }">
          文档管理 ©2018 <a href="https://github.com/YupaiTS" target="_blank">YupaiTS</a>
        </a-layout-footer>
      </a-layout>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Index",
    methods: {
      logout() {
        this.$store.dispatch('removeUserInfo');
        this.$cookies.remove(this.consts.tokenCookie);
        this.$router.push('/login');
      }
    }
  }
</script>

<style scoped>
  .layout .logo {
    color: #eee;
    margin-right: 80px;
    font-size: 24px;
    line-height: 60px;
    float: left;
  }
  .layout .opt {
    color: #eee;
    line-height: 60px;
    float: right;
  }
  .layout-header {
    position: fixed;
    width: 100%;
  }
  .header-user {
    margin-right: 16px;
    font-size: 16px;
  }
  .layout-content-container {
    padding: 0 50px;
    margin-top: 100px;
  }
  .layout-content {
    background: #fff;
    padding: 24px;
    min-height: calc(100vh - 169px);
  }
</style>