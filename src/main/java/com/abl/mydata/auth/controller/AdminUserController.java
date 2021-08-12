package com.abl.mydata.auth.controller;

import com.abl.mydata.auth.domain.User;
import com.abl.mydata.auth.domain.UserV2;
import com.abl.mydata.auth.exception.UserNotFoundException;
import com.abl.mydata.auth.service.UserDaoService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.beans.SimpleBeanInfo;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private UserDaoService service;
    private static final Logger logger = Logger.getLogger(AdminUserController.class.getName());

    // 의존성 주입 (DI) - 생성자, @Autowired, Setter
    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping(path = "/users")
    public MappingJacksonValue retrieveAllUsers() {

        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password", "ssn");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(users);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

//     /admin/v1/users/1
    @GetMapping(path = "/v1/users/{id}")  // -> URI 활용
//    @GetMapping(value="/users/{id}/", params = "version=1") -> param 활용
//    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1") -> header 활용
//    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json") // media type versioning
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {

        User user = service.findOne(id);    // Ctrl + alt + V
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%S] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password", "ssn");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(user);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    @GetMapping(path = "/v2/users/{id}")
//    @GetMapping(value="/users/{id}/", params = "version=2")
//    @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
//    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {

        User user = service.findOne(id);    // Ctrl + alt + V

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%S] not found", id));
        }

        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade", "password", "ssn");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfoV2", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userV2);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

}
