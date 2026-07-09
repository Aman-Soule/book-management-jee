package sn.iage.isi.book_management_jee2.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "books")
@Builder
@NamedQuery(name="Category.findAll", query = "select c From Category c order by c.name asc")
public class Category extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;


    @Column(columnDefinition = "boolean default true")
    private boolean state;


    // Une catégorie peut avoir plusieurs livres
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;
}
