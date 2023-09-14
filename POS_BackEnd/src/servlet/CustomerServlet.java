package servlet;

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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    //Front end eken geennath puluwan krama
        //Query String = Front Endeken Data geenna data transfer karanna oona ekkenekta puluwan...
                            //Meeken geenna baha...Form Data(x=www-form-url-encoded) = Get ekenkochchara yawanna try kalath eken enne url ekak widiyata.. eyaa form data geniyanne naha.. geniyanne query string.. get method eka thiinne deewal ganna.. habayi eyaa data yawanna haduwoth data geniyanne query string  ekak widiyata...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  /*doGet nam enne doGet method eka call wenawa...*/

//        try (Connection connection = DBConnection.getDbConnection().getConnection()){  /* closabal resourse ekak try resourse ekak karanne closabal resourse eka gihilla dhaanawa try eka issarahayin warahan dhekak dhaala eeka aethulata. ethakota apita maenuwali close karanna dheyaknaee.*/
        try (Connection connection = ((BasicDataSource)getServletContext().getAttribute("dbcp")).getConnection()){  /* closabal resourse ekak try resourse ekak karanne closabal resourse eka gihilla dhaanawa try eka issarahayin warahan dhekak dhaala eeka aethulata. ethakota apita maenuwali close karanna dheyaknaee.*/


            //            BasicDataSource bds= new BasicDataSource();
//            bds.setDriverClassName("com.mysql.jdbc.Driver");
//            bds.setUrl("jdbc:mysql://localhost:3306/market");
//            bds.setPassword("1234");
//            bds.setUsername("root");
//            bds.setMaxTotal(2); //How many connection /*awashya connection pramaanaya*/ /*Mee pool ekata connection kiiyak daanna oonada. Ee dhaana eewagen kiiyak active mattamee thiinna oonada. */
//            bds.setInitialSize(2); //How many connection should be initialized from created connection  /*Daapu connection eken kiiyan active karala thiyanna oonada.*/
//
//            Connection connection = bds.getConnection();
            /*Mema database connect wena codes nawatha nawatha repeat wena nisaa ihatha code wenuwata wenamama db connection ekak hadala eekata daala eeka methanata aragena thiinawa..
              ethakota boyiler plate codes wena eka nathi wenawa. methana idan ema singleton class eken wada tika karagena yanna puluwan. */
            PreparedStatement pstm = connection.prepareStatement("select * from Customer");
            ResultSet rst = pstm.executeQuery();

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();

        while (rst.next()) {

            JsonObjectBuilder customer = Json.createObjectBuilder();//Objectekak hadanawa
            customer.add("id",rst.getString("id"));
            customer.add("name",rst.getString("name"));
            customer.add("address",rst.getString("address"));
            customer.add("salary",rst.getDouble("salary"));
            allCustomers.add(customer.build());
        }

        //Release the connection back to the pool - kansiyuma puul ekee thiina dheyak noomal puul ekata riliis karana methad eka
//        connection.close();  closabal resourse ekak try resourse ekak karanne closabal resourse eka gihilla dhaanawa try eka issarahayin warahan dhekak dhaala eeka aethulata. ethakota apita maenuwali close karanna dheyaknaee.

            JsonObjectBuilder job = Json.createObjectBuilder();  //Create Json Object
            job .add("state","Ok");  //state ekak add karanawa
            job.add("message","Successfully Loaded..!"); //Message eka add karanawa
            job.add("data",allCustomers.build());  //data kiyana ekata thamayi ee tika yanne..  eekata add wenna oona customersla tika innna json array eka

            resp.getWriter().print(job.build());


        } catch (SQLException e ){
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
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String salary = req.getParameter("salary");



//        try (Connection connection = DBConnection.getDbConnection().getConnection()){
        try (Connection connection = ((BasicDataSource)getServletContext().getAttribute("dbcp")).getConnection()){
            /*BasicDataSource bds= new BasicDataSource();
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
                PreparedStatement pstm = connection.prepareStatement("insert into Customer values(?,?,?,?)");
                pstm.setObject(1,id);
                pstm.setObject(2,name);
                pstm.setObject(3,address);
                pstm.setObject(4,salary);

                boolean b = pstm.executeUpdate() > 0;  //*Boolean value ekakata gaththa values tika execute kiriima*//*
                if (b){
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("state","Ok");
                    responseObject.add("message","Successfully Added..!");
                    responseObject.add("data","");
                    resp.getWriter().print(responseObject.build());
                    }
        }

        catch (SQLException e){
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("data","");
//            resp.setStatus(500);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(error.build());

        }
   }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

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
            PreparedStatement pstm = connection.prepareStatement("delete from Customer where id=?");
            pstm.setObject(1, id);
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

        }catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());

        } catch (SQLException e ){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());

        }


    }

//UPDATE===============================
    //Query String
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();
        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        String salary = customer.getString("salary");

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

            PreparedStatement pstm = connection.prepareStatement("update Customer set name=?, address=?, salary=? where id=?");
                pstm.setObject(4,id);
                pstm.setObject(1,name);
                pstm.setObject(2,address);
                pstm.setObject(3,salary);
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

        }catch (RuntimeException e) {
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(rjo.build());

        } catch ( SQLException e ){
            JsonObjectBuilder rjo = Json.createObjectBuilder();
            rjo.add("state","Error");
            rjo.add("message",e.getLocalizedMessage());
            rjo.add("data","");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(rjo.build());

        }

    }


}
