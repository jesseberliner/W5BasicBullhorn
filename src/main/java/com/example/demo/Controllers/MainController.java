package com.example.demo.Controllers;

import com.cloudinary.utils.ObjectUtils;
import com.example.demo.Models.Moo;
import com.example.demo.Repositories.MooRepository;
import com.example.demo.Services.CloudinaryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    MooRepository mooRepo;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listToDos(Model model)
    {
        model.addAttribute("moos", mooRepo.findAll());
        return "allMoos";
    }

    @GetMapping("/add")
    public String toDoForm(Model model){
        model.addAttribute("moo", new Moo());
        model.addAttribute("today", new Date());
        System.out.println(new Date());
        return "addMoo";
    }

    @PostMapping("/add")
    public String processForm(@Valid Moo moo, @RequestParam("file")MultipartFile file, Model model, BindingResult result){

        if(result.hasErrors()){
            model.addAttribute(moo);
            return "addMoo";
        }
        if(file.isEmpty())
        {
            model.addAttribute("moo", moo);
            return "addMoo";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            moo.setImage(uploadResult.get("url").toString());
            mooRepo.save(moo);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMoo(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("moo", mooRepo.findById(id).get());
//        model.addAttribute("moos", mooRepo.findAll());
        return "showMoo";
    }

    @RequestMapping("/update/{id}")
    public String updateMoo(@PathVariable("id") long id, Model model)
    {
        model.addAttribute("moo", mooRepo.findById(id).get());
        return "addMoo";
    }

    @RequestMapping("/delete/{id}")
    public String delMoo(@PathVariable("id") long id)
    {
        mooRepo.deleteById(id);
        return "redirect:/";
    }
}
