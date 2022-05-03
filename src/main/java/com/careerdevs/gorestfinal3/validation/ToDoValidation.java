package com.careerdevs.gorestfinal3.validation;

import com.careerdevs.gorestfinal3.models.ToDo;
import com.careerdevs.gorestfinal3.models.User;
import com.careerdevs.gorestfinal3.repos.ToDoRepository;
import com.careerdevs.gorestfinal3.repos.UserRepository;

import java.util.Date;
import java.util.Optional;

public class ToDoValidation {


    public static ValidationError validateToDo (ToDo toDo, ToDoRepository toDoRepo, UserRepository userRepo,
                                                boolean isUpdate) {

        ValidationError errors = new ValidationError();

        if (isUpdate) {
            if (toDo.getId() == 0) {
                errors.addError("id", "ID can not be left blank");
            } else {
                Optional<ToDo> foundToDo = toDoRepo.findById(toDo.getId());
                if (foundToDo.isEmpty()) {
                    errors.addError("id", "No user found with the ID: " + toDo.getId());
                }
            }
        }
        long toDoUserId = toDo.getUser_id();
        long toDoId = toDo.getId();
        String toDoTitle = toDo.getTitle();
        String toDoStatus = toDo.getStatus();
        String toDoDue_on = toDo.getDue_on();


        if (toDoTitle == null || toDoTitle.trim().equals("")) {
            errors.addError("Title", "Title can not be left blank");
        }

        if (toDoStatus == null || toDoStatus.trim().equals("")) {
            errors.addError("status", "Status can not be left blank");
        }

        if (toDoDue_on == null) {
            errors.addError("date", "Date can not be left blank");
        }

        if ( toDoUserId == 0) {
            errors.addError("user_id", "ToDor_ID can not be left blank");
        } else {
            // is the postUserId connected to an existing user.
            Optional<User> foundUser = userRepo.findById(toDoId);

            if (foundUser.isEmpty()) {
                errors.addError("user_id",
                        "User_ID is invalid because there is no user found with the id: " + toDoUserId);
            }
        }
        return errors;
    }
}



