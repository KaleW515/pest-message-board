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
      },
      {
        path: '/home/user',
        name: 'User',
        component: () => import('@/views/user/index')
      },
      {
        path: '/home/publish',
        name: 'Publish',
        component: () => import('@/views/publish/index')
      },
      {
        path: '/admin/user',
        name: 'UserAdmin',
        component: () => import('@/views/admin/component/user')
      }
    ]
  },
  {
    path: '/other',
    name: 'Other',
    component: () => import('@/views/other/index')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
