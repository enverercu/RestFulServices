package com.enver.rest.webservices.restfulwebservices;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJPAResource {
    @Autowired
    private UserDaoService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/jpa/filtering")
    public List<Filtering> retrieveFilter(){
        return Arrays.asList(new Filtering("enver","ercu","yilmaz"));
    }

    @GetMapping("/jpa/dfiltering")
    public MappingJacksonValue retrievedFilter(){

        List<Filtering> list = Arrays.asList(new Filtering("enver","ercu","yilmaz"));

        SimpleBeanPropertyFilter filter
                = SimpleBeanPropertyFilter.filterOutAllExcept("field1","field2");

        FilterProvider filters = new SimpleFilterProvider().addFilter("FilterIt",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(list);
        mapping.setFilters(filters);

        return mapping;

    }

    @GetMapping("/jpa/users/{id}")
    public Optional<User> getUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);
        if (user == null){
            throw new UserNotFoundException("id - " + id);
        }
        return user;
    }

    @PostMapping ("/jpa/users")
    public ResponseEntity createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }


}
