<template>
  <div class="container mt-3" style="max-width: 400px;">
    <h1 class="mb-4">註冊</h1>
    <form @submit.prevent="register" class="needs-validation" novalidate>
      <div class="mb-3" style="text-align: left;">
        <label for="phone" class="form-label">行動電話：</label>
        <input type="text" id="phone" class="form-control" v-model="phone" @blur="checkPhone" required>
        <p v-if="!phoneCheck" class="text-success">電話可用</p>
        <p class="text-danger">{{ phoneCheckError }}</p>
      </div>
      <div class="mb-3" style="text-align: left;">
        <label for="password" class="form-label">密碼：</label>
        <input type="password" id="password" class="form-control" v-model="password" required>
      </div>
      <div class="mb-3" style="text-align: left;">
        <label for="confirmPassword" class="form-label">確認密碼：</label>
        <input type="password" id="confirmPassword" class="form-control" v-model="confirmPassword" required>
      </div>
      <div class="mb-3" style="text-align: left;">
        <label for="nickname" class="form-label">暱稱：</label>
        <input type="text" id="nickname" class="form-control" v-model="nickname" required>
      </div>
      <button type="submit" class="btn btn-success btn-lg w-100" :class="{ 'disabled': phoneCheck }">註冊</button>
      <span v-if="registerError" class="text-danger">{{ registerError }}</span>
    </form>
    <div class="text-center mt-3">
      <router-link to="/login">已有帳號？ 點擊登入</router-link>
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
      confirmPassword: '',
      nickname: '',
      registerError: '',
      phoneCheck: true,
      phoneCheckError: '',
    };
  },
  methods: {
    async checkPhone() {
      const phoneRegex = /^\d{10}$/; // 正則表達式，要求為 10 個數字
      if (!phoneRegex.test(this.phone)) {
        this.phoneCheckError = '電話格式錯誤';
        return;
      }

      const response = await axios.get(`/user/check/phone/${this.phone}`);
      // true 代表已被註冊, false 代表未被註冊
      this.phoneCheck = response.data;
      if (this.phoneCheck) {
        this.phoneCheckError = '電話已被註冊';
        return;
      }
      this.phoneCheckError = '';
    },
    async register() {
      this.registerError = '';
      // 驗證表單
      if (!this.phone || !this.password || !this.confirmPassword || !this.nickname) {
        this.registerError = '請填寫所有欄位'
        return;
      }
      if (this.password !== this.confirmPassword) {
        this.registerError = '密碼不一致'
        return;
      }
      let timerInterval;
      Swal.fire({
        title: "註冊中...",
        html: "請稍後",
        timerProgressBar: true,
        timer: 900,
        allowOutsideClick: false,
        allowEscapeKey: false,
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
        const response = await axios.post('/register', {
          phone: this.phone,
          password: this.password,
          nickname: this.nickname,
        });

        await new Promise((resolve) => setTimeout(resolve, 850));

        if (response.status === 200) {
          const token = response.data.token;
          localStorage.setItem('Authorization', `Bearer ${token}`);
          // 註冊成功，顯示成功提示
          Swal.fire({
            title: "註冊成功！",
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
        console.error('註冊錯誤:', error);
        Swal.fire({
          title: "註冊失敗",
          text: "註冊時發生錯誤",
          icon: "error",
          confirmButtonText: "確認",
        });
      }
    },
  },
};
</script>
