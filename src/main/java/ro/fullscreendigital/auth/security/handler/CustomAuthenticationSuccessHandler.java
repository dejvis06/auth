package ro.fullscreendigital.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ro.fullscreendigital.auth.custom_exception.EmailExistsException;
import ro.fullscreendigital.auth.custom_exception.UsernameExistsException;
import ro.fullscreendigital.auth.model.entity.User;
import ro.fullscreendigital.auth.model.security.CustomOAuth2User;
import ro.fullscreendigital.auth.model.security.UserCustody;
import ro.fullscreendigital.auth.security.util.JwtTokenUtil;
import ro.fullscreendigital.auth.security.util.SecurityConstant;
import ro.fullscreendigital.auth.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        System.err.println(new ObjectMapper().writeValueAsString(oauthUser));

        try {
            // TODO: 1. add name and surname to user entity 2. uncomment email validation
            User user = userService.register(new User(oauthUser.getEmail()));

            response.setHeader(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenUtil.generateToken(new UserCustody(user)));
            response.sendRedirect("/user/test");
        } catch (EmailExistsException | UsernameExistsException e) {
            e.printStackTrace();
            // TODO: change redirect for error
            // response.sendRedirect("/error");
        }
    }
}
