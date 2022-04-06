package ro.fullscreendigital.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.fullscreendigital.auth.custom_exception.EmailExistsException;
import ro.fullscreendigital.auth.custom_exception.UsernameExistsException;
import ro.fullscreendigital.auth.jpa.UserRepository;
import ro.fullscreendigital.auth.model.entity.User;
import ro.fullscreendigital.auth.model.security.UserCustody;
import ro.fullscreendigital.auth.role.Role;

import javax.transaction.Transactional;

import static ro.fullscreendigital.auth.role.Role.ROLE_USER;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String USER_NOT_FOUND_BY_USERNAME = "User not found by username: ";
    private static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private static final String USERNAME_ALREADY_EXISTS = "Username already exists";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_USERNAME + username);

        user.setAuthorities(Role.valueOf(user.getRole()).getAuthorities());
        return new UserCustody(user);
    }

    @Transactional
    public User register(User user) throws EmailExistsException, UsernameExistsException {

        validate(user.getUsername(), user.getEmail());

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setNonLocked(true);
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());

        return userRepository.save(user);
    }

    private void validate(String username, String email)
            throws EmailExistsException, UsernameExistsException {

        if (validateUsername(username))
            throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);

        if (validateEmail(email))
            throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
    }

    public boolean validateUsername(String username) {
        return userRepository.validateUsername(username) == 1;
    }

    public boolean validateEmail(String email) {
        return userRepository.validateEmail(email) == 1;
    }

    public BCryptPasswordEncoder getbCryptPasswordEncoder() {
        return bCryptPasswordEncoder;
    }
}
