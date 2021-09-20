package com.abl.mydata.auth.controller;

import com.abl.mydata.auth.domain.*;
import com.abl.mydata.auth.exception.PostNotFoundException;
import com.abl.mydata.auth.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserJpaController(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // http://localhost:8088/jpa/users
    @GetMapping(path="/users")
    public MappingJacksonValue retrieveAllUsers() {

        List<User> users = userRepository.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "posts");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(users);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getUserId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    @GetMapping(path = "/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id) {

        // 값이 존재한다면 (isPresent()) vs isEmpty()
        // orElseThrow(Supplier<? extends T> exceptionsupplier) -> 값이 없으면 예외를 발생
        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(String.format("ID[%S] not found", id)));

        // 값이 존재 시 해당 값 반환
        // null일 시 다르게 처리
        // Optional.ofNullable(value) -> 있으면 해당 값 없으면 비어있는 Optional 객체
        // get() -> 비어있는 Optional 객체에 대해서, NoSuchElementException을 던집니다.
        // orElse(T other) ->  비어있는 Optional 객체에 대해서, 넘어온 인자를 반환합니다. (orElse는 null이던말던 항상 불립니다.)
        // orElseGet(Supplier<? extends T> other) -> 비어있는 경우에만 함수가 호출 (orElseGet은 null일 때만 불립니다.)

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

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(String.format("ID[%S] not found", id)));

        return user.getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable Integer id, @RequestBody Post post) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(String.format("ID[%S] not found", id)));

        post.setUser(user);
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getPostId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts/{postId}")
    public Post retrievePost(@PathVariable int id, @PathVariable int postId) {

        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(String.format("ID[%S] not found", id)));

        Optional<Post> post = postRepository.findById(postId);

        if(post.isEmpty()) {
            throw new PostNotFoundException(String.format("UserId[%s], PostId[%s] not found", id, postId));
        }

        return post.get();
    }

}
