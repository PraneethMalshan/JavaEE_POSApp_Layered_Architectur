package lk.ijse.pos.dao;

import java.sql.SQLException;

public interface CrudDAO<T> extends SuperDAO {
    public boolean save(T obj) throws SQLException, ClassNotFoundException;
}
