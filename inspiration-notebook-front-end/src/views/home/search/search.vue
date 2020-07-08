<template>
  <div class="search" id="search">
    <div class="search-header">
      <Input search enter-button="搜索" placeholder="在此输入要搜索的内容" @on-search="search"/>
    </div>
    <Card title="搜索结果" icon="ios-apps" :padding="0" shadow class="search-body">
      <CellGroup @on-click="getSearchBranch">
        <template v-for="(item, index) in result">
          <Cell
              :key="index"
              :title="item.noteName"
              :label="item.notebookName + ' - ' + item.noteName + ' - ' + item.branchName"
              :name="index"
              :selected="item.branchId=== $store.state.selectedBranch.branchId"
          />
        </template>
      </CellGroup>
    </Card>
  </div>
</template>

<script>
  import {getBranch, searchKeywords} from "../../../api"

  export default {
    data() {
      return {
        result: []
      }
    },
    methods: {
      search(keywords) {
        if (keywords === "") {
          return this.$Message.error("搜索关键字不得为空")
        }

        this.$Spin.show()
        searchKeywords({
          keywords: keywords,
          userId: this.$store.state.userId
        })
          .then(data => {
            data = data.data
            if (data.code === "200") {
              this.result = data.data
              console.log(this.result)
            } else {
              this.$Message.error("搜索失败：" + data.msg)
            }
          })
          .catch(err => {
            this.$Message.error("搜索失败：" + err)
          })
          .finally(() => {
            this.$Spin.hide()
          })
      },
      getSearchBranch(index) {
        // this.$store.commit('setSelectedBranch', this.result[index])
        this.$Spin.show()
        getBranch({
          userId: this.$store.state.userId,
          branchId: this.result[index].branchId
        }).then(data => {
          data = data.data
          if (data.code === "200") {
            this.$store.commit("setSelectedBranch", data.data)
            this.$store.commit("setContent", data.data.content)
            console.log(data.data)
          } else {
            this.$Message.error("加载笔记内容失败：" + data.msg)
          }
        }).catch(err => {
          this.$Message.error("未知错误" + err)
        }).finally(() => {
          this.$Spin.hide()
        })
      }
    }
  }
</script>

<style lang="less">
  .search {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: stretch;

    &-header {
      padding: 16px;
    }

    &-body {
      flex-grow: 1;
    }

    .ivu-card-body {
      border-radius: 0;
    }
  }
</style>
