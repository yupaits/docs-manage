<template>
  <b-container>
    <b-row class="mt-3">
      <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
        <b>{{alert.msg}}</b>
      </b-alert>
      <b-col cols="2">
        <b-button variant="outline-success" to="/templates/add">添加模板</b-button>
        <div class="mt-3" v-if="categories.length > 0">
          <p>模板分类</p>
          <b-button size="sm"
                    variant="outline-secondary"
                    block
                    @click="selectCate('')"
                    :pressed="selectedCate === ''">查看全部</b-button>
          <b-button v-for="category in categories"
                    size="sm"
                    variant="outline-secondary"
                    block
                    @click="selectCate(category)"
                    :pressed="selectedCate === category">{{category}}</b-button>
        </div>
        <div class="mt-3">
          <p>热门标签</p>

        </div>
      </b-col>
      <b-col cols="10">
        <b-card-group columns class="mt-5">
          <b-card v-for="template in templatePage.content"
                  :title="template.name"
                  @mouseenter="hoverId = template.id"
                  @mouseleave="hoverId = null">
            <p class="text-muted">创建于: {{template.createdAt | timeFormat}}</p>
            <p class="text-muted" v-if="template.updateAt">修改于: {{template.updateAt | timeFormat}}</p>
            <p class="card-text">
              {{template.description}}
            </p>
          </b-card>
        </b-card-group>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
  import dateFns from 'date-fns'
  import zh_cn from 'date-fns/locale/zh_cn'
  import request from '../../utils/request'
  import constant from '../../utils/constant'

  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        hoverId: null,
        templatePage: [],
        categories: [],
        selectedCate: '',
        selectedTag: '',
        keyword: ''
      }
    },
    created() {
      this.fetchData();
    },
    filters: {
      dateFormat(date) {
        return dateFns.distanceInWordsToNow(date, {addSuffix: true, locale: zh_cn});
      },
      timeFormat(date) {
        return dateFns.format(date, 'YYYY-MM-DD HH:mm:ss');
      }
    },
    methods: {
      fetchData: function () {
        this.fetchTemplatePage();
        this.fetchCategories();
      },
      fetchTemplatePage: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        const instance = this;
        const fetchTemplatePageUrl = '/templates/owner/' + user.id + '?category=' + this.selectedCate + '&tag=' + this.selectedTag + '&keyword=' + this.keyword;
        request.Api.get(fetchTemplatePageUrl).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.templatePage = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取模板备选分类列表出错', show: 5};
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
      selectCate: function (category) {
        this.selectedCate = category;
        this.fetchTemplatePage();
      }
    }
  }
</script>

<style>

</style>
