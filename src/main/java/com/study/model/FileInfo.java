package com.study.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class FileInfo {
    private static final long serialVersionUID = -6812242071705361506L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;

    private String fileaddress;

    private String filedescription;

    private String fileimgsrc;

    private String createtime;

    private String enable;

    private String filetype;

    private String subtitle;

    private String disciplines;

    private String pdfurl;

    private String appvideourl;

    public FileInfo() {
    }

    public FileInfo(Integer id, String filename, String fileaddress, String filedescription, String fileimgsrc, String createtime, String enable,
                    String filetype, String subtitle, String disciplines, String pdfurl, String appvideourl) {
        this.id=id;
        this.filename=filename;
        this.fileaddress=fileaddress;
        this.filedescription=filedescription;
        this.fileimgsrc=fileimgsrc;
        this.createtime=createtime;
        this.enable=enable;
        this.filetype=filetype;
        this.subtitle=subtitle;
        this.disciplines=disciplines;
        this.pdfurl=pdfurl;
        this.appvideourl=appvideourl;
    }
    @Column(name = "package_name")
    private String packageName;
    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return fileaddress
     */
    public String getFileaddress() {
        return fileaddress;
    }

    /**
     * @param fileaddress
     */
    public void setFileaddress(String fileaddress) {
        this.fileaddress = fileaddress;
    }

    /**
     * @return filedescription
     */
    public String getFiledescription() {
        return filedescription;
    }

    /**
     * @param filedescription
     */
    public void setFiledescription(String filedescription) {
        this.filedescription = filedescription;
    }

    /**
     * @return fileimgsrc
     */
    public String getFileimgsrc() {
        return fileimgsrc;
    }

    /**
     * @param fileimgsrc
     */
    public void setFileimgsrc(String fileimgsrc) {
        this.fileimgsrc = fileimgsrc;
    }

    /**
     * @return createtime
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    /**
     * @return enable
     */
    public String getEnable() {
        return enable;
    }

    /**
     * @param enable
     */
    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(String disciplines) {
        this.disciplines = disciplines;
    }

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }

    public String getAppvideourl() {
        return appvideourl;
    }

    public void setAppvideourl(String appvideourl) {
        this.appvideourl = appvideourl;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}