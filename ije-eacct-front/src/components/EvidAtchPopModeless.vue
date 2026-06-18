<template>
<layout>
  <span slot="header">
    {{ title }}
    <button class="btn-pop-close delete" aria-label="close" @click="$parent.close"></button>
  </span>
  <div slot="content">
    <div class="scan-area">
<!--      <div class="scan-item01">-->
<!--        <div class="scan-name">-->
<!--          <span><img src="/img/arr_right.png" alt="image">모바일 스캔</span>-->
<!--        </div>-->
<!--        <div class="scan-desc">-->
<!--          <ul>-->
<!--            <li v-for="(file, index) in m_files" :key="index">-->
<!--              <article class="media" v-if="!isSel(file)">-->
<!--                <input type="checkbox" :id="'mobile-file-' + index" v-model="file.chk" @change="toggle(file, mobileCheckHandler)" v-if="!readonly" /><label :for="'mobile-file-' + index" v-if="!readonly">&nbsp;</label>-->
<!--                <div class="media-left">-->
<!--                  <figure class="image" v-if="isPdf(file)">-->
<!--                    <img src="/img/pdf.png" alt="Image">-->
<!--                  </figure>-->
<!--                  <figure class="image" v-else>-->
<!--                    <img :src="file.attribute2" alt="Image" onerror="this.src='/img/image.png'" style="object-fit: contain; width: 100%; height: 100%;">-->
<!--                  </figure>-->
<!--                </div>-->
<!--                <div class="media-content">-->
<!--                  <div class="content">-->
<!--                    <p class="name-tit">{{ file.originalName }}</p>-->
<!--                    <p class="name-sub">{{ $moment(file.modifiedDate).format('MM.DD(dd) HH:mm') }}</p>-->
<!--                  </div>-->
<!--                </div>-->
<!--              </article>-->
<!--            </li>-->
<!--          </ul>-->
<!--        </div>-->
<!--      </div>-->
      <div class="scan-item02">
        <div class="scan-name">
          <span><img src="/img/arr_right.png" alt="image">첨부파일</span>
        </div>
        <div class="scan-btn" style="margin-right: 10px;">
          <input type="file" ref="file" @change="proceedFileUpload()" style="display: none;" accept="image/jpeg,image/gif,image/png,image/tiff,application/pdf,application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document, application/vnd.openxmlformats-officedocument.wordprocessingml.template, application/zip" multiple v-if="!readonlyCtrl"/>
          <button @click="fileDownload()"><i class="fas fa-download"></i></button>
          <button @click="startFileUpload()" v-if="!readonlyCtrl"><i class="fas fa-plus"></i></button>
          <button @click="clearAll()" v-if="!readonly"><i class="far fa-trash-alt"></i></button>
        </div>
        <div class="scan-desc" @dragover.prevent @dragenter.prevent @drop.prevent="dragoverInput" style="height: 628px; margin-right: 10px;">
          <ul>
            <li v-for="(file, index) in a_files" :key="index" :class="{ 'selected': isSel(file) }">
              <article class="media" @click="toggle(file, file.chk = !file.chk)" @dblclick="doubleClick(file)">
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
              <button class="scan-trash" @click="clear(file)" v-if="!readonly"><i class="far fa-trash-alt"></i></button>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="scan-img-area">
      <!-- TODO::이미지/PDF 전시 영역 -->
      <div class="display-area" v-if="sel.length > 0">
        <v-zoomer style="width: 100%; height:calc(100vh - 180px);">
<!--        <pdf-viewer :src="sel[0].downloadUrl" style="height:calc(100vh - 200px);" v-if="isPdf(sel[0])  && !isIE" />-->
<!--        <iframe :src="sel[0].downloadUrl" style="width: 100%; height:calc(100vh - 200px);" v-else-if="isPdf(sel[0]) && isIE"/>-->
        <iframe :src="sel[0].downloadUrl" style="width: 100%; height:calc(100vh - 200px);" v-if="isPdf(sel[0])"/>
        <img id="img" :src="sel[0].downloadUrl" style="height:80%;" alt="Image" onerror="this.src='/img/image.png'" @click="rotate('img')" v-else />
        </v-zoomer>
      </div>
    </div>
  </div>
</layout>
</template>

<script>

import Layout from '@/components/ModalWindow.vue'
import PdfViewer from '@/components/PdfViewer.vue'

export default {
  compatConfig: { MODE: 2 },
  props: {
    title: {
      type: String,
      required: false,
      default: '증빙첨부'
    },
    slipNo: {
      Type: String,
      required: false
    }
    // docMngNo: {
    //   type: String,
    //   required: true
    // },
    ,disabled: {
      type: Boolean,
    },
    rdoCtrl: {
      type: Boolean,
    }
  },
  components: {
    Layout,
    PdfViewer
  },
  data() {
    return {
      m_files: [],
      a_files: [],
      sel: [],
      isIE : false,
      windowRef: null,
      docMngNo : null,
      readonly : false,
      readonlyCtrl : false
    }
  },
  methods: {
    doubleClick(file){
      
      this.sel.unshift(file)

      if( file && file.fileKind === '.pdf'){
        //pdf
        window.open(this.sel[0].downloadUrl,"" , "width=1000,height=600,left=200,top=200");
      }else{
        //img
        this.windowRef = window.open("", "", "width=1000,height=600,left=200,top=200");
        this.windowRef.document.body.innerHTML += "<img src='" + this.sel[0].downloadUrl + "' style='height: 100%; width: 100%;'>";
      }

    },
    rotate(pId){
      let style = document.getElementById( pId ).getAttribute('style');

      if(style.includes("rotate")){
        
        if(style.includes("90deg")) document.getElementById( pId ).setAttribute( 'style', style.replace("90deg","180deg")) 

        if(style.includes("180deg")) document.getElementById( pId ).setAttribute( 'style', style.replace("180deg","270deg")) 

        if(style.includes("270deg")) document.getElementById( pId ).setAttribute( 'style', style.replace("270deg","360deg")) 

        if(style.includes("360deg")) document.getElementById( pId ).setAttribute( 'style', style.replace("360deg","90deg")) 

      } else{
        document.getElementById( pId ).setAttribute( 'style', style + 'transform:rotate(90deg);')
      } 
    },
    fileDownload() {
      let chk_files = this.a_files.filter(x => x.chk) 
      console.log("check file ", chk_files)

      if(chk_files.length === 0) {
        this.$swal({
          type: 'warning',
          text: '파일을 선택해주세요.'
        })
      }else {
        chk_files.forEach((x, i) => {
          this.$store.commit('loading');
          let url = ''

          if(x.downloadUrl.indexOf("/download/before") > -1){
            url = 'api/v1/file/download/before/' + x.storedName + '?id=' + x.id
          }else{
            url = 'api/v1/file/download2/' + x.storedName
          }

          this.$http.get(url, { responseType: 'blob' })
            .then((response) => {
                const downloadedfile = new Blob([response.data], { type: 'blob' });
                let url;

                const contentDisposition = response.headers['content-disposition'];
                let fileName = 'unknown';
                if (contentDisposition) {
                    const fileNameMatch = contentDisposition.match(/filename="(.+)"/);
                    if (fileNameMatch.length === 2)
                        fileName = fileNameMatch[1];
                }

                if (window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveOrOpenBlob(downloadedfile, fileName);
                } else {
                    url = window.URL.createObjectURL(downloadedfile);

                    const link = document.createElement('a');
                    link.href = url;
                    link.setAttribute('download', x.originalName);
                    document.body.appendChild(link);
                    link.click();
                }

            }).catch(response => {
              console.log("catch response" , response)
            }).finally(()=> {
              this.$store.commit('finish');
            });
          
        })
      }
      
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

        this.$store.commit('loading')
        this.$http.post(`/api/evid/addFiles/${this.docMngNo}`, form, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }).then(response => {
          this.$swal({
            type: 'success',
            text: '파일이 업로드 되었습니다.'
          }).then(() => {
            this.a_files = this.a_files.concat(response.data)
            this.storeFileCnt()
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
        this.storeFileCnt()
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
          this.storeFileCnt()
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
          this.storeFileCnt()
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
        this.$http.get(`/api/evid/fileList/${this.docMngNo}`)
        .then(response => {   
          let m_files = response.data.mFiles.map(x => {
            x.chk = false
            return x
          })
          let a_files = response.data.aFiles.map(x => {
            x.chk = false
            return x
          })

          let a_filtered = m_files.filter(x => a_files.filter(y => y.id === x.id)[0])
          _.forEach(a_filtered, x => {
            x.chk = true
          })

          // let array = m_files.filter(item1 => a_files.some(item2 => (item2.id !== item1.id)));
                
          this.m_files = m_files
          this.a_files = a_files
          this.sel = m_files.filter(x => x.chk)
        })
        .finally(() => {
          // this.storeFileCnt()
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
    },
    storeFileCnt(){
      this.$store.commit('setEvidFileSize', this.a_files.length)
      localStorage.setItem("popFileCnt", this.a_files.length);
    },
    dragoverInput(event){

      if(this.readonlyCtrl){
        this.$swal({type: 'warning', text: '상신 이후에는 업로드가 불가능 합니다.'});
        return
      }

      let files = event.dataTransfer.files

      this.$swal({
        type: 'warning',
        text: `image, pdf 타입이 아닌 파일은 미리보기를 지원하지 않습니다. 계속 업로드 하시겠습니까?`,
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오',
      }).then(result => {
        if(result.value){
          // Ok to proceed
          if (files.length > 0) {
            let form = new FormData()

            _.forEach(files, (file, i) => {
              form.append(`files`, file)
            })

            this.$store.commit('loading')
            this.$http.post(`/api/evid/addFiles/${this.docMngNo}`, form, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }).then(response => {
              this.$swal({
                type: 'success',
                text: '파일이 업로드 되었습니다.'
              }).then(() => {
                this.a_files = this.a_files.concat(response.data);
                this.storeFileCnt();
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
        }else{
          return
        }
      })

    }
  },
  created() {
    this.docMngNo = this.$route.query.docMngNo || this.slipNo;

    this.readonly = this.disabled || (this.$route.query.readonly === 'true')

    this.readonlyCtrl = this.rdoCtrl || (this.$route.query.readonlyCtrl === 'true')

    if(this.$store.state.loginInfo.authorities[0].roleCd === 'ADMIN' || this.$store.state.loginInfo.authorities[0].roleCd === 'ACCOUNT'){
      this.readonly = false
      this.readonlyCtrl = false
    }
    
    //브라우저 체크(IE 체크)
    var agent = navigator.userAgent.toLowerCase();    
    if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)){
      this.isIE = true;
    }

    this.query()
    console.log(this.docMngNo);
  },
  destroyed() {
    this.$emit('close', this.a_files);
  }
}
</script>

<style lang="scss" scoped>
.modal-card {
  width: 1060px;
  height: 100%;
}

.display-area {
  :global(img) {
    width: 100%;
  }
}
</style>

