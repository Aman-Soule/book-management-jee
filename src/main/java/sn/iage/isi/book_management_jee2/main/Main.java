package sn.iage.isi.book_management_jee2.main;

import sn.iage.isi.book_management_jee2.repositories.CategoryRepository;

public class Main {
    public static void main(String[] args) {
        CategoryRepository cr = new CategoryRepository();
        System.out.println("Connexion + creation OK");
    }
}
