package carsharing.dao;

import carsharing.database.DBClient;
import carsharing.entity.Customer;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS CUSTOMER
            (
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR UNIQUE NOT NULL,
                RENTED_CAR_ID INT,
                CONSTRAINT R_C_ID FOREIGN KEY (RENTED_CAR_ID)
                REFERENCES CAR (ID)
            );
            """;

    private static final String SELECT_CUSTOMERS = """
            SELECT * FROM CUSTOMER;
            """;

    private static final String CREATE_CUSTOMER = """
            INSERT INTO CUSTOMER (NAME)
            VALUES (?);
            """;

    private static final String SELECT_CUSTOMER_BY_ID = """
            SELECT ID, NAME, RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?;
            """;

    private static final String RENT_CAR_QUERY = """
            UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?;
            """;

    private static final String RETURN_CAR_QUERY = """
            UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?;
            """;

    private static final String SELECT_RENTED_CAR_ID = """
            SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = ?;
            """;

    private final DBClient dbClient;

    public CustomerDAOImpl(DBClient dbClient) {
        this.dbClient = dbClient;
        dbClient.run(CREATE_TABLE);
    }

    @Override
    public void createCustomer(String name) {
        dbClient.createCustomer(CREATE_CUSTOMER, name);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return dbClient.selectAllCustomers(SELECT_CUSTOMERS);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return dbClient.selectCustomerById(SELECT_CUSTOMER_BY_ID, customerId);
    }

    @Override
    public void rentCar(int customerId, int carId) {
        dbClient.rentCar(RENT_CAR_QUERY, customerId, carId);
    }

    @Override
    public void returnCar(int customerId) {
        dbClient.returnCar(RETURN_CAR_QUERY, customerId);
    }

    @Override
    public Integer getRentedCarId(int customerId) {
        return dbClient.getRentCarId(SELECT_RENTED_CAR_ID, customerId);
    }
}
