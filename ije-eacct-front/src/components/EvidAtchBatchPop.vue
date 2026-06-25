<template>
<layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="$emit('close', a_files)"></button>
  </span>
  <div slot="content">
    <div class="scan-area">
      <div class="scan-item01">
        <div class="scan-name">
          <span><img src="/img/arr_right.png" alt="image">선택전표</span>
        </div>
          <div class="grid-wrap">
              <ag-grid-vue ref="grid" style="width: 100%; height: calc(50vh - 119px);" class="ag-theme-alpine" 
                      :columnDefs="columnDefs" 
                      :rowData="data" 
                      :gridOptions="gridOptions"
                      :defaultColDef="defaultColDef"
                      
                      @grid-ready="onReady"
                      />                    
          </div>
      </div>
      <div class="scan-item02">
        <div class="scan-name">
          <span><img src="/img/arr_right.png" alt="image">첨부파일</span>
        </div>
        <div class="scan-btn">
          <input type="file" ref="file" @change="proceedFileUpload()" style="display: none;" accept="image/jpeg,image/gif,image/png,image/tiff,application/pdf,application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.wordprocessingml.template, application/zip" multiple v-if="!readonlyCtrl"/>
          <button @click="startFileUpload()" v-if="!readonlyCtrl"><i class="fas fa-plus"></i></button>
          <!-- <button @click="clearAll()" v-if="!readonly"><i class="far fa-trash-alt"></i></button> -->
        </div>
        <div class="scan-desc">
          <ul>
            <li v-for="(file, index) in a_files_view" :key="index" :class="{ 'selected': isSel(file) }">
              <article class="media" @click="toggle(file, file.chk = !file.chk)">
                <div class="media-left">
                  <figure class="image" v-if="isPdf(file)">
                    <img src="/img/pdf.png" alt="Image">
                  </figure>
                  <figure class="image" v-else>
                    <img :src="file.attribute2" alt="Image" onerror="this.src='/img/image.png'">
                  </figure>
                </div>
                <div class="media-content">
                  <div class="content">
                    <p class="name-tit">{{ file.originalName }}</p>
                    <p class="name-sub">{{ file.fileKind }} / {{ getFileSize(file) }}</p>
                  </div>
                </div>
              </article>
              <!-- <button class="scan-trash" @click="clear(file)" v-if="!readonly"><i class="far fa-trash-alt"></i></button> -->
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="scan-img-area">
      <!-- TODO::이미지/PDF 전시 영역 -->
      <div class="display-area" v-if="sel.length > 0">
        <v-zoomer style="width: 100%; height:calc(100vh - 180px);">
        <pdf-viewer :src="sel[0].downloadUrl" style="height: calc(100vh - 200px);" v-if="isPdf(sel[0])" />
        <img :src="sel[0].downloadUrl" style="height:95%;" alt="Image" onerror="this.src='/img/image.png'" v-else />
        </v-zoomer>
      </div>
      <!-- <div class="display-area" v-if="finalSelection !== undefined">
        <img src="/img/pdf.png" alt="Image" />
      </div> -->
    </div>
  </div>
</layout>
</template>

<script>
import Layout from '@/components/ModalSlot4.vue'
import PdfViewer from '@/components/PdfViewer'

export default {
  props: {
    title: {
      type: String,
      required: false,
      default: '증빙일괄첨부'
    },
    docMngNo: {
      type: Array,
      required: true
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
    readonlyCtrl: {
      type: Boolean,
      required: false,
      default: false
    },
    value: {
        type: Array,
        required: false,
        default() {
          return {}
        } 
    }       
  },
  components: {
    Layout,
    PdfViewer
  },
  data() {
    return {
      data: [],
      m_files: [],
      a_files: [],
      a_files_view: [], //일괄 업로드 후 전표별로 모든 증빙 파일이 리스트로 오기 떄문에 한 전표에 대해서만 보여줌
      sel: [],
      dto: [],
      defaultColDef:[],
      gridOptions:[],
      columnDefs:[]
    }
  },
  methods: {
    //그리드 Ready
    onReady(params) {      
        
        //그리드용 api 정의        
        this.gridApi = params.api;
        this.columnApi = params.columnApi;

        this.gridApi.sizeColumnsToFit();
    }, 
    makeColDef(){
      this.columnDefs = [
          {
            headerName: 'No.', 
            width: 55,
            valueFormatter: (params) => {               
                return params.node.rowIndex + 1;
            },
            cellStyle:{textAlign: 'center'}
          },
          {
              headerName: 'ERP번호', 
              field: 'slipNo', 
              width: 145,
              cellStyle:{textAlign: 'center'}
          },
          {
              headerName: '거래처명', 
              field: 'vendorNm', 
              width: 160,
              cellStyle:{textAlign: 'left'}
          }                    
      ]
    },       
    startFileUpload() {
      // Trigger click event
      this.$refs.file.click()
    },
    proceedFileUpload() {    
      let files = this.$refs.file.files

      // Ok to proceed
      if (files.length > 0) {
        let form = new FormData()

        _.forEach(files, (file, i) => {
          form.append(`files`, file)
        })

        form.append('list', this.dto)

        this.$store.commit('loading')
        this.$http.post(`/api/evid/addfiles/bundle`, form , {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Accept':'application/json'
          },
        }).then(response => {
          this.$swal({
            type: 'success',
            text: '파일이 업로드 되었습니다.'
          }).then(() => {

            this.a_files = this.a_files.concat(response.data)

            //팝업 첨부파일 목록에 보여질 리스트
            for(let i = 0; i < files.length; i++){
              this.a_files_view = this.a_files_view.concat(response.data[i])
            }
          })
          return response
        }).catch(response => {
          this.$swal({
            type: 'error',
            text: '파일업로드에 실패하였습니다.'
          })
          return response
        }).finally(() => {
          this.$store.commit('finish')
        })
      }
    },
    clearAll() {
      this.$store.commit('loading')
      this.$http.post('/api/evid/removeFiles', {
        documentHId: this.docMngNo
      }).then(() => {
        let am_files = this.a_files.filter(x => x.attribute3)

        let m_filtered = this.m_files.filter(x => am_files.filter(y => y.id === x.id)[0])
        _.forEach(m_filtered, x => {
          x.chk = false
        })

        this.a_files = []
        this.sel = []
      }).finally(() => {
        this.$store.commit('finish')
      })
    },
    clear(file) {
      if (file.lock) {
        return
      } else {
        // 모바일 파일
        let m_f = this.m_files.filter(x => x.id === file.id)[0]
        // 모바일 파일에 있음
        if (m_f !== undefined) {
          m_f.lock = true
        }
        // 업로드 파일 목록
        let a_f = this.a_files.filter(x => x.id === file.id)[0]
        // 파일 목록에 있다
        if (a_f !== undefined) {
          a_f.lock = true
        }
        this.$store.commit('loading')
        this.$http.post('/api/evid/removeFiles', {
          documentHId: this.docMngNo,
          id: file.id
        }).then(response => {
          // 파일 목록을 정리해봅니다.
          // 모바일 파일
          let m_f = this.m_files.filter(x => x.id === file.id)[0]
          // 모바일 파일에 있음
          if (m_f !== undefined) {
            m_f.chk = false
            delete m_f.lock
            let index = this.sel.indexOf(m_f)
            index >= 0 ? this.sel.splice(index, 1) : 0
          }
          // 업로드 파일 목록
          let a_f = this.a_files.filter(x => x.id === file.id)[0]
          // 파일 목록에 있다
          if (a_f !== undefined) {
            a_f.chk = false
            let index = this.sel.indexOf(a_f)
            index >= 0 ? this.sel.splice(index, 1) : 0
            this.a_files.splice(this.a_files.indexOf(a_f), 1)
          }
        }).finally(() => {
          this.$store.commit('finish')
        })
      }
    },
    /**
     * 선택 이벤트 핸들러
     */
    toggle(file, callback) {
      if (file.chk) {
        this.sel.unshift(file)
      } else {
        this.sel.splice(this.sel.indexOf(file), 1)
      }

      if (callback !== undefined && typeof callback === 'function') {
        callback.apply(this, [file])
      }
    },
    mobileCheckHandler(file) {
      // 추가를 해야한다
      if (file.chk) {
        this.$http.post('/api/evid/mCheck', {
          documentHId: this.docMngNo,
          id: file.id,
          attribute4: 'Y'
        }).then(response => {
          this.a_files.unshift(response.data)
          this.a_files = this.a_files.sort((a, b) => a.modifiedDate > b.modifiedDate)
        })
      } else {
        this.clear(file)
      }
    },
    /**
     * 선택되었는지 체크
     */
    isSel(file) {
      return file.chk
    },
    /**
     * 서버에서 첨부된 파일 정보를 가져온다
     */
    query() {
      if(this.docMngNo) {
        this.$store.commit('loading')
        this.$http.get(`/api/evid/fileList/temp`)
        .then(response => {
          let m_files = response.data.mFiles.map(x => {
            x.chk = false
            return x
          })
          
          this.m_files = m_files
          
          this.sel = m_files.filter(x => x.chk)
        })
        .finally(() => {
          this.$store.commit('finish')
        })
      }
    },
    isPdf(item) {
      return item && item.fileKind === '.pdf'
    },
    getFileSize(file) {
      let types = ['Byte', 'KB', 'MB', 'GB', 'TB', 'PB']
      var amt = (file || {}).fileAmount || 0
      var cnt = 0
      while (amt > 1000) {
        amt /= 1000
          ++cnt
      }
      let rst = ''
      if (cnt === 0) {
        rst = [this.$numeral(amt).format('0,0'), types[cnt]].join(' ')
      } else {
        rst = [this.$numeral(amt).format('0,0.00'), types[cnt]].join(' ')
      }
      return rst
    }
  }
  ,created() {
    this.docMngNo.forEach(v => {this.dto.push(v)})
    this.query()
    this.makeColDef();
  },
  watch: {
      value: {
          immediate: true,
          deep: true,
          handler(value) {
              if(value !== undefined) {
                  this.data = value
              }
          }
      }
  },  
}
</script>

<style lang="scss" scoped>
.modal-card {
  width: 600px;
}

.display-area {
  ::v-deep img {
    width: 100%;
  }
}
</style>
