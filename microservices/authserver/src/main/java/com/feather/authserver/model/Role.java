package com.feather.authserver.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.feather.lib.model.AuditEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Feather-Role")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class Role extends AuditEntity {

    @Id
    @Column(name = "role_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private FeatherRole name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();

    public Role(FeatherRole name) {
        this.name = name;
    }

}
