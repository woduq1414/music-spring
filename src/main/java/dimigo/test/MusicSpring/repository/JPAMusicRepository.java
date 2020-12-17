package dimigo.test.MusicSpring.repository;

import dimigo.test.MusicSpring.domain.Music;

import javax.persistence.EntityManager;
import java.util.ArrayList;

public class JPAMusicRepository implements MusicRepository {

    private EntityManager em;

    public JPAMusicRepository(EntityManager em) {
        this.em = em;
    }


    //음악 등록
    @Override
    public Music save(Music music) {
        em.persist(music);
        return music;
    }

    @Override
    public Music findById(int id) {
        Music m = em.find(Music.class,id);
        return m;
    }


    //제목으로 찾기
    @Override
    public ArrayList<Music> findByTitle(String title) {
        ArrayList<Music> list = (ArrayList)em.createQuery
                ("select m from Music m where m.title like :title order by m.id desc", Music.class)
                .setParameter("title","%"+title+"%").getResultList();
        return list;
    }


    //아티스트로 찾기
    @Override
    public ArrayList<Music> findByArtist(String artist) {
        ArrayList<Music> list = (ArrayList)em.createQuery
                ("select m from Music m where m.artist like :artist order by m.id desc", Music.class)
                .setParameter("artist","%"+artist+"%").getResultList();
        return list;
    }


    //비디오 코드 수정
    @Override
    public void updateVideoCode(Music music, String videoCode) {
        music.setVideoCode(videoCode);
    }


    //음악 삭제
    @Override
    public void deleteMusic(Music music) {
        em.remove(music);
    }



    @Override
    public ArrayList<Music> findAll() {
        return (ArrayList)em.createQuery
                ("select m from Music m", Music.class).getResultList();
    }
}
