package carsharing.dao;

import carsharing.database.DBClient;
import carsharing.entity.Company;

import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS COMPANY
            (
                ID INT PRIMARY KEY AUTO_INCREMENT,
                NAME VARCHAR UNIQUE NOT NULL
            );
            """;

    private static final String SELECT_ALL = """
            SELECT * FROM COMPANY;
            """;

    private static final String INSERT_DATA = """
            INSERT INTO COMPANY (NAME) VALUES (?);
            """;

    private static final String SELECT_COMPANY_BY_ID = """
            SELECT ID, NAME FROM COMPANY WHERE ID = ?;
            """;

    private final DBClient dbClient;

    public CompanyDAOImpl(DBClient dbClient) {
        this.dbClient = dbClient;
        dbClient.run(CREATE_TABLE);
    }

    @Override
    public List<Company> findAllCompanies() {
        return dbClient.selectAllCompanies(SELECT_ALL);
    }

    @Override
    public void add(String name) {
        dbClient.addCompany(INSERT_DATA, name);
    }

    @Override
    public Company findCompanyById(int companyId) {
        return dbClient.selectCompanyById(SELECT_COMPANY_BY_ID, companyId);
    }
}
