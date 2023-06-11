<template>
  <div style="display: flex;line-height: 60px;">
    <div style="margin-top: 8px;">
      <i :class="icon" style="font-size: 20px;cursor: pointer" @click="collapse"></i>
    </div>
    <div style="flex: 1;text-align: center;font-size: 34px;">
      <span>欢迎来到仓库管理系统</span>
    </div>
    <el-dialog
        title="修改密码"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
    <el-form ref="form" :model="form" status-icon :rules="rules" label-width="100px">
      <el-form-item label="账号" prop="no">
        <el-col :span="20">
          <el-input v-model="form.no" readonly></el-input>
        </el-col>
      </el-form-item>

      <el-form-item label="当前密码" prop="oldPassword">
        <el-col :span="20">
          <el-input show-password v-model="form.oldPassword" autocomplete="off"></el-input>
        </el-col>
      </el-form-item>

      <el-form-item label="新密码" prop="password">
        <el-col :span="20">
          <el-input show-password v-model="form.password" autocomplete="off"></el-input>
        </el-col>
      </el-form-item>

      <el-form-item label="确认新密码" prop="checkPass" >
        <el-col :span="20">
          <el-input show-password v-model="form.checkPass" autocomplete="off"></el-input>
        </el-col>
      </el-form-item>

    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="cancle">取 消</el-button>
        <el-button type="primary" @click="mod">确 定</el-button>
      </span>

    </el-dialog>

    <el-dropdown>
      <span>
        <el-avatar style="margin-right: 5px" :src="user.roleId === 0 ? Url1 : Url2 " />
        {{user.name}}<i class="el-icon-arrow-down" style="margin-left: 5px;"></i></span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="modPassword">修改密码</el-dropdown-item>
        <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: "Header",
  data(){
    let checkOldPassword =(rule,value,callback)=>{
      this.userinfo.id = this.form.id;
      this.userinfo.password = this.form.oldPassword;
      this.$axios.post("/user/checkPassword",this.userinfo).then(res=>res.data).then(res=>{
        if(res.code == 200){
          callback()
        }else{
          callback(new Error('当前密码验证错误'));
        }
      })
    };
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else {
        if (this.form.password === this.form.oldPassword){
          callback(new Error("新密码不能跟当前密码一致！"))
        }
        if (this.form.checkPass !== '') {
          this.$refs.form.validateField('checkPass');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'));
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return{
      user : JSON.parse(window.sessionStorage.getItem('CurUser')),
      userinfo:{
        id:'',
        password:''
      },
      Url1:"https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
      Url2:"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202103%2F23%2F20210323142848_6a340.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1674380588&t=8e23db1cd7a8babb2c59ce552cee26ab",
      centerDialogVisible:false,
      form:{
        id:'',
        no:'',
        oldPassword:'',
        password:'',
        checkPass:''
      },
      rules: {
        oldPassword:[
          { required: true, message: '请输入当前密码', trigger: 'blur' },
          { validator: checkOldPassword, trigger: 'blur' }
        ],
        password: [
          {
            //插入正则验证：大小写、数字、至少8位、不常用字符
            pattern:
                /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[.$@#!%^*?&+-])[A-Za-z\d.$@#!%^*?&+-]{8,}/,
            message: "密码应当至少8位且含有数字、大小写字母及特殊字符",
          },
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  props:{
    icon:String
  },
  methods:{
    toUser(){
      this.$router.push("/Home")
    },
    logout(){
      this.$confirm('您确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',  //确认按钮的文字显示
        type: 'warning',
        center: true, //文字居中显示

      }).then(() => {
            this.$axios.post('/user/loginout',this.user).then(res=>res.data).then(res=>{
              if (res.code==200){
                this.$message({
                  type:'success',
                  message:'退出登录成功'
                })
                this.$router.push("/")
                sessionStorage.clear()
              }else{
                this.$message({
                  type:'error',
                  message:'退出登录失败'
                })
              }
            });
          })
          .catch(() => {
            this.$message({
              type:'info',
              message:'已取消退出登录'
            })
          })
    },
    resetForm(){
      this.$refs['form'].resetFields();
    },
    cancle(){
      this.centerDialogVisible = false;
      this.resetForm()
    },
    modPassword(){
      this.centerDialogVisible = true;
      this.$nextTick(()=>{
        this.resetForm()
        this.form.id = this.user.id;
        this.form.no = this.user.no;
      })
    },
    mod(){
      this.$axios.post('/user/update',this.form).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.$message({
            message:'修改成功，请重新登录！',
            type:'success'
          });
          this.centerDialogVisible=false;
          router.push("/")
          sessionStorage.clear()
        }else{
          this.$message({
            message:'操作失败！',
            type:'error'
          });
        }
      })
    },
    collapse(){
      this.$emit('doCollapse')
    }
  },
  created(){
    this.$router.push("/Home")
  }
}
</script>

<style scoped>

</style>