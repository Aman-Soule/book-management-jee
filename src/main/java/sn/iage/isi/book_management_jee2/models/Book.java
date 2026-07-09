package sn.iage.isi.book_management_jee2.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Book extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false,length = 150)
    private String author;

    @Column(name = "publication_year")
    private int publicationYear;

    @Column(name = "count_pages")
    private int countPages;

    // Côté Many : plusieurs livres appartiennent à une catégorie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
