import Vue from "vue";
import BootstrapVue from "bootstrap-vue";
import VueCookies from "vue-cookies";
import VueSimpleMDE from "vue-simplemde";
import App from "./App";
import router from "./router";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue/dist/bootstrap-vue.css";
import "font-awesome/css/font-awesome.min.css";
import "simplemde/dist/simplemde.min.css";

Vue.use(BootstrapVue);
Vue.use(VueCookies);
Vue.use(VueSimpleMDE);
Vue.config.productionTip = false;

new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App},
  data: {
    eventHub: new Vue()
  }
});
