package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private UserAccountStatus userAccountStatus;

    @PrePersist
    void onCreate() {
        if (Objects.isNull(userAccountStatus)) {
            userAccountStatus = UserAccountStatus.ACTIVE;
        }
    }

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
