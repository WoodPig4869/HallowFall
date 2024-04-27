import axios from 'axios';
import router from '@/router';
import { ElMessage } from "element-plus";

// 環境變數：開發環境為dev，生產環境為prod
const baseURL =
  import.meta.env.MODE === "production"
    ? import.meta.env.VITE_BASE_URL_PROD
    : import.meta.env.VITE_BASE_URL_DEV;

// 創建axios實例
const instance = axios.create({
  baseURL: baseURL,
  headers: {
    "Content-Type": "application/json; charset=UTF-8",
  },
  timeout: 10000, // 設定請求超時時間，單位為毫秒 (10秒)
});

// 請求攔截器：在請求發送前執行
instance.interceptors.request.use(
    (config) => {
        // 可在這裡添加請求攔截的處理邏輯
        const token = localStorage.getItem('Authorization');
        if (token) {
            config.headers.Authorization = `${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 回應攔截器：在回應回來後執行
instance.interceptors.response.use(
    (response) => {
        // statusCode: 200-299
        return response;
    },
    (error) => {
        // statusCode: 400-599
        if (error.response.status === 401) {
            // 401 代表尚未登入，或登入憑證過期
            ElMessage.warning("請登入");
            // 重新導向到登入頁
            router.push("/login");
        }else if (error.response.status === 403) {
            ElMessage.warning("權限不足，請聯繫管理員");
        }
        if (error.code === "ECONNABORTED") {
          // 請求超時處理
          ElMessage.error("請求超時");
        }
        return Promise.reject(error);
    }
);

export default instance;
