package com.unbosque.mundial_hub.models;

import com.unbosque.mundial_hub.utilities.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = TableNames.PREFERENCE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    private UUID prefId;
    private String category;
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}