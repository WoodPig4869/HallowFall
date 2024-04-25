<template>
  <div class="container mt-3" style="max-width: 400px;">
    <h1 class="mb-4">登入</h1>
    <form @submit.prevent="login" class="needs-validation" novalidate>
      <div class="mb-3" style="text-align: left;">
        <label for="phone" class="form-label">行動電話：</label>
        <input type="text" id="phone" class="form-control" v-model="phone" placeholder="手機號碼" @change="checkNull" required>
      </div>
      <div class="mb-3" style="text-align: left;">
        <label for="password" class="form-label">密碼：</label>
        <input type="password" id="password" class="form-control" v-model="password" @change="checkNull" required>
      </div>
      <button type="submit" class="btn btn-primary btn-lg w-100" :disabled="loginBtnDisabled">送出</button>
      <span v-if="loginError" class="text-danger">{{ loginError }}</span>
    </form>
    <div class="text-center mt-3">
      <router-link to="/register">沒有帳號？ 點擊註冊</router-link>
    </div>
  </div>
</template>

<script>
import Swal from 'sweetalert2'
import axios from '@/axios';

export default {
  data() {
    return {
      phone: '',
      password: '',
      loginError: '',
      loginBtnDisabled: true,
    };
  },
  methods: {
    checkNull() {
      this.loginBtnDisabled = true;
      if (this.phone !== '' && this.password !== '') {
        this.loginBtnDisabled = false;
      }

    },
    async login() {
      let timerInterval;
      Swal.fire({
        title: "登入中...",
        html: "請稍後",
        timerProgressBar: true,
        timer: 2000,
        allowOutsideClick: false,// 防止點擊背景關閉
        allowEscapeKey: false,// 防止按 ESC 關閉
        didOpen: () => {
          Swal.showLoading();
          const timer = Swal.getPopup().querySelector(".swal2-progress-bar");
          timerInterval = setInterval(() => {
            timer.style.width = `${Swal.getTimerLeft()}%`;
          }, 100);
        },
      }).then((result) => {
        /* Read more about handling dismissals below */
        if (result.dismiss === Swal.DismissReason.timer) {
          console.log("I was closed by the timer");
        }
      });
      try {
        localStorage.removeItem('Authorization');
        const response = await axios.post('/login', {
          phone: this.phone,
          password: this.password,
        });

        await new Promise((resolve) => setTimeout(resolve, 850));
        
        if (response.status === 200) {
          const token = response.data.token;
          // 登入成功，將 JWT token 存入 localStorage
          localStorage.setItem('Authorization', `Bearer ${token}`);
          // 導向到/content
          this.$router.push('/content');
          // 顯示成功提示
          Swal.fire({
            title: "登入成功！",
            icon: "success",
            confirmButtonText: "確認",
            allowOutsideClick: false,
            allowEscapeKey: false
          }).then((result) => {
            if (result.isConfirmed) {
              window.location.href = '/content';
            }
          });
        }
      } catch (error) {
        await new Promise((resolve) => setTimeout(resolve, 850));
        console.error('登入錯誤:', error);
        Swal.fire({
          title: "登入失敗",
          text: "電話或密碼錯誤",
          icon: "error",
          confirmButtonText: "確認",
        });
        this.password = '';
      }
    },
    
  },
};
</script>
