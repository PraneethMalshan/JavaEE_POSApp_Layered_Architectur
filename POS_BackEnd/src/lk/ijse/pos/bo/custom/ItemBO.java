package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.dto.ItemDTO;

import java.sql.SQLException;

public interface ItemBO extends SuperBo {
    boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException;
}
