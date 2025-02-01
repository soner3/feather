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
@Table(name = "Authority")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class Authority extends AuditEntity {

    @Id
    @Column(name = "authority_id", nullable = false, unique = true)
    private String authorityId;

    private String name;

    public Authority(String authority) {
        this.authorityId = UUID.randomUUID().toString();
        this.name = authority;
    }

}
