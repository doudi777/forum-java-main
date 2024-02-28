<!--<template>-->
<!--  <div v-if="visible" class="popup">-->
<!--    &lt;!&ndash; 弹窗的内容 &ndash;&gt;-->
<!--    <div class="popup-content">-->
<!--      {{ content }}-->
<!--    </div>-->
<!--    &lt;!&ndash; 关闭按钮 &ndash;&gt;-->
<!--    <button class="close-button" @click="closePopup">关闭</button>-->
<!--  </div>-->
<!--</template>-->

<template>
  <div v-if="show" class="mask">
    <div class="dlg-show flex flex-col">
      <div class="flex flex-space-between full-width">
        <div class="font-bold">结果</div>
        <div class="pointer" @click="close"></div>
      </div>
      <div class="margin-top-l" style="height:45px;">{{ content }}</div>
      <div class="flex flex-end margin-top-xl">
        <div class="margin-left-m btn-blue-auto pointer full-width" @click="closePopup">确定</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'MsgShow',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    content: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
    }
  },

  methods: {
    closePopup () {
      this.$emit('close')
    }
  }
}
</script>

<style>
.dlg-show {
  border-radius: 5px;
  width: 300px;
  height: 140px;
  background-color: #fff;
  padding: 30px;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
}
</style>
