<!--
 * @Author: sjjh
 * @Date: 2025-04-12 22:52:19
 * @Description:
-->
<template>
  <el-scrollbar height="100%" width="200px" class="folder-container">
    <div :style="{ marginLeft: `${level * 4}px` }">
      <template v-for="(folderItem, index) in props.folder" :key="index">
        <div class="folder-header" @click="toggleFolder(folderItem)">
          <el-icon v-if="folderItem.children && folderItem.children.length > 0">
            <component :is="folderItem.isOpen ? 'Fold' : 'Expand'" />
          </el-icon>
          <el-tooltip
            :content="folderItem.name"
            placement="right"
            v-if="folderItem.name.length >= 10"
          >
            <el-text truncated> {{ folderItem.name }}</el-text>
          </el-tooltip>
          <el-text v-else truncated> {{ folderItem.name }}</el-text>
        </div>

        <div v-if="folderItem.isOpen" class="folder-content">
          <div v-if="folderItem.children && folderItem.children.length" class="sub-folder">
            <diagnose-folder :folder="folderItem.children" :level="level + 1" />
          </div>
        </div>
      </template>
    </div>
  </el-scrollbar>
</template>

<script setup >
import { defineProps } from 'vue'
import { ElIcon } from 'element-plus'

interface FolderItem {
  name: string
  children?: FolderItem[]
  type?: number // 0:文件夹 1:文件
  isOpen?: boolean
}

interface Props {
  folder: FolderItem[]
  level: number
}

const props = defineProps<Props>()

const toggleFolder = (folder: FolderItem) => {
  folder.isOpen = !folder.isOpen
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
