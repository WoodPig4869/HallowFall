<template>
    <div class="post-info">
        <header>
            <div class="container text-center">
                <div class="row">
                    <div class="col-md-4">
                        <p class="fw-bold">文章ID：</p>
                        <p>{{ post.postId }}</p>
                    </div>
                    <div class="col-md-4">
                        <p class="fw-bold">發布日期：</p>
                        <p>{{ post.createdAt }}</p>
                    </div>
                    <div class="col-md-4">
                        <p class="fw-bold">最後編輯於：</p>
                        <p>{{ post.updatedAt }}</p>
                    </div>
                </div>
            </div>
        </header>
        <div id="edit container" class="container m-3">
            <div v-if="postOwner" class="container d-flex justify-content-end align-items-center">
                <button class="btn btn-primary" @click="editorModeToggle()">編輯模式</button>
                <button class="btn btn-danger" @click="deletePost()">刪除文章</button>
            </div>
            <br />

            <div class="w-100 text-center">
                <h1 class="m-2">{{ post.title }}</h1>
            </div>
            <div class="container d-flex justify-content-center align-items-center">
                <el-image :src=post.image fit="cover" class="img-fluid" style="max-width: 500px;" />
            </div>
            <div id="editor">
                <v-md-editor v-model="this.post.content" :mode="this.editorMode" placeholder="請輸入內容..."
                    @save="save()"></v-md-editor>
                <el-upload ref="image" class="upload-demo" :limit="1" :on-change="handleFileChange1">
                    <template #trigger>
                        <el-button type="primary" style="margin: 10px;">上傳封面圖</el-button>
                    </template>
                    <button class="btn btn-success" @click="save()" style="max-width: 20%;">儲存</button>
                    <button class="btn btn-secondary" @click="reloadInfo()" style="max-width: 20%;">取消</button>
                </el-upload>
            </div>
        </div>
        <br /><br />
        <hr />
        <div class="container mt-3">
            <h1 class="m-2">留言板</h1>
            <form @submit.prevent="addComment()">
                <input type="text" class="form-control" placeholder="請輸入留言內容" aria-label="请输入留言内容"
                    aria-describedby="button-addon2" v-model="newComment.content">
                <button class="btn btn-primary" type="button" @click="addComment()"
                    :disabled="(!newComment.content)">提交留言</button>
            </form>
            <div class="container mt-2">
                <div v-for="comment in comments" :key="comment.commentId">
                    <CommentCard :comment="comment" />
                    
                </div>
            </div>

        </div>
    </div>
</template>

<script>
import axios from '@/axios';
import Swal from 'sweetalert2';
import { jwtDecode } from "jwt-decode";
import DOMPurify from 'isomorphic-dompurify';
import CommentCard from '@/components/CommentCard.vue';
import router from '@/router';

export default {
    components: {
        CommentCard,
    },
    data() {
        return {
            postId: parseInt(this.$route.params.postId),
            post: [],
            comments: [],
            newComment: {
                postId: parseInt(this.$route.params.postId),
                content: '',
            },
            isDataDirty: false,
            editorMode: 'preview',
            postOwner: false,

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

    created() {
        this.fetchPost();
        this.fetchComment();
    },

    methods: {
        checkOwner() {
            // 從 localStorage 中取出 JWT
            const jwt = localStorage.getItem('Authorization');
            // 去掉 "Bearer " 字串，只取得 JWT 本身
            const token = jwt ? jwt.replace('Bearer ', '') : null;
            if (!token) {
                return;
            }
            // 解析 JWT
            const decoded = jwtDecode(token);
            const loggedUserId = parseInt(decoded.userId);
            const role = decoded.role;
            if (this.post.userId == loggedUserId || role == "ROOT" || role == "ADMIN") {
                this.postOwner = true;
                // console.log(this.postOwner)
            }
        },
        fetchComment() {
            axios.get(`/comment/${this.postId}`).then(response => {
                this.comments = response.data
                console.log(this.comments)
            })
        },
        fetchPost() {
            axios.get(`/post/${this.postId}`).then(response => {
                this.post = response.data
                this.checkOwner();
            });
        },
        setIsDataDirtyTrue() {
            this.isDataDirty = true;
        },

        handleFileChange1(file) {
            const reader = new FileReader();
            reader.readAsDataURL(file.raw);
            reader.onload = () => {
                this.userInfo.avatar = reader.result;
                upload.value.clearFiles();
            };
            // 設定資料未儲存
            this.setIsDataDirtyTrue();
        },

        // 上傳檔案發生變化觸發事件
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
        toggleEdit() {
            this.editMode = !this.editMode
        },
        async deletePost() {
            Swal.fire({
                title: '刪除文章！',
                text: '您確定刪除這篇文章嗎？',
                icon: 'warning',
                confirmButtonText: '確定',
                showCancelButton: true,
                cancelButtonText: "取消"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`/post/${this.postId}`)
                    router.push({ path: '/posts' })
                };
            })
        },

        async save() {
            console.log(this.post)
            // DOMPurify防禦XSS
            this.post.title = DOMPurify.sanitize(this.post.title);
            this.post.content = DOMPurify.sanitize(this.post.content);
            this.post.image = DOMPurify.sanitize(this.post.image);

            console.log(this.post)
            Swal.fire({
                title: "儲存中...",
                html: "請稍後",
                timerProgressBar: true,
                timer: 2000,
                allowOutsideClick: false,// 防止點擊背景關閉
                allowEscapeKey: false,// 防止按 ESC 關閉
                didOpen: () => {
                    Swal.showLoading();
                    const timer = Swal.getPopup().querySelector(".swal2-progress-bar");
                },
            }).then((result) => {
                /* Read more about handling dismissals below */
                if (result.dismiss === Swal.DismissReason.timer) {
                    console.log("I was closed by the timer");
                }
            });
            try {
                const response = await axios.put('/post', this.post);

                await new Promise((resolve) => setTimeout(resolve, 550));
                const token = response.data.token;
                Swal.fire({
                    title: "儲存成功！",
                    icon: "success",
                    confirmButtonText: "確認",
                    allowOutsideClick: false,
                    allowEscapeKey: false
                });
                // 設定資料已儲存
                this.isDataDirty = false;
            } catch (error) {
                await new Promise((resolve) => setTimeout(resolve, 450));
                Swal.fire({
                    title: "錯誤",
                    text: "伺服器無回應，請聯繫管理員",
                    icon: "error",
                    confirmButtonText: "確認",
                });
            }
        },
        reloadInfo() {
            Swal.fire({
                title: '所有修改將還原！',
                text: '您確定要這麼做嗎？',
                icon: 'warning',
                confirmButtonText: '確定',
                showCancelButton: true,
                cancelButtonText: "取消"
            }).then((result) => {
                if (result.isConfirmed) {
                    this.fetchPost()
                }
            })
        },
        addComment() {
            if (!this.newComment.content) {
                return;
            }
            // DOMPurify防禦XSS
            this.newComment.content = DOMPurify.sanitize(this.newComment.content)

            axios.post('/comment', this.newComment).then(response => {
                window.location.reload();
            })
        },
        editorModeToggle() {
            this.editorMode = this.editorMode === 'preview' ? 'editable' : 'preview';
            this.setIsDataDirtyTrue();
        },
    },
}
</script>
