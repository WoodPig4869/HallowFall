<template>
    <div class="container">
        <div class="row">
            <div class="col">
                <h2 class="text-center">文章列表</h2>
            </div>
            <div class="col text-end">
                <button v-if="logged" @click="goMyPosts()" type="button" class="btn btn-outline-success">我的文章</button>
                <button @click="goNewPost()" type="button" class="btn btn-primary">發新文章</button>
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
    <el-backtop :right="60" :bottom="60" />
</template>

<script>
import axios from '@/axios';
import PostCard from '@/components/PostCard.vue';
import { jwtDecode } from "jwt-decode";
import { ElMessage } from 'element-plus';

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
        goNewPost() {
            if (this.logged) {
                this.$router.push({ path: `/newpost` })
            } else {
                ElMessage.warning("請先登入")
                this.$router.push({ path: `/login` })
            }
        },
        goMyPosts(){
            if (this.logged) {
                this.$router.push({ path: `/userPosts/${this.loggedUser.userId}` })
            } else {
                ElMessage.warning("請先登入")
                this.$router.push({ path: `/login` })
            }
        }
    },
    watch: {
        '$route.params.userId': function () {
            location.reload();
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
