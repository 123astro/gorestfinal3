package com.careerdevs.gorestfinal3.repos;

import com.careerdevs.gorestfinal3.models.ToDo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<ToDo, Integer> {

}
