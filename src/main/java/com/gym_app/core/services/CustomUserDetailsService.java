package com.gym_app.core.services;

import com.gym_app.core.dao.UserJpaDao;
import com.gym_app.core.dto.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserJpaDao userJpaDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userJpaDao.getByUserName(username);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        User user = userOptional.get();

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER")
        );
    }
}

