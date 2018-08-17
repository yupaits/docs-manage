import Vue from "vue";
import Router from "vue-router"
import store from './store'

import Index from './views/Index'
import Docs from './views/Docs'
import ReadDoc from './views/ReadDoc'
import Cates from './views/Cates'

import Login from './views/Login'
import Register from './views/Register'

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
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    }
  ]
});

//无需认证信息的path
const ignorePaths = ['/register'];

//登录状态判断
router.beforeEach((to, from, next) => {
  //设置导航栏当前激活的项
  store.dispatch('setCurrentKeys', [to.path]);
  const accessToken = window.$cookies.get(consts.tokenCookie);
  //Token是否有效
  const tokenValid = accessToken !== undefined && accessToken !== null && accessToken !== '';
  //跳转的目标路径是否忽略认证
  const isIgnore = ignorePaths.indexOf(to.path) >= 0;
  if (to.path === '/login') {
    //token有效跳转主页
    if (tokenValid) {
      next('/');
    } else {
      next();
    }
  } else if (!tokenValid) {
    isIgnore ? next() : next('/login');
  } else {
    next();
  }
});

export default router
