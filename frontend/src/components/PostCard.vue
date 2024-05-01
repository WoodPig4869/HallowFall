<template>

    <el-card class="mt-2" shadow="hover">
        <div class="d-flex flex-column flex-md-row">
            <el-image class="me-md-3 mb-3 mb-md-0" style="width: 150px; height: 150px;" :src="post.image" fit="cover" />
            <div class="flex-grow-1">
                <router-link :to="'/postInfo/' + post.postId">
                    <h4 style="font-weight: bold">{{ post.title }}</h4>
                </router-link>
                <div class="d-flex flex-column flex-md-row justify-content-between">
                    <div class="mb-2 mb-md-0">
                        <p class="fw-bold mb-1">作者：</p>
                        <router-link :to="'/userPosts/' + post.userId" style="font-size: 23px;"
                            class="text-decoration-none">{{ post.nickname }}</router-link>

                    </div>
                    <div class="mb-2 mb-md-0">
                        <p class="fw-bold mb-1">發布日期：</p>
                        <p>{{ post.createdAt }}</p>
                    </div>
                    <div>
                        <p class="fw-bold mb-1">最後編輯於：</p>
                        <p>{{ post.updatedAt }}</p>
                    </div>
                </div>
            </div>
        </div>
    </el-card>

</template>
<script>


export default {
    props: {
        post: {
            type: Object,
            required: true,
        },
    },

    methods: {
        goToUserPost() {
            this.$router.push('/posts/' + this.post.userId).then(() => {
                location.reload();
            });    
        },
    }
};
</script>

<style scoped>
img {
    max-width: 80px;
    height: auto;
}
</style>