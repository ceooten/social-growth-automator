package com.jamesmhare.socialgrowthautomator.task;

import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPost;
import com.jamesmhare.socialgrowthautomator.service.FacebookPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class FacebookTask {

    private final Logger log = LoggerFactory.getLogger(FacebookTask.class);
    final FacebookPostService facebookPostService;

    public FacebookTask(final FacebookPostService facebookPostService) {
        this.facebookPostService = facebookPostService;
    }

    // TODO sort out this cron job
    @Scheduled(cron = "0 0 8,10,12,14,16,20,22 ? * *")
    public void postImageTask() {
        final FacebookPost post = facebookPostService.getImagePost();
        if (post.getId() != null) {
            if (facebookPostService.publishImagePost(post)) {
                facebookPostService.deletePost(post);
                log.info("The following post was published: " + post.toString());
            } else {
                log.error("There was a problem publishing the following post: " + post.toString());
            }
        } else {
            log.info("There are currently no image posts ready to be posted.");
        }
    }

}
