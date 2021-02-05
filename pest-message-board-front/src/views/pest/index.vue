<template>
  <div class="pest-container">
    <el-carousel :interval="4000" type="card" height="200px">
      <el-carousel-item v-for="item in pics" :key="item">
        <el-image :src=item>
          <div slot="placeholder" class="image-slot">
            加载中<span class="dot">...</span>
          </div>
        </el-image>
      </el-carousel-item>
    </el-carousel>
    <el-card class="pest-card-1">
      <div slot="header">
        <span class="pest-card-1-title">全国疫情数据</span>
      </div>
      <div class="pest-card-1-body">
        <div>
          <span>确诊:</span>
          <el-tooltip :content="contrastYesData.confirmData" placement="bottom"
                      effect="light">
            <span v-text="chinaPestData.confirmData"></span>
          </el-tooltip>
        </div>
        <div>
          <span>疑似:</span>
          <el-tooltip :content="contrastYesData.suspectData" placement="bottom"
                      effect="light">
            <span v-text="chinaPestData.suspectData"></span>
          </el-tooltip>
        </div>
        <div>
          <span>死亡:</span>
          <el-tooltip :content="contrastYesData.deadData" placement="bottom" effect="light">
            <span v-text="chinaPestData.deadData"></span>
          </el-tooltip>
        </div>
        <div>
          <span>治愈:</span>
          <el-tooltip :content="contrastYesData.healData" placement="bottom" effect="light">
            <span v-text="chinaPestData.healData"></span>
          </el-tooltip>
        </div>
      </div>
    </el-card>
    <el-card class="pest-card-2">
      <div slot="header">
        <div class="pest-card-2-title">
          <span v-text="curCity"></span>
          <span>疫情数据</span>
        </div>
        <div class="pest-card-2-select">
          <el-select v-model="value" placeholder="请选择" @change="changeCity">
            <el-option
              v-for="item in cities"
              :key="item.value"
              :label="item.label"
              :value="item.value">
              <span style="float: left">{{ item.label }}</span>
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="pest-card-2-body">
        <div>
          <span>新增确诊:</span>
          <span v-text="areaPestData.areaNewConfirmData"></span>
        </div>
        <div>
          <span>确诊:</span>
          <span v-text="areaPestData.areaConfirm"></span>
        </div>
        <div>
          <span>死亡:</span>
          <span v-text="areaPestData.areaDeadData"></span>
        </div>
        <div>
          <span>治愈:</span>
          <span v-text="areaPestData.areaHealData"></span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getPestData } from '@/api/pest'

export default {
  name: 'PestIndex',
  components: {},
  props: {},
  data () {
    return {
      pics: [
        require('@/assets/img1.jpeg'),
        require('@/assets/img2.jpeg'),
        require('@/assets/img3.jpeg')
      ],
      contrastYesData: {
        confirmData: '',
        suspectData: '',
        deadData: '',
        healData: ''
      },
      value: '',
      chinaPestData: {
        confirmData: 0,
        suspectData: 0,
        deadData: 0,
        healData: 0
      },
      curCity: '安徽',
      cities: [{
        value: 'HuBei',
        label: '湖北'
      }, {
        value: 'GuangDong',
        label: '广东'
      }, {
        value: 'HeNan',
        label: '河南'
      }, {
        value: 'ZheJiang',
        label: '浙江'
      }, {
        value: 'HuNan',
        label: '湖南'
      }, {
        value: 'AnHui',
        label: '安徽'
      }, {
        value: 'JiangXi',
        label: '江西'
      }, {
        value: 'JiangSu',
        label: '江苏'
      }, {
        value: 'ChongQin',
        label: '重庆'
      }, {
        value: 'ShanDong',
        label: '山东'
      }, {
        value: 'SiChuan',
        label: '四川'
      }, {
        value: 'HeLongJiang',
        label: '黑龙江'
      }, {
        value: 'BeiJing',
        label: '北京'
      }, {
        value: 'ShangHai',
        label: '上海'
      }, {
        value: 'HeBei',
        label: '河北'
      }, {
        value: 'FuJian',
        label: '福建'
      }, {
        value: 'GuangXi',
        label: '广西'
      }, {
        value: 'ShaXi',
        label: '陕西'
      }, {
        value: 'YunNan',
        label: '云南'
      }, {
        value: 'HaiNan',
        label: '海南'
      }, {
        value: 'GuiZhou',
        label: '贵州'
      }, {
        value: 'ShanXi',
        label: '山西'
      }, {
        value: 'TianJin',
        label: '天津'
      }, {
        value: 'LiaoNing',
        label: '辽宁'
      }, {
        value: 'GanSu',
        label: '甘肃'
      }, {
        value: 'JiLin',
        label: '吉林'
      }, {
        value: 'XinJiang',
        label: '新疆'
      }, {
        value: 'NeiMengGu',
        label: '内蒙古'
      }, {
        value: 'NingXia',
        label: '宁夏'
      }, {
        value: 'XiangGang',
        label: '香港'
      }, {
        value: 'TaiWan',
        label: '台湾'
      }, {
        value: 'QinHai',
        label: '青海'
      }, {
        value: 'AoMen',
        label: '澳门'
      }, {
        value: 'XiZang',
        label: '西藏'
      }],
      areaPestData: {
        areaNewConfirmData: 0,
        areaConfirm: 0,
        areaDeadData: 0,
        areaHealData: 0
      }
    }
  },
  computed: {},
  watch: {},
  created () {
  },
  mounted () {
    this.onPestData()
  },
  methods: {
    changeCity (curCity) {
      let label = ''
      label = this.cities.find((item) => {
        return item.value === curCity
      })
      this.curCity = label.label
      for (const key in this.cityData) {
        if (key === this.curCity) {
          this.areaPestData.areaConfirm = this.cityData[key].totalData.confirm
          this.areaPestData.areaNewConfirmData = this.cityData[key].todayData.confirm
          this.areaPestData.areaHealData = this.cityData[key].totalData.heal
          this.areaPestData.areaDeadData = this.cityData[key].totalData.dead
          if (this.areaPestData.areaNewConfirmData === '') {
            this.areaPestData.areaNewConfirmData = '待更新'
          }
        }
      }
    },
    onPestData () {
      getPestData()
        .then(response => {
          this.chinaPestData.confirmData = response.data.totalData.confirm
          this.chinaPestData.suspectData = response.data.totalData.suspect
          this.chinaPestData.healData = response.data.totalData.heal
          this.chinaPestData.deadData = response.data.totalData.dead
          this.cityData = response.data.cityData
          this.changeContrastData(response)
        })
        .catch(err => {
          console.log(err)
        })
    },
    changeContrastData (response) {
      if (response.data.todayData.confirm === null) {
        return
      }
      if (response.data.todayData.confirm >= 0) {
        this.contrastYesData.confirmData = '较昨日增长' + response.data.todayData.confirm.toString()
      } else {
        this.contrastYesData.confirmData = '较昨日减少' + response.data.todayData.confirm.toString()
      }
      if (response.data.todayData.suspect >= 0) {
        this.contrastYesData.suspectData = '较昨日增长' + response.data.todayData.suspect.toString()
      } else {
        this.contrastYesData.suspectData = '较昨日减少' + response.data.todayData.suspect.toString()
      }
      if (response.data.todayData.heal >= 0) {
        this.contrastYesData.healData = '较昨日增长' + response.data.todayData.heal.toString()
      } else {
        this.contrastYesData.healData = '较昨日减少' + response.data.todayData.heal.toString()
      }
      if (response.data.todayData.dead >= 0) {
        this.contrastYesData.deadData = '较昨日增长' + response.data.todayData.dead.toString()
      } else {
        this.contrastYesData.deadData = '较昨日减少' + response.data.todayData.dead.toString()
      }
    }
  }
}
</script>

<style scoped lang="less">
.pest-card-1 {
  margin-top: 30px;

  .pest-card-1-title {
    font-size: 30px;
  }

  .pest-card-1-body {
    line-height: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    height: 100px;
  }
}

.pest-card-2 {
  margin-top: 30px;

  .pest-card-2-title {
    font-size: 30px;
  }

  .pest-card-2-select {
    margin-top: 20px;
  }

  .pest-card-2-body {
    line-height: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 18px;
    height: 100px;
  }
}

</style>
