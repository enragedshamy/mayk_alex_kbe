package de.htw.ai.kbe.songsServlet;

//import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement(name = "song")
public class Song {

    private Integer id;
	private String title;
	private String artist;
	private String album;
	private Integer released;

	public Song() {    
	}

	public Song(Integer id, String title, String artist, String album, Integer released) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.released = released;
    }
	
	public Integer getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public Integer getReleased() {
		return released;
	}

    @Override
    public String toString() {
        return "OurSong [id=" + id + ", title=" + title + ", artist=" + artist + ", album=" + album + ", released="
                + released + "]";
    }
	
}
