package ro.fullscreendigital.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ro.fullscreendigital.auth.custom_exception.EmailExistsException;
import ro.fullscreendigital.auth.custom_exception.UsernameExistsException;
import ro.fullscreendigital.auth.model.entity.User;
import ro.fullscreendigital.auth.model.security.UserCustody;
import ro.fullscreendigital.auth.security.util.JwtTokenUtil;
import ro.fullscreendigital.auth.security.util.SecurityConstant;
import ro.fullscreendigital.auth.service.UserService;
import ro.fullscreendigital.auth.util.HttpResponse;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil generateJwtToken;

    @PostMapping(value = "/register")
    public ResponseEntity<HttpResponse> register(@RequestBody User user) {

        try {
            user = userService.register(user);
            return new ResponseEntity<>(new HttpResponse(200, HttpStatus.OK, null, "Success", user), HttpStatus.OK);

        } catch (UsernameExistsException | EmailExistsException e) {
            return new ResponseEntity<>(new HttpResponse(400, HttpStatus.BAD_REQUEST, e.getMessage(), "Error", null),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new HttpResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<HttpResponse> login(@RequestBody User user) {

        try {
            authenticate(user.getUsername(), user.getPassword());

            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            HttpHeaders header = getHeaderWithJwt((UserCustody) userDetails);

            return new ResponseEntity<>(new HttpResponse(200, HttpStatus.OK, null, "Success", userDetails), header,
                    HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new HttpResponse(400, HttpStatus.BAD_REQUEST, e.getMessage(), "Error", null),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new HttpResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Error", null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/login/google")
    public String loginGoogle() {
        return "test";
    }

    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getHeaderWithJwt(UserCustody userCustody) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConstant.JWT_TOKEN_HEADER, generateJwtToken.generateToken(userCustody));
        return httpHeaders;
    }
}
