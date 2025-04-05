package com.unicesumar;

import com.unicesumar.entities.User;
import com.unicesumar.repository.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static UserRepository userRepository;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:your-database.db")) {
            initializeDatabase(connection);
            userRepository = new UserRepository(connection);
            runApplication();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "uuid UUID PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
        try (java.sql.Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    private static void runApplication() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> registerUser(scanner);
                case 2 -> displayUsers();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("1. Cadastrar usuário");
        System.out.println("2. Listar usuários");
        System.out.println("0. Sair");
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();
        User user = new User(UUID.randomUUID(), name, email, password);
        userRepository.save(user);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void displayUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            System.out.println("UUID: " + user.getUuid());
            System.out.println("Nome: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Senha: " + user.getPassword());
            System.out.println("-----");
        });
    }
}