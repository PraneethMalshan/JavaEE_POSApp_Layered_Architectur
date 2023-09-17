package lk.ijse.pos.servlet;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.PurchaseOrderBO;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.PurchaseOrderDTO;
import lk.ijse.pos.util.ResponseUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/purchase")
public class PurchaseOrderServlet extends HttpServlet {

    private PurchaseOrderBO purchaseOrderBO ;
    public void init() throws ServletException {
        super.init();
        purchaseOrderBO = (PurchaseOrderBO) BOFactory.getBoFactory().getBoType(BOFactory.BoType.PURCHASEORDER);
    }


    @Override
//    Json formate eken yana data ekak back end ekenread kara ganna nam aniwaaryen  Json processing hari kawuru hari Json ekka wada karanna puluwan ekkenek inna oona.
//Java EE wala Json processing api eka thamayi ee wadeeta inne..  Ethakota api karanne , doPost ekata Json request eka aawata passe datath ekka  eeka enne --> HttpServletRequest "req" methanata...

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try (JsonReader reader = Json.createReader(req.getReader())){
            JsonObject orderJsonOb = reader.readObject();


            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");


            String oid=orderJsonOb.getString("oid");
            String date =orderJsonOb.getString("date");
            String cusID =orderJsonOb.getString("cusID");

            JsonArray orderDetails = orderJsonOb.getJsonArray("orderDetails");

            OrderDTO orderDTO = new OrderDTO(oid,date,cusID);

            List<PurchaseOrderDTO> purchaseOrderDTOS = new ArrayList<>();
            for (JsonValue orderDetail : orderDetails){
                JsonObject obObject = orderDetail.asJsonObject();
                String itemCode = obObject.getString("code");
                String qty = obObject.getString("qty");
                String avQty = obObject.getString("avQty");
                String unitPrice = obObject.getString("price");

                double price = Double.parseDouble(unitPrice);
                double buyQty = Double.parseDouble(avQty);
                int qtyOnHand = Integer.parseInt((qty));

                purchaseOrderDTOS.add(new PurchaseOrderDTO(oid,itemCode,qtyOnHand,price,buyQty));

            }

            boolean placeOrder = purchaseOrderBO.placeOder(orderDTO,purchaseOrderDTOS);

                if (placeOrder) {
                   resp.getWriter().print(ResponseUtil.genJson("success", "Successful placeOrder"));

                  } else {
                    resp.getWriter().print(ResponseUtil.genJson("fail", "Failed placeOrder"));


            }


        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        } catch (SQLException e){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
    }


}

