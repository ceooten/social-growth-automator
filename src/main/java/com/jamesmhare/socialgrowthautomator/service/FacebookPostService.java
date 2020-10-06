package com.jamesmhare.socialgrowthautomator.service;

import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPost;
import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPostType;
import com.jamesmhare.socialgrowthautomator.repository.FacebookPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacebookPostService {

    private final FacebookPostRepository facebookPostRepository;

    public FacebookPostService(final FacebookPostRepository facebookPostRepository) {
        this.facebookPostRepository = facebookPostRepository;
    }

    public Optional<FacebookPost> getPost(final Long id) {
        return facebookPostRepository.findById(id);
    }

    public List<FacebookPost> getAllPosts() {
        return facebookPostRepository.findAll();
    }

    public void savePost(FacebookPost post) {
        facebookPostRepository.save(post);
    }

    public void deletePost(FacebookPost post) {
        facebookPostRepository.delete(post);
    }

    public FacebookPost getImagePost() {
        return facebookPostRepository.findFirstByFacebookPostType(FacebookPostType.IMAGE);
    }

}
