<template>
  <b-container fluid class="mt-3">
    <b-row align-h="center">
      <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
        <b>{{alert.msg}}</b>
      </b-alert>
      <b-col cols="3">
        <b-button variant="light" to="/docs"><span class="fa fa-arrow-left"> 返回</span></b-button>
        <b-card class="mt-3">
          <h4 slot="header">文档目录</h4>
          <ul>
            <tree-item v-for="(directory, index) in directoryTree" :model="directory"
                       :id="'directory-' + index"></tree-item>
            <div class="fa fa-plus" @click="addChild"> 新建</div>
          </ul>
        </b-card>
      </b-col>

      <b-col cols="8">
        <b-breadcrumb>
          <b-breadcrumb-item
            :text="'<b>' + selectedProject.name + '</b>' + (selectedDocument.name != '' ? ' --> ' : '') + selectedDocument.name"
            active/>
        </b-breadcrumb>
        <div v-show="selectedDocument.id != null">
          <b-card no-body class="mb-5">
            <b-tabs card no-fade v-model="tabIndex">
              <b-tab title="文档">
                <div v-html="marked(selectedDocument.content)" class="markdown-body"></div>
              </b-tab>
              <b-tab title="编辑">
                <textarea ref="editor"></textarea>
              </b-tab>
            </b-tabs>
          </b-card>
        </div>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  export default {
    data() {
      return {
        alert: {},
        breadcrumbItems: [],
        directoryTree: [],
        selectedDocument: {},
        documentContent: null,
        editor: undefined,
        tabIndex: 0
      }
    },
    methods: {

    }
  }
</script>

<style>

</style>
