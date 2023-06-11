<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width:200px;"
                @keyup.enter.native="loadPost">  </el-input>
      <el-select v-model="sex" style="margin-left: 5px" filterable placeholder="请选择性别">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px" @click="loadPost(1) ">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="no" label="账号" width="180">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="180">
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80">
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="80">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.sex === 1 ? 'primary' : 'danger'"
              disable-transitions><i :class="scope.row.sex==1?'el-icon-male':'el-icon-female'"></i>{{scope.row.sex === 1 ? '男' : '女'}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleId" label="角色" width="120">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions><i :class="scope.row.roleId === 0 ? 'el-icon-user-solid' :
              (scope.row.roleId === 1 ? 'el-icon-s-custom' : 'el-icon-user')"></i>{{scope.row.roleId === 0 ? '超级管理员' :
              (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话" width="180">
      </el-table-column>
      <el-table-column prop="isvalid" label="状态" width="180">
        <template slot-scope="scope">
          {{scope.row.isvalid === 'Y' ? '启用' : '禁用'}}
          <el-switch
              v-model= "scope.row.isvalid"
              active-color="#13ce66"
              inactive-color="#ff4949"
              style="margin-left: 30px"
              active-value="Y"
              inactive-value="N"
              @change="change(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px"
          >
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20,30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>

      <el-form ref="form" :model="form" status-icon :rules="rules" label-width="80px">
        <el-form-item label="账号" prop="no">
          <el-col :span="20">
            <el-input v-model="form.no" :disabled="formNo"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input show-password v-model="form.password" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-col :span="20">
            <el-input show-password v-model="form.checkPass" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-col :span="20">
            <el-input v-model.number="form.age"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancle">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>

    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserManage",
  data() {
    let checkAge = (rule, value, callback) => {
      setTimeout(() => {
          if (value < 18 || value > 60) {
            callback(new Error('年龄范围必须在18-60岁'));
          } else{
            callback();
          }
      }, 1000);
    };
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.form.checkPass !== '') {
          this.$refs.form.validateField('checkPass');
        }
        callback();
      }
    };
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.form.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.id){
        return callback();
      }
      this.$axios.get("/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
        if(res.code!=200){
          callback()
        }else{
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      tableData: [],
      pageSize:5,
      pageNum:1,
      total:5,
      name:'',
      sex:'',
      formNo:false,
      sexs:[
        {
          value:'1',
          label:'男'
        },
        {
          value:'0',
          label:'女'
        }
      ],
      centerDialogVisible:false,
      form:{
        id:'',
        no:'',
        name:'',
        password:'',
        checkPass:'',
        age:'',
        phone:'',
        sex:'0',
        roleId:'2'
      },
      rules: {
        no:[
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' },
          { validator: checkDuplicate, trigger: 'blur' }
        ],
        name:[
          { required: true, message: '请输入名字', trigger: 'blur' },
          { min: 2, max: 4, message: '长度在 2 到 4 个字符', trigger: 'blur' },
        ],
        password: [
          {
            required: true,
            trigger: "blur",
            message: "密码不能为空",
          },
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
        ],
        age: [
          {required: true, message: '请输⼊年龄', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "⼿机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger: "blur"}
        ]
      }
    }
  },
  methods:{
    change(row){
      this.$axios.post( '/user/changeUserValid',row).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.$message({
            message:'操作成功！',
            type:'success'
          });
          this.loadPost(this.pageNum);
        }else{
          this.$message({
            message:'操作失败！',
            type:'error'
          });
        }
      })
    },
    resetForm(){
      this.$refs['form'].resetFields();
    },
    cancle(){
      this.centerDialogVisible = false;
      this.resetForm()
    },
    del(id){
      this.$axios.get( '/user/delete?id=' + id ).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.$message({
            message:'删除成功！',
            type:'success'
          });
          this.loadPost(this.pageNum);
        }else{
          this.$message({
            message:'删除失败！',
            type:'error'
          });
        }
      })
    },
    mod(row){
      this.centerDialogVisible=true;
      this.formNo = true;
      this.$nextTick(()=>{
        this.resetForm();
        // 赋值到表单
        this.form.id = row.id;
        this.form.no = row.no;
        this.form.name = row.name;
        this.form.password = '';
        this.form.checkPass='';
        this.form.age = row.age.toString();
        this.form.phone = row.phone;
        this.form.sex = row.sex.toString();
        this.form.roleId = row.roleId;
      })
    },
    add(){
      this.centerDialogVisible=true;
      this.formNo = false;
      this.$nextTick(()=>{
        this.resetForm()
      })
    },
    doSave(){
      this.$axios.post( '/user/save',this.form).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.$message({
            message:'操作成功！',
            type:'success'
          });
          this.centerDialogVisible=false;
          this.loadPost(this.pageNum);
        }else{
          this.$message({
            message:'操作失败！',
            type:'error'
          });
        }
      })
    },
    doMod(){
      this.$axios.post( '/user/update',this.form).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.$message({
            message:'操作成功！',
            type:'success'
          });
          this.centerDialogVisible=false;
          this.loadPost(this.pageNum);
        }else{
          this.$message({
            message:'操作失败！',
            type:'error'
          });
        }
      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else {
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });


    },
    loadPost(num){
      this.pageNum = num,
      this.$axios.post( '/user/listPage',{
        pageSize:this.pageSize,
        pageNum:num,
        param:{
          name:this.name,
          sex:this.sex,
          roleId:'2'
        }
      }).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.tableData = res.data;
          this.total = res.total;
        }else{
          alert('获取数据失败');
        }
      })
    },
    resetParam(){
      this.name='';
      this.sex=''
    },
    handleSizeChange(val) {
      this.pageNum=1
      this.pageSize=val
      this.loadPost(this.pageNum)
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.loadPost(this.pageNum)
    }
  },
  beforeMount() {
    this.loadPost(this.pageNum);
  }
}
</script>

<style scoped>

</style>