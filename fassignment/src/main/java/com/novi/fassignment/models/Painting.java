package com.novi.fassignment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "paintings")
public class Painting  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paintingId;
    private String title;
    private String artist;
    private String description;
    private byte[] image;

    @CreationTimestamp
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:01")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="GMT+00:01")
    //private ZonedDateTime dateTimePosted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimePosted;


    @UpdateTimestamp
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:01")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="GMT+00:01")
    //private ZonedDateTime lastUpdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

/*    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
    @Column(name = "create_date")
    private Date dateTimePosted;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
    @Column(name = "modify_date")
    private Date lastUpdate;*/



    @OneToMany(
            targetEntity = com.novi.fassignment.models.FileStoredInDataBase.class,
            mappedBy = "painting",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<FileStoredInDataBase> files = new HashSet<>();

    @OneToMany(
            targetEntity = com.novi.fassignment.models.MusicFileStoredInDataBase.class,
            mappedBy = "painting",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<MusicFileStoredInDataBase> musicFiles = new HashSet<>();



    @OneToMany(
            targetEntity = com.novi.fassignment.models.Question.class,
            mappedBy = "painting",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<com.novi.fassignment.models.Question> questions = new HashSet<>();

    @OneToMany(
            targetEntity = com.novi.fassignment.models.Answer.class,
            mappedBy = "painting",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<com.novi.fassignment.models.Answer> answers = new HashSet<>();



/*    @ManyToMany
    @JoinTable(
            name = "painting_files_join_table",
            joinColumns = @JoinColumn(name = "paintingId"),
            inverseJoinColumns = @JoinColumn(name = "filesId"))
    private List<FileStoredInDataBase> files = new ArrayList<FileStoredInDataBase>();*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;

    public Long getPaintingId() {
        return paintingId;
    }

    public void setPaintingId(Long paintingId) {
        this.paintingId = paintingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public LocalDateTime getDateTimePosted() {
        return dateTimePosted;
    }

    public void setDateTimePosted(LocalDateTime dateTimePosted) {
        this.dateTimePosted = dateTimePosted;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<FileStoredInDataBase> getFiles() {
        return files;
    }

    public void setFiles(Set<FileStoredInDataBase> files) {
        this.files = files;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<MusicFileStoredInDataBase> getMusicFiles() {
        return musicFiles;
    }

    public void setMusicFiles(Set<MusicFileStoredInDataBase> musicFiles) {
        this.musicFiles = musicFiles;
    }
}
