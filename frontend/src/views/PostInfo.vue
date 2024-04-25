<template>
    <div class="post-info">
        <header>
            <el-row>
                <el-col :span="8">文章ID：<br />{{ post.postId }}</el-col>
                <el-col :span="6">發布日期：<br />{{ post.createdAt }}</el-col>
                <el-col :span="6">最後編輯於：<br />{{ post.updatedAt }}</el-col>
            </el-row>
        </header>
        <div id="edit container" v-if="postOwner" class="container m-3">
            <button class="btn btn-primary" @click="toggleEdit()" style="max-width: 20%;">編輯模式</button>
            <button class="btn btn-danger" @click="deletePost()" style="max-width: 20%;">刪除文章</button>
            <br />

            <div id="editor" v-if="editMode" @click="setIsDataDirtyTrue">
                <froala id="edit" :tag="'textarea'" :config="editorConfig" v-model:value="post.content"></froala>
                <el-upload ref="image" class="upload-demo" :limit="1" :on-change="handleFileChange1">
                    <template #trigger>
                        <el-button type="primary" style="margin: 10px;">上傳封面圖</el-button>
                    </template>
                    <button class="btn btn-success" @click="save()" style="max-width: 20%;">儲存</button>
                    <button class="btn btn-secondary" @click="reloadInfo()" style="max-width: 20%;">取消</button>
                </el-upload>
            </div>
        </div>

        <body class="container m-5">
            <h1 class="m-2">{{ post.title }}</h1>
            <el-image style="width: auto; height: 200px" :src=post.image fit="cover" />
            <br /><br />
            <froalaView v-model:value="post.content"></froalaView>
        </body>
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
                <template v-for="comment in comments" :key="comment.comment_id">
                    <el-card class="m-3" style="width: 100%;">
                        <el-row justify="space-between">
                            <el-col :span>
                                <a :href="'/posts/' + comment.userId">{{ comment.nickname }}</a>
                            </el-col>

                            <el-col :h1="10"><br />{{ comment.content }}</el-col>
                            <el-col :span="3"><br /></el-col>
                            <el-col :span="6">發布日期：<br />{{ comment.createdAt }}</el-col>
                        </el-row>
                    </el-card>
                </template>
            </div>

        </div>
    </div>
</template>

<script>
import axios from '@/axios';
import Swal from 'sweetalert2';
import { jwtDecode } from "jwt-decode";
import DOMPurify from 'isomorphic-dompurify';

export default {
    data() {
        return {
            postId: parseInt(this.$route.params.postId),
            post: [],
            comments: [],
            newComment: {
                postId: parseInt(this.$route.params.postId),
                content: '',
            },
            editorConfig: {
                toolbarButtons: {
                    'moreText': {
                        'buttons': ['italic', 'underline', 'bold', 'strikeThrough', 'subscript', 'superscript', 'fontFamily', 'fontSize', 'textColor', 'backgroundColor', 'inlineClass', 'inlineStyle', 'clearFormatting']
                    },
                    'moreParagraph': {
                        'buttons': ['alignLeft', 'alignCenter', 'formatOLSimple']
                    },
                    'moreRich': {
                        'buttons': ['insertLink', 'insertImage', 'insertVideo', 'insertTable', 'emoticons', 'fontAwesome', 'specialCharacters', 'embedly', 'insertFile', 'insertHR']
                    },
                    'moreMisc': {
                        'buttons': ['undo', 'redo', 'fullscreen', 'print', 'getPDF', 'spellChecker', 'selectAll', 'html', 'help'],
                        'align': 'right',
                        'buttonsVisible': 2
                    }
                }
            },
            isDataDirty: false,
            editMode: false,
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
            if (this.post.userId == loggedUserId || role == "ROOT") {
                this.postOwner = true;
                // console.log(this.postOwner)
            }
        },
        fetchComment() {
            axios.get(`/comment/${this.postId}`).then(response => {
                this.comments = response.data
                // console.log(this.comments)
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
                    window.location.href = '/posts'
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
        }
    },
}
</script>
