package com.padshift.sonic.controller;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.padshift.sonic.entities.*;
import com.padshift.sonic.service.GenreService;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import org.hibernate.annotations.SourceType;
import org.hibernate.mapping.Array;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import radams.gracenote.webapi.GracenoteException;
import radams.gracenote.webapi.GracenoteMetadata;
import radams.gracenote.webapi.GracenoteWebAPI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by ruzieljonm on 26/06/2018.
 */


@SuppressWarnings("Duplicates")
@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    VideoService videoService;

    @Autowired
    GenreService genreService;

    @RequestMapping("/sonic")
    public String showLoginPage(HttpSession session, Model model) {
        if(session.isNew()) {
            return "signinsignup";
        }else{
            return showHomepage(model,session);
        }
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String generalSigninPost(HttpServletRequest request, Model model, HttpSession session) {
        String userName = request.getParameter("inputUserName1");
        String userPass = request.getParameter("inputPassword1");

        if(userName.equals("admin") && userPass.equals("admin")){
            return "HomePageAdmin";
        }else {

            User checkUser = userService.findByUsernameAndPassword(userName, userPass);

            if (checkUser != null) {
                session.setAttribute("userid", checkUser.getUserId());
                session.setAttribute("username", checkUser.getUserName());
                session.setAttribute("sessionid", checkUser.getUserId()+getSaltString());
                System.out.println(checkUser.getUserId() + " " + checkUser.getUserName());

                return showHomepage(model, session);
            } else {
                return "signinsignup";
            }
        }
    }

    @RequestMapping("/signoutadmin")
    public String signout(){
        return "signinsignup";
    }

    @RequestMapping("/signoutuser")
    public String signOutUser(HttpSession session){

        session.invalidate();

        return "signinsignup";

    }



    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String generalSignup(HttpServletRequest request, Model model, HttpSession session) {
        User newUser = new User();
        newUser.setUserName(request.getParameter("inputUserName"));
        newUser.setUserPass(request.getParameter("inputPassword"));
        newUser.setUserEmail(request.getParameter("inputEmail"));

        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int year = now.get(Calendar.YEAR);       // The current year

        System.out.println("BIRTHDAY CYST : " + request.getParameter("bday"));
        System.out.println("TYPE CYST : " + request.getParameter("radio"));

        String bday = request.getParameter("bday");
        String upToNCharacters = bday.substring(0, Math.min(bday.length(), 4));
        System.out.println(upToNCharacters);

        newUser.setUserAge(year-Integer.parseInt(upToNCharacters));
        newUser.setUserPersonality(request.getParameter("radio"));

        userService.saveUser(newUser);
        User checkUser = userService.findByUsername(request.getParameter("inputUserName"));
        session.setAttribute("userid", checkUser.getUserId());
        session.setAttribute("username", request.getParameter("inputUserName"));
        session.setAttribute("sessionid", checkUser.getUserId()+getSaltString());

        return showGenreSelection(model,session);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    @RequestMapping("/genreselection")
    public String showGenreSelection(Model model, HttpSession session){

        ArrayList<Genre> genres = genreService.findAll();

        for (int i=0; i<genres.size(); i++){
            System.out.println(genres.get(i).getGenreName());
        }
        model.addAttribute("genres", genres);
        model.addAttribute("starname", "pota");

        return "GenreSelection";
    }

    @RequestMapping(value = "/submitpref", method = RequestMethod.POST)
    public String submitPreference(HttpServletRequest request, HttpSession session, Model model) {

        System.out.println(session.getAttribute("userid") + "usah id");

        ArrayList<Genre> genres = genreService.findAll();
        UserPreference[] genrePreference = new UserPreference[genres.size()];

        for (int i=0; i<genrePreference.length; i++){
            genrePreference[i] = new UserPreference();
        }
        int userid = Integer.parseInt(session.getAttribute("userid").toString());

        for (int i=0; i<genrePreference.length; i++){
            genrePreference[i].setUserId(userid);
            float temp;
            if(request.getParameter(genres.get(i).getGenreName().toString())==null){
                temp=0;
            }else {
                temp = Float.parseFloat(request.getParameter(genres.get(i).getGenreName().toString()));
            }
            genrePreference[i].setPrefWeight(temp);
            genrePreference[i].setGenreId(genres.get(i).getGenreId());
            System.out.println(genrePreference[i].getUserId() + "-" + genrePreference[i].getGenreId() + "-" + genrePreference[i].getPrefWeight());

            userService.saveUserPreference(genrePreference[i]);


        }

        return showHomepage(model,session);

    }

    String topgenre=null;

    @RequestMapping("/homepagev2")
    public String showHomepage(Model model, HttpSession session) {


        String userid = session.getAttribute("userid").toString();
        User user = userService.findByUserId(Integer.parseInt(userid));
        System.out.println(user.getUserId() + " "+ user.getUserName());


        ArrayList<VideoDetails> videos = videoService.findAllVideoDetails();
        ArrayList<RecVid> recVideos = new ArrayList<>();

        for( VideoDetails vid : videos){
            RecVid rv = new RecVid();
            rv.setVideoid(vid.getVideoid());
            rv.setTitle(vid.getTitle());
            rv.setArtist(vid.getArtist());
            rv.setGenre(vid.getGenre());
            rv.setViewCount(vid.getViewCount());
            rv.setWeight(computeInitialVideoWeight(vid,user));
            recVideos.add(rv);
        }

        Collections.sort(recVideos);


        for(int i=0; i<10; i++){
            System.out.println(recVideos.get(i).getTitle() + " : " + recVideos.get(i).getWeight());
        }

        ArrayList<RecVid> vr1 = new ArrayList<RecVid>();
        ArrayList<RecVid> vr2 = new ArrayList<RecVid>();
        ArrayList<RecVid> vr3 = new ArrayList<RecVid>();
        ArrayList<RecVid> vr4 = new ArrayList<RecVid>();

        for(int i=0; i<=5; i++) {
            RecVid vid1 = new RecVid(recVideos.get(i).getVideoid(), recVideos.get(i).getTitle(), recVideos.get(i).getArtist(), recVideos.get(i).getGenre(), "https://i.ytimg.com/vi/" + recVideos.get(i).getVideoid() + "/mqdefault.jpg");
            vr1.add(vid1);
            vid1 = null;
        }

        for(int i=6; i<=11; i++) {
            RecVid vid2 = new RecVid(recVideos.get(i).getVideoid(), recVideos.get(i).getTitle(), recVideos.get(i).getArtist(), recVideos.get(i).getGenre(), "https://i.ytimg.com/vi/" + recVideos.get(i).getVideoid() + "/mqdefault.jpg");
            vr2.add(vid2);
            vid2 = null;
        }

        for(int i=12; i<=17; i++) {
            RecVid vid3 = new RecVid(recVideos.get(i).getVideoid(), recVideos.get(i).getTitle(), recVideos.get(i).getArtist(), recVideos.get(i).getGenre(), "https://i.ytimg.com/vi/" + recVideos.get(i).getVideoid() + "/mqdefault.jpg");
            vr3.add(vid3);
            vid3 = null;
        }

        for(int i=18; i<=23; i++) {
            RecVid vid4 = new RecVid(recVideos.get(i).getVideoid(), recVideos.get(i).getTitle(), recVideos.get(i).getArtist(), recVideos.get(i).getGenre(), "https://i.ytimg.com/vi/" + recVideos.get(i).getVideoid() + "/mqdefault.jpg");
            vr4.add(vid4);
            vid4 = null;


        }
        model.addAttribute("r1", vr1);
        model.addAttribute("r2", vr2);
        model.addAttribute("r3", vr3);
        model.addAttribute("r4", vr4);
        return "Homepage";
    }

    public float computeInitialVideoWeight(VideoDetails video, User user){
        float vidWeight;
        float userInput = 0;

        float genreAgePop,genreAgeRock, genreAgeAlt, genreAgeRBS, genreAgeCntry, genreAgeHouse, genreAgeReg, genreAgeRel, genreAgeHH;

        if(user.getUserAge()<=24){
            genreAgePop = (float) 0.50;
            genreAgeRock = (float) 0.40;
            genreAgeHH = (float) 0.40;
            genreAgeAlt = (float) 0.30;
            genreAgeRBS = (float) 0.20;
            genreAgeCntry = (float) 0.10;
            genreAgeHouse = (float) 0.10;
            genreAgeReg = (float) 0.10;
            genreAgeRel = (float) 0.10;
        }else if(user.getUserAge()>=25 && user.getUserAge()<=34){
            genreAgePop = (float) 0.50;
            genreAgeRock = (float) 0.40;
            genreAgeHH = (float) 0.30;
            genreAgeAlt = (float) 0.20;
            genreAgeCntry = (float) 0.20;
            genreAgeRBS = (float) 0.10;
            genreAgeHouse = (float) 0.10;
            genreAgeReg = (float) 0.10;
            genreAgeRel = (float) 0.10;
        }else if(user.getUserAge()>=35 && user.getUserAge()<=44) {
            genreAgeRock = (float) 0.50;
            genreAgePop = (float) 0.40;
            genreAgeCntry = (float) 0.30;
            genreAgeAlt = (float) 0.20;
            genreAgeHH = (float) 0.10;
            genreAgeRBS = (float) 0.05;
            genreAgeHouse = (float) 0.05;
            genreAgeReg = (float) 0.05;
            genreAgeRel = (float) 0.05;
        }else if(user.getUserAge()>=45 && user.getUserAge()<=54) {
            genreAgeRock = (float) 0.50;
            genreAgePop = (float) 0.40;
            genreAgeCntry = (float) 0.30;
            genreAgeAlt = (float) 0.20;
            genreAgeRBS = (float) 0.10;
            genreAgeHH = (float) 0.05;
            genreAgeHouse = (float) 0.05;
            genreAgeReg = (float) 0.05;
            genreAgeRel = (float) 0.05;
        }else if(user.getUserAge()>=55 && user.getUserAge()<=64) {
            genreAgeRock = (float) 0.50;
            genreAgeCntry = (float) 0.40;
            genreAgePop = (float) 0.30;
            genreAgeRBS = (float) 0.20;
            genreAgeAlt = (float) 0.10;
            genreAgeHH = (float) 0.05;
            genreAgeHouse = (float) 0.05;
            genreAgeReg = (float) 0.05;
            genreAgeRel = (float) 0.05;
        }else{
            genreAgeRock = (float) 0.50;
            genreAgeCntry = (float) 0.40;
            genreAgePop = (float) 0.40;
            genreAgeRBS = (float) 0.30;
            genreAgeAlt = (float) 0.20;
            genreAgeHH = (float) 0.10;
            genreAgeHouse = (float) 0.05;
            genreAgeReg = (float) 0.05;
            genreAgeRel = (float) 0.05;
        }

        float genrePTPop,genrePTRock, genrePTAlt, genrePTRBS, genrePTCntry, genrePTHouse, genrePTReg, genrePTRel, genrePTHH;


        if(user.getUserPersonality().equals("introvert")){
            genrePTRock = (float) 0.60;
            genrePTAlt = (float) 0.60;
            genrePTReg = (float) 0.60;
            genrePTRel = (float) 0.60;

            genrePTPop = (float) 0.40;
            genrePTRBS = (float) 0.40;
            genrePTCntry = (float) 0.40;
            genrePTHouse = (float) 0.40;
            genrePTHH = (float) 0.40;
        }else{

            genrePTRock = (float) 0.40;
            genrePTAlt = (float) 0.40;
            genrePTReg = (float) 0.40;
            genrePTRel = (float) 0.40;

            genrePTPop = (float) 0.60;
            genrePTRBS = (float) 0.60;
            genrePTCntry = (float) 0.60;
            genrePTHouse = (float) 0.60;
            genrePTHH = (float) 0.60;

        }

        ArrayList<UserPreference> userPref = userService.findAllGenrePreferenceByUserId(user.getUserId());

        float genreAge = 0;
        float genrePT =0;
        float genreLoc = 0;

        if(video.getGenre().equals("Pop Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),1).getPrefWeight();
            genreAge = genreAgePop;
            genrePT = genrePTPop;
            genreLoc = (float) 35.19;

        }
        if(video.getGenre().equals("Rock Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),2).getPrefWeight();
            genreAge = genreAgeRock;
            genrePT = genrePTRock;
            genreLoc = (float) 6.48;
        }

        if(video.getGenre().equals("Alternative Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),3).getPrefWeight();
            genreAge = genreAgeAlt;
            genrePT = genrePTAlt;
            genreLoc = (float) 6.48;
        }
        if(video.getGenre().equals("R&B/Soul Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),4).getPrefWeight();
            genreAge = genreAgeRBS;
            genrePT = genrePTRBS;
            genreLoc = (float)15.74;
        }
        if(video.getGenre().equals("Country Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),5).getPrefWeight();
            genreAge = genreAgeCntry;
            genrePT = genrePTCntry;
            genreLoc = (float)35.19;
        }
        if(video.getGenre().equals("House Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),6).getPrefWeight();
            genreAge = genreAgeHouse;
            genrePT = genrePTHouse;
            genreLoc = (float) 43.59;
        }
        if(video.getGenre().equals("Reggae Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),7).getPrefWeight();
            genreAge = genreAgeReg;
            genrePT = genrePTReg;
            genreLoc = 0;
        }
        if(video.getGenre().equals("Religious Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),8).getPrefWeight();
            genreAge = genreAgeRel;
            genrePT = genrePTRel;
            genreLoc = (float)35.19;
        }
        if(video.getGenre().equals("Hip-Hop/Rap Music")){
            userInput = userService.findUserPreferenceByUserIdAndGenreId(user.getUserId(),9).getPrefWeight();
            genreAge = genreAgeHH;
            genrePT = genrePTHH;
            genreLoc = (float) 43.59;
        }

        float uipercent, agepercent,pertypepercent;
        Criteria ui = userService.findCriteriaByCriteriaName("userinput");
        if(ui!=null){
            uipercent=ui.getCriteriaPercentage();
        }else{
            uipercent=0;
        }

        Criteria age = userService.findCriteriaByCriteriaName("age");
        if(age!=null){
            agepercent = age.getCriteriaPercentage();
        }else{
            agepercent=0;
        }

        Criteria pertype = userService.findCriteriaByCriteriaName("personality");
        if(pertype!=null){
            pertypepercent = pertype.getCriteriaPercentage();
        }else{
            pertypepercent=0;
        }

        Criteria perloc = userService.findCriteriaByCriteriaName("location");
        float locpercent;
        if(perloc!=null){
            locpercent = perloc.getCriteriaPercentage();
        }else{
            locpercent=0;
        }


        float likes;
        float views = Float.parseFloat(video.getViewCount().toString());
        if(video.getLikes().equals("0")){
             likes =1;
        }else{
             likes = Float.parseFloat(video.getLikes().toString());
        }


        vidWeight= (float) ((((userInput/10)*uipercent)+((genreAge/1)*agepercent)+((genrePT/1)*pertypepercent)+(genreLoc/100)*locpercent  )*likes);
        return vidWeight;
    }



    @RequestMapping("/gotoPlayer")
    public String gotoPlayer(HttpServletRequest request, Model model, HttpSession session){
        String vididtoplay = request.getParameter("clicked");


        UserHistory userhist = new UserHistory();
        userhist.setUserId(Integer.parseInt(session.getAttribute("userid").toString()));
        userhist.setVideoid(vididtoplay);
        userhist.setSeqid(session.getAttribute("sessionid").toString());
        userService.saveUserHistory(userhist);


        VideoDetails playvid = videoService.findByVideoid(vididtoplay);
        ArrayList<VideoDetails> upnext = (ArrayList<VideoDetails>) videoService.findAllVideoDetails();

        Collections.shuffle(upnext);
        System.out.println(playvid.getTitle() + " " + playvid.getArtist());

        String url = "https://www.youtube.com/embed/" + playvid.getVideoid();

        String thumbnail1 = "https://i.ytimg.com/vi/" + upnext.get(1).getVideoid() +"/mqdefault.jpg";
        String thumbnail2 = "https://i.ytimg.com/vi/" + upnext.get(2).getVideoid() +"/mqdefault.jpg";
        String thumbnail3 = "https://i.ytimg.com/vi/" + upnext.get(3).getVideoid() +"/mqdefault.jpg";

        ArrayList<VideoDetails> videoList = new ArrayList<VideoDetails>();

        videoList = videoService.findAllVideoDetails();

        ArrayList<VVD> vr1 = new ArrayList<VVD>();

        for (int i = 0; i < 6; i++) {
            VVD vid = new VVD(videoList.get(i).getVideoid(), videoList.get(i).getTitle(), videoList.get(i).getArtist(), videoList.get(i).getGenre(), videoList.get(i).getDate(),"https://i.ytimg.com/vi/" + videoList.get(i).getVideoid() + "/mqdefault.jpg");
            vr1.add(vid);
            vid = null;
        }

        model.addAttribute("r1", vr1);

        model.addAttribute("emblink", url);
        model.addAttribute("vidtitle", playvid.getTitle());
        model.addAttribute("vidviews", playvid.getViewCount());
        model.addAttribute("vidlikes", playvid.getLikes());


        model.addAttribute("upnext1", upnext.get(1));
        model.addAttribute("upnext2", upnext.get(2));
        model.addAttribute("upnext3", upnext.get(3));

        model.addAttribute("tn1", thumbnail1);
        model.addAttribute("tn2", thumbnail2);
        model.addAttribute("tn3", thumbnail3);

        return "VideoPlayerV2";

    }


    @RequestMapping("/vplayer")
    public String showVideoPlayer() {
        return "VideoPlayerV2";
    }


    @RequestMapping("/profile")
    public String showUserProfile(HttpServletRequest request, Model model) {
        List<Video> videoList = videoService.findAll();
        for (int i = 0; i < videoList.size(); i++) {
            System.out.println(videoList.get(i).getVideoid());
        }
        model.addAttribute("vids", videoList);
        return "UserProfile";
    }



    public void saveMV(String vidId, String title, String url) {
        Video newVideo = new Video();
        newVideo.setVideoid(vidId);
        newVideo.setMvtitle(title);
        newVideo.setThumbnail(url);
        videoService.saveVideo(newVideo);
    }


    @RequestMapping("/homefeed")
    public String showHomeFeed(Model model){
        return "HomeFeed";
    }



    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    public String explore(Model model){
        ArrayList<Genre> genres = videoService.findAllGenre();
        model.addAttribute("genre", genres);
        return "Explore";

    }




}