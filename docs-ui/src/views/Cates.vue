<template>
  <div>
    <a-row>
      <a-col :span="4" v-for="cate in cates" :key="cate.id">
        <a-card class="cate-item">
          <p><a-icon type="inbox"/> {{cate.cateName}}</p>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  export default {
    name: "Cates",
    data() {
      return {
        cates: []
      }
    },
    mounted() {
      this.fetchCates();
    },
    methods: {
      fetchCates() {
        let fetchUrl = '/docs/cate/list';
        this.api.get(fetchUrl).then(res => {
          this.cates = res.data;
        })
      }
    }
  }
</script>

<style scoped>
  .cate-item {
    margin: 10px 10px;
  }
</style>