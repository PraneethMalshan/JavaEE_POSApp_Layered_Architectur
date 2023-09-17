package lk.ijse.pos.dao;

import lk.ijse.pos.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.pos.dao.custom.impl.ItemDAOImpl;
import lk.ijse.pos.dao.custom.impl.OrderDAOImpl;
import lk.ijse.pos.dao.custom.impl.PurchaseOrderDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory(){
    }

    public static DAOFactory getDaoFactory(){
        if (daoFactory == null){
            daoFactory = new DAOFactory();
        }
            return daoFactory;
    }

    public enum DoType{
        CUSTOMER,ITEM,ORDER,PURCHASEORDER
    }

    public SuperDAO getDo(DAOFactory.DoType doType){
        switch (doType){
            case CUSTOMER:
                return (SuperDAO) new CustomerDAOImpl();
            case ITEM:
                return (SuperDAO) new ItemDAOImpl();
            case ORDER:
                return (SuperDAO) new OrderDAOImpl();
            case PURCHASEORDER:
                return (SuperDAO) new PurchaseOrderDAOImpl();

        }
        return null;
    }

}
