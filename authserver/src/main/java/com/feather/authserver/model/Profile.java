package com.feather.authserver.model;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Feather-Profile")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class Profile {

    @Id
    @Column(name = "profile_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID profileId;

    private char gender;

    private boolean isOnline;

    @OneToOne
    @JoinColumn(name = "user_fk")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    public Profile(User user) {
        this.isOnline = false;
        this.gender = 'u';
        this.user = user;
    }

}
