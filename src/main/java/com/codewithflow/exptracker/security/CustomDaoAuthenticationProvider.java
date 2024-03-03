package com.codewithflow.exptracker.security;

import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authn) throws AuthenticationException {
        final Optional<User> user = userRepository.findByEmail(authn.getName());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username");
        }
        final Authentication result = super.authenticate(authn);
        return new UsernamePasswordAuthenticationToken(user.get(), result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
