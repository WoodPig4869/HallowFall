<template>
  <el-card style="max-width: 300px">
    <h3 style="text-align: center;">{{ item.title }}</h3>
    <hr />
    <img :src="item.imgSrc" style="width: 100%" />
    <div style="display: flex; justify-content: center; margin-top: 10px;margin-bottom: 15px;">
      <el-button type="primary" plain @click="item-method" v-if="!item.btn">借閱</el-button>
      <el-button type="info" plain disabled v-else>暫無庫存</el-button>
    </div>
    </el-card>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    item: {
      type: Object,
      required: true,
      default: () => ({
        id: 0,
        title: '',
        imgSrc: '',
        btn: false
      })
    }
  },
  mounted() {
    // 在這裡可以發送 Axios GET 請求獲取更詳細的書籍資訊，或者從父組件中直接接收到完整的 item 物件
  },
  methods: {
    async borrowBook() {
      try {
        const response = await axios.post(`/item/${this.book.id}`);
        // 更新 屬性等操作
      } catch (error) {
        console.error('Error item:', error);
      }
    }
  }
};
</script>
