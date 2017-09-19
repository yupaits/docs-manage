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
                         :id="'directory-' + index" :projectId="selectedProject.id"
                         :activeDocumentId="selectedDocument.id"></tree-item>
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
          <b-button-toolbar justify v-show="showEdit">
            <h1><span class="fa fa-file-text-o"> {{selectedDocument.name}}</span></h1>
            <b-button-group class="mb-3">
              <b-button variant="outline-primary"
                        :to="'/docs/projects/' + selectedProject.id + '/documents/' + selectedDocument.id + '/edit'">
                <span class="fa fa-pencil"> 编辑</span></b-button>
              <b-dropdown text="历史" variant="outline-secondary" right>
                <b-dropdown-item-button @click="showHistory(selectedDocument.content)"><span class="fa fa-eye"> 显示当前文档</span>
                </b-dropdown-item-button>
                <b-dropdown-header v-show="documentHistories.length > 0">文档记录</b-dropdown-header>
                <span v-for="history in documentHistories">
                  <b-dropdown-item-button @click="showHistory(history.content)">
                    <span class="fa fa-history"> {{history.savedTime | timeFormat}}</span>
                  </b-dropdown-item-button>
                </span>
              </b-dropdown>
              <b-dropdown text="删除" variant="outline-danger" right>
                <b-dropdown-header class="text-danger"><h6 class="text-bold"><b>确定删除吗?</b></h6></b-dropdown-header>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item-button @click="submitDelete"><span class="fa fa-check"> 确定</span>
                </b-dropdown-item-button>
                <b-dropdown-item-button @click="cancelDelete"><span class="fa fa-times"> 取消</span>
                </b-dropdown-item-button>
              </b-dropdown>
            </b-button-group>
          </b-button-toolbar>
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
  import marked from 'marked'
  import dateFns from 'date-fns'
  import request from '../../utils/request'

  const defaultDocument = {id: null, name: '', content: '', sortCode: null};
  export default {
    components: {
      TreeItem,
      AddDirectoryModal,
    },
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        selectedProject: {},
        breadcrumbItems: [],
        directoryTree: [],
        selectedDocument: Object.assign({}, defaultDocument),
        documentHistories: [],
        documentContent: null,
        showEdit: false
      }
    },
    created() {
      this.selectedProject.id = parseInt(this.$route.params.id);
      this.fetchData();
      const documentId = this.$route.query.documentId;
      if (documentId) {
        this.selectDocument(documentId);
      }
      this.$root.eventHub.$on('updateTree', () => this.fetchDirectoryTree());
      this.$root.eventHub.$on('selectDocument', documentId => this.selectDocument(documentId));
    },
    filters: {
      timeFormat(date) {
        return dateFns.format(date, 'YYYY-MM-DD HH:mm:ss');
      }
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
      fetchDocumentHistory: function (documentId) {
        const instance = this;
        request.Api.get('/documentHistories/documents/' + documentId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.documentHistories = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取文档出错', show: 5};
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
            instance.fetchDocumentHistory(documentId);
            instance.showEdit = true;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取文档出错', show: 5};
        });
      },
      submitDelete: function () {
        const instance = this;
        request.Api.delete('/documents/' + this.selectedDocument.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.selectedDocument = Object.assign({}, defaultDocument);
            instance.documentContent = null;
            instance.showEdit = false;
            instance.fetchDirectoryTree();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '删除文档出错', show: 5};
        });
      },
      cancelDelete: function () {
        this.alert = {variant: 'success', msg: '多谢兄台放我一马，日后相见，必有重谢！', show: 5};
      },
      showHistory: function (historyContent) {
        this.documentContent = marked(historyContent);
      }
    }
  }
</script>

<style>
  @import '~simplemde/dist/simplemde.min.css';
  @import '~github-markdown-css';
</style>
