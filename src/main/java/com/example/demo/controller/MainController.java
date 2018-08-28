package com.example.demo.controller;


import com.example.demo.model.*;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class MainController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/")
    public String main(ModelMap map) {
        List<Post> posts = postRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Comment> comments = commentRepository.findAll();
        map.addAttribute("posts", posts);
        map.addAttribute("comments", comments);
        map.addAttribute("categories", categories);

        return "index";
    }

    @GetMapping("/login")
    public String login(ModelMap map) {
        map.addAttribute("user", new User());
        return "loginPage";
    }

    @PostMapping("/loginUser")
    public String loginUser(@AuthenticationPrincipal UserDetails userDetails, ModelMap map) {
        User user = ((CurrentUser) userDetails).getUser();
        if (user.getUserType() == UserType.ADMIN) {
            List<Post> posts = postRepository.findAll();
            List<Comment> comments = commentRepository.findAll();
            map.addAttribute("comments", comments);
            List<Category> categories = categoryRepository.findAll();
            CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            map.addAttribute("posts", posts);
            map.addAttribute("categories", categories);
            map.addAttribute("user", principal);
            return "adminPage";
        } else {
            List<Post> posts = postRepository.findAll();
            List<Category> categories = categoryRepository.findAll();
            CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Comment> comments = commentRepository.findAll();
            map.addAttribute("comments", comments);
            map.addAttribute("posts", posts);
            map.addAttribute("categories", categories);
            map.addAttribute("user", principal);
            return "userPage";
        }

    }


    @PostMapping("/addCategory")
    public String add(@RequestParam("category") String cat) {
        Category category = Category.builder()
                .name(cat).build();
        categoryRepository.save(category);
        return "adminPage";
    }


}


