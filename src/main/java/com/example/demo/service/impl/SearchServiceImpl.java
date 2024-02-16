package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.ResultUser;
import com.example.demo.mysql.model.User;
import com.example.demo.mysql.repo.MysqlRepo;
import com.example.demo.postgres.repo.PostgresRepo;
import com.example.demo.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService{
  @Autowired
  private MysqlRepo mysqlRepo;
  
  @Autowired
  private PostgresRepo postgresRepo;

  @Override
  public List<ResultUser> userList(String name) {
    List<User> userMysql = mysqlRepo.getUserByName(name);
    List<com.example.demo.postgres.model.User> userPostgres = postgresRepo.getUserByName(name);
    ArrayList<ResultUser>userDetailList = new ArrayList<>();
    
    for(User user1 :userMysql) {
      ResultUser resultUser = new ResultUser();
      resultUser.setAge(user1.getAge());
      resultUser.setDbName(user1.getDbName());
      resultUser.setEmail(user1.getEmail());
      resultUser.setName(user1.getName());
      
      userDetailList.add(resultUser);
    }
    
    for(com.example.demo.postgres.model.User user1 :userPostgres) {
      ResultUser resultUser = new ResultUser();
      resultUser.setAge(user1.getAge());
      resultUser.setDbName(user1.getDbName());
      resultUser.setEmail(user1.getEmail());
      resultUser.setName(user1.getName());
      
      userDetailList.add(resultUser);
    }
    
    return userDetailList;
  }

  @Override
  public List<ResultUser> searchUserList(String name, String rationBtn) {
    List<User> userMysql = mysqlRepo.getUserByName(name);
    List<com.example.demo.postgres.model.User> userPostgres = postgresRepo.getUserByName(name);
    ArrayList<ResultUser>userDetailList = new ArrayList<>();
    if (rationBtn.equals("1") || rationBtn.equals("3")) {
      for(User user1 :userMysql) {
        ResultUser resultUser = new ResultUser();
        resultUser.setAge(user1.getAge());
        resultUser.setDbName(user1.getDbName());
        resultUser.setEmail(user1.getEmail());
        resultUser.setName(user1.getName());
        
        userDetailList.add(resultUser);
      }
    }
    
    if (rationBtn.equals("2") || rationBtn.equals("3")) {
      for(com.example.demo.postgres.model.User user1 :userPostgres) {
        ResultUser resultUser = new ResultUser();
        resultUser.setAge(user1.getAge());
        resultUser.setDbName(user1.getDbName());
        resultUser.setEmail(user1.getEmail());
        resultUser.setName(user1.getName());
        
        userDetailList.add(resultUser);
      }
    }
    
    
    return userDetailList;
  }
  
}
