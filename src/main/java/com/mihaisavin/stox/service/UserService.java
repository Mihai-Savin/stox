package com.mihaisavin.stox.service;

import com.mihaisavin.stox.dao.UserDAO;
import com.mihaisavin.stox.domain.User;
import com.mihaisavin.stox.domain.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmService.class);

    @Autowired
    UserDAO dao;

    @RequestMapping(method = RequestMethod.POST)
    public void save(User user) throws ValidationException {
        LOGGER.debug("Saving: " + user);
        validate(user);


        dao.update(user);
    }
    private void validate(User user) throws ValidationException {
        List<String> errors = new LinkedList<String>();

        if ("password".equals(user.getPassword()))
            errors.add("Password cannot be 'password'");
        if (user.getUsername() == null)
            user.setUsername(user.getEmail());

        if (user.getEmail() == null)
            errors.add("Please enter your email.");

        if (!user.getPassword().equals(user.getOldPassword()))
            errors.add("Passwords don't match.");

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toArray(new String[]{}));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) dao.findByUsername(username);
        if (user != null) {
            System.out.println("Fetching login details for " + user.toString());
            String role = "" + user.getRole();
			 List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
			 gas.add(new SimpleGrantedAuthority(role));
			 user.setAuthorities(gas);
			 UserDetails userDetails = new
			 org.springframework.security.core.userdetails.User(username,
			 user.getPassword(), true, true,
			 true, true, AuthorityUtils.createAuthorityList(role));

            return user;
        } else {
            throw new UsernameNotFoundException("The username was not found");
        }
    }

    public String getEmailByUserId(long userId) {
        return dao.getUsersEmail(userId);
    }

}