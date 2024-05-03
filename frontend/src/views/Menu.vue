<template>
    <div>
        <el-button v-model="isToggled" @click="toggleButton">{{ isToggled ? '已開啟' : '已關閉' }}</el-button>
    </div>
</template>
<script>
import { ref } from 'vue'
import { ElMessage } from "element-plus";
export default {
    setup() {
        const isToggled = ref(false);
        
        const toggleButton = () => {
            isToggled.value = !isToggled.value;
            if (isToggled.value) {
                ElMessage.success('已開啟');
            } else {
                ElMessage.info('已關閉');
            }
        }
        return {
            isToggled,
            toggleButton
        }
    }
}
</script>
<style lang="">
    
</style>