package com.feather.authserver.model;

import java.util.UUID;

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
@Table(name = "Group_Authority")
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@Getter
@Setter
public class GroupAuthority {

    @Id
    @Column(name = "group_authority_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    public GroupAuthority(Group group, Authority authority) {
        this.group = group;
        this.authority = authority;
    }

}
