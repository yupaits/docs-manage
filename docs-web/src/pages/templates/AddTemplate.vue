<template>
  <b-container class="mt-3">
    <b-row align-h="center">
      <b-col cols="6">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-button variant="light" @click="back"><span class="fa fa-arrow-left"> 返回</span></b-button>
        <b-card class="mt-3">
          <b-form @submit="addTemplate">
            <b-form-group id="template-sort-code"
                          label="模板名"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入模板名"></b-form-input>
            </b-form-group>
            <b-form-group id="template-sort-code"
                          label="模板简介"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入模板简介"></b-form-input>
            </b-form-group>
            <b-form-group id="template-sort-code"
                          label="模板内容"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入模板内容"></b-form-input>
            </b-form-group>
            <b-form-group id="template-sort-code"
                          label="模板分类"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入模板分类"></b-form-input>
            </b-form-group>
            <b-form-group id="template-sort-code"
                          label="模板标签"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入模板标签"></b-form-input>
            </b-form-group>
            <b-form-group id="template-sort-code"
                          label="是否开放"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入排序码"></b-form-input>
            </b-form-group>
            <b-form-group id="template-sort-code"
                          label="排序码"
                          lable-for="template-sort-code-input"
                          description="越小越靠前">
              <b-form-input id="template-sort-code-input"
                            type="number"
                            v-model="template.sortCode"
                            required
                            :state="sortCodeState"
                            placeholder="输入排序码"></b-form-input>
            </b-form-group>
            <b-button type="submit" variant="success">提交</b-button>
            <b-button type="reset" variant="secondary">重置</b-button>
          </b-form>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  import constant from '../../utils/constant'

  const defaultTemplate = {ownerId: null, name: '', description: '', content: '', category: null, tags: null, isOpen: false, sortCode: null};
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        template: Object.assign({}, defaultTemplate)
      }
    },
    created() {

    },
    computed: {
      sortCodeState() {
        return this.template.sortCode > 0 ? null : 'invalid';
      }
    },
    methods: {
      back: function () {
        this.$router.go(-1);
      },
      addTemplate: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        this.project.ownerId = user.id;
        const instance = this;
        request.Api.post('/templates', this.template).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.$router.go(-1);
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '创建项目出错', show: 5};
        });
      }
    }
  };
</script>

<style>

</style>
