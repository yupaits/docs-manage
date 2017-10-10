package com.yupaits.auth.repository;

import com.yupaits.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 * Created by yupaits on 2017/9/13.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Override
    <S extends User> S save(S s);
}
