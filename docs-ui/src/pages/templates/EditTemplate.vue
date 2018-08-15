<template>
  <b-container fluid class="mt-3 mb-3">
    <b-row align-h="center">
      <b-col cols="11">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-form @submit="save">
          <b-button-toolbar justify class="mr-auto mb-3">
            <h4 class="mt-3 w-75">基本信息</h4>
            <b-button-group size="sm" class="mb-3">
              <b-button type="submit" variant="outline-success"><span class="fa fa-save"> 保存</span></b-button>
              <b-button variant="outline-secondary" @click="back"><span class="fa fa-times"> 取消</span></b-button>
            </b-button-group>
          </b-button-toolbar>

          <b-row>
            <b-col cols="6">
              <b-input-group left="模板名" class="mb-3">
                <b-form-input type="text"
                              v-model="template.name"
                              required
                              placeholder="输入模板名"></b-form-input>
              </b-input-group>
              <b-button-toolbar justify class="mb-3">
                <b-input-group left="排序码">
                  <b-form-input type="number"
                                v-model="template.sortCode"
                                required
                                placeholder="输入排序码，越小越靠前"></b-form-input>
                </b-input-group>
                <b-input-group left="是否开放">
                  <b-form-select v-model="template.isOpen" required :options="openOptions"></b-form-select>
                </b-input-group>
              </b-button-toolbar>
            </b-col>
            <b-col cols="6">
              <b-input-group left="简介">
                <b-form-textarea v-model="template.description" :rows="4" :max-rows="6"></b-form-textarea>
              </b-input-group>
            </b-col>
          </b-row>
        </b-form>

        <h4>模板内容</h4>
        <b-button-toolbar class="mb-3">
          <b-input-group size="sm" left="分类" class="w-25 mx-1">
            <b-form-input v-model="template.category" required placeholder="选择或输入分类"></b-form-input>
            <b-input-group-button slot="right">
              <b-dropdown text="选择" variant="outline-secondary" right>
                <b-dropdown-item v-for="category in categories" @click="selectCategory(category)">{{category}}</b-dropdown-item>
              </b-dropdown>
            </b-input-group-button>
          </b-input-group>
          <b-input-group size="sm" left="标签" class="w-25 mx-1">
            <b-form-input v-model="tag"
                          :state="tagState"
                          placeholder="不能包含英文逗号，Enter添加"
                          @keyup.enter.native="addTag"></b-form-input>
          </b-input-group>
          <h5 class="mx-1">
            <span v-for="(t, index) in tags">
              <b-badge size="lg" variant="secondary" class="mx-1" @mouseenter="hoverTag = t" @mouseleave="hoverTag = null">
                {{t}} <span class="fa fa-remove" v-if="hoverTag === t" @click="removeTag(index)"></span>
              </b-badge>
            </span>
          </h5>
        </b-button-toolbar>
        <markdown-editor previewClass="markdown-body" v-model="template.content" :configs="configs"></markdown-editor>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  import constant from '../../utils/constant'

  const openOptions = [
    {text: '开放', value: true},
    {text: '不开放', value: false}
  ];
  const configs = {
    showIcons: ['code', 'table']
  };
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        template: {},
        openOptions: openOptions,
        tags: [],
        tag: null,
        hoverTag: null,
        categories: [],
        configs: configs
      }
    },
    created() {
      this.fetchData();
    },
    computed: {
      tagState() {
        return this.tag !== null && this.tag.indexOf(',') >= 0 ? 'invalid' : null;
      }
    },
    methods: {
      fetchData: function () {
        this.fetchCategories();
        this.fetchTemplate();
      },
      fetchTemplate: function () {
        const templateId = parseInt(this.$route.params.id);
        const instance = this;
        request.Api.get('/templates/' + templateId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.template = result.data;
            instance.tags = instance.template.tags.split(',');
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取模板信息出错', show: 5};
        });
      },
      fetchCategories: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        const instance = this;
        request.Api.get('/templates/categories/owner/' + user.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.categories = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取模板备选分类列表出错', show: 5};
        });
      },
      back: function () {
        this.$router.push('/templates');
      },
      selectCategory: function (category) {
        this.template.category = category;
      },
      addTag: function () {
        if (this.tag !== '' && this.tag !== null && this.tag.indexOf(',') < 0) {
          this.tags.push(this.tag);
          this.tag = null;
        }
      },
      removeTag: function (index) {
        this.tags.splice(index, 1);
      },
      save: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        this.template.ownerId = user.id;
        this.template.tags = this.tags.join(',');
        const category = this.template.category;
        this.template.category = (category === null || category === '') ? '未分类' : category;
        const instance = this;
        request.Api.put('/templates/' + this.template.id, this.template).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.back();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '创建文档模板出错', show: 5};
        });
      }
    }
  };
</script>

<style>
  @import '~simplemde/dist/simplemde.min.css';
  @import '~github-markdown-css';

  .markdown-editor .CodeMirror {
    height: 28rem;
  }
</style>
