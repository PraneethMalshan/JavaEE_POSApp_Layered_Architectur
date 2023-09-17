package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.PurchaseOrderBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.dao.custom.OrderDAO;
import lk.ijse.pos.dao.custom.PurchaseOrderDAO;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.PurchaseOrderDTO;
import lk.ijse.pos.entity.Order;
import lk.ijse.pos.entity.PurchaseOrder;

import java.sql.SQLException;
import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDo(DAOFactory.DoType.ITEM);
    OrderDAO orderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDo(DAOFactory.DoType.ORDER);
    PurchaseOrderDAO detailDAO = (PurchaseOrderDAO) DAOFactory.getDaoFactory().getDo(DAOFactory.DoType.PURCHASEORDER);

    @Override
    public boolean placeOder(OrderDTO ordDTO, List<PurchaseOrderDTO> purchaseOrderDTOS) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getDbConnection().getConnection().setAutoCommit(false);

            boolean isSave = orderDAO.save(new Order(ordDTO.getOid(),ordDTO.getDate(),ordDTO.getCustomerID()));
            if (isSave){
                for (PurchaseOrderDTO odt : purchaseOrderDTOS){
                    boolean isSaveOD = detailDAO.save(new PurchaseOrder(odt.getOrderID(),odt.getItemCode(),odt.getOrderQty(),odt.getPrice()));
                    if (isSaveOD){
                        boolean updateQty = itemDAO.updateQty(odt.getOrderQty()-odt.getBuyQty(), odt.getItemCode());
                        if (updateQty){
                            DBConnection.getDbConnection().getConnection().commit();
                            return true;
                        }
                    }
                }
            }
            DBConnection.getDbConnection().getConnection().rollback();
            return false;
        }finally {
            DBConnection.getDbConnection().getConnection().setAutoCommit(true);
        }
    }
}
