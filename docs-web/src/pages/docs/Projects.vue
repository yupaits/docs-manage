<template>
  <b-container fluid class="my-3">
    <b-row align-h="center">
      <b-col cols="9">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-card-group columns class="mt-3">
          <b-card>
            <h4 class="text-center my-auto"><b-link to="/docs/projects/add" class="text-success"><span class="fa fa-plus"> 创建项目</span></b-link></h4>
          </b-card>
          <b-card v-for="project in projects" @mouseenter="hoverId = project.id" @mouseleave="hoverId = null">
            <h4>
              <b-link active :to="{path: '/docs/projects/' + project.id + '/documents', params: {project: project}}"
                      class="card-link">{{project.name}}
              </b-link>
            </h4>
            <div class="float-right">
              <b-button size="sm" variant="light" :to="'/docs/projects/' + hoverId + '/edit'" v-if="hoverId === project.id">
                <span class="fa fa-pencil"> 编辑</span>
              </b-button>
              <b-button size="sm" variant="light" id="delete-button" @click="showDeleteModal(project.id)" v-if="hoverId === project.id">
                <span class="fa fa-trash"> 删除</span>
              </b-button>
            </div>
            <p><span class="fa fa-clock-o"> {{project.createdAt | dateFormat}}</span></p>
            <p>{{project.description}}</p>
          </b-card>
        </b-card-group>
      </b-col>
    </b-row>
    <b-modal ref="project_delete_modal"
             title="删除项目"
             button-size="sm"
             no-fade
             no-close-on-backdrop
             no-close-on-esc
             cancel-title="取消"
             ok-title="确定"
             @ok="submitDelete"
             @cancel="cancelDelete"
             v-cloak>
      <p class="text-danger"><b>确定删除该项目吗？</b></p>
    </b-modal>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  import constant from '../../utils/constant'
  import dateFns from 'date-fns'
  import zh_cn from 'date-fns/locale/zh_cn'
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        hoverId: null,
        projects: [],
        projectId: null
      }
    },
    created() {
      this.fetchData();
    },
    filters: {
      dateFormat(date) {
        return dateFns.distanceInWordsToNow(date, {addSuffix: true, locale: zh_cn});
      }
    },
    methods: {
      fetchData: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        const instance = this;
        request.Api.get('/projects/owner/' + user.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            const projects = result.data;
            if (projects.length === 0) {
              instance.alert = {variant: 'info', msg: '项目清单为空', show: 5};
            } else {
              instance.projects = result.data;
            }
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取项目清单出错', show: 5};
        });
      },
      showDeleteModal: function (projectId) {
        this.projectId = projectId;
        this.$refs.project_delete_modal.show();
      },
      submitDelete: function () {
        const instance = this;
        request.Api.delete('/projects/' + this.projectId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.fetchData();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '删除文档出错', show: 5};
        });
      },
      cancelDelete: function () {
        this.projectId = null;
      }
    }
  }
</script>

<style>

</style>
