package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.CustomerBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dto.CustomerDTo;
import lk.ijse.pos.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDo(DAOFactory.DoType.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDTo cusDto) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(cusDto.getId(),cusDto.getName(),cusDto.getAddress(),cusDto.getSalary()));

    }

    @Override
    public List<CustomerDTo> getAllCustomer() throws SQLException, ClassNotFoundException {
        ResultSet rst = customerDAO.getAll();
        List<CustomerDTo> allCustomer = new ArrayList<>();
        while (rst.next()){
            CustomerDTo customer = new CustomerDTo();
            customer.setId(rst.getString("id"));
            customer.setName(rst.getString("name"));
            customer.setAddress(rst.getString("address"));
            customer.setSalary(rst.getDouble("salary"));
            allCustomer.add(customer);


        }
        rst.close();
        return allCustomer;

    }

    @Override
    public boolean updateCustomer(CustomerDTo cusDTo) throws SQLException, ClassNotFoundException {
        return customerDAO.update(new Customer(cusDTo.getId(),cusDTo.getName(),cusDTo.getAddress(),cusDTo.getSalary()));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id);
    }

}
