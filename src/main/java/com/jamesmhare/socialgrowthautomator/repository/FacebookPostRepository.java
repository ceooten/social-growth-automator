package com.jamesmhare.socialgrowthautomator.repository;

import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPost;
import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPostType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacebookPostRepository extends JpaRepository<FacebookPost, Long> {

    FacebookPost findFirstByFacebookPostType(FacebookPostType facebookPostType);

}