package sn.iage.isi.book_management_jee2.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Tools {

    private Tools() {}

    /**
     * Génère un ISBN-13 valide au format lisible : 978-X-XXXX-XXXX-X
     */
    public static String generateIsbn() {
        String[] prefixes = {"978", "979"};
        Random random = new Random();

        String prefix = prefixes[random.nextInt(2)];
        String group = String.valueOf(random.nextInt(2));
        String publisher = String.format("%04d", random.nextInt(10000));
        String title = String.format("%04d", random.nextInt(10000));

        String base = prefix + group + publisher + title; // 12 chiffres
        int checkDigit = computeIsbn13CheckDigit(base);

        return String.format("%s-%s-%s-%s-%d", prefix, group, publisher, title, checkDigit);
    }

    private static int computeIsbn13CheckDigit(String base12) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(base12.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int remainder = sum % 10;
        return remainder == 0 ? 0 : 10 - remainder;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}