import Vue from "vue";
import Router from "vue-router";

import constant from "./utils/constant";

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/',
      component: Manage,
      children: [
        {
          path: 'docs',
          component: Projects
        },
        {
          path: 'docs/projects/add',
          component: AddProject
        },
        {
          path: 'docs/projects/:id/edit',
          component: EditProject
        },
        {
          path: 'docs/projects/:id/documents',
          component: Documents
        },
        {
          path: 'docs/projects/:id/documents/:docId/edit',
          component: EditDocument
        },
        {
          path: 'templates',
          component: Templates
        },
        {
          path: 'templates/add',
          component: AddTemplate
        },
        {
          path: 'templates/:id/edit',
          component: EditTemplate
        }
      ]
    },
    {
      path: '/docs/read/documents/:id',
      name: 'ReadDocument',
      component: ReadDocument
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/register',
      component: Register
    },
    {
      path: '/feedback',
      component: Feedback
    },
    {
      path: '/forgetPassword',
      component: ForgetPassword
    },
    {
      path: '/passwordReset',
      component: PasswordReset
    }
  ]
});

//无需认证信息的path
const ignorePaths = ['/register', '/feedback', '/forgetPassword', '/passwordReset', '/'];

//特殊path无需认证的特殊
const ignoreNames = ['ReadDocument'];

//登录状态判断
router.beforeEach((to, from, next) => {
  const accessToken = window.$cookies.get(constant.accessToken);
  if ((accessToken === null || accessToken === undefined || accessToken === '') && to.path !== '/login') {
    if (ignorePaths.indexOf(to.path) >= 0 || ignoreNames.indexOf(to.name) >= 0) {
      next();
    } else {
      next('/login');
    }
  } else {
    next();
  }
});

export default router;
