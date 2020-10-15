package com.jamesmhare.socialgrowthautomator.repository;

import com.jamesmhare.socialgrowthautomator.model.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);

    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);

}