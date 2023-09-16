package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.dto.CustomerDTo;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBo {
    public boolean saveCustomer(CustomerDTo cusDto) throws SQLException, ClassNotFoundException;
    public List<CustomerDTo> getAllCustomer() throws SQLException, ClassNotFoundException;
    public boolean updateCustomer(CustomerDTo cusDTo ) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String id)throws SQLException, ClassNotFoundException;
}
