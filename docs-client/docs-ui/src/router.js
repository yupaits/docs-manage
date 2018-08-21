import Vue from "vue";
import Router from "vue-router"
import store from './store'

import Index from './views/Index'
import Docs from './views/Docs'
import ReadDoc from './views/ReadDoc'
import Cates from './views/Cates'

import consts from "./consts"

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/',
      component: Index,
      redirect: '/docs',
      children: [
        {
          path: '/docs',
          component: Docs,
        },
        {
          path: '/docs/read',
          component: ReadDoc,
          props: (route) => ({query: route.query.postId})
        },
        {
          path: '/cates',
          component: Cates
        }
      ]
    }
  ]
});

//登录状态判断
router.beforeEach((to, from, next) => {
  //设置导航栏当前激活的项
  store.dispatch('setCurrentKeys', [to.path]);
  const accessToken = window.$cookies.get(consts.tokenCookie);
  //Token是否有效
  const tokenValid = accessToken !== undefined && accessToken !== null && accessToken !== '';
  //token有效跳转主页
  if (!tokenValid) {
    window.location.href = consts.authUri + to.path;
  } else {
    next();
  }
});

export default router
