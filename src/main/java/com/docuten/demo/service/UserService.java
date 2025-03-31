package com.docuten.demo.service;

import com.docuten.demo.DTO.UserDto;
import com.docuten.demo.exceptions.UserIdNotProvidedException;
import com.docuten.demo.exceptions.UserNotFoundException;
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

    public User create(UserDto userDto) {
        User user = createUser(userDto);
        repository.save(user);

        return user;
    }

    public User get(UUID id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User update(UserDto userDto) throws UserIdNotProvidedException, UserNotFoundException {
        if (userDto.getId() == null) {
            throw new UserIdNotProvidedException();
        }
        repository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);

        return repository.save(create(userDto));
    }

    public void delete(UUID id) throws UserNotFoundException {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException();
        }
        repository.deleteById(id);
    }
}
