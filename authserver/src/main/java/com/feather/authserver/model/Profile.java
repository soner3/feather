package com.feather.authserver.model;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "Feather-Profile")
@EqualsAndHashCode(callSuper = false)
@Data
@EntityListeners(AuditingEntityListener.class)
public class Profile {

    @Id
    @Column(name = "profile_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID profileId;

    private char gender;

    private boolean isOnline;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.MERGE)
    private User user;

    public Profile() {
        this.isOnline = false;
        this.gender = 'u';
    }

}
