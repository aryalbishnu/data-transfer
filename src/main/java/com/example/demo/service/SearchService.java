package com.example.demo.service;

import java.util.List;

import com.example.demo.model.ResultUser;


public interface SearchService {
  
  List<ResultUser>userList(String name);
  
  List<ResultUser>searchUserList(String name, String rationBtn);
  
 

}
