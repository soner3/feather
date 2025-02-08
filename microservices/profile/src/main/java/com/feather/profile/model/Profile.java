package com.feather.profile.model;

import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.feather.lib.model.AuditEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Feather-Profile")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Profile extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "profile_id", unique = true, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String country;

    private String city;

    private String street;

    private String plz;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.REMOVE)
    private ProfileMeta profileMeta;

    public Profile(Gender gender, String country, String city, String street, String plz, String userId) {
        this.gender = gender;
        this.country = country;
        this.city = city;
        this.street = street;
        this.plz = plz;
        this.userId = userId;
    }

}
