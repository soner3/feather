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
@Table(name = "User_Group")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class UserGroup extends AuditEntity {

    @Id
    @Column(name = "user_group_id", unique = true, nullable = false)
    private String userGroupId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    public UserGroup(String userId, String groupId) {
        this.userGroupId = UUID.randomUUID().toString();
        this.userId = userId;
        this.groupId = groupId;
    }

}
