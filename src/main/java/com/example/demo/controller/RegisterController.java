package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class RegisterController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/back")
    public String Back() {

        return "redirect:/";
    }

    @GetMapping("/register")
    public String regPage() {

        return "register";
    }
    @Value("${webSite.pic.url}")
    private String userPicDir;
    @PostMapping("/registration")
    public String add(@ModelAttribute User user,
                      @RequestParam("image") MultipartFile multipartFile) {
        File dir = new File(userPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        try {
            multipartFile.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setPicUrl(picName);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserType(UserType.USER);
        userRepository.save(user);
        return "redirect:/";
    }
}
