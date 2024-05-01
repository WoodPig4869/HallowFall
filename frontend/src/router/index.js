// router.js

import { createRouter, createWebHistory } from "vue-router";
import Home from "@/views/Home.vue";
import LoginPage from "@/views/Login.vue";
import Register from "@/views/Register.vue";
import Content from "@/views/Content.vue";
import Posts from "@/views/Posts.vue";
import UserPosts from "@/views/UserPosts.vue";
import PostInfo from "@/views/PostInfo.vue";
import NewPost from "@/views/NewPost.vue";
import Header from "@/components/Header.vue";

const routes = [
  {
    path: "/",
    component: Header,
    children: [
      {
        path: "", // 空路徑表示默認子路由，例如訪問 /nav 時會顯示 Nav.vue 的內容
        component: Home,
        name: "Home",
      },
      {
        path: "login",
        component: LoginPage,
        name: "Login",
      },
      {
        path: "content",
        component: Content,
        name: "Content",
      },
      {
        path: "register",
        component: Register,
        name: "Register",
      },
      {
        path: "posts",
        component: Posts,
        name: "Posts",
      },
      {
        path: "userPosts/:userId",
        component: UserPosts,
        name: "UserPosts",
      },
      {
        path: "postInfo/:postId",
        component: PostInfo,
        name: "PostInfo",
      },
      {
        path: "newPost",
        component: NewPost,
        name: "NewPost",
      },
      {
        path: "/test",
        component: () => import("@/components/Card.vue"),
        name: "Test",
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
