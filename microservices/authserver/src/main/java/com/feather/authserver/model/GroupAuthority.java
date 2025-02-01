package com.feather.authserver.model;

import java.util.UUID;

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
@Table(name = "Group_Authority")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class GroupAuthority {

    @Id
    @Column(name = "group_authority_id", unique = true, nullable = false)
    private String groupAuthorityId;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "authority_id", nullable = false)
    private String authorityId;

    public GroupAuthority(String groupId, String authorityId) {
        this.groupAuthorityId = UUID.randomUUID().toString();
        this.groupId = groupId;
        this.authorityId = authorityId;
    }

}
