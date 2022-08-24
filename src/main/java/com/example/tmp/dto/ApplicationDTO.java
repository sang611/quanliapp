package com.example.tmp.dto;

import org.springframework.web.multipart.MultipartFile;

public class ApplicationDTO {
    private String name;
    private  String image;
    private String os_type;
    private float version;
    private float build_number;
    private String shortdescripti;
    private String descripti;
    private MultipartFile document;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOs_type() {
        return os_type;
    }

    public void setOs_type(String os_type) {
        this.os_type = os_type;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public float getBuild_number() {
        return build_number;
    }

    public void setBuild_number(float build_number) {
        this.build_number = build_number;
    }

    public String getShortdescripti() {
        return shortdescripti;
    }

    public void setShortdescripti(String shortdescripti) {
        this.shortdescripti = shortdescripti;
    }

    public String getDescripti() {
        return descripti;
    }

    public void setDescripti(String descripti) {
        this.descripti = descripti;
    }

    public MultipartFile getDocument() {
        return document;
    }

    public void setDocument(MultipartFile document) {
        this.document = document;
    }
}
