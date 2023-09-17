package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.dto.ItemDTO;

import java.sql.SQLException;
import java.util.List;

public interface ItemBO extends SuperBo {
    boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException;
    List<ItemDTO> getAllItem()throws SQLException, ClassNotFoundException;
    boolean updateItem(ItemDTO itemDTO)throws SQLException, ClassNotFoundException;
}
