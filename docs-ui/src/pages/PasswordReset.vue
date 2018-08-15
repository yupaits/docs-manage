<template>
  <b-container>
    <b-row align-h="center">
      <b-col lg="4" md="6" sm="8">
        <h1 class="text-center mt-5"><span class="fa fa-book"></span></h1>
        <p class="lead text-center mt-2">更改密码 @YupaiTS</p>
        <b-card>
          <b-form @submit="">
            <p>密码必须包含至少一个小写字母、一个数字，长度不少于7位。</p>
            <b-form-group id="password"
                          label="密码"
                          lable-for="password-input">
              <b-form-input id="password-input"
                            type="password"
                            v-model="password"
                            required
                            placeholder="输入密码"></b-form-input>
            </b-form-group>
            <b-form-group id="confirmPassword"
                          label="确认密码"
                          lable-for="confirmPassword-input">
              <b-form-input id="confirmPassword-input"
                            type="password"
                            v-model="confirmPassword"
                            required
                            placeholder="确认密码"></b-form-input>
            </b-form-group>
            <b-button type="submit" variant="success" block>更改密码</b-button>
          </b-form>
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
  export default {}
</script>

<style>

</style>
