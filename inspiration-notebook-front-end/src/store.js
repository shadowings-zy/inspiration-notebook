import Vue from "vue"
import Vuex from "vuex"
import {getNoteDetail, updateBranchContent} from "./api"

Vue.use(Vuex)

let vx = new Vuex.Store({
  state: {
    username: "",
    userId: 0,
    operatorName: "",
    userType: "个人",
    websocket: null,
    content: "",
    noteMarkdownHtmlList: [],
    fileList: [] /* { name: xxx, src: xxx } */,
    selectedNotebook: {},
    selectedNote: {},
    selectedBranch: {},
    autoSaveInterval: false,
    showBranch: false
  },
  mutations: {
    setUserInformation(state, payload) {
      state.username = payload.username
      state.userId = payload.userId
      state.userType = payload.userType
      state.operatorName = payload.operatorName
    },
    setContent(state, content) {
      state.content = content
    },
    setSelectedNotebook(state, notebook) {
      state.selectedNotebook = notebook
    },
    setSelectedNote(state, note) {
      state.selectedNote = note
    },
    setSelectedBranch(state, branch) {
      state.selectedBranch = branch
    },
    setShowBranch(state, flag) {
      state.showBranch = flag
    }
  },
  actions: {
    initStore({commit, state}) {
      commit('setUserInformation', {
        username: "",
        userId: 0,
        operatorName: "",
        userType: "个人",
      })
      commit('setContent', "")
      commit('setSelectedNotebook', {})
      commit('setSelectedNote', {})
      commit('setSelectedBranch', {})
      commit('setShowBranch', false)
    },
    getNote({commit, state}, branchId) {
      this.$app.$Spin.show()
      getNoteDetail({
        noteName: state.selectedNote.noteName,
        notebookName: state.selectedNotebook.notebookName,
        userId: state.userId
      }).then(data => {
        data = data.data
        if (data.code === "200") {
          if (branchId !== undefined && branchId >= 0) {
            for (let item of data.data) {
              if (item.branchId === branchId) {
                commit("setSelectedBranch", item)
                commit("setContent", item.content)
              }
            }
          } else {
            commit("setSelectedBranch", data.data[0])
            commit("setContent", data.data[0].content)
          }
        } else {
          this.$app.$Message.error("加载笔记内容失败：" + data.msg)
        }
      }).catch(err => {
        this.$app.$Message.error("未知错误" + err)
      }).finally(() => {
        this.$app.$Spin.hide()
      })
    },
    saveNote({commit, state}, type) {
      if (state.content !== undefined && state.selectedNote.noteName !== undefined && state.selectedNotebook.notebookName !== undefined) {
        let updateNotePromise = updateBranchContent({
          userId: state.userId,
          branchId: state.selectedBranch.branchId,
          notebookName: state.selectedNotebook.notebookName,
          noteName: state.selectedNote.noteName,
          content: state.content,
          operator: state.operatorName
        })

        if (type === 0) {
          // 手动保存
          this.$app.$Spin.show()
          updateNotePromise
            .then(data => {
              data = data.data
              if (data.code === "200") {
                this.$app.$Message.success("保存成功")
                let newSelectedNote = state.selectedNote
                newSelectedNote.branchArrayList = data.data
                commit("setSelectedNote", newSelectedNote)
                for (let branch of data.data) {
                  if (branch.branchId === state.selectedBranch.branchId) {
                    commit("setSelectedBranch", branch)
                  }
                }
              } else {
                this.$app.$Message.error("保存失败：" + data.msg)
              }
            })
            .catch(err => {
              this.$app.$Message.error("笔记保存失败：" + err)
            })
            .finally(() => {
              this.$app.$Spin.hide()
            })
        } else if (type === 1) {
          // 自动保存
          updateNotePromise
            .then(data => {
              data = data.data
              if (data.code === "200") {
                let newSelectedNote = state.selectedNote
                newSelectedNote.branchArrayList = data.data
                commit("setSelectedNote", newSelectedNote)
                for (let branch of data.data) {
                  if (branch.branchId === state.selectedBranch.branchId) {
                    commit("setSelectedBranch", branch)
                  }
                }
              }
            })
        }
      }
    },
  }
})

export default vx


