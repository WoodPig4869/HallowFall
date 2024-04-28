<template>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8">

                <div class="card">
                    <div class="card-header">個人資料</div>
                    <div class="d-flex align-items-center justify-content-center mb-3 mt-4">
                        <div class="avatar-container">
                            <el-avatar id="avatarImg" :src="userInfo.avatar"
                                class="rounded-circle border border-dark me-2" style="width: auto; height: 160px;"
                                fit="cover" />
                        </div>
                        <div class="d-flex flex-column" style="max-width: 75%;">
                            <div class="mb-3">
                                <el-input v-model="userInfo.email" @change="setIsDataDirtyTrue" disabled>
                                    <template #prepend>電子郵箱</template>
                                </el-input>
                            </div>
                            <div class="mb-3">
                                <el-input v-model="userInfo.registrationDate" disabled>
                                    <template #prepend>註冊時間</template>
                                </el-input>
                            </div>
                            <div class="mb-3">
                                <el-input v-model="userInfo.nickname" @change="setIsDataDirtyTrue">
                                    <template #prepend>用戶暱稱</template>
                                </el-input>
                            </div>
                            <div class="mb-3">
                                <div class="mb-3 ">
                                    <el-input v-model="userInfo.phone">
                                        <template #prepend>手機號碼</template>
                                    </el-input>
                                </div>

                            </div>
                        </div>
                        <el-image :src="userInfo.coverImage" class="me-3 d-none d-sm-block" fit="fill"
                            style="max-width: 40%; max-height: 300px ;margin: 5px; border-radius: 10px;" />
                    </div>
                    <!-- 適應小型螢幕，顯示在容器外面 -->
                    <el-image :src="userInfo.coverImage" class="me-3 d-block d-sm-none" fit="fill"
                        style="max-width: 100%; max-height: 300px ;margin: 5px; border-radius: 10px;" />
                    <div class="card-body">
                        <div class="mb-3" id="signature" @click="setIsDataDirtyTrue">
                            <h5>個人簽名</h5>
                            <v-md-editor v-model="this.editorData" :mode="this.editorMode" placeholder="請輸入內容..."
                                @save="save"></v-md-editor>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="container d-flex justify-content-center align-items-center">
            <el-upload ref="upload-avatar" class="upload-demo" :limit="1" :on-change="handleFileChange1">
                <template #trigger>
                    <el-button type="primary" style="margin: 10px;">上傳頭像</el-button>
                </template>
            </el-upload>
            <el-upload ref="upload-cover" class="upload-demo" :limit="1" :on-change="handleFileChange2">
                <template #trigger style="max-width: 15%;">
                    <el-button type="primary" style="margin: 10px;">上傳封面照片</el-button>
                </template>
            </el-upload>
            <button class="btn btn-success" @click="save()" style="max-width: 20%;">儲存</button>
            <button class="btn btn-secondary" @click="editorModeToggle()" style="max-width: 20%;">編輯模式</button>
            <button class="btn btn-danger" @click="resetPassword()" style="max-width: 30%;">重設密碼</button>
        </div>

    </div>
    <div class="container mt-3 d-flex justify-content-center">
        <el-table :data="tableData" border style="max-width: 600px;" class="mt-5">
            <el-table-column prop="time" label="紀錄時間" width="180" align="center" />
            <el-table-column prop="status" label="操作類型" width="180" />
            <el-table-column prop="ipAddress" label="IP位置" />
        </el-table>
    </div>

</template>

<script>
import { defineComponent, reactive } from 'vue'

import axios from '@/axios';
import Swal from 'sweetalert2';

// DOMPurify
import DOMPurify from 'isomorphic-dompurify';

export default defineComponent({
    beforeDestroy() {
        window.removeEventListener('beforeunload', this.handleBeforeUnload);
    },
    setup() {

        const userInfo = reactive({
            phone: '',
            signature: '<p></p>',
            nickname: '',
            registrationDate: '',
            avatar: null,
            coverImage: null,
            email: '',
        });
        return {
            userInfo
        }
    },
    created() {
        axios.get('/user/self').then(response => {
            this.userInfo.phone = response.data.phone
            this.userInfo.signature = response.data.signature
            this.userInfo.nickname = response.data.nickname
            this.userInfo.avatar = response.data.avatar
            this.userInfo.coverImage = response.data.coverImage
            this.userInfo.registrationDate = response.data.registrationDate
            if (this.userInfo.avatar == null) {
                this.userInfo.avatar = '/avatar.png'
            }
            if (this.userInfo.coverImage == null) {
                this.userInfo.coverImage = '/cover.jpg'
            }
            this.userInfo.email = response.data.email

            this.initEditor()
        });
        axios.get('/userlog').then(response => {
            this.tableData = response.data
        })
    },
    mounted() {
        this.isDataDirty = false
    },
    data() {
        return {
            editorMode: 'preview',
            editorData: '',
            tableData: [],
            isDataDirty: false,

        };
    },

    // 離開頁面前觸發提示
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
        // 初始化CKEditor
        initEditor() {
            this.editorData = this.userInfo.signature
        },

        // 設定資料未儲存
        setIsDataDirtyTrue() {
            this.isDataDirty = true;
        },

        // 上傳檔案發生變化觸發事件
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
        handleFileChange2(file) {
            const reader = new FileReader();
            reader.readAsDataURL(file.raw);
            reader.onload = () => {
                this.userInfo.coverImage = reader.result;
                upload.value.clearFiles();
            };
            // 設定資料未儲存
            this.setIsDataDirtyTrue();
        },


        async save() {
            // DOMPurify防禦XSS
            this.userInfo.signature = DOMPurify.sanitize(this.editorData);
            this.userInfo.nickname = DOMPurify.sanitize(this.userInfo.nickname);
            this.userInfo.email = DOMPurify.sanitize(this.userInfo.email);
            this.userInfo.avatar = DOMPurify.sanitize(this.userInfo.avatar);
            this.userInfo.coverImage = DOMPurify.sanitize(this.userInfo.coverImage);

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
                const response = await axios.put('/user/self', {
                    signature: this.editorData,
                    nickname: this.userInfo.nickname,
                    avatar: this.userInfo.avatar,
                    coverImage: this.userInfo.coverImage,
                    email: this.userInfo.email
                });

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
        async resetPassword() {
            const { value: password } = await Swal.fire({
                title: "輸入新密碼",
                input: "password",
                inputLabel: "Password",
                inputPlaceholder: "密碼",
                inputAttributes: {
                    maxlength: "10",
                    autocapitalize: "off",
                    autocorrect: "off"
                },
                showCancelButton: true, // 顯示取消按鈕
                confirmButtonText: "確認", // 確認按鈕文本
                confirmButtonColor: "#3085d6", // 確認按鈕顏色
                cancelButtonText: "取消" // 取消按钮文本
            });

            if (password) {
                const { value: confirmPassword } = await Swal.fire({
                    title: "確認密碼",
                    input: "password",
                    inputLabel: "Confirm Password",
                    inputPlaceholder: "請再次確認密碼",
                    inputAttributes: {
                        maxlength: "10",
                        autocapitalize: "off",
                        autocorrect: "off"
                    },
                    showCancelButton: true,
                    confirmButtonText: "確認",
                    confirmButtonColor: "#3085d6",
                    cancelButtonText: "取消"
                });

                if (confirmPassword) {
                    if (password === confirmPassword) {
                        // 密碼匹配
                        Swal.fire({
                            title: "儲存中...",
                            html: "請稍後",
                            timerProgressBar: true,
                            timer: 500,
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
                            const response = await axios.put('/user/self', {
                                password: `${password}`

                            })

                            await new Promise((resolve) => setTimeout(resolve, 450));
                            const token = response.data.token;
                            Swal.fire({
                                title: "密碼修改成功！",
                                text: "請重新登入",
                                icon: "success",
                                confirmButtonText: "確認",
                                allowOutsideClick: false,
                                allowEscapeKey: false
                            }).then((result) => {
                                if (result.isConfirmed) {
                                    localStorage.removeItem('Authorization');
                                    window.location.href = '/login';
                                }
                            });


                        } catch (error) {
                            await new Promise((resolve) => setTimeout(resolve, 450));
                            Swal.fire({
                                title: "錯誤",
                                text: "伺服器無回應，請聯繫管理員",
                                icon: "error",
                                confirmButtonText: "確認",
                            });
                        }
                    } else {
                        Swal.fire("Error", "密碼輸入不一致，請重新輸入", "error");
                    }
                }
            }

        },
        editorModeToggle() {
            this.editorMode = this.editorMode === 'preview' ? 'editable' : 'preview';
            console.log(this.editorMode);
        },
        // next funtion
    },

   
});

</script>

<style></style>