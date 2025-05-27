package carsharing.dao;

import carsharing.entity.Car;

import java.util.List;

public interface CarDAO {

    List<Car> findAllCarsByCompanyId(int companyId);

    void addNewCar(String name, int companyId);

    Car findCarById(int carId);

    List<Car> findAvailableCarsByCompanyId(int companyId);
}
