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


    public String parseVideoCode(String urlString){

        URL url = null;
        String videoCode = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
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


    @GetMapping("/search")
    public String search(@RequestParam("q") String q, @RequestParam("type") String type, Model model)
    {
        System.out.println(q);
        System.out.println(type);
        ArrayList<Music> m;
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
        model.addAttribute("list",m);
        model.addAttribute("q",q);
        model.addAttribute("type",type);


        return "search";
    }

    @GetMapping("/music/write")
    public String musicWriteForm(MusicForm mf)
    {

        return "writeMusic";
    }

    @GetMapping("/music/edit")
    public String musicEditForm(@RequestParam("id") int id, Model model)
    {
        model.addAttribute("id", id);
        return "editMusic";
    }

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
    @GetMapping("/music")
    public String musicList(Model model)
    {
        ArrayList<Music> m = ms.findMembers();
        model.addAttribute("list",m);
        return "musicList";
    }

}
