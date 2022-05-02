package com.careerdevs.gorestfinal3.validation;

import com.careerdevs.gorestfinal3.models.Comment;
import com.careerdevs.gorestfinal3.models.Post;
import com.careerdevs.gorestfinal3.models.User;
import com.careerdevs.gorestfinal3.repos.CommentRepository;
import com.careerdevs.gorestfinal3.repos.PostRepository;
import com.careerdevs.gorestfinal3.repos.UserRepository;

import java.util.Optional;

public class CommentValidation {


    public static ValidationError validateComment(Comment comment, CommentRepository postRepo, UserRepository userRepo,
                                                  boolean isUpdate) {

        ValidationError errors = new ValidationError();

        if (isUpdate) {
            if (comment.getId() == 0) {
                errors.addError("id", "ID can not be left blank");
            } else {
                Optional<Post> foundUser = postRepo.findById(comment.getId());
                if (foundUser.isEmpty()) {
                    errors.addError("id", "No user found with the ID: " + comment.getId());
                }
            }
        }

        long commentId = comment.getId();
        String commentName = comment.getName();
        String commentEmail = comment.getEmail();
        String commentBody = comment.getBody();


        if (commentName == null || commentName.trim().equals("")) {
            errors.addError("name", "Name can not be left blank");
        }

        if (commentBody == null || commentBody.trim().equals("")) {
            errors.addError("body", "Body can not be left blank");
        }

        if (commentEmail == null || commentEmail.trim().equals("")) {
            errors.addError("email", "Email can not be left blank");
        }

        if (commentId == 0) {
            errors.addError("user_id", "User_ID can not be left blank");
        } else {
            // is the postUserId connected to an existing user.
            Optional<User> foundUser = userRepo.findById(commentId);

            if (foundUser.isEmpty()) {
                errors.addError("comment_id",
                        "Comment_ID is invalid because there is no user found with the id: " + commentId);
            }
        }
        return errors;
    }
}

