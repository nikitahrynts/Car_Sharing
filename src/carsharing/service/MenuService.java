package carsharing.service;

import carsharing.dao.CarDAO;
import carsharing.dao.CompanyDAO;
import carsharing.dao.CustomerDAO;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.util.List;
import java.util.Scanner;

public class MenuService {

    private final Scanner scanner;
    private final CompanyDAO companyDAO;
    private final CarDAO carDAO;
    private final CustomerDAO customerDAO;

    public MenuService(Scanner scanner, CompanyDAO companyDAO, CarDAO carDAO, CustomerDAO customerDAO) {
        this.scanner = scanner;
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
    }

    public void start() {
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    managerMenu();
                    break;
                case "2":
                    loginCustomer();
                    break;
                case "3":
                    createCustomer();
                    break;
                case "0":
                    return;
            }
        }
    }

    private void managerMenu() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    List<Company> companies = companyDAO.findAllCompanies();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        System.out.println("Choose the company:");
                        for (int i = 0; i < companies.size(); i++) {
                            System.out.println(i + 1 + ". " + companies.get(i).getName());
                        }
                        System.out.println("0. Back");
                        int companyChoice = Integer.parseInt(scanner.nextLine());
                        if (companyChoice == 0) continue;
                        Company selected = companies.get(companyChoice - 1);
                        carMenu(scanner, selected);
                    }
                    break;
                case "2":
                    System.out.println("Enter the company name:");
                    String name = scanner.nextLine();
                    companyDAO.add(name);
                    break;
                case "0":
                    return;
            }
        }
    }

    private void carMenu(Scanner scanner, Company selected) {
        while (true) {
            System.out.printf("'%s' company\n", selected.getName());
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    List<Car> cars = carDAO.findAllCarsByCompanyId(selected.getId());
                    if (cars.isEmpty()) {
                        System.out.println("The car list is empty!");
                    } else {
                        for (int i = 0; i < cars.size(); i++) {
                            System.out.println(i + 1 + ". " + cars.get(i).getName());
                        }
                    }
                    break;
                case "2":
                    System.out.println("Enter the car name:");
                    String name = scanner.nextLine();
                    carDAO.addNewCar(name, selected.getId());
                    break;
                case "0":
                    return;
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDAO.createCustomer(name);
        System.out.println("The customer was added!");
    }

    private void loginCustomer() {
        List<Customer> customers = customerDAO.findAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }
        System.out.println("Customer list:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(i + 1 + ". " + customers.get(i).getName());
        }
        System.out.println("0. Back");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 0) return;
        Customer customer = customers.get(choice - 1);
        customerMenu(customer);
    }

    private void customerMenu(Customer customer) {
        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    rentCar(customer);
                    break;
                case "2":
                    returnCar(customer);
                    break;
                case "3":
                    showRentedCar(customer);
                    break;
                case "0":
                    return;
            }
        }
    }

    private void showRentedCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }
        Car car = carDAO.findCarById(customer.getRentedCarId());
        Company company = companyDAO.findCompanyById(car.getCompanyId());
        System.out.printf("Your rented car '%s':\n", car.getName());
        System.out.printf("Company '%s':\n", company.getName());
    }

    private void returnCar(Customer customer) {
        if (customer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }
        customerDAO.returnCar(customer.getId());
        customer.setRentedCarId(null);
        System.out.println("You've returned a rented car!");
    }

    private void rentCar(Customer customer) {
        if (customer.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
            return;
        }
        List<Company> companies = companyDAO.findAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }
        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println(i + 1 + ". " + companies.get(i).getName());
        }
        System.out.println("0. Back");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 0) return;
        Company selectedCompany = companies.get(choice - 1);
        List<Car> cars = carDAO.findAvailableCarsByCompanyId(selectedCompany.getId());
        if (cars.isEmpty()) {
            System.out.printf("No available cars in the '%s' company", selectedCompany.getName());
            return;
        }
        System.out.println("Choose a car:");
        for (int i = 0; i < cars.size(); i++) {
            System.out.println(i + 1 + ". " + cars.get(i).getName());
        }
        System.out.println("0. Back");

        int carChoice = Integer.parseInt(scanner.nextLine());
        if (carChoice == 0) return;

        Car selectedCar = cars.get(carChoice - 1);
        customer.setRentedCarId(selectedCar.getId());
        customerDAO.rentCar(customer.getId(), selectedCar.getId());
        System.out.printf("You rented '%s'\n", selectedCar.getName());
    }
}
