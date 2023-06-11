<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入仓库名" suffix-icon="el-icon-search" style="width:200px;"
                @keyup.enter.native="loadPost">  </el-input>

      <el-button type="primary" style="margin-left: 5px" @click="loadPost(1)">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>

      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="仓库名" width="180">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>

      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？将删除仓库中所有物品！！"
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
        <el-form-item label="仓库名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form.remark" autocomplete="off"></el-input>
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
  name: "StorageManage",
  data() {
    let checkname =(rule,value,callback)=>{
      this.$axios.get("/storage/findByName?name="+this.form.name).then(res=>res.data).then(res=>{
        if(res.code!=200){
          callback()
        }else{
          callback(new Error('该仓库名已存在'));
        }
      })
    };
    return {
      tableData: [],
      pageSize:5,
      pageNum:1,
      total:5,
      name:'',
      centerDialogVisible:false,
      form:{
        id:'',
        name:'',
        remark:''
      },
      rules: {
        name:[
          { required: true, message: '请输入仓库名', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ]
      }
    }
  },
  methods:{
    resetForm(){
      this.$refs['form'].resetFields();
    },
    cancle(){
      this.centerDialogVisible = false;
      this.resetForm()
    },
    del(id){
      this.$axios.get('/storage/delete?id=' + id ).then(res=>res.data).then(res=>{
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
      this.$nextTick(()=>{
        this.resetForm();
        // 赋值到表单
        this.form.id = row.id;
        this.form.name = row.name;
        this.form.remark = row.remark;
      })
    },
    add(){
      this.centerDialogVisible=true;
      this.$nextTick(()=>{
        this.resetForm()
      })
    },
    doSave(){
      this.$axios.post('/storage/save',this.form).then(res=>res.data).then(res=>{
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
      this.$axios.post('/storage/update',this.form).then(res=>res.data).then(res=>{
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
      this.$axios.post('/storage/listPage',{
        pageSize:this.pageSize,
        pageNum:num,
        param:{
          name:this.name
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