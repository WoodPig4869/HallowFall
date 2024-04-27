
import { createApp } from 'vue'
import App from './App.vue'

// import
import router from '@/router'
import axios from '@/axios'

// import markdown
import VueMarkdownEditor from "@kangc/v-md-editor";
import "@kangc/v-md-editor/lib/style/base-editor.css";
import vuepressTheme from "@kangc/v-md-editor/lib/theme/vuepress.js";
import "@kangc/v-md-editor/lib/theme/style/vuepress.css";
import zhTW from "@kangc/v-md-editor/lib/lang/zh-TW";
import Prism from "prismjs";

VueMarkdownEditor.lang.use("zh-TW", zhTW);
VueMarkdownEditor.use(vuepressTheme, {
  Prism
});

// import element+
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// import bootstrap
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

const app = createApp(App)
app.config.productionTip = false 
document.documentElement.lang = 'zh-TW'

app.use(VueMarkdownEditor)
app.use(axios)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
