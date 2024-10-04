package account;


import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api")
public class AccountController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping ("/auth/signup")
    @ResponseStatus(HttpStatus.OK)
    UserResponse UserGetsCreated(@Valid @RequestBody UserRequest userRequest) throws WrongEmailException, UserAlreadyExistsException {

        if(!StringUtils.endsWith(userRequest.email(), "@acme.com") ){
            throw new WrongEmailException();
        }
        if(userRepository.existsByEmailIgnoreCase(userRequest.email())){
            throw new UserAlreadyExistsException("User exist!");
        }
        User user = new User();
        user.setName(userRequest.name());
        user.setLastname(userRequest.lastname());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setEmail(userRequest.email());
        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(),user.getLastname(), user.getEmail());
    }

    @GetMapping("/empl/payment")
    @ResponseStatus(HttpStatus.OK)
    UserResponse UserGetsPayment(@AuthenticationPrincipal UserDetails userDetails) throws UserIsNotAuthenticated {

        User user = new User();

        AppAuthentication authentication = (AppAuthentication) SecurityContextHolder.getContext().getAuthentication();

//         authentication instanceof AnonymousAuthenticationToken
//        || userDetails.getAuthorities() == null
        //authentication.getPrincipal()
        if(!authentication.isAuthenticated()){
            throw new UserIsNotAuthenticated("");
        }
//        User user = (User) authentication.getPrincipal();
        UserRepository userRepository = (UserRepository) authentication.getPrincipal();
        return new UserResponse(authentication.getDetails(user.getId()), authentication.getCredentials(),  );
//        return new UserResponse(userRepository.findById(),user.getName(),user.getLastname(),);
//        userDetails.getUsername()

    }


    @ExceptionHandler(WrongEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void wrongEmailException(WrongEmailException ex) {
    }

//    @ExceptionHandler(UserAlreadyExistsException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    MyErrorResponse userAlreadyExistsException(UserAlreadyExistsException ex) {
//        return new MyErrorResponse(ex.getMessage());
//    }

    @ExceptionHandler(UserIsNotAuthenticated.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    void userIsNotAuthenticated(UserIsNotAuthenticated ex) {
    }

}
