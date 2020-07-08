import Vue from 'vue'
import Router from 'vue-router'
import store from './store'

Vue.use(Router)

let router = new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import(/* webpackChunkName: "login" */ './views/login')
    },
    {
      path: '/home',
      component: () => import(/* webpackChunkName: "home" */ './views/home/home.vue'),
      children: [
        {
          path: '',
          redirect: 'list'
        },
        {
          path: 'list',
          component: () => import(/* webpackChunkName: "list" */ './views/home/list')
        },
        {
          path: 'search',
          component: () => import(/* webpackChunkName: "search" */ './views/home/search')
        }
      ]
    },
    {
      path: '*',
      component: () => import(/* webpackChunkName: "404" */'./views/404')
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (store.state.selectedNotebook && store.state.selectedNote && store.state.note) {
    store.dispatch('wsSaveNote')
  }
  //当用户还未登录时，转跳到登录页
  if (to.path !== '/login' && store.state.username === '') {
    next('/login')
  } else {
    next()
  }
})

export default router
