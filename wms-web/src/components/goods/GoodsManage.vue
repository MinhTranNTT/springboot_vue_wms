<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width:200px;"
                @keyup.enter.native="loadPost">  </el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>

      <el-select v-model="goodstype" placeholder="请选择分类" style="margin-left: 5px">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>


      <el-button type="primary" style="margin-left: 5px" @click="loadPost(1)">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add" v-if="user.roleId!=2">新增</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="inGoods" v-if="user.roleId!=2">入库</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="outGoods" v-if="user.roleId!=2">出库</el-button>
    </div>
    <div style="margin-top: 10px;margin-bottom: 10px">
      <el-button @click="setCurrent()" plain v-if="user.roleId!=2">取消选择</el-button>
    </div>
    <el-table
              :data="tableData"
              ref="singleTable"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="name" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" width="120" :formatter="formatStorage">
      </el-table-column>
      <el-table-column prop="goodstype" label="分类" width="120" :formatter="formatGoodstype">
      </el-table-column>
      <el-table-column prop="count" label="数量" sortable width="120">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
      </el-table-column>


      <el-table-column prop="operate" label="操作" v-if="user.roleId!=2">
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
        title="新增物品"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>

      <el-form ref="form" :model="form" status-icon :rules="rules" label-width="80px">
        <el-form-item label="仓库" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" placeholder="请选择仓库" style="margin-left: 5px">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="分类" prop="goodstype">
          <el-col :span="20">
            <el-select v-model="form.goodstype" placeholder="请选择分类" style="margin-left: 5px">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="物品名" prop="name">
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

    <el-dialog
        title="出入库"
        :visible.sync="inDialogVisible"
        width="30%"
        center>
      <el-dialog
          width="60%"
          title="用户选择"
          :visible.sync="innerVisible"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
          <span slot="footer" class="dialog-footer">
          <el-button @click="innerVisible=false">取 消</el-button>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
        </span>
      </el-dialog>


      <el-form ref="form2" :model="form2" :rules="rules2" label-width="80px">
        <el-form-item label="物品名">
          <el-col :span="20">
            <el-input v-model="form2.goodsname" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人" prop="username">
          <el-col :span="20">
            <el-input v-model="form2.username" readonly :show-message="false" @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form2.count"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input type="textarea" v-model="form2.remark" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancleInGoods">取 消</el-button>
        <el-button type="primary" @click="doInGoods">确 定</el-button>
      </span>

    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "../user/SelectUser";
export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule, value, callback) => {
      if(value>9999){
        callback(new Error('数量输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkname =(rule,value,callback)=>{
      if(this.form.storage == '' || this.form.goodstype == ''){
          this.form.name = '';
         return callback(new Error('请先选择仓库和分类再填物品名'));
      }
      this.$axios.post("/goods/findGoods",{
         name:this.form.name,
         storage:this.form.storage,
         goodstype:this.form.goodstype
      }).then(res=>res.data).then(res=>{
        if(res.code!=200){
          callback()
        }else{
          callback(new Error('该仓库已存在此物品！'));
        }
      })
    };
    return {
      user : JSON.parse(window.sessionStorage.getItem('CurUser')),
      goodstypeData:[],
      storageData:[],
      tableData: [],
      pageSize:5,
      pageNum:1,
      total:5,
      name:'',
      storage: '',
      goodstype: '',
      centerDialogVisible:false,
      inDialogVisible:false,
      innerVisible:false,
      currentRow:{},
      tempUser:{},
      form:{
        id:'',
        name:'',
        storage:'',
        goodstype:'',
        count:'0',
        remark:''
      },
      form2:{
        goods:'',
        goodsname:'',
        count:'',
        username:'',
        userid:'',
        adminid:'',
        remark:'',
        action:'1'
      },
      rules: {
        name:[
          { required: true, message: '请输入物品名', trigger: 'blur' },
          { validator: checkname, trigger: 'blur' }
        ],
        goodstype:[
          { required: true, message: '请选择分类', trigger: 'blur' }
        ],
        storage:[
          { required: true, message: '请选择仓库', trigger: 'blur' }
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
      },
      rules2:{
        username: [
          {required: true, message: '请选择申请人', trigger: 'blur'},
        ],
        count:[
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
      this.currentRow = {}
    },
    doSelectUser(val){
      this.tempUser = val
    },
    confirmUser(){
      if(this.tempUser != null) {
        this.resetInForm();
        this.form2.username = this.tempUser.name;
        this.form2.userid = this.tempUser.id;
        this.innerVisible = false;
      }else {
        this.$message({
          message:'请选择申请人',
          type:'error'
        });
      }
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    formatStorage(row){
      let temp = this.storageData.find(item=>{
        return item.id == row.storage
      })
      return temp && temp.name
    },
    formatGoodstype(row){
      let temp = this.goodstypeData.find(item=>{
        return item.id == row.goodstype
      })
      return temp && temp.name
    },
    resetForm(){
      this.$refs['form'].resetFields();
    },
    resetInForm(){
      this.$refs['form2'].resetFields();
    },
    cancle(){
      this.centerDialogVisible = false;
      this.resetForm()
    },
    cancleInGoods(){
      this.inDialogVisible =false;
      this.resetInForm();
    },
    del(id){
      this.$axios.get( '/goods/delete?id=' + id ).then(res=>res.data).then(res=>{
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
        this.form.storage=row.storage;
        this.form.goodstype=row.goodstype;
        this.form.count=row.count;
        this.form.remark = row.remark;
      })
    },
    add(){
      this.setCurrent();
      this.centerDialogVisible=true;
      this.$nextTick(()=>{
        this.resetForm()
      })
    },
    inGoods(){
      if(!this.currentRow.id){
        alert("请选择物品！");
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(()=>{
        this.resetInForm()
      })
      this.form2.goodsname = this.currentRow.name;
      this.form2.goods = this.currentRow.id;
      this.form2.adminId = this.user.id;
      this.form2.action='1';
    },
    outGoods(){
      if(!this.currentRow.id){
        alert("请选择记录");
        return;
      }
      this.inDialogVisible=true;
      this.$nextTick(()=>{
        this.resetInForm()
      })
      this.form2.goodsname = this.currentRow.name;
      this.form2.goods = this.currentRow.id;
      this.form2.adminId = this.user.id;
      this.form2.action='2';
    },
    selectUser(){
      this.innerVisible=true;
    },
    doSave(){
      this.$axios.post( '/goods/save',this.form).then(res=>res.data).then(res=>{
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
      this.$axios.post( '/goods/update',this.form).then(res=>res.data).then(res=>{
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
    doInGoods(){
      this.$refs.form2.validate((valid) => {
        if (valid) {
          this.$axios.post( '/record/save',this.form2).then(res=>res.data).then(res=>{
            if(res.code == 200){
              this.$message({
                message:"操作成功！",
                type:'success'
              });
              this.inDialogVisible=false;
              this.loadPost(this.pageNum);
            }else{
              this.$message({
                message:res.msg,
                type:'error'
              });
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    loadPost(num){
      this.pageNum = num,
      this.$axios.post( '/goods/listPage',{
        pageSize:this.pageSize,
        pageNum:num,
        param:{
          name:this.name,
          goodstype: this.goodstype.toString(),
          storage: this.storage.toString()
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
    loadStorage(){
      this.$axios.get( '/storage/list',).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.storageData = res.data;
        }else{
          alert('获取数据失败');
        }
      })
    },
    loadGoodstype(){
      this.$axios.get( '/goodstype/list',).then(res=>res.data).then(res=>{
        if(res.code == 200){
          this.goodstypeData = res.data;
        }else{
          alert('获取数据失败');
        }
      })
    },
    resetParam(row){
      this.name='';
      this.storage='';
      this.goodstype='';
    },
    handleSizeChange(val) {
      this.pageNum=1;
      this.pageSize=val;
      this.loadPost(this.pageNum);
    },
    handleCurrentChange(val) {
      this.pageNum = val;
      this.loadPost(this.pageNum);
    }
  },
  beforeMount() {
    this.loadStorage();
    this.loadGoodstype();
    this.loadPost(this.pageNum);
  }
}
</script>

<style scoped>

</style>