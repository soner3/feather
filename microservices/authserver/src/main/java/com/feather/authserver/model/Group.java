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
@Table(name = "Group")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class Group extends AuditEntity {

    @Id
    @Column(name = "group_id", unique = true, nullable = false)
    private String groupId;

    private String name;

    public Group(String name) {
        this.groupId = UUID.randomUUID().toString();
        this.name = name;
    }

}
