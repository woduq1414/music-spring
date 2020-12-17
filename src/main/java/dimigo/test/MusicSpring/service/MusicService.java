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
    
    
    //음악 등록
    public int register(Music m)
    {
        musicRepository.save(m);
        return m.getId();
    }
    
    //제목으로 찾기
    public ArrayList<Music> findByTitle(String title)
    {
        return musicRepository.findByTitle(title);
    }

    //아티스트로 찾기
    public ArrayList<Music> findByArtist(String artist)
    {
        return musicRepository.findByArtist(artist);
    }

    public Music findById(int id)
    {
        return musicRepository.findById(id);
    }

    //비디오 코드 수정
    public void updateVideoCode(Music music, String videoCode)
    {
        musicRepository.updateVideoCode(music, videoCode);
    }
    
    //음악 삭제
    public void deleteMusic(Music music){
        musicRepository.deleteMusic(music);
    }

}
