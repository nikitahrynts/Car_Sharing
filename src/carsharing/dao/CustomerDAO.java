package carsharing.dao;

import carsharing.entity.Customer;

import java.util.List;

public interface CustomerDAO {

    void createCustomer(String name);

    List<Customer> findAllCustomers();

    Customer getCustomerById(int customerId);

    void rentCar(int customerId, int carId);

    void returnCar(int customerId);

    Integer getRentedCarId(int customerId);
}
