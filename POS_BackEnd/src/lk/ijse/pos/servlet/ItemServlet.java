package lk.ijse.pos.servlet;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.ItemBO;
import lk.ijse.pos.dto.ItemDTO;
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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    private ItemBO itemBO;
    @Override
    public void init(){
        itemBO = (ItemBO) BOFactory.getBoFactory().getBoType(BOFactory.BoType.ITEM);

    }


    //Front end eken geennath puluwan krama
        //Query String = Front Endeken Data geenna data transfer karanna oona ekkenekta puluwan...
                            //Meeken geenna baha...Form Data(x=www-form-url-encoded) = Get ekenkochchara yawanna try kalath eken enne url ekak widiyata.. eyaa form data geniyanne naha.. geniyanne query string.. get method eka thiinne deewal ganna.. habayi eyaa data yawanna haduwoth data geniyanne query string  ekak widiyata...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  /*doGet nam enne doGet method eka call wenawa...*/

//        try (Connection connection = DBConnection.getDbConnection().getConnection()){  /* closabal resourse ekak try resourse ekak karanne closabal resourse eka gihilla dhaanawa try eka issarahayin warahan dhekak dhaala eeka aethulata. ethakota apita maenuwali close karanna dheyaknaee.*/
        try (Connection connection = ((BasicDataSource)getServletContext().getAttribute("dbcp")).getConnection()){  /* closabal resourse ekak try resourse ekak karanne closabal resourse eka gihilla dhaanawa try eka issarahayin warahan dhekak dhaala eeka aethulata. ethakota apita maenuwali close karanna dheyaknaee.*/
            //How to configure DBCP Pool
//            BasicDataSource bds= new BasicDataSource();
//            bds.setDriverClassName("com.mysql.jdbc.Driver");
//            bds.setUrl("jdbc:mysql://localhost:3306/market");
//            bds.setPassword("1234");
//            bds.setUsername("root");
//            bds.setMaxTotal(2); //How many connection /*awashya connection pramaanaya*/ /*Mee pool ekata connection kiiyak daanna oonada. Ee dhaana eewagen kiiyak active mattamee thiinna oonada. */
//            bds.setInitialSize(2); //How many connection should be initialized from created connection  /*Daapu connection eken kiiyan active karala thiyanna oonada.*/

           /* Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/market", "root", "1234");
           */
//            Connection connection = bds.getConnection();
/*
            Connection connection = DBConnection.getDbConnection().getConnection();
*/
            PreparedStatement pstm = connection.prepareStatement("select * from Item");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allItems = Json.createArrayBuilder();

        while (rst.next()) {

            JsonObjectBuilder item = Json.createObjectBuilder();//Objectekak hadanawa
            item.add("code",rst.getString("code"));
            item.add("description",rst.getString("description"));
            item.add("qtyOnHand",rst.getString("qtyOnHand"));
            item.add("unitPrice",rst.getDouble("unitPrice"));
            allItems.add(item.build());
        }

            //Release the connection back to the pool
//            connection.close();  /* closabal resourse ekak try resourse ekak karanne closabal resourse eka gihilla dhaanawa try eka issarahayin warahan dhekak dhaala eeka aethulata. ethakota apita maenuwali close karanna dheyaknaee.*/

            JsonObjectBuilder job = Json.createObjectBuilder();  //Create Json Object
            job .add("state","Ok");  //state ekak add karanawa
            job.add("message","Successfully Loaded..!"); //Message eka add karanawa
            job.add("data",allItems.build());  //data kiyana ekata thamayi ee tika yanne..  eekata add wenna oona customersla tika innna json array eka

            resp.getWriter().print(job.build());


        } catch ( SQLException e ){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());

        }

    }


    //Front end eken geennath puluwan krama
        //Query String
        //Form Data(x=www-form-url-encoded)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* String code = req.getParameter("code");
        String description = req.getParameter("description");
        String qtyOnHand = req.getParameter("qtyOnHand");
        String unitPrice = req.getParameter("unitPrice");*/

        resp.setContentType("application/json");

        resp.addHeader("Content-Type","application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

//        try (Connection connection = DBConnection.getDbConnection().getConnection()){
        try {
            String code = req.getParameter("code");
            String description = req.getParameter("description");
            String qtyOnHand = req.getParameter("qtyOnHand");
            double unitPrice = Double.parseDouble(req.getParameter("unitPrice"));
        /*    BasicDataSource bds= new BasicDataSource();
            bds.setDriverClassName("com.mysql.jdbc.Driver");
            bds.setUrl("jdbc:mysql://localhost:3306/market");
            bds.setPassword("1234");
            bds.setUsername("root");

            bds.setMaxTotal(2);
            bds.setInitialSize(2);

            Connection connection = bds.getConnection();*/

/*
            Connection connection = DBConnection.getDbConnection().getConnection();
*/
//            PreparedStatement pstm = connection.prepareStatement("insert into Item values(?,?,?,?)");
//                pstm.setObject(1,code);
//                pstm.setObject(2,description);
//                pstm.setObject(3,qtyOnHand);
//                pstm.setObject(4,unitPrice);
//
//                boolean b = pstm.executeUpdate() > 0;  //*Boolean value ekakata gaththa values tika execute kiriima*//*

                if (itemBO.saveItem(new ItemDTO(code,description,qtyOnHand,unitPrice))){
                   /* JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("state","Ok");
                    responseObject.add("message","Successfully Added..!");
                    responseObject.add("data","");
                    resp.getWriter().print(responseObject.build());*/
                    resp.getWriter().print(ResponseUtil.genJson("Success", "Successfully Added..!"));

                    }

        }catch (ClassNotFoundException e){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        } catch (SQLException e){
            /*JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());*/
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        }
   }

    //Front end eken geennath puluwan krama
        //Query String
        //JSON = meeken backEnd eken front end ekata data geniyannath front End eken back end ekata data geniyannath puluwan...Form data gaawata iilagata data geniyanna puluwan leesima kramaya meeka
                //Form data widiyata ganne naha...
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");

//        try (Connection connection = DBConnection.getDbConnection().getConnection()){
        try (Connection connection = ((BasicDataSource)getServletContext().getAttribute("dbcp")).getConnection()){
//            BasicDataSource bds= new BasicDataSource();
//            bds.setDriverClassName("com.mysql.jdbc.Driver");
//            bds.setUrl("jdbc:mysql://localhost:3306/market");
//            bds.setPassword("1234");
//            bds.setUsername("root");
//            bds.setMaxTotal(2); //How many connection /*awashya connection pramaanaya*/ /*Mee pool ekata connection kiiyak daanna oonada. Ee dhaana eewagen kiiyak active mattamee thiinna oonada. */
//            bds.setInitialSize(2); //How many connection should be initialized from created connection  /*Daapu connection eken kiiyan active karala thiyanna oonada.*/
//
//            Connection connection = bds.getConnection();
/*
            Connection connection = DBConnection.getDbConnection().getConnection();
*/

            PreparedStatement pstm = connection.prepareStatement("delete from Item where code=?");
            pstm.setObject(1, code);
            boolean b = pstm.executeUpdate() > 0;
            if (b) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Deleted..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());

            }else {
                throw new RuntimeException("There is no such customer for that ID..!");
            }

        }catch (SQLException | RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());

        }


    }

//UPDATE===============================
    //Query String
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String code = customer.getString("code");
        String description = customer.getString("description");
        String qtyOnHand = customer.getString("qtyOnHand");
        String unitPrice = customer.getString("unitPrice");

//        try (Connection connection = DBConnection.getDbConnection().getConnection()){
        try (Connection connection = ((BasicDataSource)getServletContext().getAttribute("dbcp")).getConnection()){
//            BasicDataSource bds= new BasicDataSource();
//            bds.setDriverClassName("com.mysql.jdbc.Driver");
//            bds.setUrl("jdbc:mysql://localhost:3306/market");
//            bds.setPassword("1234");
//            bds.setUsername("root");
//            bds.setMaxTotal(2); //How many connection /*awashya connection pramaanaya*/ /*Mee pool ekata connection kiiyak daanna oonada. Ee dhaana eewagen kiiyak active mattamee thiinna oonada. */
//            bds.setInitialSize(2); //How many connection should be initialized from created connection  /*Daapu connection eken kiiyan active karala thiyanna oonada.*/
//
//            Connection connection = bds.getConnection();
/*
            Connection connection = DBConnection.getDbConnection().getConnection();
*/

            PreparedStatement pstm = connection.prepareStatement("update Item set description=?, qtyOnHand=?, unitPrice=? where code=?");
                pstm.setObject(4,code);
                pstm.setObject(1,description);
                pstm.setObject(2,qtyOnHand);
                pstm.setObject(3,unitPrice);
                boolean b = pstm.executeUpdate() > 0;
            if (b) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Updated..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());

            }else {
                throw new RuntimeException("Wrong ID, Please check the ID");

            }

        }catch (SQLException | RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());

        }
    }
}
