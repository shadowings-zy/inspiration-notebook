<style lang="less">
  @import "./editor.less";
</style>

<template>
  <div id="markdown-editor">
    <navigator/>
    <div class="markdown-editor-content">
      <Input
          id="edit-field"
          ref="edit"
          v-model="noteContent"
          type="textarea"
          placeholder="在这里输入markdown格式的笔记，右方可直接预览笔记"
          @on-change="handleAutoSave(60000)"
      />
      <div id="overview-field">
        <div v-for="(markdownHtml, index) in markdownHtmlList" :key="index" v-html="markdownHtml" class="overview-line"></div>
      </div>
    </div>
    <div class="markdown-editor-status">{{noteStatus}}</div>
  </div>
</template>

<script>
  import showdown from "showdown"
  import {parse, setScroll, diff} from "./editTools"
  import Navigator from "./editorNavigator/editorNavigator"

  const converter = new showdown.Converter()
  converter.setOption("tables", "true")
  converter.setOption("simpleLineBreaks", "true")

  export default {
    name: "editor",
    components: {Navigator},
    data: function () {
      return {
        previousNoteContentList: [], // noteContent的缓存，diff时用
        markdownHtmlList: [],
        throttlePreviousTime: 0
      }
    },
    computed: {
      noteContent: {
        get: function () {
          this.generateMarkdown()
          return this.$store.state.content
        },
        set: function (newValue) {
          this.$store.commit("setContent", newValue)
        }
      },
      noteStatus: function () {
        return this.$store.state.selectedBranch.status
      }
    },
    methods: {
      generateMarkdown: function () {
        let noteContent = this.$store.state.content
        let noteContentList = noteContent.split("\n\n")

        let {startIndex, oldEndIndex, newEndIndex} = diff(this.previousNoteContentList, noteContentList)
        for (let a = startIndex; a <= oldEndIndex; a++) {
          this.markdownHtmlList.splice(startIndex, 1)
        }
        for (let a = startIndex; a <= newEndIndex; a++) {
          let parsedNoteContent = parse(noteContentList[a])
          this.markdownHtmlList.splice(a, 0, converter.makeHtml(parsedNoteContent))
        }

        this.previousNoteContentList = noteContentList
      },
      handleAutoSave: function (wait) {
        let now = Date.now()
        if (now - this.throttlePreviousTime > wait) {
          this.$store.dispatch('saveNote', 1)
          this.throttlePreviousTime = now
        }
      }
    },
    mounted() {
      let editField = document.getElementById('edit-field').firstElementChild
      let overviewField = document.getElementById('overview-field')
      editField.addEventListener("scroll", function (e) {
        setScroll(editField, overviewField)
        e.preventDefault()
      })
    }
  }
</script>