package com.docuten.demo.service;

import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.model.User;
import com.docuten.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private User createUser(UserDto userDto) {
        return new User(
                userDto.getName(),
                userDto.getFirstSurname(),
                userDto.getSecondSurname()
        );
    }

    public User create(UserDto userDto) { // change input param to a DTO
        System.out.println("Saving user: "+ userDto);
        User user = createUser(userDto);
        repository.save(user);

        return user;
    }

    public User get(UUID id) throws Exception { // TODO: create custom exceptions
        return repository.findById(id).orElseThrow();
    }

    public User update(UserDto userDto) throws Exception { // TODO: create custom exceptions
        if (userDto.getId() == null) {
            throw new Exception();
        }
        repository.findById(userDto.getId()).orElseThrow();

        return repository.save(create(userDto));
    }

    public void delete(UUID id) throws Exception { // TODO: use custom exception
        if (!repository.existsById(id)) {
            throw new Exception();
        }
        repository.deleteById(id);
    }
}
