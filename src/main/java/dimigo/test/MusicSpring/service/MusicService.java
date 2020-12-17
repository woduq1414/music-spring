package dimigo.test.MusicSpring.service;

import dimigo.test.MusicSpring.domain.Music;
import dimigo.test.MusicSpring.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;

//import javax.transaction.Transactional;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Transactional
public class MusicService {

    private MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public int register(Music m)
    {
        musicRepository.save(m);
        return m.getId();
    }
    public ArrayList<Music> findMembers()
    {
        return musicRepository.findAll();
    }

    public ArrayList<Music> findByTitle(String title)
    {
        return musicRepository.findByTitle(title);
    }

    public ArrayList<Music> findByArtist(String artist)
    {
        return musicRepository.findByArtist(artist);
    }

    public Music findById(int id)
    {
        return musicRepository.findById(id);
    }



    public void updateVideoCode(Music music, String videoCode)
    {
        musicRepository.updateVideoCode(music, videoCode);
    }
    public void deleteMusic(Music music){
        musicRepository.deleteMusic(music);
    }

}
