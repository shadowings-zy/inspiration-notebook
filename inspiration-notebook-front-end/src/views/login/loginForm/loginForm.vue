<style lang="less">
  @import "loginForm.less";
</style>

<template>
  <Form ref="loginForm" :model="form" :rules="rules" :label-width="80" @keydown.enter.native="handleSubmit"
        class="login-form" id="login-form">
    <FormItem prop="type" label="种类">
      <Select v-model="form.userType">
        <Option value="个人" key="0">个人账号</Option>
        <Option value="团队" key="1">团队账号</Option>
      </Select>
    </FormItem>
    <FormItem prop="operatorName" v-if="form.userType === '团队'" label="昵称">
      <Input v-model="form.operatorName" placeholder="请输入您在团队中的个人昵称"/>
    </FormItem>
    <FormItem prop="userName" label="账号">
      <Input v-model="form.userName" placeholder="请输入账号"/>
    </FormItem>
    <FormItem prop="password1" label="密码">
      <Input type="password" v-model="form.password1" placeholder="请输入密码"/>
    </FormItem>
    <FormItem prop="password2" v-if="isRegister" label="确认密码">
      <Input type="password" v-model="form.password2" placeholder="请再次输入密码"/>
    </FormItem>
    <FormItem>
      <Button
          @click="handleFormStatusChange"
          class="login-button"
      >
        {{isRegister ? "切换登录" : "切换注册"}}
      </Button>
      <Button
          @click="handleSubmit"
          class="login-button"
          type="primary"
      >
        {{isRegister ? "立即注册" : "立即登录"}}
      </Button>
    </FormItem>
  </Form>
</template>
<script>
  export default {
    name: "LoginForm",
    props: {
      userNameRules: {
        type: Array,
        default: () => {
          return [
            {required: true, message: "账号不能为空", trigger: "blur"},
            {type: 'string', max: 15, message: '不能输入超过15个字符', trigger: 'blur'}
          ]
        }
      },
      passwordRules: {
        type: Array,
        default: () => {
          return [
            {required: true, message: "密码不能为空", trigger: "blur"},
            {type: 'string', max: 15, message: '不能输入超过15个字符', trigger: 'blur'}
          ]
        }
      },
      operatorNameRules: {
        type: Array,
        default: () => {
          return [
            {required: true, message: "个人昵称不能为空", trigger: "blur"},
            {type: 'string', max: 15, message: '不能输入超过15个字符', trigger: 'blur'}
          ]
        }
      }
    },
    data() {
      return {
        isRegister: false, // false为登录，true为注册
        form: {
          userType: "个人",
          operatorName: "",
          userName: "",
          password1: "",
          password2: ""
        }
      }
    },
    computed: {
      rules() {
        return {
          userName: this.userNameRules,
          password1: this.passwordRules,
          password2: this.passwordRules,
          operatorName: this.operatorNameRules
        }
      }
    },
    methods: {
      handleSubmit() {
        if (this.isRegister) {
          if (this.form.password1 !== this.form.password2) {
            this.$Message.error("第二次输入的密码与第一次不一致！")
            return
          }
        }
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.$emit("on-success-valid", {
              isRegister: this.isRegister,
              operatorName: this.form.operatorName,
              username: this.form.userName,
              password: this.form.password1,
              userType: this.form.userType
            })
          }
        })
      },
      handleFormStatusChange() {
        this.isRegister = !this.isRegister
      }
    }
  }
</script>
