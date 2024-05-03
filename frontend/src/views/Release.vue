<template lang="">
    <div>
        <v-md-editor v-model="this.editorData" mode="preview"></v-md-editor>
    </div>
</template>
<script>
export default {
    component: {
    },
    data() {
        return {
            editorData: '',
        }
    },
    mounted() {
        this.loadRelease();
    },
    methods: {
        loadRelease() {
            fetch('/RELEASE.md')
                .then(response => response.text())
                .then(data => {
                    // console.log(data);
                    this.editorData = data;
                })
                .catch(error => {
                    console.error('Failed to load data:', error);
                });
        },

    }
}
</script>
<style lang="">

</style>