package lk.ijse.pos.servlet;

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

@WebServlet(urlPatterns = "/purchase")
public class PurchaseOrderServlet extends HttpServlet {
    @Override
//    Json formate eken yana data ekak back end ekenread kara ganna nam aniwaaryen  Json processing hari kawuru hari Json ekka wada karanna puluwan ekkenek inna oona.
//Java EE wala Json processing api eka thamayi ee wadeeta inne..  Ethakota api karanne , doPost ekata Json request eka aawata passe datath ekka  eeka enne --> HttpServletRequest "req" methanata...

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());     //eeke thiinawa reader ekak.. Reader eken adala gannawa ee data..  Iita passe Json API eke thiina  .reader eka create karala "req" ge reader eka Json reader ekata denawa. Ethakota apita awasaanedi hamba wenawa "JsonReader" eka...
        //Ethakota JsonReader eken apita puluwan read karanna front end eken ewana eka.. Front end eken ewanne Objectekak.. Single Object ekak.. Ethakota ee object eka read karanna kiyala kiyanna oona..
        //Ethakota eekata kiyanne "readObject"... Ethakota array ekak nam front end eken ewala thiinne kelinma "readArray".. Pbject ekak hinda "readObject"...
        //Ethakota object eka readkarala eyaa denawa apita Json type ekee Object ekak.. ethakota jsonObject typeekee reference eken apita puluwan front end ekee thiina details tika illa ganna.
        JsonObject jsonObject = reader.readObject();
        String oid = jsonObject.getString("oid"); //Mulinma aragena thiinawa Json read karala diipu object eken String oid...
        String cusID = jsonObject.getString("cusID");
        String date = jsonObject.getString("date");  // iitapasse cusID saha date eka.. Iita passe mee thun denaa thamayi order table ekata add wenne. Ethakota meeka transaction ekak.. table 2kata yanna oona sampurna procedure eka  complete wenna.. 2n ekakata data gihilla baha. 2tama yanna oona.. 2tama add unoth witharayi sampurnayenma database ekata yannaoona...
        //2n ekaka hari issue ekak thiinawa nam siyaluma data tika roleback wenna oona ekakwath  add wenne nathuwa.

        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){  // Meeken karala thiinne dbcp object eka hadala listnner ekakin context ekata daapu method eka.. eeka connectionekakata aragena try resourse ekakata daala thiinawa... ee kiyanne meeka try catch eka iwara wenakota close wenawa.. apita close karanna awashyathaawyak naa..
            connection.setAutoCommit(false);  //connection eka aragena mulinma karala thiinne eeke thiinawa autoCommit kiyana fecture eka.. eekiyanne mokak hari executeUpdete ekak hari mokak hari deyak karapu gaman database ekee derectly gihipu gaman lees wena eka nawaththala thiinawa..

            PreparedStatement pstm = connection.prepareStatement("insert into Orders values(?,?,?)"); // Connection ekata preparedstatement ekak aragena order table ekata awashya karana data tika fead karagena thiinawa..
            pstm.setObject(1,oid);
            pstm.setObject(2,date);
            pstm.setObject(3,cusID);  // ee data tika thamayi mee..

            if (!(pstm.executeUpdate()>0)) {  //Iita passe balanawa eeka add wenawada kiyala.. Add wenawa nm prashnayak naa.. habai add wenne naththam, eekiyanne order table ekata data yawanna baa kiyala methanin kiyanawa nam,,,,,
                connection.rollback(); // rollback kalaa,,
                connection.setAutoCommit(true);  // Connection eka thibichcha widiyatama haduwa
                throw new RuntimeException("Order Issue");  // exseption ekak fire kalaa.. meeka oonama naa..
            } else {  //wadee hariyata unaa nam eeka enne methanin pallehaata.. ehema add unaa nm orderDetails tika one by one gannawa.. orderDetails enne order details kiyana key  ekakata bined welaa, iita passe array ekak widiyata thamai data tika enne..
                //mema data tika string ekak widiyata ganna baa.. ethana thiinna array ekak.. ee nisaa eekaganna wenne,,,
                JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");  //jsonObject.getJsonArray ekak widiyata.. ethakota eekata key eka 'orderDetails' dunna gaman eyaa apita denawa Json array eka.. JsonArray type ekee reference ekak denne.. Iita passe me reference ekee thiinawa ara front end eken ewaapu array eka..
                for (JsonValue orderDetail : orderDetails) { // Json array ekee thiina values one by one aragena thiinawa. ethakota reference ekata values kiyak enawada.. anna ee ena gaanata mee for eka run wenawa..
                    String code = orderDetail.asJsonObject().getString("code");  //meeke derectly String call karanna baha.. for ekee thoonne JasonValue type ekee ekak.. eeka issellama jsonObject ekakata convert kara ganna oona. ethakota eekata thiinawa method ekak "asJsonObject" kiyala.. (JasonValue eka IsonObject ekakata convert karanawa.)
                    String qty = orderDetail.asJsonObject().getString("qty");
                    String price = orderDetail.asJsonObject().getString("price");

                    PreparedStatement pstms = connection.prepareStatement("insert into OrderDetails values(?,?,?,?)");  //Iita passe uda gaththa record eka orderDetails kiyana table ekata add karnna oona.. Ara connection ekama use karala PreparedStatement ekak hadaa gaththa.. orderDetails wala query eka liyaa gaththa...4k thiinawa.. 4ma daa gaththa..
                    pstms.setObject(1,oid); // Ita passe eeka set kalaa..
                    pstms.setObject(2,code);
                    pstms.setObject(3,qty);
                    pstms.setObject(4,price);
                    if (!(pstms.executeUpdate()>0)) { //ee data 4ma set karala executeUpdate kalaa..
                        connection.rollback();  //data tiken eka data ekak hari add nowunoth okkoma tika roleBack karanawa..
                        connection.setAutoCommit(true);  //connection eka thibichcha widiyatama hadanawa..
                        throw new RuntimeException("Order Details Issue");  //exception ekak theow karanawa..

                    }
                }
                //Ee okkoma tika iwara nm mee for loop eken paninawa eliyata.
                connection.commit();  //meeken kelinma database ekata yanawa..
                connection.setAutoCommit(true);  //connection eka thibichcha widiyatama hadanawa..

                JsonObjectBuilder error = Json.createObjectBuilder();  // api front end ekata yawanna oona mokada unee kiyala.. eekata Json object ekak hadaa gaththa..
                error.add("state","Success");
                error.add("message", "Order Successfully Purchased..!");
                error.add("data","");
                resp.getWriter().print(error.build()); // response eka print karala damma..

            }


        } catch (SQLException | RuntimeException e) {  //Errors thiinawa nam mee deken handle wenawa..
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());

        }





    }

    /*   @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){
            String orderID = req.getParameter("oid");

            PreparedStatement pstm = connection.prepareStatement("select Orders.oid,Orders.date,Orders.customerID,OrderDetails.itemCode,OrderDetails.qty,OrderDetails.unitPrice from Orders inner join OrderDetails on Orders.oid = OrderDetails.oid where Orders.oid=?");
            pstm.setObject(1, orderID);

            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allOrders = Json.createArrayBuilder();
            while (rst.next()) {
                String oid = rst.getString(1);
                String date = rst.getString(2);
                String customerID = rst.getString(3);
                String itemCode = rst.getString(4);
                String qty = rst.getString(5);
                String unitPrice = rst.getString(6);

                JsonObjectBuilder orders = Json.createObjectBuilder();
                orders.add("oid", oid);
                orders.add("date", date);
                orders.add("customerID", customerID);
                orders.add("itemCode", itemCode);
                orders.add("qty", qty);
                orders.add("unitPrice", unitPrice);

                allOrders.add(orders.build());

            }
            resp.getWriter().print(ResponseUtil.genJson("Success", "Loaded", allOrders.build()));



        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String oid = jsonObject.getString("oid");
        String date = jsonObject.getString("date");
        String cusID = jsonObject.getString("cusID");


        try (Connection connection = ((BasicDataSource) getServletContext().getAttribute("dbcp")).getConnection()){

            connection.setAutoCommit(false);

            PreparedStatement pstm = connection.prepareStatement("insert into Orders values (?,?,?)");
            pstm.setObject(1,oid);
            pstm.setObject(2,date);
            pstm.setObject(3,cusID);
            if (!(pstm.executeUpdate()>0)){
                connection.rollback();
                connection.setAutoCommit(true);
                throw new RuntimeException("Order Issue");
            } else {
                JsonArray orderDetails = jsonObject.getJsonArray("orderDetails");
                for (JsonValue orderDetail : orderDetails){
                    String itemCode = orderDetail.asJsonObject().getString("code");
                    String qty = orderDetail.asJsonObject().getString("qty");
                    String avQty = orderDetail.asJsonObject().getString("avQty");
                    String price = orderDetail.asJsonObject().getString("price");

                    PreparedStatement pstms = connection.prepareStatement("insert into orderDetails values (?,?,?,?)");
                    pstms.setObject(1,oid);
                    pstms.setObject(2,itemCode);
                    pstms.setObject(3,qty);
                    pstms.setObject(4,price);
                    if (!(pstms.executeUpdate()>0)) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw new RuntimeException("Order Details Issue");
                    }





                    PreparedStatement pstm3 = connection.prepareStatement("update Item set qtyOnHand=? where code=?");
                    pstm3.setObject(2, itemCode);
                    int availableQty = Integer.parseInt(avQty);
                    int purchasingQty = Integer.parseInt(qty);
                    pstm3.setObject(1, (availableQty - purchasingQty));

                    if ((!(pstm3.executeUpdate() > 0))) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        throw  new SQLException("Item connot be updated");
                    }
                }






                }
                connection.commit();
                connection.setAutoCommit(true);
                resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added.!"));


            } catch (SQLException e) {
                resp.setStatus(500);
                resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }
    }*/
}

