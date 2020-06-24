package com.example.meetu.dao;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//public interface UserDao {
//    int add(User user);
//}
public interface UserDao extends JpaRepository<User,Integer> {
}


