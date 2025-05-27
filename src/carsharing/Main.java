package carsharing;

import carsharing.dao.*;
import carsharing.database.DBClient;
import carsharing.database.Database;
import carsharing.service.MenuService;

import java.util.Scanner;

public class Main {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    public static void main(String[] args) {
        String url = getName(args);
        Database database = new Database(url, JDBC_DRIVER);
        DBClient dbClient = new DBClient(database);
        CompanyDAO companyDAO = new CompanyDAOImpl(dbClient);
        CarDAO carDAO = new CarDAOImpl(dbClient);
        CustomerDAO customerDAO = new CustomerDAOImpl(dbClient);

        Scanner scanner = new Scanner(System.in);
        MenuService menuService = new MenuService(scanner, companyDAO, carDAO, customerDAO);
        menuService.start();
    }

    private static String getName(String[] args) {
        String dbName = (args.length > 0) ? args[1] : "carsharing";
        return DB_URL + dbName;
    }
}