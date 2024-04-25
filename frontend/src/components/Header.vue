<template>
  <nav class="navbar navbar-expand-lg justify-content-between fixed-top navbar-light"
    style="background-color: #e3f2fd; padding: 0.5rem 2rem;">
    <div class="container-fluid">
      <div class="ms-3">
        <router-link class="navbar-brand" to="/">Side Project</router-link>
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
            <h4 id="nickname-header" class="mt-2"></h4>
          </div>
          <div class="me-5" v-if="isLoggedIn">
            <router-link to="/content">
              <img id="avatar-header" src="/avatar.png" class="rounded-circle border " style="width: 80px;" />
            </router-link>
          </div>
          <a v-if="!isLoggedIn" class="btn btn-outline-success btn-lg" href="/register">註冊</a>
          <a v-if="!isLoggedIn" class="btn btn-primary btn-lg" href="/login">登入</a>
          <a v-else class="btn btn-secondary btn-lg" @click="logout()">登出</a>
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



// 使用ref創建響應式變數
const isLoggedIn = ref(false)
// 在組建加載時檢查登入狀態
onMounted(async () => {
  try {
    const response = await axios.get('/user/check/login');
    isLoggedIn.value = response.data;
  } catch (error) {
    console.error('Failed to check login status:', error);
  }
  if (isLoggedIn.value===true) {
    const response = await axios.get('/user/self');
    const nickname = response.data.nickname;
    const nicknameElement = document.getElementById('nickname-header');
    if (nicknameElement) {
      nicknameElement.textContent = nickname;
    }
    const avatar = response.data.avatar;
    if (avatar!==null) {
      document.getElementById('avatar-header').setAttribute('src', avatar);
    }
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
      window.location.href = '/';
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