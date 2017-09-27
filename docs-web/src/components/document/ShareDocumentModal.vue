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
    <b-input-group size="sm" left="文档链接">
      <b-form-input :value="shareLink" readonly disabled></b-form-input>
      <b-input-group-button>
        <b-button variant="outline-primary">Copy!</b-button>
      </b-input-group-button>
    </b-input-group>
    <vue-qr :text="shareLink"
            height="256"
            width="256"
            class="text-center"/>
  </b-modal>
</template>

<script>
  import VueQr from 'vue-qr'

  const baseUrl = window.location.origin + window.location.pathname + '#/docs/read/documents/';
  export default {
    components: {
      VueQr
    },
    props: {
      documentId: Number
    },
    data() {
      return {
        shareLink: ''
      }
    },
    created() {
      this.shareLink = baseUrl + this.documentId;
    },
    methods: {

    }
  }
</script>

<style>

</style>
