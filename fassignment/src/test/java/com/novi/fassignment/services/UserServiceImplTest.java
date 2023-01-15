package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    //@InjectMocks UserService userService = new UserService();

    //or alternatively
    @InjectMocks
    UserServiceImpl userService;

    private User user = new User();

    @BeforeEach
    void setup() {

        user.setUsername("cobergfell");
        user.setPassword("bla");
        user.setEmail("fake@mail.com");
        UserDto userDto=UserDto.fromUser(user);

    }



    @Test
    void userServiceGetUserByIdTest() {
        User expected = new User();
        expected.setUsername("myUsernameIsAlsoMyUniqueId");
        Mockito.when(userRepositoryMock.findById("myUsernameIsAlsoMyUniqueId")).thenReturn(Optional.of(expected));
        Optional<User> actual = userService.getUser(expected.getUsername());
        Assertions.assertSame(expected.getUsername(), actual.get().getUsername());
    }

    @Test
    void checkingIfExitingUserExistsReturnsTrue() {
        //User user = new User();
        //user.setUsername("myUsernameIsAlsoMyUniqueId");
        Mockito.when(userRepositoryMock.existsById("cobergfell")).thenReturn(true);
        Assertions.assertTrue(userService.userExists("cobergfell"));
    }


/*    @Test
    void searchingUnknownIdShouldReturnErrorMessage() {
        long id = 1L;
        Mockito.when(userRepositoryMock.findById(id)).thenReturn(Optional.empty());
        try {
            userService.getUserById(id);
        } catch (Exception e) {
            Assertions.assertEquals("id does not exist", e.getMessage());
        }
    }*/


}