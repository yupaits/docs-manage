<template>
  <b-container>
    <b-row align-h="center">
      <b-col  lg="5" md="7" sm="9">
        <h1 class="lead mt-5"><span class="fa fa-info-circle"> 用户反馈</span></h1>
        <b-card class="mt-2">
          <b-form @submit="">
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
  export default {}
</script>

<style>

</style>
