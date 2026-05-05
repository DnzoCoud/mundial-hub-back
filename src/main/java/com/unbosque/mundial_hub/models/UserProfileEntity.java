package com.unbosque.mundial_hub.models;

import com.unbosque.mundial_hub.utilities.TableNames;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = TableNames.USER_PROFILE)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String fullName;
    private LocalDate birthDate;
    private String city;
    private String country;
    private String avatarUrl;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;
}
