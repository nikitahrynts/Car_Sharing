package carsharing.dao;

import carsharing.database.DBClient;
import carsharing.entity.Car;

import java.util.List;

public class CarDAOImpl implements CarDAO {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS CAR
            (
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR UNIQUE NOT NULL,
                COMPANY_ID INT NOT NULL,
                CONSTRAINT FK_ID FOREIGN KEY (COMPANY_ID)
                REFERENCES COMPANY (ID)
            );
            """;

    private static final String SELECT_CARS = """
            SELECT * FROM CAR
            WHERE COMPANY_ID = ?
            ORDER BY ID;
            """;

    private static final String INSERT_CAR = """
            INSERT INTO CAR (NAME, COMPANY_ID)
            VALUES (?, ?);
            """;

    private static final String SELECT_CAR_BY_ID = """
            SELECT ID, NAME, COMPANY_ID FROM CAR WHERE ID = ?;
            """;

    private static final String SELECT_AVAILABLE_CARS_BY_COMPANY_ID = """
            SELECT * FROM CAR
            WHERE COMPANY_ID = ?
            AND ID NOT IN (
                SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL
            )
            ORDER BY ID;
            """;

    private final DBClient dbClient;

    public CarDAOImpl(DBClient dbClient) {
        this.dbClient = dbClient;
        dbClient.run(CREATE_TABLE);
    }

    @Override
    public List<Car> findAllCarsByCompanyId(int companyId) {
        return dbClient.selectAllCars(SELECT_CARS, companyId);
    }

    @Override
    public void addNewCar(String name, int companyId) {
        dbClient.addCar(INSERT_CAR, name, companyId);
    }

    @Override
    public Car findCarById(int carId) {
        return dbClient.selectCarById(SELECT_CAR_BY_ID, carId);
    }

    @Override
    public List<Car> findAvailableCarsByCompanyId(int companyId) {
        return dbClient.selectAvailableCarsByCompanyId(SELECT_AVAILABLE_CARS_BY_COMPANY_ID, companyId);
    }
}
