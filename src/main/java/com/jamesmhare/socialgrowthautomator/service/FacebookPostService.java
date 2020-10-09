package com.jamesmhare.socialgrowthautomator.service;

import com.jamesmhare.socialgrowthautomator.config.properties.SGAFacebookProperties;
import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPost;
import com.jamesmhare.socialgrowthautomator.model.facebook.FacebookPostType;
import com.jamesmhare.socialgrowthautomator.repository.FacebookPostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@EnableConfigurationProperties(SGAFacebookProperties.class)
public class FacebookPostService {

    private final Logger log = LoggerFactory.getLogger(FacebookPostService.class);
    private final FacebookPostRepository facebookPostRepository;
    final static String facebookGraphHost = "https://graph.facebook.com/v8.0/";
    final static String pathPhotos = "/photos";
    final SGAFacebookProperties properties;
    final static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public FacebookPostService(final FacebookPostRepository facebookPostRepository, final SGAFacebookProperties properties) {
        this.facebookPostRepository = facebookPostRepository;
        this.properties = properties;
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

    public boolean publishRandomPost(FacebookPost post) {
        switch(post.getFacebookPostType()) {
            case IMAGE:
                return publishImagePost(post);
            case ARTICLE:
                return publishArticlePost(post);
            case VIDEO:
                return publishVideoPost(post);
            default:
                return false;
        }
    }

    public boolean publishImagePost(FacebookPost post) {
        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("message", post.getMessage());
        data.put("url", post.getUrl());
        data.put("access_token", properties.getAccessToken());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create(facebookGraphHost + properties.getAccountNumber() + pathPhotos))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (IOException | InterruptedException e) {
            log.error("There was an issue posting to the Facebook API. " + e);
        }
        return false;
    }

    public boolean publishArticlePost(FacebookPost post) {
        // TODO: implement
        return false;
    }

    public boolean publishVideoPost(FacebookPost post) {
        // TODO: implement
        return false;
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
