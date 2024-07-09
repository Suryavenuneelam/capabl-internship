package com.project2.bookstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Customer {
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.setName(name);
        this.setEmail(email);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

class Book {
    private String title;
    private String author;
    private double price;
    private int stock;

    public Book(String title, String author, double price, int stock) {
        this.setTitle(title);
        this.setAuthor(author);
        this.price = price;
        this.setStock(stock);
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
        return price;
    }

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}

class ShoppingCart {
    private List<Book> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Book book) {
        items.add(book);
    }

    public void removeItem(Book book) {
        items.remove(book);
    }

    public List<Book> getItems() {
        return items;
    }
}

class Inventory {
    private Map<Book, Integer> stock;

    public Inventory() {
        this.stock = new HashMap<>();
    }

    public void addToStock(Book book, int quantity) {
        int currentStock = stock.getOrDefault(book, 0);
        stock.put(book, currentStock + quantity);
    }

    public void removeFromStock(Book book, int quantity) {
        int currentStock = stock.getOrDefault(book, 0);
        if (currentStock >= quantity) {
            stock.put(book, currentStock - quantity);
        }
    }

    public int getStock(Book book) {
        return stock.getOrDefault(book, 0);
    }
}

class Order {
    private static int orderCounter = 1;

    private int orderId;
    private Customer customer;
    private List<Book> orderedItems;

    public Order(Customer customer, List<Book> orderedItems) {
        this.setOrderId(orderCounter++);
        this.setCustomer(customer);
        this.orderedItems = orderedItems;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Book book : orderedItems) {
            total += book.getPrice();
        }
        return total;
    }

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}

class OnlineBookStore {
    public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Welcome to the Online Book Store!");

		System.out.println("Enter Book 1 details:");
		Book b1 = getBookDetails(scanner);

		System.out.println("Enter Book 2 details:");
		Book b2 = getBookDetails(scanner);

		System.out.println("Enter Customer 1 details:");
		Customer c1 = getCustomerDetails(scanner);

		System.out.println("Enter Customer 2 details:");
		Customer c2 = getCustomerDetails(scanner);

		ShoppingCart cartc1 = new ShoppingCart();
		ShoppingCart cartc2 = new ShoppingCart();

		cartc1.addItem(b1);
		cartc1.addItem(b2);

		cartc2.addItem(b1);

        Inventory inventory = new Inventory();
		inventory.addToStock(b1, 20);
		inventory.addToStock(b2, 10);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
			Order o1 = new Order(c1, cartc1.getItems());
			double t1 = o1.calculateTotal();
			inventory.removeFromStock(b1, 5);
			inventory.removeFromStock(b2, 3);
			System.out.println("Order 1 Total: $" + t1);
        });

        executorService.submit(() -> {
			Order o2 = new Order(c2, cartc2.getItems());
			double t2 = o2.calculateTotal();
			inventory.removeFromStock(b1, 1);
			System.out.println("Order 2 Total: $" + t2);
        });

        executorService.shutdown();
    }

	private static Book getBookDetails(Scanner scanner) {
		System.out.print("Enter Book Title: ");
		String title = scanner.nextLine();

		System.out.print("Enter Author: ");
		String author = scanner.nextLine();

		System.out.print("Enter Price: ");
		double price = scanner.nextDouble();

		System.out.print("Enter Stock Quantity: ");
		int stock = scanner.nextInt();
		scanner.nextLine();

		return new Book(title, author, price, stock);
	}

	private static Customer getCustomerDetails(Scanner scanner) {
		System.out.print("Enter Customer Name: ");
		String name = scanner.nextLine();

		System.out.print("Enter Customer Email: ");
		String email = scanner.nextLine();

		return new Customer(name, email);
	}
}
