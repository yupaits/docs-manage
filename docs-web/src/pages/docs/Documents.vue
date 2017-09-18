<template>
  <div>
    <b-container fluid class="mt-3">
      <b-row align-h="center">
        <b-col cols="11">
          <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
            <b>{{alert.msg}}</b>
          </b-alert>
        </b-col>

        <b-col cols="3">
          <b-button variant="light" to="/docs"><span class="fa fa-arrow-left"> 返回</span></b-button>
          <b-card class="mt-3">
            <h4 slot="header">文档目录</h4>
            <ul>
              <tree-item v-for="(directory, index) in directoryTree" :model="directory"
                         :id="'directory-' + index" :projectId="selectedProject.id"></tree-item>
              <div class="fa fa-plus" v-b-modal="'addModal'"> 新建</div>
            </ul>
          </b-card>
        </b-col>

        <b-col cols="8">
          <b-breadcrumb>
            <b-breadcrumb-item
              :text="'<b>' + selectedProject.name + '</b>' + (selectedDocument.name != '' ? ' --> ' : '') + selectedDocument.name"
              active/>
          </b-breadcrumb>
          <b-button variant="outline-primary" size="sm" class="mb-3" :to="'/docs/projects/' + selectedProject.id + '/documents/' + selectedDocument.id + '/edit'" v-show="showEdit">编辑</b-button>
          <div v-html="documentContent" class="markdown-body"></div>
        </b-col>
      </b-row>
    </b-container>
    <add-directory-modal :projectId="selectedProject.id"></add-directory-modal>
  </div>
</template>

<script>
  import TreeItem from '../../components/document/TreeItem'
  import AddDirectoryModal from '../../components/document/AddDirectoryModal'
  import EditDirectoryModal from '../../components/document/EditDirectoryModal'
  import DeleteDirectoryModal from '../../components/document/DeleteDirectoryModal'
  import marked from 'marked'
  import request from '../../utils/request'

  export default {
    components: {
      TreeItem,
      AddDirectoryModal,
      EditDirectoryModal,
      DeleteDirectoryModal
    },
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        selectedProject: {},
        breadcrumbItems: [],
        directoryTree: [],
        selectedDocument: {id: null, name: '', content: '', sortCode: null},
        documentContent: null,
        showEdit: false
      }
    },
    created() {
      this.selectedProject.id = parseInt(this.$route.params.id);
      this.fetchData();
      this.$root.eventHub.$on('updateTree', () => this.fetchDirectoryTree());
      this.$root.eventHub.$on('selectDocument', documentId => this.selectDocument(documentId));
    },
    methods: {
      fetchData: function () {
        this.fetchProject();
        this.fetchDirectoryTree();
      },
      fetchProject: function () {
        const instance = this;
        request.Api.get('/projects/' + this.selectedProject.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.selectedProject = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取项目信息出错', show: 5};
        });
      },
      fetchDirectoryTree: function () {
        const instance = this;
        request.Api.get('/directories/projects/' + this.selectedProject.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.directoryTree = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取文档目录出错', show: 5};
        });
      },
      selectDocument: function (documentId) {
        const instance = this;
        request.Api.get('/documents/' + documentId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.selectedDocument = result.data;
            instance.documentContent = marked(result.data.content);
            instance.showEdit = true;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取文档出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
