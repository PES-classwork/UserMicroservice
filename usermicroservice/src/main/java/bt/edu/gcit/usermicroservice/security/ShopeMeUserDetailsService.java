package bt.edu.gcit.usermicroservice.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bt.edu.gcit.usermicroservice.dao.UserDAO;
import bt.edu.gcit.usermicroservice.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import bt.edu.gcit.usermicroservice.security.ShopmeuserDetails;

@Service
public class ShopeMeUserDetailsService implements UserDetailsService {
    @Autowired
    private final UserDAO userDAO;

    @Autowired
    public ShopeMeUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // User user = userDAO.findByEmail(email);
        // if (user == null) {
        // throw new UsernameNotFoundException("User not found with email: " + email);
        // }
        // return new ShopmeuserDetails(user);
        System.out.println("Email: " + email); // print out the email
        User user = userDAO.findByEmail(email);
        if (user == null) {
            System.out.println("DEBUG: User NOT found in database");
            throw new UsernameNotFoundException("User not found with email: " +
                    email);
        }
        // System.out.println("User: " + user.getEmail()); // print out the user email
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> {
            System.out.println("Role: " + role.getName()); // print out the rolename
            return new SimpleGrantedAuthority(role.getName());
        })
                .collect(Collectors.toList());
        System.out.println("Authorities: " + authorities); // print out the list ofauthorities
        System.out.println("User in loadbyUserna: " + user.getPassword()); // print out the user email
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), authorities);
    }
}