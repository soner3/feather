package com.feather.authserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.feather.lib.model.AuditEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "Feather-Authority")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class Authority extends AuditEntity {

    @Id
    @Column(name = "authority_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "authority", cascade = CascadeType.PERSIST, orphanRemoval = false)
    List<GroupAuthority> groupAuthorities = new ArrayList<>();

    public Authority(String authority) {
        this.name = authority;
    }

}
