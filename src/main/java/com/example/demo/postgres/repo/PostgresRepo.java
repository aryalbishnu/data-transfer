package com.example.demo.postgres.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.postgres.model.User;

public interface PostgresRepo extends JpaRepository<User, Integer> {

  @Query("select u from User u where u.name =:name")
  public List<User> getUserByName(@Param("name") String name);
}
