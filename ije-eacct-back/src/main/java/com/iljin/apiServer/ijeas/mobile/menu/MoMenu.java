package com.iljin.apiServer.ijeas.mobile.menu;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
@Table(name = "a_user_menu_m")
@IdClass(MoMenuKey.class)
public class MoMenu {
    //회사ID
    @Id
    @Column(name ="comp_cd")
    String compCd;

    //사용자ID
    @Id
    @Column(name = "login_id")
    String loginId;

    //메뉴코드
    @Id
    @Column(name = "menu_no")
    String menuNo;

    //메뉴명
    @Column(name = "menu_nm")
    String menuNm;

    //메뉴 순서
    @Column(name = "menu_order")
    Integer menuOrder;

    //메뉴 아이콘
    @Column(name = "menu_icon")
    String menuIcon;

    //수정일
    @Column(name = "modified_date")
    String modifiedDate;

    //remark1
    @Column(name = "remark1")
    String remark1;

    //remark2
    @Column(name = "remark2")
    String remark2;

    //remark3
    @Column(name = "remark3")
    String remark3;

    //remark4
    @Column(name = "remark4")
    String remark4;

    //remark5
    @Column(name = "remark5")
    String remark5;
}
