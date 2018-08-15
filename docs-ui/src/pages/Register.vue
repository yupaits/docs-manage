<template>
  <b-container>
    <b-row align-h="center">
      <b-col lg="5" md="7" sm="9">
        <h1 class="text-center mt-5"><span class="fa fa-book"></span></h1>
        <p class="lead text-center mt-2">注册账号</p>
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-card>
          <b-form @submit="register">
            <b-form-group id="username"
                          label="用户名"
                          lable-for="username-input"
                          description="用户名是唯一不可修改的，请仔细填写。">
              <b-form-input id="username-input"
                            type="text"
                            v-model="username"
                            required
                            placeholder="输入用户名"></b-form-input>
            </b-form-group>
            <b-form-group id="email"
                          label="邮箱"
                          lable-for="email-input"
                          description="您将偶尔收到与帐户相关的电子邮件。我们保证不公开您的邮箱。">
              <b-form-input id="email-input"
                            type="email"
                            v-model="email"
                            required
                            placeholder="输入邮箱"></b-form-input>
            </b-form-group>
            <b-form-group id="password"
                          label="密码"
                          lable-for="password-input"
                          description="密码必须包含至少一个小写字母、一个数字，长度不少于7位。">
              <b-form-input id="password-input"
                            type="password"
                            v-model="password"
                            required
                            placeholder="输入密码"></b-form-input>
            </b-form-group>
            <b-form-group id="confirmPassword"
                          label="确认密码"
                          lable-for="confirmPassword-input"
                          description="两次输入的密码需要保持一致。">
              <b-form-input id="confirmPassword-input"
                            type="password"
                            v-model="confirmPassword"
                            required
                            :state="passwordMatch"
                            placeholder="输入确认密码"></b-form-input>
            </b-form-group>
            <b-button type="submit" variant="success" block>注册账号</b-button>
          </b-form>
          <b-card-body class="text-center">
            <router-link to="/login" class="card-link">账号登录</router-link>
          </b-card-body>
          <div slot="footer" class="text-center">
            <router-link to="/" class="card-link">返回主页</router-link>
            <router-link to="/feedback" class="card-link">用户反馈</router-link>
          </div>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import request from '../utils/request'
  import constant from '../utils/constant'
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      }
    },
    computed: {
      passwordMatch() {
        return this.confirmPassword !== this.password;
      }
    },
    methods: {
      register: function () {
        const instance = this;
        const registerForm = {
          username: this.username,
          email: this.email,
          password: this.email,
          confirmPassword: this.confirmPassword
        };
        request.Auth.post(constant.register, registerForm).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.$router.push('/login');
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '注册账号出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
