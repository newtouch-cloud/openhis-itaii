<template>
  <div class="folder-container" :style="{ marginLeft: `${level * 4}px` }">
    <div class="folder-header" @click="toggleFolder">
      <el-icon>
        <component :is="isOpen ? 'Fold' : 'Expand'" />
      </el-icon>
      <span>{{ folder.name }}</span>
    </div>

    <div v-if="isOpen" class="folder-content">
      <div v-for="(item, index) in props.folder.children" :key="index">
        <div v-if="item.children && item.children.length" class="sub-folder">
          <diagnose-folder :folder="item" :level="level + 1" />
        </div>
        <div v-else class="file-item">
          {{ item.name }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup >
import { ref, defineProps } from 'vue'
import { ElIcon } from 'element-plus'

interface FolderItem {
  name: string
  children?: FolderItem[]
  type?: number // 0:文件夹 1:文件
}

interface Props {
  folder: FolderItem
  level: number
}

const props = defineProps<Props>()

// 是否展开
const isOpen = ref(false)

const toggleFolder = () => {
  isOpen.value = !isOpen.value
}
</script>

<style lang="scss" scoped>
.folder-container {
  position: relative;

  .folder-header {
    display: flex;
    align-items: center;
    cursor: pointer;
    padding: 4px 4px 4px 0px;
    border-radius: 4px;

    &:hover {
      background-color: #f5f7fa;
    }

    .el-icon {
      margin-right: 8px;
      font-size: 16px;
    }
  }

  .folder-content {
    margin-left: 8px;

    .sub-folder {
      margin-left: 10px;
    }

    .file-item {
      padding: 4px;
      margin-left: 4px;

      &:hover {
        background-color: #f5f7fa;
      }
    }
  }
}
</style>
