<style lang="less">
  @import "../editor.less";
</style>

<template>
  <div class="markdown-editor-navigator" id="markdown-editor-navigator">
    <template v-if="$store.state.showBranch">
      <div class="navigator-item-left">
        <Dropdown class="navigator-item" @on-click="handleBranchSelect">
          <Button>
            {{$store.state.selectedBranch.branchName}}
            <Icon type="ios-arrow-down"></Icon>
          </Button>
          <DropdownMenu slot="list">
            <template v-for="(branch) in $store.state.selectedNote.branchArrayList">
              <DropdownItem :key="branch.branchId" :name="branch.branchId">{{branch.branchName}}</DropdownItem>
            </template>
          </DropdownMenu>
        </Dropdown>
        <Button class="navigator-item" @click="handleAddBranch">新增分支</Button>
      </div>
      <div class="navigator-item-right">
        <Button type="primary" @click="handleSaveNote">保存笔记</Button>
      </div>
    </template>
    <template v-else>
      <h2 class="navigator-title">{{$store.state.selectedBranch.noteName}} -
        {{$store.state.selectedBranch.branchName}}</h2>
    </template>
  </div>
</template>

<script>
  import {addBranch} from "../../../api"

  export default {
    name: "navigator",
    data() {
      return {
        newBranchName: "",
      }
    },
    methods: {
      handleAddBranch: function () {
        let that = this
        this.$Modal.confirm({
          title: "新建分支",
          render: h => {
            return h("Input", {
              props: {
                value: this.newBranchName,
                autofocus: true,
                placeholder: "请输入新分支的名字"
              },
              on: {
                input: val => {
                  this.newBranchName = val
                }
              }
            })
          },
          onOk() {
            if (that.newBranchName !== "") {
              that.$Spin.show()
              addBranch({
                userId: that.$store.state.userId,
                notebookName: that.$store.state.selectedNotebook.notebookName,
                noteName: that.$store.state.selectedNote.noteName,
                content: "新分支：" + that.newBranchName,
                operator: that.$store.state.operatorName,
                branchName: that.newBranchName
              })
                .then(data => {
                  data = data.data
                  if (data.code === "200") {
                    that.$Message.success("添加分支成功")
                    let newSelectedNote = that.$store.state.selectedNote
                    newSelectedNote.branchArrayList = data.data
                    that.$store.commit("setSelectedNote", newSelectedNote)
                  } else {
                    that.$Message.error("添加分支失败：" + data.msg)
                  }
                })
                .catch(err => {
                  that.$Message.error("添加分支失败：" + err)
                })
                .finally(() => {
                  that.$Spin.hide()
                  that.branchName = ""
                })
            } else {
              that.$Message.error("名称不能为空")
            }
          }
        })
      },
      handleBranchSelect: function (index) {
        this.$store.dispatch('saveNote', 2)
        this.$store.dispatch('getNote', index)
      },
      handleSaveNote: function () {
        this.$store.dispatch('saveNote', 0)
      }
    }
  }
</script>