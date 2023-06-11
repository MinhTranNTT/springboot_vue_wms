<template>
    <div class="loginBody"
         v-loading="loading"
         element-loading-text="正在登录中"
         element-loading-spinner="el-icon-loading"
         element-loading-background="rgba(0, 0, 0, 0.8)"
         style="width: 100%"
    >
      <div class="avatar-box">
        <img src="../assets/bg.jpg" alt="">
      </div>
        <div class="loginDiv">
          <div class="login-content">
                <h2 class="login-title">Welcome 仓库管理系统</h2>
                <el-form :model="loginForm" label-width="50px"
                         :rules="rules" ref="loginForm">
                    <el-form-item prop="no" class="item">
                        <el-input style="width: 300px;" type="text" v-model="loginForm.no" placeholder="账号" prefix-icon="el-icon-user"
                                  autocomplete="off" size="small" class="input"></el-input>
                    </el-form-item>

                    <el-form-item prop="password">
                        <el-input style="width: 300px;" type="password" v-model="loginForm.password" placeholder="密码" prefix-icon="el-icon-lock"
                                  show-password autocomplete="off" size="small" class="input" @keyup.enter.native="confirm"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button style="width: 300px" type="primary" @click="confirm" :disabled="confirm_disabled">登 录</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>


<script>
    export default {
        name: "Login",
        data(){
            return{
                confirm_disabled:false,
                loading:false,
                msg:'',
                loginForm:{
                    no:'',
                    password:''
                },
                rules:{
                    no: [
                        { required: true, message: '请输入账号', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输密码', trigger: 'blur' }
                    ],
                }
            }
        },
        methods:{

            confirm(){
                this.confirm_disabled=true;
                this.loading = true;
                this.$refs.loginForm.validate((valid) => {
                    if (valid) { //valid成功为true，失败为false
                        //去后台验证用户名密码
                        this.$axios.post('/user/login',this.loginForm).then(res=>res.data).then(res=>{
                            this.msg = res.msg
                            if(res.code==200){
                                //存储
                                sessionStorage.setItem('CurUser',JSON.stringify(res.data.user));
                                this.$store.commit("setMenu",res.data.menu);
                                this.$message({
                                  type:'success',
                                  message:'登录成功！',
                                  showClose: true
                                });
                                //跳转到主页
                                this.$router.replace('/Index');
                            }else{
                                this.confirm_disabled=false;
                                this.$message({
                                  type:'error',
                                  message:this.msg,
                                  showClose: true
                                })
                                this.loading = false;
                                return false;
                            }
                        });
                    } else {
                        this.confirm_disabled=false;
                        this.loading = false;
                        console.log('校验失败');
                        return false;
                    }
                });

            }
        }
    }
</script>

<style lang="scss">
    .loginBody {
      width: 100%;
      height: 100%;
      min-width: 1000px;
      background-image: url("../assets/login.jpg");
      background-size: 100% 100%;
      background-position: center center;
      overflow: auto;
      background-repeat: no-repeat;
      position: fixed;
      line-height: 100%;
      padding-top: 150px;
      color: black;
    }

    .loginDiv {
      width: 420px;
      height: 300px;
      transform: translate(-50%);
      margin-left: 50%;
    }
    .login-title {
      margin-bottom: 20px;
      line-height: 50px;
      text-align: center;
      font-size: 30px;
      font-weight: bolder;
      color: white;
      text-shadow: 2px 2px 4px #000000;
    }
    .login-content {
        width: 400px;
        height: 250px;
        position: absolute;
        top: 25px;
        left: 25px;
    }
    .avatar-box {
         margin: 0 auto;
         width: 120px;
         height: 120px;
         border-radius: 50%;
         border: 1px solid #409eff;
         box-shadow: 0 0 10px #409eff;
         position: relative;
         bottom: 20px;
    }
    img{
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }
    .input.el-input--small .el-input__inner{
      background: transparent;
      border: 1px solid #878383;
      -webkit-appearance: none;
      border-radius: 0px;
      height: 47px;
      color: white;
      font-size: 15px;
    }

</style>