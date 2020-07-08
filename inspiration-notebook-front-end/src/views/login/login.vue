<style lang="less">
  @import "./login.less";
</style>

<template>
  <div id="login" class="login">
    <div class="login-con">
      <Spin fix v-if="loading"></Spin>
      <Card icon="log-in" title="欢迎使用 Inspiration Notebook" :bordered="false">
        <div class="form-con">
          <login-form @on-success-valid="handleSubmit"></login-form>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
  import LoginForm from "./loginForm"
  import {mapMutations} from "vuex"
  import {login, register} from "../../api"

  export default {
    components: {
      LoginForm
    },
    data() {
      return {
        loading: false
      }
    },
    methods: {
      ...mapMutations(["setUserInformation"]),
      handleSubmit({isRegister, username, password, userType, operatorName}) {
        this.loading = true
        let ajax = login
        if (isRegister) {
          ajax = register
        }
        ajax({username, password, userType})
          .then(resp => {
            if (resp.data.code === "200") {
              this.$Message.success(resp.data.msg)
              this.setUserInformation({
                username: username,
                userId: resp.data.data.userId,
                userType: userType,
                operatorName: userType === "个人" ? username : operatorName
              })
              this.$router.push({
                path: "/home/list"
              })
            } else {
              this.$Message.error(resp.data.msg)
            }
          })
          .catch(err => {
            console.log(err)
            this.$Message.error("出现未知错误！")
          })
          .finally(() => {
            this.loading = false
          })
      }
    },
    mounted() {
      this.$store.dispatch('initStore')
    },
  }
</script>

<style>
</style>
