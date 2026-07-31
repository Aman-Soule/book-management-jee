package sn.iage.isi.book_management_jee2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.iage.isi.book_management_jee2.models.BaseEntity;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor

public class Permission extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;
}