package lk.ijse.pos.servlet;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.CustomerBO;
import lk.ijse.pos.dto.CustomerDTo;
import lk.ijse.pos.util.ResponseUtil;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.*;
import javax.servlet.ServletConfig;
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
import java.util.List;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    private CustomerBO customerBO ;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        customerBO=(CustomerBO) BOFactory.getBoFactory().getBoType(BOFactory.BoType.CUSTOMER);

    }


    //Front end eken geennath puluwan krama
        //Query String = Front Endeken Data geenna data transfer karanna oona ekkenekta puluwan...
                            //Meeken geenna baha...Form Data(x=www-form-url-encoded) = Get ekenkochchara yawanna try kalath eken enne url ekak widiyata.. eyaa form data geniyanne naha.. geniyanne query string.. get method eka thiinne deewal ganna.. habayi eyaa data yawanna haduwoth data geniyanne query string  ekak widiyata...
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {  /*doGet nam enne doGet method eka call wenawa...*/

        try {

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

            List<CustomerDTo> allCustomer = customerBO.getAllCustomer();

            resp.addHeader("Content-Type", "application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");


            JsonArrayBuilder allCus = Json.createArrayBuilder();

       /* while (rst.next()) {

            JsonObjectBuilder customer = Json.createObjectBuilder();//Objectekak hadanawa
            customer.add("id",rst.getString("id"));
            customer.add("name",rst.getString("name"));
            customer.add("address",rst.getString("address"));
            customer.add("salary",rst.getDouble("salary"));
            allCustomers.add(customer.build());
        }*/

            for (CustomerDTo cus :allCustomer){
                JsonObjectBuilder customerBuilder = Json.createObjectBuilder();
                customerBuilder.add("id",cus.getId());
                customerBuilder.add("name",cus.getName());
                customerBuilder.add("address",cus.getAddress());
                customerBuilder.add("salary",cus.getSalary());
                allCus.add(customerBuilder.build());
            }

            resp.setContentType("application/json");
            resp.getWriter().print(ResponseUtil.genJson("Success","Loaded",allCus.build()));


        } catch (ClassNotFoundException e ){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));
        } catch (SQLException e){
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));
        }

    }


    //Front end eken geennath puluwan krama
        //Query String
        //Form Data(x=www-form-url-encoded)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");


        try {

            String id=req.getParameter("id");
            String name=req.getParameter("name");
            String address=req.getParameter("address");
            double salary= Double.parseDouble(req.getParameter("salary"));

            if (customerBO.saveCustomer(new CustomerDTo(id,name,address,salary))){
                resp.getWriter().print(ResponseUtil.genJson("Success","Successfully Added"));

            }else {
                resp.getWriter().print(ResponseUtil.genJson("error","Customer Added Fail"));
            }

        } catch (SQLException | ClassNotFoundException e){

            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error",e.getMessage()));

        }
   }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String id = req.getParameter("id");

        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {

            if (customerBO.deleteCustomer(id)) {
               /* JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Deleted..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());*/
                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Deleted..!"));

            }else {
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Delete Failed..!"));
            }

        }catch (ClassNotFoundException e) {
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

//            JsonObjectBuilder rjo = Json.createObjectBuilder();
//            rjo.add("state","Error");
//            rjo.add("message",e.getLocalizedMessage());
//            rjo.add("data","");
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().print(rjo.build());

        } catch (SQLException e ){
//            JsonObjectBuilder rjo = Json.createObjectBuilder();
//            rjo.add("state","Error");
//            rjo.add("message",e.getLocalizedMessage());
//            rjo.add("data","");
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().print(rjo.build());
            resp.setStatus(500);
            resp.getWriter().print(ResponseUtil.genJson("Error", e.getMessage()));

        }


    }

//UPDATE===============================
    //Query String
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customerObject = reader.readObject();

        String id = customerObject.getString("id");
        String name = customerObject.getString("name");
        String address = customerObject.getString("address");
        double salary = Double.parseDouble(customerObject.getString("salary"));
        CustomerDTo customerDTO = new CustomerDTo(id, name, address, salary);

        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {

            if (customerBO.updateCustomer(customerDTO)) {
             /*   JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state","Ok");
                rjo.add("message","Successfully Updated..!");
                rjo.add("data","");
                resp.getWriter().print(rjo.build());*/
                resp.getWriter().print(ResponseUtil.genJson("Success", "Customer Updated..!"));

            }else {
                resp.getWriter().print(ResponseUtil.genJson("Failed", "Customer Updated Failed..!"));

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
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Headers","Content-type");
    }


}
