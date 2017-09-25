<template>
  <b-container>
    <b-row class="mt-3">
      <b-col cols="3">
        <b-button variant="outline-success" to="/templates/add">
          <span class="fa fa-plus"> 添加模板</span>
        </b-button>
        <div class="mt-3" v-if="true">
          <h4><span class="fa fa-th-list"> 模板分类</span></h4>
        </div>
        <div class="mt-3" v-if="true">
          <h4><span class="fa fa-tags"> 热门标签</span></h4>
        </div>
      </b-col>
      <b-col cols="9">
        <b-card-group columns class="mt-5">
          <b-card v-for="template in templates" :title="template.name">
            <h4 slot="header">
              接口文档
              <b-button-group class="float-right">
                <b-button size="sm" variant="outline-primary" :to="'/templates/' + hoverId + '/edit'">
                  <span class="fa fa-pencil"> 编辑</span>
                </b-button>
                <b-dropdown size="sm" text="删除" variant="outline-danger" right>
                  <b-dropdown-header class="text-danger"><h6 class="text-bold"><b>确定删除吗?</b></h6></b-dropdown-header>
                  <b-dropdown-divider></b-dropdown-divider>
                  <b-dropdown-item-button @click="submitDelete"><span class="fa fa-check"> 确定</span>
                  </b-dropdown-item-button>
                  <b-dropdown-item-button @click="cancelDelete"><span class="fa fa-times"> 取消</span>
                  </b-dropdown-item-button>
                </b-dropdown>
              </b-button-group>
            </h4>
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
  export default {
    data() {
      return {
        alert: {variant: 'info', msg: '', show: null},
        hoverId: null,
        templates: []
      }
    },
    create() {

    }
  }
</script>

<style>

</style>
