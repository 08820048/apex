import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "home",
    component: () => import("../views/Home.vue"),
    meta: { title: "ApexBlog - 技术博客" },
  },
  {
    path: "/article/:id",
    name: "article",
    component: () => import("../views/ArticleDetail.vue"),
    meta: { title: "文章详情 - ApexBlog" },
  },
  {
    path: "/categories",
    name: "categories",
    component: () => import("../views/Categories.vue"),
    meta: { title: "分类 - ApexBlog" },
  },
  {
    path: "/tags",
    name: "tags",
    component: () => import("../views/Tags.vue"),
    meta: { title: "标签 - ApexBlog" },
  },
  {
    path: "/search",
    name: "search",
    component: () => import("../views/Search.vue"),
    meta: { title: "搜索 - ApexBlog" },
  },
  {
    path: "/portfolios",
    name: "portfolios",
    component: () => import("../views/Portfolios.vue"),
    meta: { title: "作品集 - ApexBlog" },
  },
  {
    path: "/friend-links",
    name: "friend-links",
    component: () => import("../views/FriendLinks.vue"),
    meta: { title: "友情链接 - ApexBlog" },
  },
  {
    path: "/subscribe",
    name: "subscribe",
    component: () => import("../views/Subscribe.vue"),
    meta: { title: "邮箱订阅 - ApexBlog" },
  },
  {
    path: "/about",
    name: "about",
    component: () => import("../views/About.vue"),
    meta: { title: "关于 - ApexBlog" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

// 路由守卫
router.beforeEach((to, from, next) => {
  try {
    // 设置页面标题
    if (to.meta.title) {
      document.title = to.meta.title;
    }
    next();
  } catch (error) {
    console.error("路由导航错误:", error);
    next(false);
  }
});

// 路由错误处理
router.onError((error) => {
  console.error("路由错误:", error);
});

export default router;
