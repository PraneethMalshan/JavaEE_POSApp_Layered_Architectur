package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDAOImpl implements ItemDAO {



    @Override
    public boolean save(Item obj) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO item VALUES(?,?,?,?)";
        return CrudUtil.execute(sql,obj.getCode(),obj.getDescription(),obj.getQtyOnHand(),obj.getUnitPrice());
    }

    @Override
    public ResultSet getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(Item obj) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }
}
