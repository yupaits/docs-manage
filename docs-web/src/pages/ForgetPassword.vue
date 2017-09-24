<template>
  <b-container>
    <b-row align-h="center">
      <b-col lg="4" md="6" sm="8">
        <h1 class="text-center mt-5"><span class="fa fa-book"></span></h1>
        <p class="lead text-center mt-2">重置登录密码</p>
        <b-card>
          <b-form @submit="sendResetPasswordEmail" v-if="emailSent">
            <b-form-group id="email"
                          label="输入您的邮箱地址，我们将发送修改密码的链接到您的邮箱。"
                          lable-for="email-input">
              <b-form-input id="email-input"
                            type="email"
                            v-model="email"
                            required
                            placeholder="邮箱地址"></b-form-input>
            </b-form-group>
            <b-button type="submit" variant="success" block class="mb-3">发送重置密码邮件</b-button>
          </b-form>
          <div v-else>
            <p>检查您的邮箱是否收到重置登录密码的链接。如果没有发现，请检查邮件垃圾箱。</p>
            <b-button variant="light" to="/login" block>返回登录</b-button>
          </div>
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
  export default {
    data() {
      return {
        email: '',
        emailSent: false
      }
    },
    methods: {
      sendResetPasswordEmail: function () {
        const instance = this;
        request.Api.post('/sendResetPasswordEmail?email=' + this.email).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.emailSent = true;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '发送重置密码邮件出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
