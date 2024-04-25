
import { createApp } from 'vue'
import App from './App.vue'

// import
import router from '@/router'
import axios from '@/axios'

// import Froala
import "froala-editor/js/plugins.pkgd.min.js";
import "froala-editor/css/froala_editor.pkgd.min.css";
import "froala-editor/css/froala_style.min.css";
import VueFroala from "vue-froala-wysiwyg";

// import element+
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// import bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

const app = createApp(App)
app.config.productionTip = false 
document.documentElement.lang = 'zh-TW'
app.config.globalProperties.$axios = axios

app.use(router)
app.use(VueFroala);
app.use(ElementPlus)
app.mount('#app')
