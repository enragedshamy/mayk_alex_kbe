package de.htw.ai.kbe.model;

import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "songList")
@Entity
@Table(name = "SongList")
public class SongList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private boolean isPrivate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "SongList_Song",
            joinColumns = {@JoinColumn(name = "songList_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id", referencedColumnName = "id")}
    )
    private Set<Song> songList;

    public SongList() {

    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isPublic() {
        return !isPrivate;
    }

    public Set<Song> getSongList() {
        return songList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setSongList(Set<Song> songList) {
        this.songList = songList;
    }
}
