package com.careerdevs.gorestfinal3.repos;

import com.careerdevs.gorestfinal3.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
