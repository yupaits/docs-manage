<template>
  <b-container fluid class="my-3">
    <b-row align-h="center">
      <b-col cols="11">
        <b-alert :variant="alert.variant" :show="alert.show" dismissible @dismissed="alert.show=null">
          <b>{{alert.msg}}</b>
        </b-alert>
      </b-col>
    </b-row>
    <b-row align-h="center">
      <b-col cols="2">
        <b-button variant="outline-success" to="/templates/add">添加模板</b-button>
        <b-input-group size="sm" class="mt-3">
          <b-form-input type="text"
                        v-model="keyword"
                        placeholder="请输入搜索关键字"></b-form-input>
          <b-input-group-button>
            <b-button variant="outline-secondary" @click="clearKeyword"><span class="fa fa-remove"></span></b-button>
            <b-button variant="outline-primary" @click="search"><span class="fa fa-search"></span></b-button>
          </b-input-group-button>
        </b-input-group>
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
        <div class="mt-3" v-if="tagRates.length > 0">
          <p>热门标签</p>
          <b-button v-for="(tagRate, index) in tagRates"
                    size="sm"
                    variant="light"
                    @click="toggleSelectTag(tagRate.tag)"
                    :pressed="selectedTag === tagRate.tag"
                    class="mx-1 mb-1">{{tagRate.tag}}
          </b-button>
        </div>
        <div class="mt-3">
          <b-link to="" class="card-link">模板广场</b-link>
        </div>
      </b-col>
      <b-col cols="9">
        <b-card-group columns class="mt-5">
          <b-card v-for="template in templatePage.content"
                  @mouseenter="hoverId = template.id"
                  @mouseleave="hoverId = null">
            <h4>
              <router-link :to="'/templates/' + template.id + '/edit'" class="card-link">{{template.name}}
              </router-link>
            </h4>
            <div class="float-right">
              <b-dropdown size="sm" text="删除" variant="light" right v-show="hoverId === template.id">
                <b-dropdown-header class="text-danger"><h6 class="text-bold"><b>确定删除吗?</b></h6></b-dropdown-header>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item-button @click="submitDelete"><span class="fa fa-check"> 确定</span>
                </b-dropdown-item-button>
                <b-dropdown-item-button><span class="fa fa-times"> 取消</span>
                </b-dropdown-item-button>
              </b-dropdown>
            </div>
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
              <small><span class="fa fa-heart-o"> {{template.likings}}</span> &nbsp;&nbsp;&nbsp;&nbsp; <span
                class="fa fa-eye"> {{template.visitCount}}</span></small>
            </p>
          </b-card>
        </b-card-group>
        <b-button-toolbar justify v-if="!templatePage.first || !templatePage.last">
          <b-button size="sm" variant="light" :disabled="templatePage.first" @click="previousPage">
            <span class="fa fa-arrow-left"> 上一页</span>
          </b-button>
          <b-button size="sm" variant="light" :disabled="templatePage.last" @click="nextPage">
            <span class="fa fa-arrow-right"> 下一页</span>
          </b-button>
        </b-button-toolbar>
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
        templatePage: {},
        categories: [],
        selectedCate: '',
        tagRates: [],
        selectedTag: '',
        keyword: '',
        page: 0
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
        const fetchTemplatePageUrl = '/templates/owner/' + user.id + '?page=' + this.page +
          '&category=' + this.selectedCate + '&tag=' + this.selectedTag + '&keyword=' + this.keyword;
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
      clearKeyword: function () {
        this.keyword = '';
      },
      search: function () {
        this.fetchTemplatePage();
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
      },
      previousPage: function () {
        if (!this.templatePage.first) {
          this.page = this.templatePage.number - 1;
          this.fetchTemplatePage();
        }
      },
      nextPage: function () {
        if (!this.templatePage.last) {
          this.page = this.templatePage.number + 1;
          this.fetchTemplatePage();
        }
      },
      submitDelete: function () {
        const instance = this;
        request.Api.delete('/templates/' + this.hoverId).then(function (result) {
          if (result.code !== 200) {
            instance.alert = {variant: 'warning', msg: result.msg, show: 5};
          } else {
            instance.fetchData();
          }
        }).catch(function (error) {
          instance.alert = {variant: 'danger', msg: '删除文档出错', show: 5};
        });
      }
    }
  }
</script>

<style>

</style>
