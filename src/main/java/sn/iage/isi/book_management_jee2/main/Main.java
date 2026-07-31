package sn.iage.isi.book_management_jee2.main;

import sn.iage.isi.book_management_jee2.repositories.CategoryRepository;

public class Main {
    public static void main(String[] args) {
        // Simule le remplissage du ThreadLocal que fera AuthFilter plus tard
//        SessionContext.setCurrentUsername("admin");
//        try {
//            CategoryRepository categoryRepository = new CategoryRepository();
//            BookRepository bookRepository = new BookRepository();
//
//            Category roman = categoryRepository.create(
//                    Category.builder().name("Roman").build()
//            );
//            System.out.println("Créée : " + roman);
//
//            Book book = bookRepository.createBook(
//                    Book.builder()
//                            .title("Les Misérables")
//                            .author("Victor Hugo")
//                            .publicationYear(1862)
//                            .countPages(1500)
//                            .category(roman)
//                            .build()
//            );
//            System.out.println("Créé : " + book);
//
//            System.out.println("\n===== LISTE DES CATEGORIES =====");
//            for (Category c : categoryRepository.getAll()) {
//                System.out.println(c);
//            }
//
//            System.out.println("\n===== LISTE DES LIVRES =====");
//            for (Book b : bookRepository.listAllBooks()) {
//                System.out.println(b);
//            }
//
//        } finally {
//            SessionContext.clear(); // toujours nettoyer, comme le fera AuthFilter dans son finally
//        }
    }
}

