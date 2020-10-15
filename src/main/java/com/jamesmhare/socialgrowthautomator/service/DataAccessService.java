package com.jamesmhare.socialgrowthautomator.service;

import com.jamesmhare.socialgrowthautomator.model.user.User;
import com.jamesmhare.socialgrowthautomator.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class DataAccessService {

    private UserRepository userRepository;

    public DataAccessService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserSchemaName() throws Exception {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        final User user = userRepository.getUserByUsername(username);
        if (user != null && user.getSchemaName() != null) {
            return user.getSchemaName();
        } else {
            throw new Exception();
        }
    }

}
