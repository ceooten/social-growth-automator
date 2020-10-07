package com.jamesmhare.socialgrowthautomator.controller;

import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPost;
import com.jamesmhare.socialgrowthautomator.repository.initializer.PostCollection;
import com.jamesmhare.socialgrowthautomator.service.FacebookPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Controller
@RequestMapping("/facebook")
public class FacebookPostController {

    private final Logger log = LoggerFactory.getLogger(FacebookPostController.class);
    private final FacebookPostService facebookPostService;

    public FacebookPostController(final FacebookPostService facebookPostService) {
        this.facebookPostService = facebookPostService;
    }

    @PostConstruct
    public void init() {
        for (FacebookPost post : PostCollection.posts) {
            facebookPostService.savePost(post);
        }
    }

    @GetMapping("/dashboard")
    public String showFacebookDashboard() {
        return "facebook-dashboard";
    }

    @GetMapping("/new-post")
    public String showNewPostForm(final FacebookPost facebookPost) {
        return "facebook-post-add";
    }

    @PostMapping("/add-post")
    public String addPost(@Valid final FacebookPost facebookPost, final BindingResult result, final Model model) {
        if (result.hasErrors()) {
            return "facebook-post-add";
        }
        facebookPostService.savePost(facebookPost);
        model.addAttribute("facebookPosts", facebookPostService.getAllPosts());
        return "facebook-post-success";
    }

    @GetMapping("/manage-posts")
    public String showManagePostsView(final Model model) {
        model.addAttribute("facebookPosts", facebookPostService.getAllPosts());
        return "facebook-post-manage";
    }

    @GetMapping("/edit-post/{id}")
    public String showEditPostForm(@PathVariable("id") final long id, final Model model) {
        final FacebookPost facebookPost = facebookPostService.getPost(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID:" + id));
        model.addAttribute("facebookPost", facebookPost);
        return "facebook-post-update";
    }

    @PostMapping("/update-post/{id}")
    public String updatePostRecord(@PathVariable("id") final long id, @Valid final FacebookPost facebookPost,
                                       final BindingResult result, final Model model) {
        if (result.hasErrors()) {
            facebookPost.setId(id);
            return "facebook-post-update";
        }
        facebookPostService.savePost(facebookPost);
        return "facebook-post-update-success";
    }

    @GetMapping("/delete-post/{id}")
    public String deleteEmployee(@PathVariable("id") final long id, final Model model) {
        final FacebookPost facebookPost = facebookPostService.getPost(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID:" + id));
        facebookPostService.deletePost(facebookPost);
        return "facebook-post-delete-success";
    }

}
