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
          <b-button v-for="category in categories"
                    size="sm"
                    variant="light"
                    block
                    @click="toggleSelectCate(category)"
                    :pressed="selectedCate === category">{{category}}
          </b-button>
        </div>
        <div class="mt-3">
          <p>热门标签</p>
          <b-button v-for="(tagRate, index) in tagRates"
                    size="sm"
                    variant="light"
                    @click="toggleSelectTag(tagRate.tag)"
                    :pressed="selectedTag === tagRate.tag"
                    class="mx-1 mb-1">{{tagRate.tag}}
          </b-button>
        </div>
      </b-col>
      <b-col cols="10">
        <b-card-group columns class="mt-5">
          <b-card v-for="template in templatePage.content"
                  @mouseenter="hoverId = template.id"
                  @mouseleave="hoverId = null">
            <h4>
              <router-link :to="'/templates/' + template.id + '/edit'" class="card-link">{{template.name}}
              </router-link>
            </h4>
            <p class="card-text text-muted">
              <small v-if="template.category">分类: {{template.category}}</small>
              <br>
              <small>创建于: {{template.createdAt | timeFormat}}</small>
              <br>
              <small v-if="template.updatedAt">修改于: {{template.updatedAt | timeFormat}}</small>
            </p>
            <p class="card-text">
              {{template.description}}
            </p>
            <p class="card-text text-muted" v-if="template.tags">
              <small>标签: {{template.tags}}</small>
              <br>
              <small><span class="fa fa-heart-o"> {{template.likings}}</span> &nbsp;&nbsp;&nbsp;&nbsp; <span class="fa fa-eye"> {{template.visitCount}}</span></small>
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
        tagRates: [],
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
        this.fetchTags();
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
          instance.alert = {variant: 'danger', msg: '获取模板列表出错', show: 5};
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
      fetchTags: function () {
        const user = JSON.parse(this.$cookies.get(constant.user));
        const instance = this;
        request.Api.get('/templates/tags/owner/' + user.id).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.tagRates = result.data;
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '获取模板标签列表出错', show: 5};
        });
      },
      toggleSelectCate: function (category) {
        if (category === this.selectedCate) {
          this.selectedCate = '';
        } else {
          this.selectedCate = category;
        }
        this.fetchTemplatePage();
      },
      toggleSelectTag: function (tag) {
        if (tag === this.selectedTag) {
          this.selectedTag = '';
        } else {
          this.selectedTag = tag;
        }
        this.fetchTemplatePage();
      }
    }
  }
</script>

<style>

</style>
