<template>
    <div class="container mb-3">
        <div class="row">
            <div class="col-12 col-md-8">
                <p class="fw-bold">會員資訊</p>
                <h2 class="mb-3 text-center" style="font-weight: bold">{{ targetUser.nickname }}</h2>
                <el-image :src="targetUser.avatar" class="rounded-circle border" style="width: 150px; height: 150px;"
                    fit="cover" />
                <div class="mt-3">
                    <p class="fw-bold mb-1">個人簽名</p>
                    <v-md-editor v-model="this.targetUser.signature" mode="preview"></v-md-editor>
                </div>
            </div>
            <div class="col-12 col-md-4">
                <div class="mb-3">
                    <p class="fw-bold mb-1">行動電話:</p>
                    <p>{{ targetUser.phone }}</p>
                </div>
                <div class="mb-3">
                    <p class="fw-bold mb-1">註冊日期:</p>
                    <p>{{ targetUser.registrationDate }}</p>
                </div>
                <div class="mb-3">
                    <p class="fw-bold mb-1">身份類別:</p>
                    <p>{{ targetUser.role }}</p>
                </div>
            </div>
        </div>
        <h3>已發布文章</h3>
        <div v-if="posts.length == 0">
            <br/>
            <h4>暫無文章</h4>
        </div>
    </div>

    <br />
    <div>
        <div v-if="loading" class="loading-text">Loading...</div>
        <div v-else>
            <div v-for="post in posts" :key="post.postId">
                <PostCard :post="post" />
            </div>
        </div>
    </div>
    <el-backtop :right="60" :bottom="60" />
</template>

<script>
import axios from '@/axios';
import PostCard from '@/components/PostCard.vue';
import { jwtDecode } from "jwt-decode";

export default {
    components: {
        PostCard,
    },
    data() {
        return {
            loading: true,
            logged: false,
            posts: [],
            userId: parseInt(this.$route.params.userId),
            loggedUser: [],
            targetUser: [],
        };
    },
    mounted() {
        if (isNaN(this.userId)) {
            this.userId = 0;
        }

        this.fetchPosts();
        this.fetchLoggedUser();
        this.fetchTargetUser();
    },
    methods: {
        fetchPosts() {
            axios
                .get(`/post/dto/${this.userId}`)
                .then(response => {
                    this.posts = response.data;
                    this.loading = false;
                    // console.log(this.posts.length);
                })
                .catch(error => {
                    console.error('Error fetching posts:', error);
                });
        },
        fetchTargetUser() {
            axios
                .get(`/user/${this.userId}`)
                .then(response => {
                    this.targetUser = response.data;
                    // console.log(this.targetUser);
                })
                .catch(error => {
                    console.error('Error fetching user:', error);
                });
        },
        fetchLoggedUser() {
            // 從 localStorage 中取出 JWT
            const jwt = localStorage.getItem('Authorization');
            // 去掉 "Bearer " 字串，只取得 JWT 本身
            const token = jwt ? jwt.replace('Bearer ', '') : null;
            if (token) {
                // 解析 JWT
                const decoded = jwtDecode(token);
                const phone = decoded.sub;
                const userId = decoded.userId;
                const nickname = decoded.nickname;
                const registrationDate = decoded.registrationDate;
                const role = decoded.role;
                this.loggedUser = { phone, userId, nickname, registrationDate, role };
                // console.log(this.user);
                this.logged = true;
            }
            return;
        },
    },
};
</script>

<style>
.loading-text {
    margin-top: 20px;
    text-align: center;
    font-size: 18px;
}
</style>
