<template>
  <b-container>
    <b-card-group columns class="mt-5">
      <b-card v-for="template in templates" :title="template.name">
        <h4 slot="header">
          接口文档
          <b-button-group class="float-right">
            <b-button size="sm" variant="outline-primary" :to="'/templates/' + hoverId + '/edit'"><span
              class="fa fa-pencil"> 编辑</span></b-button>
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
      <b-card>
        <h4 slot="header">
          接口文档
          <b-button-group class="float-right">
            <b-button size="sm" variant="outline-primary" :to="'/templates/' + hoverId + '/edit'"><span
              class="fa fa-pencil"> 编辑</span></b-button>
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
          {{}}
        </p>
      </b-card>
      <b-card title="接口文档">
        <p class="card-text">

          接口文档编写利器
        </p>
      </b-card>
      <b-card title="接口文档">
        <p class="card-text">

          接口文档编写利器
        </p>
      </b-card>
      <b-card title="接口文档">
        <p class="card-text">

          接口文档编写利器
        </p>
      </b-card>
      <b-card title="接口文档">
        <p class="card-text">

          接口文档编写利器
        </p>
      </b-card>
      <b-card title="接口文档">
        <p class="card-text">

          接口文档编写利器
        </p>
      </b-card>
      <b-card title="接口文档">
        <p class="card-text">

          接口文档编写利器
        </p>
      </b-card>
    </b-card-group>
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
