package studycom.web.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingClass {
    private static final byte[] salt = new byte[]{49, 54, 75, -31, 28, 120, -25, -55, -128, 49 - 23, 9, -124, -113, -118, -120};

    private HashingClass() {
    }

    public static String hashPassword(String userPassword) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512"); //получаем хэш-сумму текста нужного алгоритма хэширования
            md.update(salt); //передаем массив байтов соли чтобы сформировать дайджест
            byte[] bytes = md.digest(userPassword.getBytes(StandardCharsets.UTF_8)); //получаем массив байтов дайджеста сообщения
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1)); //хэшируем каждый байт
                                                                                                //и записываем в строку нового пароля
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static boolean validatePassword(String userPassword, String passwordDb){ //функция валидации нормального пароля и хэш пароля
        String hashPassword = hashPassword(userPassword);
        return hashPassword.equals(passwordDb);
    }
}
