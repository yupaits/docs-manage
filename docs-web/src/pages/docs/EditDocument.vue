<template>
  <b-container class="mt-3">
    <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
      <b>{{alert.msg}}</b>
    </b-alert>

    <b-button-group size="sm" class="mb-3" >
      <b-button variant="outline-success" @click="save"><span class="fa fa-save"> 保存</span></b-button>
      <b-button variant="outline-secondary" @click="back"><span class="fa fa-times"> 取消</span></b-button>
    </b-button-group>
    <markdown-editor v-model="document.content" ref="editor"></markdown-editor>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        document: {id: null, name: '', content: '', sortCode: null}
      }
    },
    created() {
      this.fetchData();
    },
    mounted() {
      const simplemde = this.simplemde;
      setTimeout(function () {
        simplemde.codemirror.refresh();
      }.bind(simplemde), 0);

    },
    computed: {
      simplemde() {
        return this.$refs.editor.simplemde;
      }
    },
    methods: {
      fetchData: function () {
        const instance = this;
        const documentId = this.$route.params.docId;
        request.Api.get('/documents/' + documentId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.document = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取文档出错', show: 5};
        });
      },
      save: function () {

      },
      back: function () {
        this.$router.push('/docs/projects/' + this.$route.params.id + '/documents', );
      }
    }
  }
</script>

<style>

</style>
