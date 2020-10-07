package com.jamesmhare.socialgrowthautomator.model.facebook;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FacebookPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private FacebookPostType facebookPostType;
    @NotNull
    private String message;
    @NotNull
    private String url;

    public FacebookPost(String message, String url) {
        this.message = message;
        this.url = url;
        this.facebookPostType = FacebookPostType.IMAGE; // TODO: remove this after testing
    }

}
