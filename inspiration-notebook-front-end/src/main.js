import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/iview.js'
import tracker from "./tracker"

Vue.config.productionTip = false

tracker.init()
window.oncontextmenu = function (e) {
  console.log(tracker.showSpecs())
}

let app = new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

store.$app = app //允许在vuex中获取vue实例

export default app