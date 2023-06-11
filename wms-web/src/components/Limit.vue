<template>

</template>

<script>
export default {
  name: "Limit",
  created(){
    let _this = this
    this.$alert('当前用户已被禁用！！','提示',{
        confirmButtonText:'确定'
    }).then((res)=>{
        sessionStorage.removeItem('CurUser')
        _this.$router.replace({path:'/'})
    })
  }
}
</script>

<style scoped>

</style>