<template>
  <div class="home" id="home">
    <div class="header">
      <div class="header-content">Inspiration Notebook</div>
      <Icon class="header-icon" type="md-settings" size="30" @click="handleDrawer"/>
    </div>
    <LeftNav/>
    <Split v-model="split" min="200" max="500">
      <div slot="left" class="demo-split-pane h100">
        <div class="middle">
          <router-view/>
        </div>
      </div>
      <div slot="right" class="demo-split-pane h100">
        <div class="editor-wrapper">
          <editor
              class="editor"
              v-if="$store.state.selectedBranch.branchId !== undefined"
          />
          <div class="no-note" v-if="$store.state.selectedBranch.branchId === undefined">
            <img :src="noNoteImage" alt="笔记">
            <h2>请选择笔记</h2>
          </div>
        </div>
      </div>
    </Split>
    <Drawer
        title="账号信息"
        v-model="drawerShow"
        width="500"
        :mask-closable="false"
    >
      <Form ref="modifyUserForm" :model="form" :rules="rules" :label-width="80">
        <FormItem prop="type" label="种类">
          <Select v-model="form.userType" :disabled="true">
            <Option value="个人" key="0">个人账号</Option>
            <Option value="团队" key="1">团队账号</Option>
          </Select>
        </FormItem>
        <FormItem prop="operatorName" v-if="form.userType === '团队'" label="昵称">
          <Input v-model="form.operatorName" placeholder="请输入您在团队中的个人昵称"/>
        </FormItem>
        <FormItem prop="username" label="账号">
          <Input v-model="form.userName" placeholder="请输入账号" :disabled="true"/>
        </FormItem>
        <FormItem prop="password" label="密码">
          <Input v-model="form.password" placeholder="请输入新密码"/>
        </FormItem>
        <FormItem>
          <Button @click="handleSubmit" class="login-button" type="primary">修改账号信息</Button>
        </FormItem>
      </Form>
    </Drawer>
  </div>
</template>

<script>
  import LeftNav from "./leftNavigator"
  import noNote from "../../assets/no-note.png"
  import Editor from "../../components/markdownEditor"
  import {updateUser} from "../../api"

  export default {
    components: {
      Editor,
      LeftNav
    },
    data() {
      return {
        split: 0.3,
        noNoteImage: noNote,
        drawerShow: false,
        form: {
          userType: this.$store.state.userType,
          operatorName: this.$store.state.operatorName,
          userName: this.$store.state.username,
          password: ""
        },
        rules: {
          userName: [
            {required: true, message: "账号不能为空", trigger: "blur"}
          ],
          password: [
            {required: true, message: "密码不能为空", trigger: "blur"}
          ],
          operatorName: [
            {required: true, message: "个人昵称不能为空", trigger: "blur"}
          ]
        },
      }
    },
    methods: {
      handleSubmit: function(){
        this.$refs.modifyUserForm.validate((valid) => {
          if (valid) {
            this.updateUserInformation()
          }
        })
      },
      updateUserInformation: function () {
        updateUser({
          userId: this.$store.state.userId,
          username: this.form.userName,
          password: this.form.password,
          userType: this.form.userType
        })
          .then(resp => {
            if (resp.data.code === "200") {
              this.$Message.success(resp.data.msg)
              this.$store.commit("setUserInformation", {
                username: resp.data.data.username,
                userId: resp.data.data.userId,
                userType: resp.data.data.userType,
                operatorName: resp.data.data.userType === "个人" ? resp.data.data.username : this.form.operatorName
              })
            } else {
              this.$Message.error(resp.data.msg)
            }
          })
          .catch(err => {
            console.log(err)
            this.$Message.error("修改用户信息失败：未知错误")
          })
      },
      handleDrawer: function () {
        this.drawerShow = true
      }
    }
  }
</script>

<style lang="less">
  body {
    user-select: none;
  }

  .ivu-spin-fix {
    z-index: 99999; //为了让loading蒙版显示在editor的组件之上
  }

  .h100 {
    height: 100%;
  }

  .home {
    .header {
      background-color: rgba(64, 98, 164, 1);
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      height: 48px;
      color: white;
      text-align: center;
      line-height: 48px;
      font-size: 20px;
      font-weight: 600;
      display: flex;
      flex-direction: row;

      .header-content {
        width: 80%;
        margin-left: 20%;
      }

      .header-icon {
        width: 20%;
        line-height: 30px;
        padding: 9px;
      }
    }

    padding-top: 48px;
    height: 100%;
    display: flex;
    align-items: stretch;

    .middle {
      width: 100%;
      height: 100%;
    }

    .no-note {
      text-align: center;
      position: absolute;
      width: 100%;
      top: 40%;
      transform: translateY(-50%);

      img {
        width: 10%;
        margin-bottom: 20px;
      }
    }

    .editor-wrapper {
      height: 100%;
      width: 100%;
      display: flex;
      flex-direction: column;

      .editor {
        margin: 0 0 0 6px;
        height: 100%;
      }
    }
  }
</style>

