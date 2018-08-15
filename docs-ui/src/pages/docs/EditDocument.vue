<template>
  <b-container class="mt-3">
    <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
      <b>{{alert.msg}}</b>
    </b-alert>
    <b-button-toolbar justify class="mr-auto mb-3">
      <b-input-group left="标题" class="w-75">
        <b-form-input v-model="document.name"
                      required
                      placeholder="请输入文档标题"></b-form-input>
      </b-input-group>
      <b-button-group>
        <b-button variant="outline-success" @click="save"><span class="fa fa-save"> 保存</span></b-button>
        <b-button variant="outline-secondary" @click="back"><span class="fa fa-times"> 取消</span></b-button>
      </b-button-group>
    </b-button-toolbar>
    <b-button-toolbar justify class="mb-3">
      <b-input-group size="sm" left="模板分类">
        <b-form-select v-model="selectedCate" :options="cateOptions" @input="selectCate"
                       class="select-width"></b-form-select>
        <b-input-group-addon v-if="selectedCate">选择模板</b-input-group-addon>
        <b-form-select v-model="selectedTemplate" :options="templateOptions" class="select-width"
                       v-if="selectedCate"></b-form-select>
        <b-input-group-button v-if="selectedTemplate !== null">
          <b-button variant="outline-primary" @click="insertTemplate"><span
            class="fa fa-plus-circle"> 插入模板</span>
          </b-button>
        </b-input-group-button>
      </b-input-group>
      <b-input-group size="sm">
        <b-input-group-addon @click="changeable = !changeable">{{changeable ? '更改' : '保持之前'}}访问码</b-input-group-addon>
        <b-form-input type="password" v-model="visitCode" :disabled="!changeable"></b-form-input>
      </b-input-group>
    </b-button-toolbar>
    <markdown-editor previewClass="markdown-body" v-model="document.content" ref="editor"
                     :configs="configs"></markdown-editor>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  import constant from '../../utils/constant'

  const configs = {
    showIcons: ['code', 'table']
  };
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        configs: configs,
        document: {id: null, name: '', content: '', sortCode: null},
        visitCode: null,
        changeable: false,
        categories: [],
        selectedCate: null,
        templates: [],
        selectedTemplate: null
      }
    },
    created() {
      this.fetchData();
    },
    watch: {
      changeable: function (val, oldVal) {
        if (!val) {
          this.visitCode = null;
        }
      }
    },
    computed: {
      cateOptions() {
        const cateOptions = [];
        cateOptions.push({text: '未选择', value: null});
        this.categories.forEach(function (category) {
          cateOptions.push({text: category, value: category});
        });
        return cateOptions;
      },
      templateOptions() {
        const templateOptions = [];
        templateOptions.push({text: '未选择', value: null});
        this.templates.forEach(function (template) {
          templateOptions.push({text: template.name, value: template});
        });
        return templateOptions;
      }
    },
    methods: {
      fetchData: function () {
        this.fetchCategories();
        this.fetchDocument();
      },
      fetchDocument: function () {
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
          instance.alert = {variant: 'danger', msg: '获取模板分类列表出错', show: 5};
        });
      },
      fetchTemplates: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        const instance = this;
        request.Api.get('/templates/owner/' + user.id + '/category/' + this.selectedCate).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.templates = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取模板列表出错', show: 5};
        });
      },
      selectCate: function () {
        this.templates = [];
        this.selectedTemplate = null;
        this.fetchTemplates();
      },
      insertTemplate: function () {
        this.document.content += this.selectedTemplate.content;
      },
      save: function () {
        const instance = this;
        if (this.changeable) {
          this.document.visitCode = this.visitCode;
        }
        request.Api.put('/documents/' + this.document.id, this.document).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.back();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '保存文档出错', show: 5};
        });
      },
      back: function () {
        this.$router.push({
          path: '/docs/projects/' + this.$route.params.id + '/documents',
          query: {documentId: this.document.id}
        });
      }
    }
  }
</script>

<style>
  @import '~simplemde/dist/simplemde.min.css';
  @import '~github-markdown-css';

  .select-width {
    min-width: 8rem;
    max-width: 16rem;
  }

  .markdown-editor .CodeMirror {
    height: 36rem;
  }
</style>
