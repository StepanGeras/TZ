package main;

import model.User;

import java.util.Map;
import java.util.Scanner;

import static service.UserService.*;

public class Main {

    public static void main(String[] args) {
        Map<String, User> users = loadUsers();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nВыберите действие: ");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Редактировать пользователя");
            System.out.println("3. Удалить пользователя");
            System.out.println("4. Получить информацию о пользователе");
            System.out.println("5. Выйти");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createUser(scanner, users);
                    break;
                case "2":
                    editUser(scanner, users);
                    break;
                case "3":
                    deleteUser(scanner, users);
                    break;
                case "4":
                    getUserInfo(scanner, users);
                    break;
                case "5":
                    saveUsers(users);
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }


}
