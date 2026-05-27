package com.unbosque.mundial_hub.models;

import com.unbosque.mundial_hub.models.enums.EntityStatus;
import com.unbosque.mundial_hub.models.enums.PoolStatus;
import com.unbosque.mundial_hub.utilities.TableNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = TableNames.POOL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PoolEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Column(name = "is_private")
    private boolean isPrivate = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PoolStatus status;

    @Column(nullable = false, name = "starts_at")
    private LocalDateTime startsAt = LocalDateTime.now();

    @Column(nullable = false, name = "ends_at")
    private LocalDateTime endsAt;
}
