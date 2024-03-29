package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.exceptions.AccessRestrictedException;
import com.denysenko.citymonitorweb.exceptions.IncorrectPasswordException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
import com.denysenko.citymonitorweb.models.dto.QuizPreviewDTO;
import com.denysenko.citymonitorweb.models.dto.UserDTO;
import com.denysenko.citymonitorweb.models.entities.User;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.entity.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Log4j
@Controller
public class MainController {

    private final QuizService quizService;
    private final AppealService appealService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @GetMapping("/")
    public String index(Model model) {
        List<QuizPreviewDTO> lastQuizzes = quizService.getLast10QuizzesPreviews();
        model.addAttribute("quizzes", lastQuizzes);
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) boolean loginError, Model model) {
        if (loginError) {
            String failedLoginMessage = "Ім'я користувача або пароль не вірні. Спробуйте ще раз";
            model.addAttribute("error", failedLoginMessage);
        }
        return "login";
    }

    @PatchMapping("/myprofile/changePassword")
    public ResponseEntity changePassword(@RequestParam("username") String username,
                                                       @RequestParam("oldPassword") String oldPassword,
                                                       @RequestParam("newPassword") String newPassword) {
        log.info("changing password called for username = " + username);
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String sessionUsername = auth.getName();

            if (!username.equals(sessionUsername))
                throw new AccessRestrictedException("Ви не маєте доступу до профілю: " + username);

            User user = userService.getUserByUsername(username);

            if (!passwordEncoder.matches(oldPassword, user.getPassword()))
                throw new IncorrectPasswordException("Невірний старий пароль.");

            String passwPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";

            if (!newPassword.matches(passwPattern))
                throw new InputValidationException("Пароль повинен мати від 8 до 20 символів та містити як мінімум одну цифру," +
                        " одну велику букву, одну маленьку букву.");

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);

        } catch (AccessRestrictedException e) {
            throw new RestException(e.getMessage(), e, HttpStatus.FORBIDDEN);
        } catch (InputValidationException | IncorrectPasswordException e) {
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().body("{\"msg\":\"success\"}");
    }

    @GetMapping("/myprofile")
    public String profilePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.info("Opening profile page for: " + username);
        UserDTO userDTO = userService.getUserDTOByUsername(username);
        model.addAttribute("user", userDTO);
        return "myProfile";
    }

}
