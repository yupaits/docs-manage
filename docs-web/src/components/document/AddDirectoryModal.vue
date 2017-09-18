<template>
  <b-modal id="addModal"
           ref="add_modal"
           title="新建目录"
           button-size="sm"
           no-close-on-backdrop
           no-close-on-esc
           close-title="取消"
           ok-title="提交"
           @shown="showModal"
           @ok="submitAdd"
           @cancel="hideModal"
           v-cloak>
    <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
      <b>{{alert.msg}}</b>
    </b-alert>
    <b-form>
      <b-form-group id="add-modal-name"
                    label="名称"
                    lable-for="add-modal-name-input">
        <b-form-input id="add-modal-name-input"
                      type="text"
                      v-model="name"
                      required
                      placeholder="输入名称"></b-form-input>
      </b-form-group>
      <b-form-group id="add-modal-sort-code"
                    label="排序码"
                    lable-for="add-modal-sort-code-input"
                    description="越小越靠前">
        <b-form-input id="add-modal-sort-code-input"
                      type="number"
                      v-model="sortCode"
                      required
                      :state="sortCodeState"
                      placeholder="输入排序码"></b-form-input>
        <b-form-feedback>排序吗必须大于0</b-form-feedback>
      </b-form-group>
    </b-form>
  </b-modal>
</template>

<script>
  import constant from '../../utils/constant'
  export default {
    props: {
      projectId: Number
    },
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        name: '',
        sortCode: null
      }
    },
    created() {
      this.name = '';
      this.sortCode = null;
    },
    computed: {
      sortCodeState() {
        return this.sortCode > 0 ? null : 'invalid';
      }
    },
    methods: {
      showModal: function () {
        this.name = '';
        this.sortCode = null;
        this.$refs.add_modal.show();
      },
      hideModal: function () {
        this.$refs.add_modal.hide();
      },
      submitAdd: function (event) {
        event.cancel();
        const instance = this;
        const user = JSON.parse(this.$cookies.get(constant.user));
        var directory = {
          ownerId: user.id,
          projectId: this.projectId,
          parentId: 0,
          name: this.name,
          sortCode: this.sortCode
        };
        Api.post('/directories', directory).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.$root.eventHub.$emit('updateTree');
            instance.hideModal();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '新建目录出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
