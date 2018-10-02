package com.padshift.sonic.controller;

import com.padshift.sonic.entities.Criteria;
import com.padshift.sonic.entities.Genre;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by ruzieljonm on 26/09/2018.
 */
@Controller
public class AdminController {

    @Autowired
    VideoService videoService;

    @Autowired
    UserService userService;

    @Autowired
    GracenoteAPIController gracenoteAPIController;

    @RequestMapping("/adminHomePage")
    public String showAdminHomePage(){
        return "HomePageAdmin";
    }


    @RequestMapping(value = "/querypage", method = RequestMethod.GET)
    public String showQueryPage(Model model){
        ArrayList<Genre> genres =  videoService.findAllGenre();
        model.addAttribute("genre", genres);
        return "QueryAdmin";
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public String config(HttpServletRequest request, Model model){
        String configChoice = request.getParameter("config");
        System.out.println(configChoice);
        if(configChoice.equals("1")){
             return showQueryPage(model);
        }else if(configChoice.equals("2")){
            System.out.println("GOOD MORNING");
             return gracenoteAPIController.showmetadata();
        }else if(configChoice.equals("3")) {
            return showCriteria(model);

        }else{
            return "testing";
        }
    }

    @RequestMapping(value = "/byartist", method = RequestMethod.POST)
    public String byArtist(){
        return "ByArtist";
    }


    @RequestMapping(value = "/bysinglevideo", method = RequestMethod.POST)
    public String bySingleVideo(){
        return "BySingleVideo";
    }


    @RequestMapping("/criteria")
    public String showCriteria(Model model){
        ArrayList<Criteria> criteria = userService.findAllCriteria();
        model.addAttribute("criteria", criteria);
        return "Criteria";

    }

    @RequestMapping(value = "/addcriteria", method = RequestMethod.POST)
    public String addCriteria(HttpServletRequest request, Model model){
        String criteriaName = request.getParameter("criteriaName");
        Float criteriaPercentage = Float.valueOf(request.getParameter("criteriaPercentage"));

        Criteria criteria = new Criteria();
        criteria.setCriteriaName(criteriaName);
        criteria.setCriteriaPercentage(criteriaPercentage);

        userService.saveCriteria(criteria);

        ArrayList<Criteria> crit = userService.findAllCriteria();
        model.addAttribute("criteria", crit);
        return "Criteria";

    }

    @RequestMapping("/removecriteria")
    public String deleteCriteria(HttpServletRequest request, Model model){
        int deletethis = Integer.parseInt(request.getParameter("crite").toString());
        userService.deleteCriteriaByCriteriaId(deletethis);

        ArrayList<Criteria> crit = userService.findAllCriteria();
        model.addAttribute("criteria", crit);
        return "Criteria";


    }


    @RequestMapping("/editcriteria")
    public String editCriteria(HttpServletRequest request, Model model){
        int editthis = Integer.parseInt(request.getParameter("crite").toString());
        Criteria crit = userService.findCriteriaByCriteriaId(editthis);

        model.addAttribute("critname", crit.getCriteriaName());
        model.addAttribute("critper", crit.getCriteriaPercentage());
        userService.deleteCriteriaByCriteriaId(editthis);
        ArrayList<Criteria> crite = userService.findAllCriteria();
        model.addAttribute("criteria", crite);
        return "CriteriaUpdate";



    }







}
