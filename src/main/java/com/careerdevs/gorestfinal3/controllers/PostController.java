package com.careerdevs.gorestfinal3.controllers;

import com.careerdevs.gorestfinal3.models.Post;
import com.careerdevs.gorestfinal3.models.User;
import com.careerdevs.gorestfinal3.repos.PostRepository;
import com.careerdevs.gorestfinal3.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal3.utils.BasicUtils;
import com.careerdevs.gorestfinal3.validation.UserValidation;
import com.careerdevs.gorestfinal3.validation.ValidationError;
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
@RequestMapping("/post")

public class PostController {

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
    private PostRepository postRepository;

    //http://localhost:8080/user/all
    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() {
        try {
            Iterable<Post> allUsers = postRepository.findAll();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        try {
            // control over error message and you get the 400. And code block is not needed.
            if (BasicUtils.isStrNaN(id)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + " is not a valid ID");
            }

            Long uID = Long.parseLong(id);

            Optional<Post> foundUser = postRepository.findById(uID);

            if (foundUser.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, " User not found with ID: " + id);
            }
            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @DeleteMapping("/deleteall")
    public ResponseEntity<?> deleteAllUsers() {
        try {

            long totalPost = postRepository.count(); // count method whole number
            postRepository.deleteAll();

            return new ResponseEntity<Long>(totalPost, HttpStatus.OK);

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

            Long uID = Long.parseLong(id);

            //check the range => other things to do

            Optional<Post> foundUser = postRepository.findById(uID);

            if (foundUser.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Not Found with ID: " + id);
            }

            postRepository.deleteById(uID);

            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }
        @PostMapping("/upload/{id}")
        public ResponseEntity<?> uploadPostById (
                @PathVariable("id") String postId,
                RestTemplate restTemplate // making an external api request
    ){

            try {

                if (BasicUtils.isStrNaN(postId)) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, postId + "is not a valid ID");
                }

                int uId = Integer.parseInt(postId);

                String url = "https://gorest.co.in/public/v2/posts" + uId;

                Post foundPost = restTemplate.getForObject(url, Post.class);
                if (foundPost == null) {
                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with ID:" + uId + " not found");
                }

                Post savedPost = postRepository.save(foundPost);

                return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
            } catch (HttpClientErrorException e) {
                return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
            } catch (Exception e) {
                return ApiErrorHandling.genericApiError(e);
            }
        }

        @PostMapping("/")
        public ResponseEntity<?> createNewUser (@RequestBody Post newPost){
            try {

           //     ValidationError newUserErrors = UserValidation.validateNewUser(newPost, postRepository, false);

//                if (newUserErrors.hasError()) {
//                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, newUserErrors.toString());
//                } // no else block needed

                Post savedPost = postRepository.save(newPost);

                return new ResponseEntity<>(savedPost, HttpStatus.CREATED);

            } catch (HttpClientErrorException e) {
                return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
            } catch (Exception e) {
                return ApiErrorHandling.genericApiError(e);
            }
        }

        @PostMapping("/uploadall")
        public ResponseEntity<?> uploadAll (
                RestTemplate restTemplate
    ){
            try {
                String url = "https://gorest.co.in/public/v2/posts";

                ResponseEntity<Post[]> response = restTemplate.getForEntity(url, Post[].class);

                Post[] firstPageUsers = response.getBody();

                // assert firstPageUsers != null;

                if (firstPageUsers == null) {
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                            "posts from GOREST");
                }

                ArrayList<Post> allPosts = new ArrayList<>(Arrays.asList(firstPageUsers));

                HttpHeaders responseHeaders = response.getHeaders();

                String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);
                int totalPgNum = Integer.parseInt(totalPages);

                for (int i = 2; i <= totalPgNum; i++) {
                    String pageUrl = url + "?page=" + i;
                    Post[] pagePosts = restTemplate.getForObject(pageUrl, Post[].class);

                    if (pagePosts == null) {
                        throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Failed to GET first page " + i + " of post from GoRest ");
                    }
                    allPosts.addAll(Arrays.asList(firstPageUsers));
                }

                postRepository.saveAll(allPosts);

                return new ResponseEntity<>("Posts Created " + allPosts.size(), HttpStatus.OK);

            } catch (HttpClientErrorException e) {
                return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
            } catch (Exception e) {
                return ApiErrorHandling.genericApiError(e);
            }
        }


        @PutMapping("/")
        public ResponseEntity<?> updateUser (@RequestBody Post post){
            try {

//                ValidationError newUserErrors = UserValidation.validateNewUser(post, postRepository, true);
//
//                if (newUserErrors.hasError()) {
//                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, newUserErrors.toString());
//                } // no else block needed

                Post savedPost = postRepository.save(post);

                return new ResponseEntity<>(savedPost, HttpStatus.CREATED);

            } catch (HttpClientErrorException e) {
                return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
            } catch (Exception e) {
                return ApiErrorHandling.genericApiError(e);
            }
        }

}
