<template>
  <div class="left-nav" id="left-nav">
    <div :class="{active: active === 'list'}">
      <Icon type="ios-bookmarks" :size="iconSize" @click="clickHandler('list')"/>
    </div>
    <div :class="{active: active === 'search'}">
      <Icon type="md-search" :size="iconSize" @click="clickHandler('search')"/>
    </div>
  </div>
</template>
<script>
  export default {
    name: "LeftNav",
    props: {},
    data() {
      return {
        active: "list",
        iconSize: 30
      }
    },
    computed: {},
    methods: {
      clickHandler(tab) {
        if (this.active !== tab) {
          this.$router.push(tab)
        }
        this.active = tab
        this.$store.dispatch('saveNote')
        this.$store.commit("setSelectedNote", {})
        this.$store.commit("setSelectedNotebook", {})
        this.$store.commit("setSelectedBranch", {})
        this.$store.commit("setContent", "")
        if (tab === "list") {
          this.$store.commit("setShowBranch", true)
        } else {
          this.$store.commit("setShowBranch", false)
        }
      }
    },
    mounted() {
      if (this.$route.path.indexOf("list") !== -1) {
        this.active = "list"
        this.$store.commit("setShowBranch", true)
      }
      if (this.$route.path.indexOf("search") !== -1) {
        this.active = "search"
        this.$store.commit("setShowBranch", false)
      }
    }
  }
</script>

<style lang="less">
  @theme-color: rgba(64, 98, 164, 1);
  .left-nav {
    width: 50px;
    background: #efefef;
    text-align: center;
    line-height: 60px;

    .active {
      color: @theme-color;
      background: #ddd;
    }
  }
</style>

