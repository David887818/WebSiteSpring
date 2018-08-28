package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CurrentUser;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CommentRepository commentRepository;

    @PostMapping("/addComment")
    public String addComment(@RequestParam("user_id") int user_id,
                             @RequestParam("post_id") int post_id,
                             @RequestParam("comment") String comments
    ){
        Comment comment = Comment.builder()
                .post(postRepository.getOne(post_id))
                .user(userRepository.getOne(user_id))
                .comment(comments)
                .build();
        commentRepository.save(comment);
        return "/adminPage";
    }



    @Value("${webSite.pic.url}")
    private String adPicDir;

    @PostMapping("/addPost")
    public String addPost(@ModelAttribute Post post,
                          @RequestParam("category_id") int id,
                          @RequestParam("image") MultipartFile multipartFile) {
        File dir = new File(adPicDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        try {
            multipartFile.transferTo(new File(dir, picName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        post.setPicUrl(picName);
        post.setCategory(categoryRepository.getOne(id));
        postRepository.save(post);
        return "adminPage";
    }

    @GetMapping(value = "/userImage")
    public @ResponseBody
    byte[] userImage(@RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(adPicDir + picUrl);
        return IOUtils.toByteArray(in);
    }

}
