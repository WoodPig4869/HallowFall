<template>

    <el-card class="mt-2" style="width: 100%; height: 170px;" shadow="hover">
        <div class="common-layout">
            <el-container>
                <el-aside width="300px">
                    <el-image style="width: auto; height: 150px" :src=post.image fit="cover" />
                </el-aside>
                <el-container>

                    <el-header>
                        <router-link :to="'/postInfo/' + post.postId">
                            <h4 style="font-weight: bold">{{ post.title }}</h4>
                        </router-link>
                    </el-header>
                    <el-main>
                        <el-row>
                            <p>作者：</p>
                            <a :href="'/posts/' + post.userId">
                                <el-col :span>{{ post.nickname }}</el-col>
                            </a>
                            <el-col :span="6">發布日期：<br />{{ post.createdAt }}</el-col>
                            <el-col :span="2"></el-col>
                            <el-col :span="6">最後編輯於：<br />{{ post.updatedAt }}</el-col>
                        </el-row>
                    </el-main>
                </el-container>
            </el-container>
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
};
</script>

<style scoped>
img {
    max-width: 80px;
    height: auto;
}
</style>