package com.careerdevs.gorestfinal3.validation;

import com.careerdevs.gorestfinal3.models.Post;
import com.careerdevs.gorestfinal3.repos.PostRepository;



import java.util.Optional;

public class PostValidation {


    public static ValidationError validateNewUser(Post post, PostRepository postRepo, boolean isUpdate) {
        ValidationError errors = new ValidationError();

        if (isUpdate) {
            if (post.getId() == null) {
                errors.addError("id", "id can't be left blank");
            } else {
                Optional<Post> foundUser = postRepo.findById(post.getId());
                if (foundUser.isEmpty()) {
                    errors.addError("id", "No user found with the ID: " + post.getId());
                } else {
                    System.out.println(foundUser.get());
                }
            }
        }

        int postId = post.getUser_id();
        String postTitle = post.getTitle();
        String postBody = post.getBody();


        if (post.getId() == null || post.getId().equals("")) {
            errors.addError("User Id" , "User ID can't be left blank.");
        }

        if (post.getTitle() == null || post.getTitle().trim().equals("")) {
            errors.addError("TITLE", "Title can't be left blank");
        }

        if (post.getBody() == null || post.getBody().trim().equals("")) {
            errors.addError("Body", "Body can't be left blank");
        }


        return errors;
    }
}
