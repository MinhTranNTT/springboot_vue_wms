import VueRouter from 'vue-router'

const routes =[
    {
        path:'/',
        name:'login',
        component:()=>import('../components/Login')
    },
    {
        path:'/Error',
        name:'error',
        component:()=>import('../components/Error')
    },
    {
        path:'/Limit',
        name:'limit',
        component:()=>import('../components/Limit')
    },
    {
        path: '/Index',
        name: 'index',
        component:()=>import('../components/Index'),
        children:[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'首页'
                },
                component:()=>import('../components/Home')
            }
        ]
    }
]

const router = new VueRouter({
    mode:'history',
    base:'/',
    routes
})

export function resetRouter() {
    router.matcher = new VueRouter({
        mode:'history',
        routes: []
    }).matcher
}

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

export default router;