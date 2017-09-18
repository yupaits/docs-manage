<template>
  <b-container fluid class="mt-3">
    <b-row align-h="center">
      <b-col cols="6">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
        <b-button variant="success" to="/docs/projects/add">创建项目</b-button>
        <div v-for="project in projects">
          <hr>
          <h4>
            <b-link active :to="{path: '/docs/projects/' + project.id + '/documents', params: {project: project}}" class="card-link">{{project.name}}
            </b-link>
          </h4>
          <p>{{project.description}}</p>
          <p><span class="fa fa-clock-o">{{project.createAt}}</span></p>
        </div>
        <hr>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import request from '../../utils/request'
  import constant from '../../utils/constant'
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        projects: []
      }
    },
    created() {
      this.fetchData();
    },
    methods: {
      fetchData: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        const instance = this;
        request.Api.get('/projects/owner/' + user.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            var projects = result.data;
            if (projects.length === 0) {
              instance.alert = {variant: 'info', msg: '项目清单为空', show: 5};
            } else {
              instance.projects = result.data;
            }
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取项目清单出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
