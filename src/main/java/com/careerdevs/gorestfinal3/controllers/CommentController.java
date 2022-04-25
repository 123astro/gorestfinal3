package com.careerdevs.gorestfinal3.controllers;


import com.careerdevs.gorestfinal3.models.Comment;
import com.careerdevs.gorestfinal3.models.User;
import com.careerdevs.gorestfinal3.repos.CommentRepository;
import com.careerdevs.gorestfinal3.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal3.utils.BasicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

      /*
    Required Routes for GoRestSQL Final: complete for each resource; User, Post, Comment, Todo,

            * GET route that returns one [resource] by ID from the SQL database DONE
           * GET route that returns all [resource]s stored in the SQL database DONE
           * DELETE route that deletes one [resource] by ID from SQL database (returns the deleted SQL [resource]
            data) DONE
           * DELETE route that deletes all [resource]s from SQL database (returns how many [resource]s were deleted)
            DONE
           * POST route that queries one [resource] by ID from GoREST and saves their data to your local database (returns
                                                                                                                           the SQL [resource] data)
           *POST route that uploads all [resource]s from the GoREST API into the SQL database (returns how many
            [resource]s were uploaded)
           *POST route that create a [resource] on JUST the SQL database (returns the newly created SQL [resource] data)
           *PUT route that updates a [resource] on JUST the SQL database (returns the updated SQL [resource] data)
    * */


    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable("id") String id) {
        try {
            // control over error message and you get the 400. And code block is not needed.
            if (BasicUtils.isStrNaN(id)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " is not a valid ID");
            }

            int uID = Integer.parseInt(id);

            Optional<Comment> foundComment = commentRepository.findById(uID);

            if (foundComment.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found with ID: " + id);
            }
            return new ResponseEntity<>(foundComment, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllComments() {
        try {
            Iterable<Comment> allComments = commentRepository.findAll();
            return new ResponseEntity<>(allComments, HttpStatus.OK);
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<?> deleteAllComments() {
        try {

            long totalComments = commentRepository.count(); // count method whole number
            commentRepository.deleteAll();

            return new ResponseEntity<Long>(totalComments, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());

        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id, RestTemplate restTemplate
    ) {
        try {

            if (BasicUtils.isStrNaN(id)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " is not a valid ID");
            }

            int uID = Integer.parseInt(id);

            //check the range => other things to do

            Optional<Comment> foundComment = commentRepository.findById(uID);

            if (foundComment.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Not Found with ID: " + id);
            }

            commentRepository.deleteById(uID);

            return new ResponseEntity<>(foundComment, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @PostMapping("/uploadall")
    public ResponseEntity<?> uploadAll(
            RestTemplate restTemplate
    ) {
        try {
            String url = "https://gorest.co.in/public/v2/comments";

            ResponseEntity<Comment[]> response = restTemplate.getForEntity(url, Comment[].class);

            Comment[] firstPageComments = response.getBody();

            // assert firstPageUsers != null;

            if (firstPageComments == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                        "COMMENTS from GOREST");
            }

            ArrayList<Comment> allComments = new ArrayList<>(Arrays.asList(firstPageComments));

            HttpHeaders responseHeaders = response.getHeaders();

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);
            int totalPgNum = Integer.parseInt(totalPages);

            for (int i = 2; i <= totalPgNum; i++) {
                String pageUrl = url + "?page=" + i;
                User[] pageUsers = restTemplate.getForObject(pageUrl, User[].class);

                if (pageUsers == null) {
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Failed to GET first page " + i + " of users from GoRest ");
                }
                allComments.addAll(Arrays.asList(firstPageComments));
            }

            commentRepository.saveAll(allComments);

            return new ResponseEntity<>("Comments Created " + allComments.size(), HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }



}
