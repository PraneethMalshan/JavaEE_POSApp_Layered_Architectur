package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemDAOImpl implements ItemDAO {



    @Override
    public boolean save(Item obj) throws SQLException, ClassNotFoundException {
        String sql="INSERT INTO item VALUES(?,?,?,?)";
        return CrudUtil.execute(sql,obj.getCode(),obj.getDescription(),obj.getQtyOnHand(),obj.getUnitPrice());
    }

    @Override
    public ResultSet getAll() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM item");
        return resultSet;

    }

    @Override
    public boolean update(Item obj) throws SQLException, ClassNotFoundException {
        String sql="UPDATE item SET description=?,qtyOnHand=?,unitPrice=? WHERE code=?";
        return CrudUtil.execute(sql,obj.getDescription(),obj.getQtyOnHand(),obj.getUnitPrice(),obj.getCode());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateQty(double qty, String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE item SET qty=?, WHERE code=?", qty,id);
    }
}
