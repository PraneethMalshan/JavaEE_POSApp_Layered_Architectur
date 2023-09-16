package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.CustomerBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dto.CustomerDTo;
import lk.ijse.pos.entity.Customer;

import java.sql.SQLException;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDo(DAOFactory.DoType.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDTo cusDto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(cusDto.getId(),cusDto.getName(),cusDto.getAddress(),cusDto.getSalary()));

    }

}
