<template>
  <div id="openapidebug">
    <el-row :gutter="20">
      <el-col :span="13">
        <div class="grid-content bg-purple">
          <el-form :model="form">
            <el-form-item>
              <el-input v-model="form.requestHost" style="width: 20%"
                        value="https://open.kartor.cn"></el-input>
              <el-select size="large"
                         @change="selectInterfaceMethod(form.selectinterfaces,form.selectinterface)"
                         v-model="form.selectinterface" filterable
                         placeholder="Please select an interface">
                <el-option-group
                  v-for="group in form.selectinterfaces"
                  :label="group.label">
                  <el-option
                    v-for="item in group.options"
                    :label="item.label"
                    :value="item.value">
                    <span style="float: left">{{ item.label }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>
                  </el-option>
                </el-option-group>
              </el-select>
              <el-button type="primary" @click="submitForm('POST')">Send Post</el-button>
              <el-button type="primary" @click="submitForm('GET')">Send Get</el-button>
              <el-button type="info" @click="printForm('form')">Print</el-button>
            </el-form-item>
            <el-checkbox-group v-model="form.checkedType">
              <el-form-item v-for="(field, index) in form.interfacefields" :prop="field.key">
                <el-checkbox :label="field.key" checked style="width: 10%"
                             name="checkedType"></el-checkbox>
                <el-input v-model="field.value" style="width: 85%"></el-input>
              </el-form-item>
            </el-checkbox-group>
          </el-form>
        </div>
      </el-col>
      <el-col :span="11">
        <div class="grid-content bg-purple">
          <el-input
            type="textarea"
            :autosize="{ minRows: 10, maxRows: 15}"
            placeholder="request content"
            v-model="textarea2">
          </el-input>
          <div style="margin: 20px 0;"></div>
          <el-input
            type="textarea"
            :autosize="{ minRows: 10, maxRows: 15}"
            placeholder="response content"
            v-model="textarea3">
          </el-input>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
  export default {
    name: 'openapidebug',
    data() {
      return {
        form: {
          type: [],
          selectinterfaces: [{
            label: '用户接口',
            options: [{
              value: '/user/add',
              label: '注册用户(返回OpenId)',
              properties: ['appId', 'secret', 'nonce', 'timestamp', 'signature']
            }, {
              value: '/user/ubi/create',
              label: '订阅个人车主数据',
              properties: ['appId', 'nonce', 'timestamp', 'signature', 'redirectUrl', 'userTag', 'phoneNum', 'display']
            }, {
              value: '/user/car/license',
              label: '查询用户行驶证信息',
              properties: ['appId', 'nonce', 'timestamp', 'signature', 'redirectUrl', 'userTag', 'phoneNum', 'display']
            }]
          }, {
            label: '用户授权接口',
            options: [{
              value: '/auth/request',
              label: '请求用户授权',
              properties: ['appId', 'nonce', 'timestamp', 'signature', 'redirectUrl', 'userTag', 'phoneNum', 'display']
            }, {
              value: '/auth/kartor/request',
              label: '驾图内应用用户授权'
            }, {
              value: '/auth/location/request',
              label: '请求用户车位置授权'
            }]
          }],
          selectinterface: '',
          interfacefields: [],
          checkedType: [],
          requestHost: 'https://open.kartor.cn'
        },
        textarea2: '',
        textarea3: ''

      }
    },
    methods: {
      submitForm(requestType) {
        const that = this
        let str = ''
        console.log(that.form.interfacefields)
        console.log(that.form.checkedType)
        console.log(that.form.requestHost)

        for (var j = 0; j < that.form.interfacefields.length; j++) {
          for (var i = 0; i < that.form.checkedType.length; i++) {
            if (that.form.interfacefields[j].key == that.form.checkedType[i]) {
              str += that.form.interfacefields[j].key + "=" + that.form.interfacefields[j].value + "&"
            }
          }
        }
        this.$http.get('/api/v1/servers/encryption', {
          params: {
            requestHost: that.form.requestHost,
            requestPath: that.form.selectinterface,
            requestType: requestType,
            requestData: str
          }
        }).then(function (response) {
          that.textarea2: response.data
        }).catch(function (response) {
          console.log(response)
        })
      },
      printForm(formName){
        const that = this
        alert("printForm")
      },
      selectInterfaceMethod(fsifs, fsif){
        const that = this
        for (var i = 0; i < fsifs.length; i++) {
          for (var j = 0; j < fsifs[i].options.length; j++) {
            if (fsifs[i].options[j].value == fsif) {
              that.form.interfacefields = []
              for (var k = 0; k < fsifs[i].options[j].properties.length; k++) {
                that.form.interfacefields.push({
                  value: '',
                  key: fsifs[i].options[j].properties[k]
                })
              }
            }
          }
        }
      }
    }
  }
</script>
<style>
  .el-row {
    margin-bottom: 20px;

  &
  :last-child {
    margin-bottom: 0;
  }

  }
  .el-col {
    border-radius: 4px;
  }

  .bg-purple-dark {
    background: #99a9bf;
  }

  .bg-purple {
    background: #d3dce6;
  }

  .bg-purple-light {
    background: #e5e9f2;
  }

  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }

  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
</style>
