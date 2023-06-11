<template>
  <div>
    <div style="margin-bottom: 5px">
      <el-input v-model="name" placeholder="请输入物品名" suffix-icon="el-icon-search" style="width:200px;"
                @keyup.enter.native="loadPost">  </el-input>
      <el-select v-model="storage" placeholder="请选择仓库" style="margin-left: 5px" >
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
      <el-button type="danger" @click="del" v-if="user.roleId == 0">删除</el-button>
    </div>
    <el-table
        ref="multipleTable"
        tooltip-effect="dark"
        :data="tableData"
        @selection-change="handleSelectionChange"
        :header-cell-style="{background:'#f3f6fd',color:'#555'}"
        border
    >
      <el-table-column
          type="selection"
          show-overflow-tooltip
          width="40"
          v-if="user.roleId == 0">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="60">
      </el-table-column>
      <el-table-column prop="goodsname" label="物品名" width="180">
      </el-table-column>
      <el-table-column prop="storagename" label="仓库" width="100" >
      </el-table-column>
      <el-table-column prop="goodstypename" label="分类" width="100">
      </el-table-column>
      <el-table-column prop="adminname" label="操作人" width="120">
      </el-table-column>
      <el-table-column prop="username" label="申请人" width="120">
      </el-table-column>
      <el-table-column prop="count" label="数量" sortable width="100">
      </el-table-column>
      <el-table-column prop="createtime" label="操作时间" sortable width="150">
      </el-table-column>
      <el-table-column prop="remark" label="备注">
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
  </div>
</template>

<script>
export default {
  name: "RecordManage",
  data() {
    return {
      multipleSelection: [],
      user : JSON.parse(window.sessionStorage.getItem('CurUser')),
      goodstypeData:[],
      storageData:[],
      tableData: [],
      pageSize:10,
      pageNum:1,
      total:5,
      name:'',
      storage: '',
      goodstype: ''
    }
  },
  methods:{
    del(){
      this.$axios.post( '/record/delete',this.multipleSelection).then(res=>res.data).then(res=>{
        if(res.code == 200){
          alert("成功删除记录！");
          this.loadPost(this.pageNum);
        }else{
          alert('删除失败！');
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
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
    loadStorage(){
    this.$axios.get( '/storage/list').then(res=>res.data).then(res=>{
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
    loadPost(num){
      this.pageNum = num,
      this.$axios.post( '/record/listPage',{
        pageSize:this.pageSize,
        pageNum:num,
        param: {
          name: this.name,
          goodstype: this.goodstype.toString(),
          storage: this.storage.toString(),
          roleId: this.user.roleId.toString(),
          userId: this.user.id.toString()
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
      this.storage='';
      this.goodstype='';
      this.pageSize=10
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