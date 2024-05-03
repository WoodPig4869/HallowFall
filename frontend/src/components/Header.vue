<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <router-link to="/" class="me-3"><el-image class="me-3" style="width: 50px; height: 50px;" src="/favicon.ico"
          fit="cover" /></router-link>
      <router-link class="navbar-brand" to="/">Hallow Fall</router-link>

      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item dropdown">
            <p class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button"
              data-bs-toggle="dropdown" aria-expanded="false">
              開發日誌
            </p>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
              <li><router-link class="dropdown-item" to="/release">版本日誌</router-link></li>
              <li><router-link class="dropdown-item" to="/erm">資料庫 ER Model</router-link></li>
              <li><router-link class="dropdown-item" to="/swagger">Swagger UI（API文件）</router-link></li>
              <li><router-link class="dropdown-item disabled" to="$">未來展望</router-link></li>
            </ul>
          </li>
          <li class=" nav-item">
            <router-link class="nav-link active" aria-current="page" to="/posts">社群文章</router-link>
          </li>
          <li class=" nav-item">
            <router-link class="nav-link active" aria-current="page" to="/content">會員中心</router-link>
          </li>
        </ul>

        <div v-if="isLoggedIn" class="d-flex align-items-center">
          <router-link class="nav-link" to="/content">
            <img :src="avatar" alt="Avatar" class="rounded-circle me-2" style="width: 32px; height: 32px;">
            {{ nickname }}
          </router-link>
          <button class="btn btn-outline-info" @click="logout">登出</button>
        </div>
        <div v-else>
          <router-link class="btn btn-outline-success me-2" to="/register">註冊</router-link>
          <router-link class="btn btn-primary" to="/login">登入</router-link>
        </div>
      </div>
    </div>
  </nav>

  <br />
  <br />
  <router-view />
</template>


<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from '@/axios';
import Swal from 'sweetalert2';
import router from '@/router';
import { ElMessage } from 'element-plus';

// 使用ref創建響應式變數
const avatar = ref<string>('');
const nickname = ref<string>('');
const userId = ref<number>(0);
const isLoggedIn = ref(false);

// 在組建加載時檢查登入狀態
onMounted(async () => {
  const token = localStorage.getItem('Authorization');
  if (token) {
    isLoggedIn.value = true;
  }

  const response = await axios.get('/user/0');
  nickname.value = response.data.nickname;
  avatar.value = response.data.avatar;
  userId.value = response.data.userId;
  if (avatar.value == null) {
    avatar.value = '/avatar.png';
  }
});

// 登出方法
const logout = async () => {
  Swal.fire({
    title: "登出",
    text: "確定要登出嗎?",
    icon: "info",
    confirmButtonText: "確認",
    showCancelButton: true,
    cancelButtonText: "取消",
  }).then((result) => {
    if (result.isConfirmed) {
      localStorage.removeItem('Authorization');
      ElMessage.info("已登出");
      router.push({ path: '/' }).then(() => {
        location.reload();
      });
    }
  });

}


</script>
<style>
.btn {
  margin: 8px;
}
.flex-grow {
  flex-grow: 1;
}
.user-info {
  display: flex;
  align-items: center;
}
.avatar-container {
  margin: 20px;
  /* 调整头像与昵称之间的间距 */
}
</style>