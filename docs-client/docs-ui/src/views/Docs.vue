<template>
  <div>
    <a-row>
      <a-col :span="20" class="post-list">
        <div v-for="post in postPage.content" :key="post.id" class="post-item" @click="$router.push('/docs/read?postId=' + post.id)">
          <h3><b>{{post.title}}</b></h3>
          <p>
            <span class="post-attr">分类：{{getPostCate(post.cateId)}}</span>
            <span class="post-attr">标签：{{getPostTags(post.tags)}}</span>
            <span class="post-attr">最近更新：{{post.lastModifiedAt | dateFormat}}</span>
          </p>
        </div>
        <a-pagination :current="postPage.number + 1" :hideOnSinglePage="true" :simple="true"
                      :pageSize="postPage.size" :total="postPage.totalElements"/>
      </a-col>
      <a-col :span="4">
        <a-form>
          <a-form-item label="标题关键字">
            <a-input v-model="query.keyword" placeholder="在此处输入标题关键字" @keyup.enter="search"></a-input>
          </a-form-item>
          <a-form-item label="按分类查找">
            <a-select v-model="query.cateId" placeholder="请选择分类" :allowClear="true" @change="search">
              <a-select-option v-for="cate in searchOptions.cates" :key="cate.id">{{cate.cateName}}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="标签">
            <a-tag v-for="tag in searchOptions.tags" :key="tag.tagName"
                   :color="tag.tagName === query.tagName ? '#87d068' : ''" @click="searchWithTag(tag.tagName)">
              {{tag.tagName}} ({{tag.counts}})
            </a-tag>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>

<script>
  import dateFns from 'date-fns'
  import zh_cn from 'date-fns/locale/zh_cn'
  export default {
    name: "Docs",
    data() {
      return {
        searchOptions: {
          cates: [],
          tags: []
        },
        query: {
          keyword: '',
          tagName: '',
          cateId: undefined
        },
        postPage: {
          content: [],
          totalElements: 0,
          number: 0,
          size: 10
        }
      }
    },
    mounted() {
      this.fetchOptions();
      this.search();
    },
    filters: {
      dateFormat(date) {
        return dateFns.distanceInWordsToNow(date, {addSuffix: true, locale: zh_cn});
      }
    },
    methods: {
      fetchOptions() {
        let fetchUrl = '/docs/options';
        this.api.get(fetchUrl).then(res => {
          this.searchOptions = res.data;
        });
      },
      search() {
        let searchUrl = '/docs/page';
        this.api.post(searchUrl, this.query).then(res => {
          this.postPage = res.data;
        });
      },
      searchWithTag(tag) {
        if (this.query.tagName === tag) {
          this.query.tagName = '';
        } else {
          this.query.tagName = tag;
        }
        this.search();
      },
      getPostCate(cateId) {
        let cateName = '';
        this.searchOptions.cates.forEach((cate) => {
          if (cate.id === cateId) {
            cateName = cate.cateName;
          }
        });
        return cateName;
      },
      getPostTags(tags) {
        return tags.join('、');
      }
    }
  }
</script>

<style scoped>
  .post-list {
    padding: 0 8px;
  }
  .post-item {
    padding: 8px 16px;
  }
  .post-item:hover {
    cursor: pointer;
    background-color: #eee;
  }
  .post-attr {
    margin-right: 24px;
  }
</style>