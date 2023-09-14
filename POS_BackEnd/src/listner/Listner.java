package listner;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class Listner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();

        BasicDataSource bds= new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/market");
        bds.setPassword("1234");
        bds.setUsername("root");

        bds.setMaxTotal(2);
        bds.setInitialSize(2);


        servletContext.setAttribute("dbcp",bds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            ((BasicDataSource)servletContextEvent.getServletContext().getAttribute("dbcp")).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
