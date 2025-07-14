# 平台介绍

## 🏠【关于我们】

![天天开源](https://open.tntlinking.com/assets/logo-b-BzFUYaRU.png) 

天天开源致⼒于打造中国应⽤管理软件开源⽣态，⾯向医疗、企业、教育三⼤⾏业信息化需求，提供优质的开源软件产品与解决⽅案。平台现已发布OpenHIS、OpenCOM、OpenEDU系列开源产品，并持续招募⽣态合作伙伴，期待共同构建开源创新的⾏业协作模式，加速⾏业的数字化进程。

天天开源的前⾝是新致开源，最早于2022年6⽉发布开源医疗软件平台OpenHIS.org.cn，于2023年6⽉发布开源企业软件平台OpenCOM.com.cn。2025年7⽉，新致开源品牌更新为天天开源，我们始终秉持开源、专业、协作的理念，致⼒于为医疗、教育、中⼩企业等⾏业提供优质的开源解决⽅案。

了解我们：https://open.tntlinking.com/about?site=gitee

## 💾【部署包下载】

请访问官网产品中心下载部署包：https://open.tntlinking.com/resource/productCenter?site=gitee

## 📚【支持文档】

技术支持资源：https://open.tntlinking.com/resource/technicalSupport?site=gitee
（含演示环境、操作手册、部署手册、开发手册、常见问题等）

产品介绍：https://open.tntlinking.com/resource/industryKnowledge?site=gitee

操作教程：https://open.tntlinking.com/resource/operationTutorial?site=gitee

沙龙回顾：https://open.tntlinking.com/resource/openSourceSalon#23?site=gitee

## 🤝【合作方式】

产品服务价格：https://open.tntlinking.com/cost?site=gitee

加入生态伙伴：https://open.tntlinking.com/ecology/becomePartner?site=gitee

## 🤗【技术社区】

请访问官网扫码加入技术社区交流：https://open.tntlinking.com/ecology/joinCommunity?site=gitee

请关注公众号【天天开源软件】以便获得最新产品更新信息。



# 项目介绍

OpenHIS医院系统（信创版）集十大核心模块于一体，涵盖目录管理、基础数据配置、个性化设置、门诊/住院全流程管理、药房药库智能管控、精细化耗材管理、财务核算体系、医保合规对接及多维报表分析等功能模块，共计372项标准化功能。

系统深度适配民营及公立一二级医院业务场景，支持单体医院、集团化运营及区域医疗协同等多种部署模式，并通过国家信创认证体系，确保全栈技术自主可控。如有项目需求，可联系官方平台合作。



## 运行环境

    jdk17 (必须)
    node.js-v16.15 (推荐)
    PostgreSQL-v16.2 (必须)
    redis (常用稳定版本即可)

## 开发提示

    需要修改数据库和redis的连接信息,详见:
        application.yml
        application-druid.yml

## 目录解释
	
    前端: openhis-ui-vue3
    后端: openhis-server
        启动类: OpenHisApplication
		