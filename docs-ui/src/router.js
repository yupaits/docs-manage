import Vue from "vue";
import Router from "vue-router";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Feedback from "./pages/Feedback";
import ForgetPassword from "./pages/ForgetPassword";
import PasswordReset from "./pages/PasswordReset";
import ReadDocument from "./pages/docs/ReadDocument";
import Manage from "./pages/Manage";
import Projects from "./pages/docs/Projects";
import AddProject from "./pages/docs/AddProject";
import EditProject from "./pages/docs/EditProject";
import Documents from "./pages/docs/Documents";
import EditDocument from "./pages/docs/EditDocument";
import Templates from "./pages/templates/Templates";
import AddTemplate from "./pages/templates/AddTemplate";
import EditTemplate from "./pages/templates/EditTemplate";

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
