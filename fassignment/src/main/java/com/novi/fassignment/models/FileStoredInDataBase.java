//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.models;

import javax.persistence.*;

@Entity
@Table(name = "files_database")
public class FileStoredInDataBase {
    //@Id
    //@GeneratedValue(generator = "uuid")
    //@GenericGenerator(name = "uuid", strategy = "uuid2")
    //private String file_id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String name;

    private String type;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicPieceId")
    private MusicPiece musicPiece;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public MusicPiece getMusicPiece() {
        return musicPiece;
    }

    public void setMusicPiece(MusicPiece musicPiece) {
        this.musicPiece = musicPiece;
    }

}