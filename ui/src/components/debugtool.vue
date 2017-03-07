<template>
  <div id="openapidebugtool">
    <el-menu theme="dark" default-active="1" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">debug</el-menu-item>
      <el-submenu index="2">
        <template slot="title">other</template>
        <el-menu-item index="2-1">en/decode</el-menu-item>
      </el-submenu>
    </el-menu>
    <div style="margin: 5px 0;"></div>
    <Openapidebug></Openapidebug>
  </div>
</template>
<script>
  import Openapidebug from './debug.vue'
  export default{
    name: 'openapidebugtool',
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    },
    components: {Openapidebug}
  }
</script>
<style>
</style>
