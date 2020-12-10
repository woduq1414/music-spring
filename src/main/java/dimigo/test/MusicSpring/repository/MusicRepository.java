package dimigo.test.MusicSpring.repository;

import dimigo.test.MusicSpring.domain.Music;

import java.util.ArrayList;

public interface MusicRepository {

    Music save(Music music);
    Music findById(int id);
    ArrayList<Music> findByTitle(String title);
    ArrayList<Music> findByArtist(String artist);
    ArrayList<Music> findAll();
    void updateVideoCode(Music music, String videoCode);
    void deleteMusic(Music music);
}
