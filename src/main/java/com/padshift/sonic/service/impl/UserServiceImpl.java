package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.*;
import com.padshift.sonic.repository.CriteriaRepository;
import com.padshift.sonic.repository.UserHistoryRepository;
import com.padshift.sonic.repository.UserPreferenceRepository;
import com.padshift.sonic.repository.UserRepository;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import org.hibernate.annotations.SourceType;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by ruzieljonm on 27/06/2018.
 */
@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public UserHistoryRepository userHistoryRepository;

    @Autowired
    CriteriaRepository criteriaRepository;

    @Autowired
    UserService userService;

    @Autowired
    VideoService videoService;


    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }




    @Override
    public void saveUserPreference(UserPreference userpref) {
        userPreferenceRepository.save(userpref);
    }

    @Override
    public UserPreference findUserPreferenceByUserId(int userId) {
        return userPreferenceRepository.findByUserId(userId);
    }

    @Override
    public void saveUserHistory(UserHistory userhist) {
        userHistoryRepository.save(userhist);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public User findByUsernameAndPassword(String userName, String userPass) {
        return userRepository.findByUserNameAndUserPass(userName, userPass);
    }


    @Override
    public User findByUserId(int userid) {
        return userRepository.findByUserId(userid);
    }

    @Override
    public ArrayList<UserPreference> findAllGenrePreferenceByUserId(int userid) {
        return userPreferenceRepository.findAllByUserId(userid);
    }

    @Override
    public UserPreference findUserPreferenceByUserIdAndGenreId(int userId, int i) {
        return userPreferenceRepository.findByUserIdAndGenreId(userId,i);
    }
//
//    @Override
//    public void saveCriteria(Criteria criteria) {
//        criteriaRepository.save(criteria);
//    }

    @Override
    public ArrayList<Criteria> findAllCriteria() {
        return (ArrayList<Criteria>) criteriaRepository.findAll();
    }

    @Override
    public void deleteCriteriaByCriteriaId(int deletethis) {
        criteriaRepository.deleteByCriteriaId(deletethis);
        System.out.println("DELETING");
    }

    @Override
    public Criteria findCriteriaByCriteriaName(String userinput) {
        return criteriaRepository.findByCriteriaName(userinput);
    }

    @Override
    public Criteria findCriteriaByCriteriaId(int editthis) {
        return criteriaRepository.findByCriteriaId(editthis);
    }

    @Override
    public float computeInitialVideoWeight(VideoDetails video, User user) {
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

    @Override
    public void seqRulMin(String userid, String sessionid) {

        ArrayList<String> sequences = userHistoryRepository.findDistinctSequenceIdByUserid(Integer.parseInt(userid));
        for(String s : sequences){
            System.out.println(s.toString());
        }

        ArrayList<UserHistory>[] userHistPerSeq = (ArrayList<UserHistory>[])new ArrayList[sequences.size()];

        for(int i=0; i<sequences.size(); i++){
//            ArrayList<UserHistory> temp = userHistoryRepository.findBySeqid(sequences.get(i).toString());
            userHistPerSeq[i] = userHistoryRepository.findBySeqid(sequences.get(i).toString());
        }

        for(int i=0; i<userHistPerSeq.length; i++){
            System.out.print("Sequence" + "[" +i+ "] : ");
            for(int j=0; j<userHistPerSeq[i].size(); j++) {
                System.out.print(userHistPerSeq[i].get(j).getVideoid() + ", ");
            }
            System.out.println();
        }


        ArrayList<UserHistory> currentSeq = userHistoryRepository.findBySeqid(sessionid);
        System.out.println("Current Session: ");
        for(int i=0; i<currentSeq.size(); i++){
            System.out.print(currentSeq.get(i).getVideoid() + ", ");
        }

        ArrayList<Video> allvids = (ArrayList<Video>) videoService.findAll();

        ArrayList<String>[] check = (ArrayList<String>[])new ArrayList[allvids.size()];

        for(int i=0; i<check.length; i++){

            for(int j=0; j<currentSeq.size(); j++) {
                check[i].add(currentSeq.get(j).getVideoid());
            }
            check[i].add(allvids.get(i).getVideoid());
        }

        System.out.println("Sequence Rule Mining CM Spade");

        for(int i=0; i<10; i++){
            for(int j=0; j<check[i].size(); j++) {
                System.out.println(check[i].get(j));
            }
        }










    }
//
//    @Override
//    public void seqRulMin(String userid, String sessionid) {
////
////        String seqid = sessionid;
////        ArrayList<UserHistory> currentSequence = userHistoryRepository.findByUserIdAndSeqid(Integer.parseInt(userid), seqid);
////        System.out.print("Current Sequence : ");
////        for(UserHistory s : currentSequence){
////            System.out.print(s.getVideoid() + " ,");
////        }
//
//
//    }
//
//    @Override
//    public ArrayList<String> findDistinctSequenceId() {
//        return userHistoryRepository.findDistinctSequenceId();
//    }


}
