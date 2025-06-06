package com.example.demo.user.domain.api;

import com.example.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserApiRepository extends JpaRepository<User, Long> {
}
