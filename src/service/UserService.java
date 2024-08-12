package service;

import model.User;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class UserService {
    private static final String FILE_NAME = "resources/users.txt";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^375\\d{9}$");

    public static void createUser(Scanner scanner, Map<String, User> users) {
        System.out.println("Введите имя: ");
        String firstName = scanner.nextLine();

        System.out.println("Введите фамилию: ");
        String lastName = scanner.nextLine();

        String email;
        do {
            System.out.println("Введите email (формат: any@email.com): ");
            email = scanner.nextLine();
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("Некорректный формат email. Попробуйте снова.");
            }
        } while (!EMAIL_PATTERN.matcher(email).matches());

        List<String> roles = inputRoles(scanner);
        List<String> phones = inputPhones(scanner);

        User user = new User(firstName, lastName, email, phones, roles);
        users.put(email, user);

        System.out.println("Пользователь создан: " + user);
    }

    public static void editUser(Scanner scanner, Map<String, User> users) {
        System.out.println("Введите email пользователя, которого хотите отредактировать: ");
        String email = scanner.nextLine();

        User user = users.get(email);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.println("Редактирование пользователя: " + user);

        System.out.println("Введите новое имя (оставьте пустым, чтобы не изменять): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            user.setFirstName(firstName);
        }

        System.out.println("Введите новую фамилию (оставьте пустым, чтобы не изменять): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            user.setLastName(lastName);
        }

        System.out.println("Менять роли? \n 1.Да \n 2.Нет");
        String roles = scanner.nextLine();
        if (roles.equals("1")) {
            List<String> role = inputRoles(scanner);
            user.setRoles(role);
        }

        System.out.println("Менять телефоны? \n 1.Да \n 2.Нет");
        String phone = scanner.nextLine();
        if (phone.equals("1")) {
            List<String> phones = inputPhones(scanner);
            user.setTelephoneNumbers(phones);
        }

        users.put(email, user);
        System.out.println("Пользователь обновлен: " + user);
    }

    public static void deleteUser(Scanner scanner, Map<String, User> users) {
        System.out.println("Введите email пользователя, которого хотите удалить: ");
        String email = scanner.nextLine();

        if (users.remove(email) != null) {
            System.out.println("Пользователь удален.");
        } else {
            System.out.println("Пользователь не найден.");
        }
    }

    public static void getUserInfo(Scanner scanner, Map<String, User> users) {
        System.out.println("Введите email пользователя для получения информации: ");
        String email = scanner.nextLine();

        User user = users.get(email);
        if (user == null) {
            System.out.println("Пользователь не найден.");
        } else {
            System.out.println("Информация о пользователе: " + user);
        }
    }

    public static List<String> inputRoles(Scanner scanner) {
        List<String> roles = new ArrayList<>();
        int count;

        do {
            System.out.println("Введите количество ролей (от 1 до 3): ");
            count = Integer.parseInt(scanner.nextLine());
            if (count < 1 || count > 3) {
                System.out.println("Некорректное количество ролей. Попробуйте снова.");
            }
        } while (count < 1 || count > 3);

        for (int i = 0; i < count; i++) {
            System.out.println("Введите роль #" + (i + 1) + ": ");
            roles.add(scanner.nextLine());
        }

        return roles;
    }

    public static List<String> inputPhones(Scanner scanner) {
        List<String> phones = new ArrayList<>();
        int count;

        do {
            System.out.println("Введите количество телефонов (от 1 до 3): ");
            count = Integer.parseInt(scanner.nextLine());
            if (count < 1 || count > 3) {
                System.out.println("Некорректное количество телефонов. Попробуйте снова.");
            }
        } while (count < 1 || count > 3);

        for (int i = 0; i < count; i++) {
            String phone;
            do {
                System.out.println("Введите телефон #" + (i + 1) + " (формат: 375*********): ");
                phone = scanner.nextLine();
                if (!PHONE_PATTERN.matcher(phone).matches()) {
                    System.out.println("Некорректный формат телефона. Попробуйте снова.");
                }
            } while (!PHONE_PATTERN.matcher(phone).matches());
            phones.add(phone);
        }

        return phones;
    }

    public static Map<String, User> loadUsers() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("Файл с пользователями не найден, создается новый.");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object object = ois.readObject();

            if (object instanceof Map) {
                return (Map<String, User>) object;
            } else {
                System.out.println("Ошибка: данные в файле не соответствуют ожидаемому формату.");
                return new HashMap<>();
            }
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            return new HashMap<>();
        }
    }


    public static void saveUsers(Map<String, User> users) {
        File file = new File(FILE_NAME);

        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(users);
            System.out.println("Пользователи успешно сохранены в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении пользователей в файл: " + e.getMessage());
        }
    }

}
