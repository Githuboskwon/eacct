package com.iljin.apiServer.core.files;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name="FILE_GEN", //시퀀스 제너레이터 이름
        sequenceName="file_seq", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)//Oracle
@Table(name = "A_FILE")
public class File {
    @Id
    @Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "FILE_GEN")//Oracle
    Long id;

    @Column(name = "comp_cd")
    String compCd;

    @Column(name = "file_type")
    String fileType;

    //    @ManyToOne(fetch=FetchType.LAZY)
    //    @JoinColumn(name = "document_h_id", insertable = false, updatable = false)
    //    @JsonIgnore
    //    Document document;
    @Column(name = "document_h_id")
    Long documentHId;

    @Column(name = "remark")
    String remark;

    @Column(name = "seq")
    Long seq;

    @Column(name = "original_name")
    String originalName;
    @Column(name = "stored_name")
    String storedName;
    @Column(name = "download_url")
    String downloadUrl;

    @Column(name = "file_cat")
    String fileCat;

    @Column(name = "file_size")
    Long fileSize;

    @Column(name = "attribute_1")
    String attribute1;
    @Column(name = "attribute_2")
    String attribute2;
    @Column(name = "attribute_3")
    String attribute3;
    @Column(name = "attribute_4")
    String attribute4;
    @Column(name = "attribute_5")
    String attribute5;

    @Column(name = "created_by")
    Long createdBy = Long.valueOf(1);
    @Column(name = "creation_date", insertable = false, updatable = false)
    LocalDateTime creationDate;
    @Column(name = "modified_by")
    Long modifiedBy = Long.valueOf(1);
    @Column(name = "modified_date", insertable = false, updatable = false)
    LocalDateTime modifiedDate;

    @Builder
    public File(String originalName, String storedName, String downloadUrl) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.downloadUrl = downloadUrl;
    }

    public File updateDocumentHId(Long documentHId) {
        this.documentHId = documentHId;

        return this;
    }

    public File updateDocumentHIdAndFileTypeAndSeq(Long documentHId, String fileType, Long seq) {
        this.documentHId = documentHId;
        this.fileType = fileType;
        this.seq = seq;

        return this;
    }

    public File updateRemarkAndSeq(String remark, Long seq) {
        this.remark = remark;
        this.seq = seq;

        return this;
    }
}
