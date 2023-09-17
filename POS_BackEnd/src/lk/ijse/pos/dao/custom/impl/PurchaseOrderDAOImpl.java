package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.PurchaseOrderDAO;
import lk.ijse.pos.entity.PurchaseOrder;
import lk.ijse.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseOrderDAOImpl implements PurchaseOrderDAO {

    @Override
    public boolean save(PurchaseOrder obj) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orderdetails VALUES (?,?,?,?)",obj.getOrderID(),obj.getItemCode(),obj.getOrderQty(),obj.getPrice());
    }

    @Override
    public ResultSet getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(PurchaseOrder obj) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }
}
