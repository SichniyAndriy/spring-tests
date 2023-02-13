package mate.academy.security;

import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService customUserDetailsService;
    private UserService userService;

    private final String EMAIL = "bobik@g.com";
    private final String PASSWORD = "1234567890";

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(EMAIL);
        Assertions.assertTrue(userDetails.getClass()
                .equals(org.springframework.security.core.userdetails.User.class));
        Assertions.assertEquals(userDetails.getUsername(), user.getEmail());
        Assertions.assertEquals(userDetails.getPassword(), user.getPassword());
        Assertions.assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void loadUserByUsername_NullEMAIL_NotOk() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(null));
    }
}