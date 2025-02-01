package com.feather.authserver.model;

import java.util.UUID;

import com.feather.lib.model.AuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "User")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class User extends AuditEntity {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ToString.Exclude
    private String password;

    public User(String email, String username, String phoneNumber, String firstName, String lastName,
            String password) {
        this.userId = UUID.randomUUID().toString();
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

}
