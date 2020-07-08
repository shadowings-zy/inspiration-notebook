<style lang="less">
  @import "./list.less";
</style>

<template>
  <div class="list" id="list">
    <Spin fix v-if="loading"/>
    <Card class="list-header">
      <h3 id="notebook-list-header">{{ $store.state.username }}的笔记</h3>
    </Card>
    <Split v-model="split" class="list-body">
      <div slot="left" class="demo-split-pane">
        <CellGroup @on-click="handleNotebookSelect">
          <template v-for="(notebook, index) in notebookList">
            <Cell
                :title="notebook.notebookName"
                :key="index"
                :name="index"
                :selected="$store.state.selectedNotebook.notebookName === notebook.notebookName"
            >
              <Icon slot="extra" type="md-filing" @click.stop="handleDownloadNoteSelect(notebook)" :size="16"></Icon>
              <Icon slot="extra" type="md-trash" @click.stop="handleDeleteNotebook(notebook)" :size="16"></Icon>
            </Cell>
          </template>
        </CellGroup>
        <Card class="card-wrapper">
          <div class="card-content" @click="handleAddNotebook">
            <h3>
              <Icon type="md-add" :size="24"/>
            </h3>
          </div>
        </Card>
      </div>
      <div slot="right" class="demo-split-pane">
        <CellGroup @on-click="handleSelectNote">
          <template v-for="(note, index) in noteList">
            <Cell
                :title="note.noteName"
                :key="index"
                :name="index"
                :selected="$store.state.selectedNote.noteName === note.noteName"
            >
              <Icon slot="extra" type="md-create" @click.stop="handleRenameNote(note)" :size="16"></Icon>
              <Icon slot="extra" type="md-trash" @click.stop="handleDeleteNote(note)" :size="16"></Icon>
            </Cell>
          </template>
        </CellGroup>
        <Alert v-if="$store.state.selectedNotebook.notebookName === undefined">
          请先选择笔记本
          <template slot="desc">
            之后选中笔记本中的笔记会在此显示。
          </template>
        </Alert>
        <Card class="card-wrapper" v-if="$store.state.selectedNotebook.notebookName !== undefined">
          <div class="card-content" @click="handleAddNote">
            <h3>
              <Icon type="md-add" :size="24"/>
            </h3>
          </div>
        </Card>
      </div>
    </Split>
    <Modal
        v-model="modalShow"
        title="选择需要生成markdown文件的笔记"
        @on-ok="handleDownloadNotebook"
    >
      <CheckboxGroup class="document-select-checkbox" v-model="selectGenerateDocumentNoteList">
        <template v-for="(item, index) in $store.state.selectedNotebook.noteArrayList">
          <Checkbox :key="index" :label="item.noteName"/>
        </template>
      </CheckboxGroup>
      <h4>{{selectGenerateDocumentNoteList.length === 0 ? "" : "生成顺序为：" +
        selectGenerateDocumentNoteList.map(item => " " + item + " ")}}</h4>
    </Modal>
  </div>
</template>

<script>
  import {
    addNote,
    addNotebook,
    deleteNote,
    deleteNotebook,
    getNotebookAndNote,
    updateNote,
    getDocument
  } from "../../../api"

  export default {
    components: {Document},
    data() {
      return {
        notebookList: [],
        noteList: [],
        loading: false,
        split: 0.5,
        inputName: "",
        modalShow: false,
        selectGenerateDocumentNoteList: []
      }
    },
    methods: {
      // 获取所有的笔记本以及笔记本下的笔记
      fetchAllNoteData() {
        this.noteList = []
        this.notebookList = []
        this.$store.commit('setSelectedNote', {})
        this.$store.commit("setSelectedBranch", {})
        this.$store.commit("setContent", "")
        this.$Spin.show()
        getNotebookAndNote({userId: this.$store.state.userId})
          .then(data => {
            data = data.data
            if (data.code === "200") {
              this.notebookList = data.data
              for (let notebook of data.data) {
                if (notebook.notebookName === this.$store.state.selectedNotebook.notebookName) {
                  this.noteList = notebook.noteArrayList
                }
              }
            } else {
              this.$Message.error("获取笔记信息失败: " + data.msg)
            }
          })
          .catch(err => {
            this.$Message.error("获取笔记信息失败：" + err)
          })
          .finally(() => {
            this.$Spin.hide()
          })
      },
      // 根据选择的笔记本获取笔记列表
      handleNotebookSelect(index) {
        this.$store.dispatch('saveNote', 2)
        this.$store.commit('setSelectedNotebook', this.notebookList[index])
        this.fetchAllNoteData()
      },
      // 获取某个笔记的内容
      handleSelectNote(index) {
        this.$store.dispatch('saveNote', 2)
        this.$store.commit('setSelectedNote', this.noteList[index])
        this.$store.commit('setSelectedBranch', this.noteList[index].branchArrayList[0])
        this.$store.dispatch('getNote', this.$store.state.selectedBranch.branchId)
      },
      // 选择要生成的章节
      handleDownloadNoteSelect(notebook) {
        this.modalShow = true
        this.$store.commit('setSelectedNotebook', notebook)
      },
      // 下载当前笔记本下的所有笔记
      handleDownloadNotebook() {
        let baseUrl = "http://www.shadowingszy.top:3001/inspiration-notebook/markdown/"
        let order = []
        for (const noteName of this.selectGenerateDocumentNoteList) {
          for (const note of this.$store.state.selectedNotebook.noteArrayList) {
            if (note.noteName === noteName) {
              for (const branch of note.branchArrayList) {
                if (branch.branchName === "主分支") {
                  order.push(branch.branchId)
                }
              }
            }
          }
        }
        let that = this
        getDocument({
          notebookName: this.$store.state.selectedNotebook.notebookName,
          userId: that.$store.state.userId,
          order: order.join(",")
        })
          .then(data => {
            data = data.data
            if (data.code === "200") {
              window.open(baseUrl + data.data)
            } else {
              that.$Message.error("生成markdown文件失败：" + data.msg)
            }
          })
          .catch(err => {
            that.$Message.error("生成markdown文件失败：" + err)
          })
          .finally(() => {
            that.$Spin.hide()
          })
      },
      // 删除笔记本以及笔记本下的全部笔记
      handleDeleteNotebook(notebook) {
        let that = this
        this.$Modal.confirm({
          content: "确认要删除这个笔记吗？",
          onOk() {
            deleteNotebook({
              notebookName: notebook.notebookName,
              userId: that.$store.state.userId
            })
              .then(data => {
                data = data.data
                if (data.code === "200") {
                  that.$Message.success("删除成功")
                  that.$store.commit('setSelectedNotebook', {})
                  that.fetchAllNoteData()
                } else {
                  that.$Message.error("删除失败：" + data.msg)
                }
              })
              .catch(err => {
                that.$Message.error("删除失败：" + err)
              })
              .finally(() => {
                that.$Spin.hide()
              })
          }
        })
      },
      // 新增笔记本
      handleAddNotebook() {
        let that = this
        this.$Modal.confirm({
          title: "新建笔记本",
          render: h => {
            return h("Input", {
              props: {
                value: this.inputName,
                autofocus: true,
                placeholder: "请输入要创建笔记本的名字"
              },
              on: {
                input: val => {
                  this.inputName = val
                }
              }
            })
          },
          onOk() {
            that.$Spin.show()
            addNotebook({
              notebookName: that.inputName,
              userId: that.$store.state.userId,
              operator: that.$store.state.operatorName
            })
              .then(data => {
                data = data.data
                if (data.code === "200") {
                  that.$Message.success("创建笔记本成功")
                  that.fetchAllNoteData()
                } else {
                  that.$Message.error(
                    "创建笔记本失败：" + data.msg
                  )
                }
              })
              .catch(err => {
                that.$Message.error("创建笔记本失败：" + err)
              })
              .finally(() => {
                that.$Spin.hide()
                that.inputName = ""
              })
          }
        })
      },
      // 新增笔记
      handleAddNote() {
        let that = this
        this.$Modal.confirm({
          title: "新建笔记",
          render: h => {
            return h("Input", {
              props: {
                value: this.inputName,
                autofocus: true,
                placeholder: "请输入要创建笔记的名字"
              },
              on: {
                input: val => {
                  this.inputName = val
                }
              }
            })
          },
          onOk() {
            that.$Spin.show()
            addNote({
              notebookName: that.$store.state.selectedNotebook.notebookName,
              noteName: that.inputName,
              content: "",
              userId: that.$store.state.userId,
              operator: that.$store.state.operatorName
            })
              .then(data => {
                data = data.data
                if (data.code === "200") {
                  that.$Message.success("创建笔记成功")
                  that.noteList = data.data.noteArrayList
                } else {
                  that.$Message.error(
                    "创建笔记失败：" + data.msg
                  )
                }
              })
              .catch(err => {
                that.$Message.error("创建笔记失败：" + err)
              })
              .finally(() => {
                that.$Spin.hide()
                that.inputName = ""
              })
          }
        })
      },
      // 重命名笔记
      handleRenameNote(note) {
        let that = this
        this.$Modal.confirm({
          title: "重命名笔记",
          render: h => {
            return h("Input", {
              props: {
                value: this.inputName,
                autofocus: true,
                placeholder: "请输入笔记的新名字"
              },
              on: {
                input: val => {
                  this.inputName = val
                }
              }
            })
          },
          onOk() {
            if (that.inputName !== "") {
              that.$Spin.show()
              updateNote({
                userId: that.$store.state.userId,
                notebookName: that.$store.state.selectedNotebook.notebookName,
                oldNoteName: note.noteName,
                newNoteName: that.inputName,
                operator: that.$store.state.operatorName
              })
                .then(data => {
                  data = data.data
                  if (data.code === "200") {
                    that.$Message.success("笔记重命名成功")
                    note.noteName = that.inputName
                  } else {
                    that.$Message.error("笔记重命名失败：" + data.msg)
                  }
                })
                .catch(err => {
                  that.$Message.error("重命名失败：" + err)
                })
                .finally(() => {
                  that.$Spin.hide()
                  that.inputName = ""
                })
            } else {
              that.$Message.error("名称不能为空")
            }
          }
        })
      },
      // 删除笔记
      handleDeleteNote(note) {
        let that = this
        this.$Modal.confirm({
          content: "确认要删除这个笔记吗？（如果笔记本中最后一篇笔记被删除，笔记本也将会被删除）",
          onOk() {
            if (that.noteList.length <= 1) {
              deleteNotebook({
                notebookName: that.$store.state.selectedNotebook.notebookName,
                userId: that.$store.state.userId
              })
                .then(data => {
                  data = data.data
                  if (data.code === "200") {
                    that.$Message.success("删除成功")
                    that.$store.commit('setSelectedNotebook', {})
                    that.fetchAllNoteData()
                  } else {
                    that.$Message.error("删除失败：" + data.msg)
                  }
                })
                .catch(err => {
                  that.$Message.error("删除失败：" + err)
                })
                .finally(() => {
                  that.$Spin.hide()
                })
            } else {
              deleteNote({
                noteName: note.noteName,
                userId: that.$store.state.userId,
                notebookName: that.$store.state.selectedNotebook.notebookName
              })
                .then(data => {
                  data = data.data
                  if (data.code === "200") {
                    that.$Message.success("删除成功")
                    that.noteList = data.data.noteArrayList
                  } else {
                    that.$Message.error("删除失败：" + data.msg)
                  }
                })
                .catch(err => {
                  that.$Message.error("删除失败：" + err)
                })
                .finally(() => {
                  that.$Spin.hide()
                })
            }
          }
        })
      }
    },
    mounted() {
      this.fetchAllNoteData()
    },
    watch: {
      $route: "fetchData"
    }
  }
</script>