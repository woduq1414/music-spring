package dimigo.test.MusicSpring.repository;

import dimigo.test.MusicSpring.domain.Music;

import javax.persistence.EntityManager;
import java.util.ArrayList;

public class JPAMusicRepository implements MusicRepository {

    private EntityManager em;

    public JPAMusicRepository(EntityManager em) {
        this.em = em;
    }

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

    @Override
    public ArrayList<Music> findByTitle(String title) {
        ArrayList<Music> list = (ArrayList)em.createQuery
                ("select m from Music m where m.title like :title order by m.id desc", Music.class)
                .setParameter("title","%"+title+"%").getResultList();
        return list;
    }




    @Override
    public ArrayList<Music> findByArtist(String artist) {
        ArrayList<Music> list = (ArrayList)em.createQuery
                ("select m from Music m where m.artist like :artist order by m.id desc", Music.class)
                .setParameter("artist","%"+artist+"%").getResultList();
        return list;
    }


    @Override
    public void updateVideoCode(Music music, String videoCode) {
        music.setVideoCode(videoCode);
    }

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
