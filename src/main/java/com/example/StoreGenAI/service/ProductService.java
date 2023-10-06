package com.example.StoreGenAI.service;

import com.example.StoreGenAI.dbConfig.StoreDatabaseConfiguration;
import com.example.StoreGenAI.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class ProductService {
    Scanner scanner = new Scanner(System.in);
    private StoreDatabaseConfiguration db;

    public ProductService(StoreDatabaseConfiguration db) {
        this.db = db;
    }

    public void getAllProducts() {
        System.out.println("List of products: ");
        printList(db.getProducts());
        getUserChoice();
    }

    private void printList(List<Product> list) {

        for (int i = 0; i < list.size(); i++) {
            System.out.println(String.valueOf(i).concat(". ").concat(list.get(i).toString()));
        }
        System.out.println();
    }

    public void saveNewProduct() {
        Product product = new Product();
        System.out.print("Enter product name: ");
        product.setName(scanner.nextLine());

        System.out.print("Enter product description: ");
        product.setDescription(scanner.nextLine());

        System.out.print("Enter product quantity: ");
        product.setQuantity(Integer.parseInt(scanner.nextLine()));

        System.out.print("Enter product price: ");
        product.setPrice(Double.parseDouble(scanner.nextLine()));

        System.out.println((db.saveProduct(product)) ? "New product is added." : "Something went wrong.Try one more time.");

        getUserChoice();
    }

    public void updateExistingProduct() {

        System.out.printf("Enter product ID you want to change: ");
        String id = scanner.nextLine().trim();
        Product product = db.getProduct(id);
        System.out.print("Enter new product name (leave empty to keep current): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            product.setName(newName);
        }

        System.out.print("Enter new product description (leave empty to keep current): ");
        String newDescription = scanner.nextLine().trim();
        if (!newDescription.isEmpty()) {
            product.setDescription(newDescription);
        }

        System.out.print("Enter new product quantity (leave empty to keep current): ");
        String newQuantityStr = scanner.nextLine().trim();
        if (!newQuantityStr.isEmpty()) {
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                product.setQuantity(newQuantity);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity input. Quantity not updated.");
            }
        }

        System.out.print("Enter new product price (leave empty to keep current): ");
        String newPriceStr = scanner.nextLine().trim();
        if (!newPriceStr.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                product.setPrice(newPrice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price input. Price not updated.");
            }
        }
        System.out.println(db.updateExistingProduct(product) ? "Product updated." : "Something went wrong.Try one more time.");
        getUserChoice();
    }

    public void getUserChoice() {
        System.out.print("Enter your choice(list, save, update, exit): ");
        String choice = scanner.nextLine();
        switch (choice.trim()) {
            case "list" -> getAllProducts();
            case "save" -> saveNewProduct();
            case "update" -> updateExistingProduct();
            case "exit" -> System.exit(0);
            default -> {
                System.out.println("Invalid user input. Try one more time.");
                getUserChoice();
            }
        }

    }
}
