<template>
  <b-container class="my-3">
    <div v-if="visitCodePass">
      <h1 class="text-center mt-3">{{document.name}}</h1>
      <p class="text-center text-muted">
        <span class="fa fa-user-o"> {{document.author.username}}</span>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <span class="fa fa-envelope-o"> {{document.author.email}}</span>
      </p>
      <div v-html="documentContent" class="markdown-body my-5"></div>
    </div>
    <b-row align-h="center" class="mt-5" v-else>
      <b-col cols="4">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-form @submit="visit">
          <b-form-group label="请输入文档访问码" label-for="visit-code-input">
            <b-input-group>
              <b-form-input id="visit-code-input"
                            type="password"
                            required
                            v-model="visitCode"></b-form-input>
              <b-input-group-button>
                <b-button type="submit" variant="outline-secondary">提交</b-button>
              </b-input-group-button>
            </b-input-group>
          </b-form-group>
        </b-form>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  import marked from 'marked'
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        document: {name: '', content: '', author: {}},
        visitCode: '',
        visitCodePass: false
      }
    },
    created() {
      this.visitCodePass = false;
    },
    computed: {
      documentContent() {
        return marked(this.document.content);
      }
    },
    methods: {
      visit: function () {
        const documentId = this.$route.params.id;
        const instance = this;
        request.PublicApi.get('/documents/' + documentId + '/read?visitCode=' + this.visitCode).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.alert.show = null;
            instance.document = result.data;
            instance.visitCodePass = true;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取文档出错', show: 5};
        });
      }
    }
  }
</script>

<style>
  @import '~github-markdown-css';
</style>
