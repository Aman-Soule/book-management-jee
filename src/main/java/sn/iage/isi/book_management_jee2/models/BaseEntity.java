package sn.iage.isi.book_management_jee2.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass //permet à JPA de partager les champs sans créer une table séparée pour BaseEntity.
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at", nullable = false)
    protected LocalDateTime createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    protected LocalDateTime updatedAt;


    @Column(name = "user_created", nullable = false, updatable = false, length = 100)
    protected String userCreated;


    @Column(name = "user_updated", nullable = false, length = 100)
    protected String userUpdated;

    @PrePersist
    public void prePersist() {
        this.userCreated = "system";
        this.userUpdated = "system";
    }

    @PreUpdate
    public void preUpdate() {
        this.userUpdated = "system";
    }
}
