package account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // Set email as the username
                .password(user.getPassword())   // Set the user's password
                .roles()                        // Assign roles (can be adjusted as needed)
                .build();
    }
}
