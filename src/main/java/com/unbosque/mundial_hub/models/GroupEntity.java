package com.unbosque.mundial_hub.models;

import com.unbosque.mundial_hub.utilities.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = TableNames.GROUP)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GroupEntity extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;

    @Column(unique = true, length = 100)
    private String inviteToken;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> users;

    @OneToMany(mappedBy = "group")
    private List<PoolEntity> pools;
}
