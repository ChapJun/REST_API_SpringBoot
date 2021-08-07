package com.abl.mydata.auth.controller;

import com.abl.mydata.auth.domain.User;
import com.abl.mydata.auth.exception.UserNotFoundException;
import com.abl.mydata.auth.service.UserDaoService;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private UserDaoService service;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    // 의존성 주입 (DI) - 생성자, Setter, @Autowired
    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping(path = "/users")
    public MappingJacksonValue retrieveAllUsers() {

        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(users);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping(path = "/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id) {

        User user = service.findOne(id);    // Ctrl + alt + V
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%S] not found", id));
        }

        // HATEOS -> 링크추가 (REST API Level 3)
        EntityModel<User> model = EntityModel.of(user);
        WebMvcLinkBuilder linkBuilder= linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkBuilder.withRel("all-users"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(model);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;

    }

    @PostMapping(path = "/users")
    // form 형식이 아닌 json or xml 같은 오브젝트 형태의 데이터를 받기 위해서 @RequestBody vs @RequestParam
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        logger.info(location.toString());
        return ResponseEntity.created(location).build();    // 201 Created
    }

    @PutMapping("/users")
    public void updateUser(@Valid @RequestBody User user) {
        User updateUser = service.update(user);

        if(updateUser == null) {
            throw new UserNotFoundException(String.format("ID[%S] not found", user.getId()));
        }
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        boolean isDelete = service.deleteById(id);

        if(!isDelete) {
            throw new UserNotFoundException(String.format("ID[%S] not found", id));
        }

    }
}
