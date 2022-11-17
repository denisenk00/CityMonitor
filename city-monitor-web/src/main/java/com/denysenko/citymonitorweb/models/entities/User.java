package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Data
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

}