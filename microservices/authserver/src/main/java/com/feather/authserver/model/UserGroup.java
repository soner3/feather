package com.feather.authserver.model;

import java.util.UUID;

import com.feather.lib.model.AuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "User_Group")
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserGroup extends AuditEntity {

    @Id
    @Column(name = "user_group_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_fk", nullable = false)
    private Group group;

    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }

}
