package com.denysenko.citymonitorweb.controllers;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import com.denysenko.citymonitorweb.exceptions.AccessRestrictedException;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.exceptions.InputValidationException;
import com.denysenko.citymonitorweb.exceptions.RestException;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.models.dto.UserDTO;
import com.denysenko.citymonitorweb.models.entities.User;
import com.denysenko.citymonitorweb.services.converters.impl.UserEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.AppealService;
import com.denysenko.citymonitorweb.services.entity.UserServicee;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

    private final AppealService appealService;
    private final UserServicee userServicee;
    private final UserEntityToDTOConverter userConverter;


    @ModelAttribute("unreadAppealsCnt")
    public long getCountOfUnreadAppeals(){
        return appealService.countOfUnreadAppeals();
    }

    @PreAuthorize("hasAnyAuthority('users:read', 'users:write')")
    @GetMapping()
    public String usersPage(Model model,
                            @RequestParam(name = "page", defaultValue = "1", required = false) int pageNumber,
                            @RequestParam(name = "size", defaultValue = "30", required = false) int pageSize) {
        if (pageNumber < 1 || pageSize < 1)
            throw new InputValidationException("Номер сторінки та її розмір має бути більше нуля. Поточні значення: pageNumber = " + pageNumber + ", pageSize = " + pageSize);

        Page<User> usersPage = userServicee.getPageOfUsers(pageNumber, pageSize);
        Page<UserDTO> dtoPage = usersPage.map(user -> userConverter.convertEntityToDTO(user));
        Paged<UserDTO> paged = new Paged(dtoPage, Paging.of(dtoPage.getTotalPages(), pageNumber, pageSize));
        List<UserAccountStatus> accountStatuses = Arrays.stream(UserAccountStatus.values()).toList();
        List<UserRole> roles = Arrays.stream(UserRole.values()).toList();
        model.addAttribute("users", paged);
        model.addAttribute("accountStatuses", accountStatuses);
        model.addAttribute("roles", roles);
        return "users/users";
    }

    @PreAuthorize("hasAnyAuthority('users:write')")
    @PostMapping()
    public ResponseEntity createUser(@RequestParam("username") String username, @RequestParam("role") String role){
        String newPassword;
        try {
            if (userServicee.userWithUsernameExists(username))
                throw new InputValidationException("Користувач з ім'ям " + username + " вже існує");

            UserRole userRole = UserRole.valueOf(role);

            String usernamePattern = "^[A-Za-z0-9]{6,20}$";
            if (!username.matches(usernamePattern))
                throw new InputValidationException("Ім'я користувача повинно мати від 6 до 20 символів, в які входять латинські букви та цифри");

            newPassword = userServicee.createUser(username, userRole);

        }catch (IllegalArgumentException | InputValidationException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"password\":\""+ newPassword +"\"}");
    }

    @PreAuthorize("hasAnyAuthority('users:write')")
    @PatchMapping("/changeAccountStatus")
    public ResponseEntity updateUserAccountStatus(@RequestParam String username, @RequestParam String status){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String sessionUsername = auth.getName();

            if (username.equals(sessionUsername) || username.equals("sysadm"))
                throw new AccessRestrictedException("Ви не маєте права змінювати статус цього аккаунту: " + username);

            UserAccountStatus userAccountStatus = UserAccountStatus.valueOf(status);

            userServicee.updateUserAccountStatus(username, userAccountStatus);

        } catch (AccessRestrictedException e){
            throw new RestException(e.getMessage(), e, HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException | EntityNotFoundException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }

    @PreAuthorize("hasAnyAuthority('users:write')")
    @PatchMapping("/changeRole")
    public ResponseEntity updateUserRole(@RequestParam String username, @RequestParam String role){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String sessionUsername = auth.getName();

            if (username.equals(sessionUsername) || username.equals("sysadm"))
                throw new AccessRestrictedException("Ви не маєте права змінювати статус цього аккаунту: " + username);

            UserRole userRole = UserRole.valueOf(role);
            userServicee.updateUserRole(username, userRole);

        } catch (AccessRestrictedException e){
            throw new RestException(e.getMessage(), e, HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException | EntityNotFoundException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"msg\":\"success\"}");
    }

    @PreAuthorize("hasAnyAuthority('users:write')")
    @PatchMapping("/resetPassword")
    public ResponseEntity resetPasswordForUser(@RequestParam String username){
        String newPassword;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String sessionUsername = auth.getName();

            if (username.equals(sessionUsername) || username.equals("sysadm"))
                throw new AccessRestrictedException("Ви не маєте права скидати пароль для цього аккаунту: " + username);

            newPassword = userServicee.resetUserPassword(username);

        } catch (AccessRestrictedException e){
            throw new RestException(e.getMessage(), e, HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException | EntityNotFoundException e){
            throw new RestException(e.getMessage(), e, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            throw new RestException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"password\":\""+ newPassword +"\"}");
    }


}
