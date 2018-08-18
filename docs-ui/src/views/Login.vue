<template>
  <div>
    <a-row type="flex" justify="center" class="register-form">
      <a-col :lg="{span: 6}" :md="{span: 8}" :sm="{span: 10}" :xs="{span: 12}">
        <a-card title="文档管理 | 登录">
          <a-row>
            <a-form>
              <a-form-item label="用户名" :labelCol="{span: 6}" :wrapperCol="{span: 18}"
                           :validateStatus="validate.username.status" :help="validate.username.help">
                <a-input v-model="loginForm.username" placeholder="请输入用户名">
                  <a-icon slot="addonBefore" type="user"/>
                </a-input>
              </a-form-item>
              <a-form-item label="密码" :labelCol="{span: 6}" :wrapperCol="{span: 18}"
                           :validateStatus="validate.password.status" :help="validate.password.help">
                <a-input type="password" v-model="loginForm.password" placeholder="请输入登录密码" @keyup.enter="login">
                  <a-icon slot="addonBefore" type="key"/>
                </a-input>
              </a-form-item>
              <a-form-item :wrapperCol="{span: 18, offset: 6}">
                <a-button type="primary" :loading="loginProcessing" class="form-btn" @click="login">登录</a-button>
                <a class="form-btn" @click="$router.push('/register')">注册账号</a>
              </a-form-item>
            </a-form>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  export default {
    name: "Login",
    data() {
      return {
        loginForm: {
          username: '',
          password: ''
        },
        validate: {
          username: {status: '', help: ''},
          password: {status: '', help: ''}
        },
        loginProcessing: false
      }
    },
    methods: {
      login() {
        if (this.validateLoginForm()) {
          this.loginProcessing = true;
          let loginUrl = '/login';
          this.api.post(loginUrl, this.loginForm).then(res => {
            this.$cookies.set(this.consts.tokenCookie, res.data, this.consts.tokenExpiredInMinutes);
            this.fetchUser();
            this.$router.push('/');
            this.loginProcessing = false;
          }).catch(error => {
            this.loginProcessing = false;
          });
        }
      },
      fetchUser() {
        let fetchUserUrl = '/user';
        this.api.get(fetchUserUrl).then(res => {
          this.$store.dispatch('setUserInfo', res.data);
        });
      },
      validateLoginForm() {
        let valid = true;
        let form = this.loginForm;
        if (form.username === '') {
          this.validate.username.status = 'error';
          this.validate.username.help = '用户名不能为空';
          valid = false;
        } else if (form.username.length < 2 || form.username.length > 20) {
          this.validate.username.status = 'error';
          this.validate.username.help = '用户名的长度为2-20位';
          valid = false;
        } else {
          this.validate.username.status = '';
          this.validate.username.help = '';
        }
        if (!form.password) {
          this.validate.password.status = 'error';
          this.validate.password.help = '密码不能为空';
          valid = false;
        } else if (form.password.length < 6 || form.password.length > 16) {
          this.validate.password.status = 'error';
          this.validate.password.help = '密码长度为6-16位';
          valid = false;
        } else {
          this.validate.password.status = '';
          this.validate.password.help = '';
        }
        return valid;
      }
    }
  }
</script>

<style scoped>
  .register-form {
    padding-top: 100px;
  }
  .form-btn {
    margin-right: 24px;
  }
</style>