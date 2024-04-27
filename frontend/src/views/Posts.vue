<template>
    <div v-if="logged" class="container mb-3">
        <el-button type="primary" round plain @click="goToNewPost">新貼文</el-button>
        <el-button type="info" round plain @click="goToMyPosts">我的貼文</el-button>
    </div>
    <div v-if="!this.userId == 0" class="container mb-3">
        <div class="row">
            <div class="col-12 col-md-8">
                <p class="fw-bold">會員資訊</p>
                <h2 class="mb-3 text-center" style="font-weight: bold">{{ targetUser.nickname }}</h2>
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
                })
                .catch(error => {
                    console.error('Error fetching posts:', error);
                    this.loading = false;
                });
        },
        fetchTargetUser() {
            // 先檢查是否為訪問所有文章
            if (this.userId === 0) {
                return;
            }
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
        goToMyPosts() {
            window.location.href = '/posts/' + this.loggedUser.userId;
        },
        goToNewPost() {
            window.location.href = '/newpost';
        }
    },
    watch: {
        '$route.params.userId': function () {
            window.location.reload();
        }
    }
};
</script>

<style>
.loading-text {
    margin-top: 20px;
    text-align: center;
    font-size: 18px;
}
</style>
