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
      <el-button type="primary" style="margin-left: 5px" @click="loadPost(1)">查询</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
    </div>
    <div style="margin-top: 10px;margin-bottom: 10px">
      <el-button @click="setCurrent()" plain>取消选择</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              ref="singleTable"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
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
              :type="scope.row.sex === 1 ? 'primary' : 'success'"
              disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roleId" label="角色" width="120">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' :
              (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话">
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
  name: "SelectUser",
  data() {
    return {
      tableData: [],
      pageSize:5,
      pageNum:1,
      total:5,
      name:'',
      sex:'',
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
    }
  },
  methods:{
    setCurrent(row) {
      this.$refs.singleTable.setCurrentRow(row);
      this.currentRow = {};
      this.$emit("doSelectUser",null);
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
    selectCurrentChange(val) {
      this.$emit("doSelectUser",val);
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