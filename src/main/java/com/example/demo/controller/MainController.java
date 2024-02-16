package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.ResultUser;
import com.example.demo.mysql.model.User;
import com.example.demo.mysql.repo.MysqlRepo;
import com.example.demo.postgres.repo.PostgresRepo;
import com.example.demo.service.SearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {
  
  @Autowired
  private MysqlRepo mysqlRepo;
  
  @Autowired
  private PostgresRepo postgresRepo;
  
  @Autowired
  private SearchService serchService;
  
  /**
   * 初期表示画面
   * @param model
   * @return home.html
   */
  @GetMapping("/")
  public String mainPage(Model model) {
      return "home";
  }
  
  /**
   * 子画面画面
   * @param model
   * @return nameSearch.html
   * @throws UnsupportedEncodingException 
   * @throws JsonProcessingException 
   * @throws JsonMappingException 
   */
  @SuppressWarnings({ "unchecked"})
  @GetMapping("/nameSearch")
  public String userLoginby(Model model, @RequestParam String arrayList) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
    String decoded = URLDecoder.decode(arrayList, StandardCharsets.UTF_8.name());
    ObjectMapper mapper = new ObjectMapper();
    List<Map<String, Object>> data = mapper.readValue(decoded, List.class);
    List<User>userdata = new ArrayList<>();
    
    for (Map<String, Object> element : data) {
      User user = new User();
      String name = (String) element.get("name");
      int age = (Integer) element.get("age");
      String email = (String) element.get("email");
      String dbName = (String) element.get("dbName");
      user.setAge(age);
      user.setDbName(dbName);
      user.setEmail(email);
      user.setName(name);
      userdata.add(user);
  }
    String nameValue = userdata.get(0).getName();
    model.addAttribute("nameValue", nameValue);
    model.addAttribute("dbValue", "3");
    model.addAttribute("userList", userdata);
      return "nameSearch";
  }
     
  
  /**
   * ユーザーの追加 mysqlDb
   * @param mysqlUser
   */
  @PostMapping("/addUser/mysql")
  public ResponseEntity<User> addUserMysql(@RequestBody User mysqlUser) {
      User user = this.mysqlRepo.save(mysqlUser);
      return  new ResponseEntity<User>(user, HttpStatus.OK);
  }
  
  /**
   * ユーザーの追加 postgresDb
   * @param postgresUser
   */
  @PostMapping("/addUser/postgres")
  public ResponseEntity<com.example.demo.postgres.model.User> addUserPostgres(@RequestBody com.example.demo.postgres.model.User postgresUser) {
      com.example.demo.postgres.model.User user = this.postgresRepo.save(postgresUser);
      return  new ResponseEntity<com.example.demo.postgres.model.User>(user, HttpStatus.OK);
  }
  
  /**
   * ユーザーの検索 mysqlDb
   * @param mysqlUser
   */
  @PostMapping("/search/{query}")
  public ResponseEntity<List<ResultUser>> searchUserMainScreen(@PathVariable("query") String query) {
    List<ResultUser> userList = this.serchService.userList(query);
    return  new ResponseEntity<List<ResultUser>>(userList, HttpStatus.OK);
  }
  
  /**
   * ユーザーの検索 
   * @param userSearch
   * @param inlineRadioOptions
   */
  @PostMapping("/userSearch")
  public String searchUserSubScreen(Model model,  @RequestParam("userSearch") String userSearch, @RequestParam("inlineRadioOptions") String inlineRadioOptions) {
    List<ResultUser> userList = this.serchService.searchUserList(userSearch, inlineRadioOptions);
    
    model.addAttribute("userList", userList);
    model.addAttribute("nameValue", userSearch);
    model.addAttribute("dbValue", inlineRadioOptions);
      
      return  "nameSearch";
  }
  

}
