<template>
  <el-card class="mt-2" shadow="hover">
    <div class="d-flex">
      <el-avatar :src="userAvatar" class="me-3">
        <img :src="this.comment.avatar" class="rounded-circle border" style="width: 60px; height: 45px;" fit="cover" />
      </el-avatar>
      <div class="flex-grow-1">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <router-link :to="`/userPosts/${comment.userId}`" class="text-decoration-none fw-bold">{{ this.comment.nickname
            }}</router-link>
        </div>
        <p class="mb-0">{{ this.comment.content }}</p>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  props: {
    comment: {
      type: Object,
      required: true,
    },
  },
};
</script>