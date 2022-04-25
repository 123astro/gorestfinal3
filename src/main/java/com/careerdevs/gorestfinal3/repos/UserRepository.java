package com.careerdevs.gorestfinal3.repos;

import com.careerdevs.gorestfinal3.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {


}
