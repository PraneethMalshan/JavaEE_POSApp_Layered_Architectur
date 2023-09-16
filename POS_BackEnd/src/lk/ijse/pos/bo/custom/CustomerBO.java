package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.dto.CustomerDTo;

import java.sql.SQLException;

public interface CustomerBO extends SuperBo {
    public boolean saveCustomer(CustomerDTo cusDto) throws SQLException, ClassNotFoundException;

}
