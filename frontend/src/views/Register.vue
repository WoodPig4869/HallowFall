<template>
  <div v-loading="loading">
    <h2 class="text-center mb-3">會員註冊</h2>
    <div class="container mt-2 d-flex justify-content-center">
      <div class="container" style="max-width: 420px;">
        <el-form ref="ruleFormRef" @keydown.enter.prevent="submitForm(ruleFormRef)" style="max-width: 800px"
          :model="ruleForm" status-icon :rules="rules" label-width="auto" size="large" labelPosition="top">
          <el-form-item label="電子信箱" prop="email">
            <el-input v-model="ruleForm.email" maxlength="30" />
          </el-form-item>
          <el-form-item label="密碼" prop="password">
            <el-input v-model="ruleForm.password" type="password" autocomplete="off" maxlength="30" />
          </el-form-item>
          <el-form-item label="確認密碼" prop="checkPass">
            <el-input v-model="ruleForm.checkPass" type="password" autocomplete="off" maxlength="30" />
          </el-form-item>
          <el-form-item label="暱稱" prop="nickname">
            <el-input v-model="ruleForm.nickname" maxlength="30" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm(ruleFormRef)">送出</el-button>
            <el-button @click="resetForm(ruleFormRef)">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import axios from '@/axios'
import DOMPurify from 'isomorphic-dompurify';
import { ElMessage } from "element-plus";
import router from '@/router';

const loading = ref(false)
const ruleFormRef = ref<FormInstance>()

const checkEmail = (rule: any, value: any, callback: any) => {
  if (!value) {
    return callback(new Error('請輸入電子郵件地址'))
  }

  axios.get(`/user/checkEmail/${ruleForm.email}`)
    .then(response => {
      if (response.status === 200) {
        callback(new Error('該電子郵件地址已被註冊'));
      } else if (response.status === 404) {
        callback();
      } else {
        callback(new Error('發生錯誤'));
      }
    })

  setTimeout(() => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    if (!emailRegex.test(value)) {
      callback(new Error('請輸入正確的電子郵件地址'))
    } else {
      callback()
    }
  }, 1000)
}

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('請輸入密碼'))
  } else {
    if (ruleForm.checkPass !== '') {
      if (!ruleFormRef.value) return
      ruleFormRef.value.validateField('checkPass', () => null)
    }
    callback()
  }
}

const validatePass2 = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('請再次輸入密碼'))
  } else if (value !== ruleForm.password) {
    callback(new Error('兩次輸入的密碼不相符'))
  } else {
    callback()
  }
}

const checkNickname = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('請輸入暱稱'))
  } else {
    callback()
  }
}

const ruleForm = reactive({
  password: '',
  checkPass: '',
  email: '',
  nickname: '',
})

const rules = reactive<FormRules<typeof ruleForm>>({
  password: [{ validator: validatePass, trigger: 'blur' }],
  checkPass: [{ validator: validatePass2, trigger: 'blur' }],
  email: [{ validator: checkEmail, trigger: 'blur' }],
  nickname: [{ validator:checkNickname, trigger: 'blur' }],
})

const submitForm = async(formEl: FormInstance | undefined) => {
  // 在發送請求前顯示 loading 狀態
  loading.value = true;
  if (!formEl) return
  const valid = new Promise<boolean>((resolve) => {
    formEl.validate((valid) => {
      resolve(valid);
    });
  });

  if (valid) {
    // 使用 DOMpurify 防禦 XSS
    ruleForm.email = DOMPurify.sanitize(ruleForm.email)
    ruleForm.password = DOMPurify.sanitize(ruleForm.password)
    ruleForm.nickname = DOMPurify.sanitize(ruleForm.nickname)

    try {
      const response = await axios.post('/register', ruleForm);
      console.log(response.status);
      if (response.status === 201) {
        ElMessage.success("註冊成功");
        // 登入成功，將 JWT token 存入 localStorage
        console.log(response.data.token);
        localStorage.setItem('Authorization', `Bearer ${response.data.token}`);
        // 導向到/content
        router.push('/content');
      }
    } catch (error) {
      ElMessage.error("註冊失敗");
    } finally {
      // 無論請求成功或失敗，都將 loading 狀態設置為 false
      loading.value = false;
    }
    
  } else {
    return false;
  }
}


const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>