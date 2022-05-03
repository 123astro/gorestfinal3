package com.careerdevs.gorestfinal3.controllers;

import com.careerdevs.gorestfinal3.models.ToDo;
import com.careerdevs.gorestfinal3.models.User;
import com.careerdevs.gorestfinal3.repos.ToDoRepository;
import com.careerdevs.gorestfinal3.repos.UserRepository;
import com.careerdevs.gorestfinal3.utils.ApiErrorHandling;
import com.careerdevs.gorestfinal3.utils.BasicUtils;
import com.careerdevs.gorestfinal3.validation.ToDoValidation;
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
@RequestMapping("/api/todos")

public class ToDoController {

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
    private ToDoRepository toDoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllToDo() {
        try {
            Iterable<ToDo> allToDo = toDoRepository.findAll();
            return new ResponseEntity<>(allToDo, HttpStatus.OK);
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

            Optional<ToDo> foundUser = toDoRepository.findById(uID);

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

            long totalUsers = toDoRepository.count(); // count method whole number
            toDoRepository.deleteAll();

            return new ResponseEntity<Long>(totalUsers, HttpStatus.OK);

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

            Optional<ToDo> foundUser = toDoRepository.findById(uID);

            if (foundUser.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User Not Found with ID: " + id);
            }

            toDoRepository.deleteById(uID);

            return new ResponseEntity<>(foundUser, HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadToDoById(
            @PathVariable("id") String userId,
            RestTemplate restTemplate // making an external api request
    ) {

        try {

            if (BasicUtils.isStrNaN(userId)) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId + "is not a valid ID");
            }

            int uId = Integer.parseInt(userId);

            String url = "https://gorest.co.in/public/v2/todos/" + uId;

            ToDo foundUser = restTemplate.getForObject(url, ToDo.class);
            if (foundUser == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with ID:" + uId + " not found");
            }

            ToDo savedToDo = toDoRepository.save(foundUser);

            return new ResponseEntity<>(savedToDo, HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewUser(@RequestBody ToDo newTodo) {
        try {

            ValidationError newToDoErrors = ToDoValidation.validateToDo(newTodo, toDoRepository, userRepository, false);

            if (newToDoErrors.hasError()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, newToDoErrors.toString());
            } // no else block needed

            ToDo savedTodo = toDoRepository.save(newTodo);

            return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);

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
            String url = "https://gorest.co.in/public/v2/todos";

            ResponseEntity<ToDo[]> response = restTemplate.getForEntity(url, ToDo[].class);

            ToDo[] firstPageTodos = response.getBody();

            // assert firstPageUsers != null;

            if (firstPageTodos == null) {
                throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to GET first page of " +
                        "todos from GOREST");
            }

            ArrayList<ToDo> allTodo = new ArrayList<>(Arrays.asList(firstPageTodos));

            HttpHeaders responseHeaders = response.getHeaders();

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);
            int totalPgNum = Integer.parseInt(totalPages);

            for (int i = 2; i <= totalPgNum; i++) {
                String pageUrl = url + "?page=" + i;
                ToDo[] pageTodos = restTemplate.getForObject(pageUrl, ToDo[].class);

                if (pageTodos == null) {
                    throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Failed to GET first page " + i + " of TodoS from GoRest ");
                }
                allTodo.addAll(Arrays.asList(pageTodos));
            }

            toDoRepository.saveAll(allTodo);

            return new ResponseEntity<>("ToDos Created " + allTodo.size(), HttpStatus.OK);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }


    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody ToDo toDo) {
        try {

            //    ValidationError newUserErrors = UserValidation.validateNewUser(toDo, toDoRepository, true);

//            if (newUserErrors.hasError()) {
//                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, newUserErrors.toString());
//            } // no else block needed

            ToDo savedTodo = toDoRepository.save(toDo);

            return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);

        } catch (HttpClientErrorException e) {
            return ApiErrorHandling.customApiError(e.getMessage(), e.getStatusCode());
        } catch (Exception e) {
            return ApiErrorHandling.genericApiError(e);
        }
    }

}
