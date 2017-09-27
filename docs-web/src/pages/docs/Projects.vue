<template>
  <b-container fluid class="mt-3">
    <b-row align-h="center">
      <b-col cols="6">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-button variant="outline-success" to="/docs/projects/add">创建项目</b-button>
        <div v-for="project in projects" @mouseenter="hoverId = project.id" @mouseleave="hoverId = null">
          <hr>
          <b-button-toolbar justify>
            <h3>
              <b-link active :to="{path: '/docs/projects/' + project.id + '/documents', params: {project: project}}"
                      class="card-link">{{project.name}}
              </b-link>
            </h3>
            <b-button-group v-if="hoverId === project.id">
              <b-button variant="light" :to="'/docs/projects/' + hoverId + '/edit'"><span
                class="fa fa-pencil"> 编辑</span></b-button>
              <b-dropdown text="删除" variant="light" right>
                <b-dropdown-header class="text-danger"><h6 class="text-bold"><b>确定删除吗?</b></h6></b-dropdown-header>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item-button @click="submitDelete"><span class="fa fa-check"> 确定</span>
                </b-dropdown-item-button>
                <b-dropdown-item-button @click="cancelDelete"><span class="fa fa-times"> 取消</span>
                </b-dropdown-item-button>
              </b-dropdown>
            </b-button-group>
          </b-button-toolbar>
          <p>{{project.description}}</p>
          <p><span class="fa fa-clock-o"> {{project.createdAt | dateFormat}}</span></p>
        </div>
        <hr>
      </b-col>
    </b-row>
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
        projects: []
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
      submitDelete: function () {
        const instance = this;
        request.Api.delete('/projects/' + this.hoverId).then(function (result) {
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
        this.alert = {variant: 'success', msg: '多谢兄台放我一马，日后相见，必有重谢！', show: 5};
      }
    }
  }
</script>

<style>

</style>
