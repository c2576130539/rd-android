package com.cc.rd.bean.vo;

import lombok.Data;

@Data
public class FileVO {
    private String hash;
    private String originName;
    private Long fileSize;
}