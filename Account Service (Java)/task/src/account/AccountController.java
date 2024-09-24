package account;


import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api")
public class AccountController {

    @Autowired
    UserRepository userRepository;


    @PostMapping ("/auth/signup")
    @ResponseStatus(HttpStatus.OK)
    UserResponse UserGetsCreated(@Valid @RequestBody UserRequest userRequest) throws WrongEmailException, UserAlreadyExistsException {

        if(!StringUtils.endsWith(userRequest.email(), "@acme.com") ){
            throw new WrongEmailException();
        }
        if(userRepository.existsByEmailIgnoreCase(userRequest.email())){
            throw new UserAlreadyExistsException("User exists!");
        }
        User user = new User();
        user.setName(userRequest.name());
        user.setLastname(userRequest.lastname());
        user.setPassword(userRequest.password());
        user.setEmail(userRequest.email());
        user = userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(),user.getLastname(), user.getEmail());
    }

    @GetMapping("/empl/payment")
    @ResponseStatus(HttpStatus.OK)
    UserResponse UserGetsPayment(@RequestBody UserLoggingRequest userLoggingRequest) throws UserIsNotAuthenticated {

        if(!userRepository.existsByEmailIgnoreCase(userLoggingRequest.email()) && !userRepository.existsByPassword(userLoggingRequest.password())){
            throw new UserIsNotAuthenticated("");
        }
        User user = new User();
        return new UserResponse(user.getId(), user.getName(),user.getLastname(), user.getEmail());

//        AppAuthentication authentication = (AppAuthentication) SecurityContextHolder.getContext().getAuthentication();
//
//        if(!authentication.isAuthenticated()){
//            throw new UserIsNotAuthenticated();
//        }


    }


    @ExceptionHandler(WrongEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void wrongEmailException(WrongEmailException ex) {
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void userAlreadyExistsException(UserAlreadyExistsException ex) {
    }

    @ExceptionHandler(UserIsNotAuthenticated.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    void userIsNotAuthenticated(UserIsNotAuthenticated ex) {
    }

}
