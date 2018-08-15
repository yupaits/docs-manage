<template>
  <li>
    <div :id="id" :class="{bold: isFolder}" @mouseenter="hover = true" @mouseleave="hover = false">
      <span @click="toggle"><span v-if="isFolder"
                                  :class="open ? 'fa fa-folder-open' : 'fa fa-folder'"> {{model.name}}</span></span>
      <b-badge variant="light" class="text-primary" v-if="hover" @click="editDirectory(model.id)"><span
        class="fa fa-pencil"> 编辑</span></b-badge>
      <b-badge variant="light" class="text-danger" v-if="hover" @click="deleteDirectory(model.id)"><span
        class="fa fa-remove"> 删除</span></b-badge>
    </div>
    <ul v-show="open" v-if="isFolder">
      <tree-item v-for="(directory, index) in model.subDirectories" :model="directory"
                 :id="id + '-' + index" :projectId="projectId" :activeDocumentId="activeDocumentId"></tree-item>
      <div v-for="document in model.documents" @click="showDoc(document.id)"
           :class="{'document-active': document.id === activeDocumentId}">&nbsp;<span
        class="fa fa-file-text"> {{document.name}}</span></div>
      <div class="fa fa-plus" @click="addChild(model.id)"> 新建</div>
    </ul>

    <b-modal ref="tree_add_modal"
             title="新建目录或文档"
             button-size="sm"
             no-fade
             no-close-on-backdrop
             no-close-on-esc
             cancel-title="取消"
             ok-title="提交"
             @ok="submitAdd"
             @cancel="cancelAdd"
             v-cloak>
      <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
        <b>{{alert.msg}}</b>
      </b-alert>

      <b-form>
        <b-form-group id="add-modal-type"
                      label="类型"
                      label-for="add-modal-type-radio">
          <b-form-radio-group id="add-modal-type-radio"
                        v-model="type"
                        :options="typeOptions"></b-form-radio-group>
        </b-form-group>
        <b-form-group id="add-modal-name"
                      label="名称"
                      lable-for="add-modal-name-input">
          <b-form-input id="add-modal-name-input"
                        type="text"
                        v-model="item.name"
                        required
                        placeholder="输入名称"></b-form-input>
        </b-form-group>
        <b-form-group id="add-modal-sort-code"
                      label="排序码"
                      lable-for="add-modal-sort-code-input"
                      description="越小越靠前">
          <b-form-input id="add-modal-sort-code-input"
                        type="number"
                        v-model="item.sortCode"
                        required
                        :state="addSortCodeState"
                        placeholder="输入排序码"></b-form-input>
          <b-form-feedback>排序码必须大于0</b-form-feedback>
        </b-form-group>
        <b-form-group id="visit-code"
                      label="访问码"
                      lable-for="visit-code-input"
                      description="分享的文档需要输入访问码才能阅读"
                      v-if="type === 1">
          <b-form-input id="visit-code-input"
                        type="password"
                        v-model="item.visitCode"
                        placeholder="输入访问码，输入为空时默认设置为文档名"></b-form-input>
          <b-form-feedback>访问码不少于6位</b-form-feedback>
        </b-form-group>
      </b-form>
    </b-modal>

    <b-modal ref="directory_edit_modal"
             title="编辑目录"
             button-size="sm"
             no-fade
             no-close-on-backdrop
             no-close-on-esc
             cancel-title="取消"
             ok-title="提交"
             @ok="submitEdit"
             @cancel="cancelEdit"
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
                        v-model="directory.name"
                        required
                        placeholder="输入名称"></b-form-input>
        </b-form-group>
        <b-form-group id="add-modal-sort-code"
                      label="排序码"
                      lable-for="add-modal-sort-code-input"
                      description="越小越靠前">
          <b-form-input id="add-modal-sort-code-input"
                        type="number"
                        v-model="directory.sortCode"
                        required
                        :state="editSortCodeState"
                        placeholder="输入排序码"></b-form-input>
          <b-form-feedback>排序码必须大于0</b-form-feedback>
        </b-form-group>
      </b-form>
    </b-modal>

    <b-modal ref="directory_delete_modal"
             title="删除目录"
             button-size="sm"
             no-fade
             no-close-on-backdrop
             no-close-on-esc
             cancel-title="取消"
             ok-title="确定"
             @ok="submitDelete"
             @cancel="cancelDelete"
             v-cloak>
      <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
        <b>{{alert.msg}}</b>
      </b-alert>
      <p class="text-danger"><b>确定删除该目录吗？</b></p>
    </b-modal>
  </li>
</template>

<script>
  import request from '../../utils/request'
  import constant from '../../utils/constant'

  const typeOptions = [{text: '目录', value: 0}, {text: '文档', value: 1}];
  const defaultItem = {id: null, name: '', sortCode: null};
  export default {
    name: 'treeItem',
    props: {
      model: Object,
      projectId: Number,
      id: String,
      activeDocumentId: Number
    },
    data: function () {
      return {
        hover: false,
        open: false,
        alert: {variant: 'info', msg: '', show: null},
        typeOptions: typeOptions,
        type: null,
        item: Object.assign({}, defaultItem),
        directory: Object.assign({}, defaultItem),
        deleteId: null
      }
    },
    computed: {
      isFolder() {
        return this.model.subDirectories && this.model.subDirectories.length >= 0;
      },
      addSortCodeState() {
        return this.item.sortCode > 0 ? null : 'invalid';
      },
      editSortCodeState() {
        return this.directory.sortCode > 0 ? null : 'invalid';
      }
    },
    methods: {
      fetchDirectory: function (directoryId) {
        const instance = this;
        request.Api.get('/directories/' + directoryId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.directory = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取目录详情出错', show: 5};
        });
      },
      toggle: function () {
        if (this.isFolder) {
          this.open = !this.open;
        }
      },
      showDoc: function (documentId) {
        this.$root.eventHub.$emit('selectDocument', documentId);
      },
      showAdd: function () {
        this.$refs.tree_add_modal.show();
      },
      showEdit: function () {
        this.$refs.directory_edit_modal.show();
      },
      showDelete: function () {
        this.$refs.directory_delete_modal.show();
      },
      hideAdd: function () {
        this.$refs.tree_add_modal.hide();
      },
      hideDelete: function () {
        this.$refs.directory_delete_modal.hide();
      },
      hideEdit: function () {
        this.$refs.directory_edit_modal.hide();
      },
      addChild: function () {
        this.alert = {variant: 'info', msg: '', show: null};
        this.type = null;
        this.item = Object.assign({}, defaultItem);
        this.showAdd();
      },
      editDirectory: function (directoryId) {
        this.alert = {variant: 'info', msg: '', show: null};
        this.fetchDirectory(directoryId);
        this.showEdit();
      },
      deleteDirectory: function (directoryId) {
        this.alert = {variant: 'info', msg: '', show: null};
        this.deleteId = directoryId;
        this.showDelete();
      },
      submitAdd: function (event) {
        event.cancel();
        const instance = this;
        const user = JSON.parse(this.$cookies.get(constant.user));
        if (this.type === 0) {
          const directory = {
            ownerId: user.id,
            projectId: this.projectId,
            parentId: this.model.id,
            name: this.item.name,
            sortCode: this.item.sortCode
          };
          request.Api.post('/directories', directory).then(function (result) {
            if (result.code !== 200) {
              instance.alert = {variant: 'warning', msg: result.msg, show: 5};
            } else {
              instance.$root.eventHub.$emit('updateTree');
              instance.hideAdd();
            }
          }).catch(function (error) {
            instance.alert = {variant: 'danger', msg: '新建目录出错', show: 5};
          });
        } else if (this.type === 1) {
          const document = {
            ownerId: user.id,
            directoryId: this.model.id,
            name: this.item.name,
            content: '',
            sortCode: this.item.sortCode,
            visitCode: this.item.visitCode
          };
          request.Api.post('/documents', document).then(function (result) {
            if (result.code !== 200) {
              instance.alert = {variant: 'warning', msg: result.msg, show: 5};
            } else {
              instance.$root.eventHub.$emit('updateTree');
              instance.hideAdd();
            }
          }).catch(function (eroor) {
            instance.alert = {variant: 'danger', msg: '新建文档出错', show: 5};
          });
        } else {
          instance.alert = {variant: 'warning', msg: '请选择一种类型', show: 5};
        }
      },
      cancelAdd: function () {
        this.hideAdd();
      },
      submitEdit: function (event) {
        event.cancel();
        const instance = this;
        request.Api.put('/directories/' + this.directory.id, this.directory).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.$root.eventHub.$emit('updateTree');
            instance.hideEdit();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '编辑目录出错', show: 5};
        });
      },
      cancelEdit: function () {
        this.hideEdit();
      },
      submitDelete: function (event) {
        event.cancel();
        const instance = this;
        request.Api.delete('/directories/' + this.deleteId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.$root.eventHub.$emit('updateTree');
            instance.hideDelete();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '删除目录出错', show: 5};
        });
      },
      cancelDelete: function () {
        this.hideDelete();
      }
    }
  }
</script>

<style>
  .document-active {
    background-color: darkslategray;
    color: whitesmoke;
  }
</style>
