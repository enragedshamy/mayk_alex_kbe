package de.htw.ai.kbe.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "song")
public class Song {
    private int id;
    private String title;
    private String artist;
    private String album;
    private int released;

    public Song() {

    }

    public Song(int id, String title, String artist, String album, int released) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.released = released;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }
}
