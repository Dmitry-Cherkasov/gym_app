package com.gym_app.core.dao;

import com.gym_app.core.dto.common.User;
import org.springframework.stereotype.Component;

@Component
public class UserJpaDao extends JpaDao<User, Long> {

    public UserJpaDao() {
        super(User.class);
    }
}
