<template>
  <div>
    <h3><b>{{post.title}}</b></h3>
    <p>
      <span class="post-attr">最近更新：{{post.createAt | dateFormat}}</span>
      <span class="post-attr">最近更新：{{post.lastModifiedAt | dateFormat}}</span>
    </p>
    <p>{{post.content}}</p>
  </div>
</template>

<script>
  import dateFns from 'date-fns'
  export default {
    name: "ReadDoc",
    data() {
      return {
        post: {}
      }
    },
    mounted() {
      this.fetchPost();
    },
    filters: {
      dateFormat(date) {
        return dateFns.format(date, 'YYYY-MM-DD HH:mm:ss');
      }
    },
    methods: {
      fetchPost() {
        let postId = this.$route.query.postId;
        let fetchUrl = '/docs/' + postId;
        this.api.get(fetchUrl).then(res => {
          this.post = res.data;
        });
      }
    }
  }
</script>

<style scoped>
  .post-attr {
    margin-right: 24px;
  }
</style>