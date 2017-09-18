import Vue from "vue";
import Router from "vue-router";
import Login from "@/pages/Login";
import Manage from "@/pages/Manage";
import Projects from "@/pages/docs/Projects";
import AddProject from "@/pages/docs/AddProject";
import EditProject from "@/pages/docs/EditProject";
import Documents from "@/pages/docs/Documents";
import EditDocument from "@/pages/docs/EditDocument";

import constant from "@/utils/constant";

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
          path: 'files'
        }
      ]
    },
    {
      path: '/login',
      component: Login
    },
  ]
});

// 登录状态判断
router.beforeEach((to, from, next) => {
  const accessToken = window.$cookies.get(constant.accessToken);
  if ((accessToken === null || accessToken === undefined || accessToken === '') && to.path !== '/login') {
    next('/login');
  } else {
    next();
  }
});

export default router;
