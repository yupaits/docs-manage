<template>
  <div>
    <a-row type="flex" justify="center" class="register-form">
      <a-col :lg="{span: 6}" :md="{span: 8}" :sm="{span: 10}" :xs="{span: 12}">
        <a-card title="文档管理 | 注册">
          <a-row>
            <a-form>
              <a-form-item label="用户名" :labelCol="{span: 6}" :wrapperCol="{span: 18}"
                           :validateStatus="validate.username.status" :help="validate.username.help">
                <a-input v-model="registerForm.username" placeholder="请输入用户名">
                  <a-icon slot="addonBefore" type="user"/>
                </a-input>
              </a-form-item>
              <a-form-item label="邮箱" :labelCol="{span: 6}" :wrapperCol="{span: 18}"
                           :validateStatus="validate.email.status" :help="validate.email.help">
                <a-input type="email" v-model="registerForm.email" placeholder="请输入邮箱">
                  <a-icon slot="addonBefore" type="mail"/>
                </a-input>
              </a-form-item>
              <a-form-item label="密码" :labelCol="{span: 6}" :wrapperCol="{span: 18}"
                           :validateStatus="validate.password.status" :help="validate.password.help">
                <a-input type="password" v-model="registerForm.password" placeholder="请输入密码">
                  <a-icon slot="addonBefore" type="key"/>
                </a-input>
              </a-form-item>
              <a-form-item label="确认密码" :labelCol="{span: 6}" :wrapperCol="{span: 18}"
                           :validateStatus="validate.confirmPassword.status" :help="validate.confirmPassword.help">
                <a-input type="password" v-model="registerForm.confirmPassword" placeholder="请输入确认密码" @keyup.enter="register">
                  <a-icon slot="addonBefore" type="key"/>
                </a-input>
              </a-form-item>
              <a-form-item :wrapperCol="{span: 18, offset: 6}">
                <a-button type="primary" class="form-btn" :loading="registerProcessing" @click="register">注册</a-button>
                <a class="form-btn" @click="$router.push('/login')">直接登录</a>
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
    name: "Register",
    data() {
      return {
        registerForm: {
          username: '',
          email: '',
          password: '',
          confirmPassword: ''
        },
        validate: {
          username: {status: '', help: ''},
          email: {status: '', help: ''},
          password: {status: '', help: ''},
          confirmPassword: {status: '', help: ''},
        },
        registerProcessing: false
      }
    },
    methods: {
      register() {
        if (this.validateRegisterForm()) {
          this.registerProcessing = true;
          let registerUrl = '/register/123';
          this.api.post(registerUrl, this.registerForm).then(res => {
            this.$notification.success({message: '注册成功', duration: 3});
            this.$router.push('/login');
            this.registerProcessing = false;
          }).catch(error => {
            this.registerProcessing = false;
          });
        }
      },
      validateRegisterForm() {
        let valid = true;
        let form = this.registerForm;
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
        if (!form.email) {
          this.validate.email.status = 'error';
          this.validate.email.help = '邮箱不能为空';
          valid = false;
        } else {
          this.validate.email.status = '';
          this.validate.email.help = '';
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
        if (!form.confirmPassword) {
          this.validate.confirmPassword.status = 'error';
          this.validate.confirmPassword.help = '确认密码不能为空';
          valid = false;
        } else if (form.confirmPassword !== form.password) {
          this.validate.confirmPassword.status = 'error';
          this.validate.confirmPassword.help = '两次输入的密码不一致';
          valid = false;
        } else {
          this.validate.confirmPassword.status = '';
          this.validate.confirmPassword.help = '';
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