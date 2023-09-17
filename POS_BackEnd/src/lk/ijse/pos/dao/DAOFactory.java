package lk.ijse.pos.dao;

import lk.ijse.pos.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.pos.dao.custom.impl.ItemDAOImpl;

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
        CUSTOMER,ITEM
    }

    public SuperDAO getDo(DAOFactory.DoType doType){
        switch (doType){
            case CUSTOMER:
                return (SuperDAO) new CustomerDAOImpl();
            case ITEM:
                return (SuperDAO) new ItemDAOImpl();

        }
        return null;
    }

}
