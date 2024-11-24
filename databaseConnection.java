import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection{
    private static final String uName = "root";
    private static final String passWord = "306599";
    private static final String url = "jdbc:mysql://localhost:3306/fitnesszone";
    public static Connection getConnection()throws Exception{
        return DriverManager.getConnection(url,uName,passWord);
    }
}