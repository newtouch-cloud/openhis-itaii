import { defineStore } from 'pinia'

export const useStore = defineStore('current', {
  state: () => ({
    currentData: null, //采购入库新增数据
    currentReturnData:null,//采购退库新增数据
    currentDataDB:null, //调拨新增数据
    currentDataDBAll:null,// 调拨批量保存所有数据
    currentDataPLDB:null, //批量调拨新增数据
    currentDataPLDBAll:null,//批量调拨所有数据
    currentDataPD:null, //盘点
    currentDataPLPD:null, //批量盘点
    currentDataPLPDAll:null,//批量盘点所有数据
    currentDataBS:null, //报损
    currentDataLYCK:null, //领用出库
    currentDataLYTK:null, //领用退库
  }),
  actions: {
    setCurrentData(data) {
      this.currentData = data
    },
    setCurrentReturnData(data) {
      this.currentReturnData = data
    },
    setCurrentDataDB(data) {
      this.currentDataDB = data
    },
    setCurrentDataDBAll(data) {
      this.currentDataDBAll = data
    },
    setCurrentDataPLDB(data) {
      this.currentDataPLDB = data
    },
    setCurrentDataPD(data) {
      this.currentDataPD = data
    },
    setCurrentDataPLPD(data) {
      this.currentDataPLPD = data
    },

    setCurrentDataPLDBAll(data) {
      this.currentDataPLDBAll = data
    },
    setCurrentDataPLPDAll(data) {
      this.currentDataPLPDAll = data
    },

    setCurrentDataBS(data) {
      this.currentDataBS = data
    },
    setCurrentDataLYCK(data) {
      this.currentDataLYCK = data
    },
    setCurrentDataLYTK(data) {
      this.currentDataLYTK = data
    },
    
    clearCurrentDataDBAll() {
      this.currentDataDBAll = null
    },
    clearCurrentDataLYCK() {
      this.currentDataLYCK = null
    },
    clearCurrentDataLYTK() {
      this.currentDataLYTK = null
    },
    clearCurrentDataBS() {
      this.currentDataBS = null
    },
    clearCurrentDataPLPDAll() {
      this.currentDataPLPDAll = null
    },
    clearCurrentDataPLDBAll() {
      this.currentDataPLDBAll = null
    },

    clearCurrentDataPD() {
      this.currentDataPD = null
    },
    clearCurrentDataPLPD() {
      this.currentDataPLPD = null
    },

    clearCurrentDataPLDB() {
      this.currentDataPLDB = null
    },
    clearCurrentData() {
      this.currentData = null
    },
    clearCurrentReturnData() {
      this.currentReturnData = null
    },
    clearCurrentDataDB() {
      this.currentDataDB = null
    }
  }
})