package com.licenta.web.rest.custom;

import com.licenta.domain.AppUser;
import com.licenta.domain.User;
import com.licenta.repository.AppUserRepository;
import com.licenta.repository.UserRepository;
import com.licenta.security.SecurityUtils;
import com.licenta.service.AppUserService;
import com.licenta.service.MailService;
import com.licenta.service.UserService;
import com.licenta.service.dto.AppUserDTO;
import com.licenta.service.mapper.UserMapper;
import com.licenta.web.rest.AccountResource;
import com.licenta.web.rest.errors.EmailAlreadyUsedException;
import com.licenta.web.rest.errors.InvalidPasswordException;
import com.licenta.web.rest.errors.LoginAlreadyUsedException;
import com.licenta.web.rest.vm.ManagedUserVM;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/custom")
public class AccountCustomResources {

    private static class AccountResourcesException extends RuntimeException {

        private AccountResourcesException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final AppUserService appUserService;

    private final UserMapper userMapper;

    private final AppUserRepository appUserRepository;

    public AccountCustomResources(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        AppUserService appUserService,
        UserMapper userMapper,
        AppUserRepository appUserRepository
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.appUserService = appUserService;
        this.userMapper = userMapper;
        this.appUserRepository = appUserRepository;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param ManagedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM ManagedUserVM) {
        if (isPasswordLengthInvalid(ManagedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }

        User user = userService.registerUser(ManagedUserVM, ManagedUserVM.getPassword());
        mailService.sendActivationEmail(user);
        //for appUser
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUser(userMapper.userToUserDTO(user));
        appUserDTO.setFirstName(ManagedUserVM.getFirstName());
        appUserDTO.setLastName(ManagedUserVM.getLastName());
        appUserService.save(appUserDTO);
    }

    @GetMapping("/current/appuser")
    public Long getAppUserId() {
        Long result = Long.valueOf("0");
        if (SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).isPresent()) {
            User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).get();
            AppUser appUser = appUserRepository.findAppUserByUserId(user.getId()).get();
            result = appUser.getIdAppUser();
        }
        return result;
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
