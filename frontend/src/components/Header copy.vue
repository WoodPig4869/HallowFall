<template>
  <nav class="navbar navbar-expand-lg justify-content-between fixed-top navbar-light"
    style="background-color: #e3f2fd; padding: 0.5rem 2rem;">
    <div class="container-fluid">
      <div class="ms-3">
        <router-link class="navbar-brand" to="/">Hallow Fall</router-link>
      </div>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link" to="/">
              <button class="btn btn-outline-success" type="button">首頁</button>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/content">
              <button class="btn btn-outline-success" type="button">用戶中心</button>
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/posts" replace>
              <button class="btn btn-outline-success me-2" type="button">社群文章</button>
            </router-link>
          </li>
        </ul>
        <div class="d-flex me-3 align-items-center">
          <div class="me-5" v-if="isLoggedIn">
            <h4 class="mt-2">{{ nickname }}</h4>
          </div>
          <div class="me-5" v-if="isLoggedIn">
            <router-link to="/content">
              <el-image :src="avatar" class="rounded-circle border" style="width: 50px; height: 50px;" fit="cover" />
            </router-link>
          </div>
          <router-link to="/register" v-if="!isLoggedIn" class="btn btn-outline-success btn-lg">註冊</router-link>
          <router-link to="/login" v-if="!isLoggedIn" class=" btn btn-primary btn-lg">登入</router-link>
          <button v-if="isLoggedIn" class="btn btn-secondary btn-lg" @click="logout()">登出</button>
        </div>
      </div>
    </div>
  </nav>
  <br />
  <br />
  <router-view />
</template>


<script setup lang="ts">
import { ref ,onMounted} from 'vue'
import axios from '@/axios';
import Swal from 'sweetalert2';
import router from '@/router';
import { ElMessage } from 'element-plus';

// 使用ref創建響應式變數
const avatar = ref<string>('');
const nickname = ref<string>('');
const isLoggedIn = ref(false);

// 在組建加載時檢查登入狀態
onMounted(async () => {
  const token = localStorage.getItem('Authorization');
  if (token) {
    isLoggedIn.value = true;
  }

    const response = await axios.get('/user/self');
    nickname.value = response.data.nickname;
    avatar.value = response.data.avatar;
    if (avatar.value == null) {
      avatar.value = '/avatar.png';
    }
});

// 登出方法
const logout = async ()=>{
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

const goToPosts = ()=>{
  window.location.href = '/posts/0';
}


</script>
<style>
.btn{
  margin: 8px;
}
</style>