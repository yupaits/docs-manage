import Vue from "vue";
import App from "./App";
import router from "./router";
import store from './store';
import Antd from 'ant-design-vue';
import api from './api'
import VueCookies from "vue-cookies";
import VueSimpleMDE from "vue-simplemde";

import 'ant-design-vue/dist/antd.min.css';

Vue.config.productionTip = false;

Vue.use(Antd);
Vue.use(VueCookies);
Vue.use(VueSimpleMDE);

Vue.prototype.api = api;

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
