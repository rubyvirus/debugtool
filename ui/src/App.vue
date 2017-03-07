<template>
  <div id="app">
    <!--<hello></hello>-->
    <Openapidebugtool></Openapidebugtool>
    <p>
      <el-button @click="getWeather">Let's go</el-button>
      <span style="color:red">[{{ serverInfo }}]</span>
    </p>
  </div>
</template>

<script>
  //  import Hello from './components/Hello'
  import Openapidebugtool from './components/debugtool.vue'

  export default {
    name: 'app',
    data: function () {
      return {
        serverInfo: ''
      }
    },
    components: {Openapidebugtool},
    created: function () {
      const that = this
      this.$http.get('/api/v1/servers/info').then(function (response) {
        that.serverInfo = response.data.name
      })
    },
    methods: {
      getWeather(){
        alert('点击我干嘛')
      }
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    margin-top: 5px;
  }
</style>
