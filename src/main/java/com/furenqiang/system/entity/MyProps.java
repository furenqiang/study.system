package com.furenqiang.system.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Eric
 * */
@Component
@ConfigurationProperties(prefix="myprops") //接收application-dev.yml中的myProps下面的属性
public class MyProps {
    public String fileUrl;

    public String filePath;

    public String unzipPath;

    public String ftpIp;

    public int ftpPort;

    public String ftpUsername;

    public String ftpPassword;

    public String ftpPath;

    public String ftpUnzipPath;

    public String ftpUnzipUrl;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUnzipPath() {
        return unzipPath;
    }

    public void setUnzipPath(String unzipPath) {
        this.unzipPath = unzipPath;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getFtpUnzipPath() {
        return ftpUnzipPath;
    }

    public void setFtpUnzipPath(String ftpUnzipPath) {
        this.ftpUnzipPath = ftpUnzipPath;
    }

    public String getFtpUnzipUrl() {
        return ftpUnzipUrl;
    }

    public void setFtpUnzipUrl(String ftpUnzipUrl) {
        this.ftpUnzipUrl = ftpUnzipUrl;
    }
}

