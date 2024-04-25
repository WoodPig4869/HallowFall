import axios from 'axios';

// 創建axios實例
const instance = axios.create({
    baseURL: 'http://localhost:8080', // 設定預設的URL
    headers: {
        'Content-Type': 'application/json; charset=UTF-8'
    }
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
            window.location.href = '/login';// 導向登入頁
        }
        return Promise.reject(error);
    }
);

export default instance;
