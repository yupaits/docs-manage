<template>
  <div>
    <header-nav :hasLogin="hasLogin" :username="username"></header-nav>
    <router-view></router-view>
  </div>
</template>

<script>
  import HeaderNav from '../components/HeaderNav'
  import constant from '../utils/constant'
  export default {
    data(){
      return {
        hasLogin: false,
        username: null
      }
    },
    created() {
      const user = JSON.parse(this.$cookies.get(constant.user));
      const hasLogin = (user !== null && user !== undefined && user !== '');
      this.hasLogin = hasLogin;
      if (hasLogin) {
        this.username = user.username;
      }
    },
    components: {
      HeaderNav
    }
  }
</script>

<style>

</style>
