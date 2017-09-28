<template>
  <b-modal id="shareModal"
           ref="share_modal"
           title="分享文档"
           button-size="sm"
           no-fade
           no-close-on-backdrop
           no-close-on-esc
           ok-only
           v-cloak>
    <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
      <b>{{alert.msg}}</b>
    </b-alert>
    <b-input-group size="sm" left="文档链接" class="mb-3">
      <b-form-input :value="shareLink" readonly disabled></b-form-input>
      <b-input-group-button>
        <b-button variant="outline-primary"
                  v-clipboard="shareLink"
                  @success="onCopy"
                  @error="onError"><span class="fa fa-clipboard"> 复制</span></b-button>
      </b-input-group-button>
    </b-input-group>
    <div class="text-center">
      <qrcode-vue :value="shareLink" size="200" level="M"></qrcode-vue>
      <p class="text-muted">二维码分享</p>
    </div>
  </b-modal>
</template>

<script>
  import Vue from 'vue'
  import VueClipboards from 'vue-clipboards'
  import QrcodeVue from 'qrcode.vue'

  Vue.use(VueClipboards);
  const baseUrl = window.location.origin + window.location.pathname + '#/docs/read/documents/';
  export default {
    components: {
      QrcodeVue
    },
    props: {
      documentId: Number
    },
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        shareLink: ''
      }
    },
    created() {
      this.shareLink = baseUrl + this.documentId;
    },
    watch: {
      documentId: function () {
        this.shareLink = baseUrl + this.documentId;
      }
    },
    methods: {
      onCopy: function (e) {
        this.alert = {variant: 'success', msg: '复制链接成功！', show: 1};
      },
      onError: function (e) {
        this.alert = {variant: 'danger', msg: '复制链接出错！', show: 1};
      }
    }
  }
</script>

<style>

</style>
