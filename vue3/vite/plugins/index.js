import vue from '@vitejs/plugin-vue'

import createAutoImport from './auto-import'
import createSvgIcon from './svg-icon'
import createCompression from './compression'
import createSetupExtend from './setup-extend'

export default function createVitePlugins(viteEnv, isBuild = false) {
    const vitePlugins = [vue()]
    vitePlugins.push(createAutoImport())
	vitePlugins.push(createSetupExtend())
    vitePlugins.push(createSvgIcon(isBuild))    
	// 住院代码影响打包 打包时暂时替换为空文件
    // 新增排除住院管理模块的插件
    vitePlugins.push({
        name: 'exclude-in-hospital-management',
        enforce: 'pre',
        load(id) {
        const normalizedPath = id.replace(/\\/g, '/')
        if (normalizedPath.includes('/src/views/inHospitalManagement/') 
            || normalizedPath.includes('/src/views/inpatientDoctor/') 
            || normalizedPath.includes('/src/views/inpatientNurse/')) {
            return {
            code: `
                <template>
                </template>
                <script>
                </script>
            `,
            map: null
            }
        }
        return null
        }
    })
	isBuild && vitePlugins.push(...createCompression(viteEnv))
    return vitePlugins
}
