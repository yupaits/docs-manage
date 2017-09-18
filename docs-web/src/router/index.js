import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/Login'
import Manage from '@/pages/Manage'
import Projects from '@/pages/docs/Projects'
import AddProject from '@/pages/docs/AddProject'
import Documents from '@/pages/docs/Documents'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      component: Manage,
      children: [
        {
          path: '/docs',
          component: Projects
        },
        {
          path: '/docs/projects/add',
          component: AddProject
        },
        {
          path: '/docs/projects/:id/documents',
          component: Documents
        },
        {
          path: '/files'
        }
      ]
    },
    {
      path: '/login',
      component: Login
    },
  ]
})
