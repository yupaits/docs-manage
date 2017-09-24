<template>
  <b-container>
    <b-row align-h="center">
      <b-col lg="5" md="7" sm="9">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <h1 class="lead mt-5"><span class="fa fa-info-circle"> 用户反馈</span></h1>
        <b-card class="mt-2">
          <b-form @submit="sendFeedback">
            <b-form-group id="username"
                          label="用户名"
                          lable-for="username-input">
              <b-form-input id="username-input"
                            type="text"
                            v-model="username"
                            required
                            placeholder="输入您的用户名"></b-form-input>
            </b-form-group>
            <b-form-group id="email"
                          label="邮箱"
                          lable-for="email-input">
              <b-form-input id="email-input"
                            type="email"
                            v-model="email"
                            required
                            placeholder="输入您的邮箱"></b-form-input>
            </b-form-group>
            <b-form-group id="subject"
                          label="主题"
                          lable-for="subject-input">
              <b-form-input id="subject-input"
                            type="text"
                            v-model="subject"
                            required
                            placeholder="输入反馈主题"></b-form-input>
            </b-form-group>
            <b-form-group id="password"
                          label="反馈内容"
                          lable-for="content-input">
              <b-form-textarea id="content-input"
                               type="text"
                               v-model="content"
                               required
                               :rows="5"
                               :maxrows="8"
                               placeholder="描述你的问题或建议"></b-form-textarea>
            </b-form-group>
            <b-button type="submit" variant="success" block>提交反馈</b-button>
          </b-form>
          <div slot="footer" class="text-center">
            <router-link to="/" class="card-link">返回主页</router-link>
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
        subject: '',
        content: ''
      }
    },
    created() {
      const user = JSON.parse(this.$cookies.get(constant.user));
      this.username = user.username;
      this.email = user.email;
    },
    methods: {
      sendFeedback: function () {
        const instance = this;
        const feedbackForm = {
          username: this.username,
          email: this.email,
          subject: this.subject,
          content: this.content
        };
        request.Api.post('/feedback', feedbackForm).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.$router.go(-1);
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '发送反馈出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
