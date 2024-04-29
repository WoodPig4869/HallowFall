<template>
    <div v-loading="isLoading" @click="setIsDataDirtyTrue" class="container m-3">
        <el-image v-if="(post.image)" style="width: auto; height: 200px" :src=post.image fit="cover" />
        <label for="title" class="form-label"></label>
        <input type="text" v-model="post.title" class="form-control m-4" id="title" placeholder="請輸入標題"
            style="width: 98%;">
        <div class="container m-3">
            <v-md-editor v-model="this.editorData" placeholder="請輸入內容..."
                @save="save"></v-md-editor>
        </div>
        <el-upload ref="image" class="upload-demo" :limit="1" :on-change="handleFileChange1">
            <template #trigger>
                <el-button type="primary" style="margin: 10px;">上傳封面圖</el-button>
            </template>
            <button class="btn btn-success" @click="save()" style="max-width: 20%;">儲存</button>
        </el-upload>
        <froalaView v-model:value="post.content"></froalaView>
    </div>
</template>

<script>
import axios from '@/axios';
import DOMPurify from 'isomorphic-dompurify';
import { ElMessage } from "element-plus";
import router from '@/router';


export default {
    data() {
        return {
            isLoading: false,
            isDataDirty: false,
            post: {
                'title': '',
                'content': '',
                'image': ''
            },

        }
    },
    beforeRouteLeave(to, from, next) {
        if (this.isDataDirty) { // 判斷是否有資料未儲存
            const answer = window.confirm('您的資料可能尚未保存，確定要離開？');
            if (!answer) {
                next(false); // 取消訪問
                return;
            }
        }
        next(); // 繼續訪問
    },
    methods: {
        setIsDataDirtyTrue() {
            this.isDataDirty = true;
        },
        async save() {
            this.isLoading = true;
            // DOMPurify防禦XSS
            this.post.title = DOMPurify.sanitize(this.post.title);
            this.post.content = DOMPurify.sanitize(this.post.content);
            this.post.image = DOMPurify.sanitize(this.post.image);
            // console.log(this.post)
            try {
                const response = await axios.post('/post', this.post);
                if (response.status === 201) {
                    console.log(response.status);
                    console.log(response.status);
                    console.log(response.status);
                    // 設定資料已儲存
                    this.isDataDirty = false;
                    // 顯示成功提示
                    ElMessage.success("發文成功");
                    // 導向到 /posts
                    router.push({ path: '/posts' }).then(() => {
                        location.reload();
                    });
                }
            } catch (error) {
                console.error('Error posting:', error);
            }
        },
        handleFileChange1(file) {
            const reader = new FileReader();
            reader.readAsDataURL(file.raw);
            reader.onload = () => {
                this.post.image = reader.result;
                upload.value.clearFiles();
            };
            // 設定資料未儲存
            this.setIsDataDirtyTrue();
        },

    },
}
</script>