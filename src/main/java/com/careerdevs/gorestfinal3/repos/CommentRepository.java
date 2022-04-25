package com.careerdevs.gorestfinal3.repos;

import com.careerdevs.gorestfinal3.models.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository <Comment, Integer>{
}
