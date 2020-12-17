package dimigo.test.MusicSpring.controller;

import dimigo.test.MusicSpring.domain.Music;
import dimigo.test.MusicSpring.service.MusicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class MusicController {

    private MusicService ms;

    public MusicController(MusicService ms) {
        this.ms = ms;
    }

    // 유튜브 url -> 비디오 코드만 추출하는 함수
    public String parseVideoCode(String urlString){

        URL url = null;
        String videoCode = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {

            e.printStackTrace();
            return null;
        }
        System.out.println(url.getHost());

        String host = url.getHost();
        String query = url.getQuery();
        if(host.equals("www.youtube.com")){
            String[] params = query.split("&");
            for (String param : params){
                if(param.split("=")[0].equals("v")){
                    videoCode = param.split("=")[1];
                }
            }
        }else if(host.equals("youtu.be")){
            videoCode = url.getPath().split("/")[1];
        }

        System.out.println(videoCode);


        System.out.println(url.getPath()+"?"+url.getQuery());
        return videoCode;
    }

    // 검색 페이지
    @GetMapping("/search")
    public String search(@RequestParam(value="q", defaultValue = "") String q,
                         @RequestParam(value="type",  defaultValue = "title") String type,
                         @RequestParam(value="page",  defaultValue = "1") int page,
                         Model model)
    {
        System.out.println(q);
        System.out.println(type);
        ArrayList<Music> m;

        final int pageSize = 10;

        switch (type){
            case "title":

                m = ms.findByTitle(q);

                break;
            case "artist":

                m = ms.findByArtist(q);

                break;

            default:
                return "redirect:/";

        }

        int maxPage = Math.floorDiv(m.size() - 1, pageSize) + 1;
        model.addAttribute("maxPage", maxPage);

        model.addAttribute("page",page);

        if(m.size() > 0 && (page < 1 || page > maxPage) ){
            return "redirect:/";
        }

        model.addAttribute("list",m.subList((page - 1) * pageSize, Math.min(page * pageSize, m.size())));
        model.addAttribute("q",q);
        model.addAttribute("type",type);


        return "search";
    }


    // 음악 추가 폼
    @GetMapping("/music/write")
    public String musicWriteForm(MusicForm mf)
    {

        return "writeMusic";
    }


    //음악 수정 폼
    @GetMapping("/music/edit")
    public String musicEditForm(@RequestParam("id") int id, Model model)
    {
        model.addAttribute("id", id);
        return "editMusic";
    }

    //음악 수정 처리
    @PostMapping("/music/edit")
    public RedirectView musicEdit(MusicForm mf)
    {

        System.out.println(mf.getVideoCode());

        if(mf.getVideoCode().length() == 0){
            return new RedirectView("/");
        }

        String videoCode = parseVideoCode(mf.getVideoCode());
        if(videoCode == null){
            return new RedirectView("/");
        }

        int id = mf.getId();
        Music music =  ms.findById(id);

        ms.updateVideoCode(music, videoCode);

        String encodedTitle = "";
        try {
            encodedTitle = URLEncoder.encode(music.getTitle(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new RedirectView("/search?q=" + encodedTitle + "&type=title");
    }


    //음악 삭제 처리
    @PostMapping("/music/delete")
    public String musicDelete(MusicForm mf)
    {



        int id = mf.getId();
        Music music =  ms.findById(id);
        if(music != null){
            ms.deleteMusic(music);
        }





        return "redirect:/";
    }


    //음악 등록 처리
    @PostMapping("/music/add")
    public String register(MusicForm mf)
    {
        String nowDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

        Music m = new Music();

        if(mf.getVideoCode().length() == 0 || mf.getArtist().length() == 0 || mf.getTitle().length() == 0){
            return "redirect:/";
        }

        m.setTitle(mf.getTitle());
        m.setArtist(mf.getArtist());


        String videoCode = parseVideoCode(mf.getVideoCode());
        if(videoCode == null){
            return "redirect:/";
        }


        m.setVideoCode(videoCode);
        m.setAddDate(nowDate);
        ms.register(m);

        String encodedTitle = "";
        try {
            encodedTitle = URLEncoder.encode(mf.getTitle(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        return "redirect:/search?q=" + encodedTitle + "&type=title";
    }


}
