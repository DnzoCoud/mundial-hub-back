package com.unbosque.mundial_hub.models;

import com.unbosque.mundial_hub.models.enums.UserRole;
import com.unbosque.mundial_hub.utilities.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = TableNames.USER)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class        UserEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;

    private String email;

    private String status;

    private String password;

    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "user_role")
    private UserRole role;

    @OneToOne(mappedBy = "user")
    private UserProfileEntity profile;

    @OneToMany(mappedBy = "user")
    private List<UserGroup> groups;
}