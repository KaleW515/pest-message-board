import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('@/views/home/index'),
    children: [
      {
        path: '/home/pest',
        name: 'Pest',
        component: () => import('@/views/pest/index')
      },
      {
        path: '/home/message-board',
        name: 'MessageBoard',
        component: () => import('@/views/message/board/index')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
