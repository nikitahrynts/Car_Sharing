package carsharing.dao;

import carsharing.entity.Company;

import java.util.List;

public interface CompanyDAO {

    List<Company> findAllCompanies();

    void add(String name);

    Company findCompanyById(int companyId);
}
