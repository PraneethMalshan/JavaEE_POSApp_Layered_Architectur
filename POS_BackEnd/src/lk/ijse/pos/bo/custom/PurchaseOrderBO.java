package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBo;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.PurchaseOrderDTO;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseOrderBO  {
    public boolean placeOder(OrderDTO orderDTO, List<PurchaseOrderDTO> purchaseOrderDTOS ) throws SQLException, ClassNotFoundException;
}
