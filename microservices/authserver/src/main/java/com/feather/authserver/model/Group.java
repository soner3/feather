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
@Table(name = "Feather-Groups")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class Group extends AuditEntity {

    @Id
    @Column(name = "group_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<UserGroup> userGroups = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<GroupAuthority> groupAuthorities = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

}
