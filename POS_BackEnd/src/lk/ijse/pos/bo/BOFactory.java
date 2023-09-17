package lk.ijse.pos.bo;

import lk.ijse.pos.bo.custom.impl.CustomerBOImpl;
import lk.ijse.pos.bo.custom.impl.ItemBOImpl;
import lk.ijse.pos.bo.custom.impl.PurchaseOrderBOImpl;

public class BOFactory {
    private static BOFactory boFactory ;
    private BOFactory(){
    }

    public static BOFactory getBoFactory(){

        if (boFactory==null){
            boFactory=new BOFactory();
        }
            return boFactory;
    }

    public enum BoType{
        CUSTOMER,ITEM,PURCHASEORDER
    }
    public SuperBo getBoType(BOFactory.BoType boType ) {
        switch (boType) {
            case CUSTOMER:
                return (SuperBo) new CustomerBOImpl();
            case ITEM:
                return (SuperBo) new ItemBOImpl();
            case PURCHASEORDER:
                return (SuperBo) new PurchaseOrderBOImpl();

        }
        return null;
    }
}
