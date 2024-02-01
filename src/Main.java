import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Bike {
    private String bikeId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Bike(String bikeId, String brand, String model, double basePricePerDay) {
        this.bikeId = bikeId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }
    public String getBikeId() {
        return bikeId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable; //to check if bike available
    }

    public void rent() {
        isAvailable = false; // if rented set the IsAvailable value to
        // false
    }

    public void returnBike() {
        isAvailable = true; //If Available keep it true
    }
}

class Customer {
    // Customer Class to Keep the credentials of User
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    // rental class to check which bike is given to which customer for how
    // many days

    private Bike Bike;
    private Customer customer;
    private int days;

    public Rental(Bike Bike, Customer customer, int days) {
        this.Bike = Bike;
        this.customer = customer;
        this.days = days;
    }

    public Bike getBike() {
        return Bike;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class BikeRentalSystem {
    //
    private List<Bike> Bikes;
    private List<Customer> customers;
    private List<Rental> rentals;

    public BikeRentalSystem() {
        Bikes = new ArrayList<>();// for storing the data temporary of bikes as we aren,t using JDBC or other external database
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addBike(Bike Bike) {
        Bikes.add(Bike);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentBike(Bike Bike, Customer customer, int days) {
        if (Bike.isAvailable()) { // check if bike available

            Bike.rent();    // executing rent() to turn that
            // this bike is rented not available
            // for others
            rentals.add(new Rental(Bike, customer, days));

        } else {
            System.out.println("Bike is not available for rent.");
        }
    }

    public void returnBike(Bike Bike) {
        Bike.returnBike(); // it is setting the isAvailable = true
        Rental rentalToRemove = null;
        for (Rental rental : rentals) { // for each rental of type
            // (Rental) in rentals list
            if (rental.getBike() == Bike) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);

        } else {
            System.out.println("Bike not rented.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Bike Rental System =====");
            System.out.println("1. Rent a Bike");
            System.out.println("2. Return a Bike");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("\n== Rent a Bike ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();

                System.out.println("\nAvailable Bikes:");
                for (Bike Bike : Bikes) {
                    if (Bike.isAvailable()) {
                        System.out.println(Bike.getBikeId() + " - " + Bike.getBrand() + " " + Bike.getModel());
                    }
                }

                System.out.print("\nEnter the Bike ID you want to rent: ");
                String bikeId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine();

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Bike selectedBike = null;
                for (Bike Bike : Bikes) {
                    if (Bike.getBikeId().equals(bikeId) && Bike.isAvailable()) {
                        selectedBike = Bike;
                        break;
                    }
                }

                if (selectedBike != null) {
                    double totalPrice = selectedBike.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Bike: " + selectedBike.getBrand() + " " + selectedBike.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentBike(selectedBike, newCustomer, rentalDays);
                        System.out.println("\nBike rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid Bike selection or Bike not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Bike ==\n");
                System.out.print("Enter the Bike ID you want to return: ");
                String bikeId = scanner.nextLine();

                Bike BikeToReturn = null;
                for (Bike Bike : Bikes) {
                    if (Bike.getBikeId().equals(bikeId) && !Bike.isAvailable()) {
                        BikeToReturn = Bike;
                        break;
                    }
                }

                if (BikeToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getBike() == BikeToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnBike(BikeToReturn);
                        System.out.println("Bike returned successfully by " + customer.getName());
                    } else {
                        System.out.println("Bike was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid Bike ID or Bike is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Bike Rental System!");
    }

}
public class Main{
    public static void main(String[] args) {
        BikeRentalSystem rentalSystem = new BikeRentalSystem();

        Bike Bike1 = new Bike("C01", "Bajaj", "CD100", 60.0); // Different base price per day for each Bike
        Bike Bike2 = new Bike("C02", "Hero", "Splendor", 70.0);
        Bike Bike3 = new Bike("C03", "Royal Enfield", "Bullet 350", 150.0);
        rentalSystem.addBike(Bike1);
        rentalSystem.addBike(Bike2);
        rentalSystem.addBike(Bike3);

        rentalSystem.menu();
    }
}