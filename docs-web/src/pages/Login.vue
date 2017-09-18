<template>
  <div>
    <b-container>
      <b-row align-h="center">
        <b-col lg="4" md="6" sm="8">
          <h1 class="text-center mt-5"><span class="fa fa-book"></span></h1>
          <p class="lead text-center mt-2">登录文档管理</p>
          <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
            <b>{{alert.msg}}</b>
          </b-alert>
          <b-card>
            <b-form @submit="login">
              <b-form-group id="username"
                            label="用户名"
                            lable-for="username-input"
                            description="项目名称是唯一的，不允许重复。">
                <b-form-input id="username-input"
                              type="text"
                              v-model="username"
                              required
                              placeholder="输入用户名"></b-form-input>
              </b-form-group>
              <b-form-group id="password"
                            label="密码"
                            lable-for="password-input"
                            description="密码不少于3位">
                <b-form-input id="password-input"
                              type="password"
                              v-model="password"
                              required
                              placeholder="输入密码"></b-form-input>
              </b-form-group>
              <b-button type="submit" variant="success" block>登录</b-button>
            </b-form>
            <b-card-body class="text-center">
              <a href="/register.html" class="card-link">创建账号</a>
              <a href="/forgetPassword.html" class="card-link">忘记密码？</a>
            </b-card-body>
            <div slot="footer" class="text-center">
              <router-link to="/" class="card-link">返回主页</router-link>
              <router-link to="/feedback" class="card-link">用户反馈</router-link>
            </div>
          </b-card>
        </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
  import request from '../utils/request'
  import constant from '../utils/constant'
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        username: '',
        password: ''
      }
    },
    methods: {
      login: function (event) {
        event.preventDefault();
        const instance = this;
        const loginForm = {
          username: this.username,
          password: this.password
        };
        request.Auth.post(constant.login, loginForm).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
            instance.password = '';
          } else {
            instance.$cookies.set(constant.user, JSON.stringify(result.data.user), result.data.expiredIn);
            instance.$cookies.set(constant.accessToken, result.data.accessToken, result.data.expiredIn);
            instance.$router.push('/');
          }
        }).catch(function (error) {
          console.error(error);
          instance.alert = {variant: 'danger', msg: '登录出错', show: 5};
          instance.password = '';
        });
      }
    }
  }
</script>

<style>

</style>
