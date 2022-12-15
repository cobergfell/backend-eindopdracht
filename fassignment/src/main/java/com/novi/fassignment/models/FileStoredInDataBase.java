//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.models;

import javax.persistence.*;

@Entity
@Table(name = "files_database")
public class FileStoredInDataBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String name;
    private String description;
    private String type;
    private String bytesInDatabaseUrl;//url to bytes
    private String fileOnDiskUrl;// url file stored on disc; Remark: fileOnDiskUrl as attribute name was not recognised in Spring boot but file_on_disk_url is recognised, do not ask me why...
    private Long fileOnDiskId;// idfile stored on disc
    @Lob
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questionId")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answerId")
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paintingId")
    private Painting painting;

    //default constructor needs to be given otherwise Postman send a 500 error, the reason for that is still unclear
    public FileStoredInDataBase(){
        super();
    }


/*    public FileStoredInDataBase(String name, String type, byte[] data) {
        //this.fileId = fileId;
        this.name = name;
        this.type = type;
        this.data = data;
    }*/

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBytesInDatabaseUrl() {
        return bytesInDatabaseUrl;
    }

    public void setBytesInDatabaseUrl(String bytesInDatabaseUrl) {
        this.bytesInDatabaseUrl = bytesInDatabaseUrl;
    }

    public String getFileOnDiskUrl() {
        return fileOnDiskUrl;
    }

    public void setFileOnDiskUrl(String fileOnDiskUrl) {
        this.fileOnDiskUrl = fileOnDiskUrl;
    }

    public Long getFileOnDiskId() {
        return fileOnDiskId;
    }

    public void setFileOnDiskId(Long fileOnDiskId) {
        this.fileOnDiskId = fileOnDiskId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Painting getPainting() {
        return painting;
    }

    public void setPainting(Painting painting) {
        this.painting = painting;
    }
}