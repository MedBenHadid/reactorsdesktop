package SharedResources.Utils.Connector;

import SharedResources.URLServer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConnectionUtil {

    private static Connection conn;
    private static Statement statement;
    private static ConnectionUtil instance;
    private final static Logger logger = Logger.getLogger(ConnectionUtil.class.getName());

    public static boolean isIsConnected() {
        return isConnected.get();
    }

    public static BooleanProperty isConnectedProperty() {
        return isConnected;
    }

    private final static BooleanProperty isConnected = new SimpleBooleanProperty(false);
    public Connection getConn() {
        return conn;
    }

    private ConnectionUtil() {
        try {
            conn = DriverManager.getConnection(URLServer.DATABASE_DRIVER + URLServer.DATABASE_HOST + URLServer.DATABASE_PORT + URLServer.DATABASE_NAME + URLServer.DATABASE_UTC_SUPPORT, URLServer.DATABASE_USERNAME, URLServer.DATABASE_PASSWORD);
            statement = conn.createStatement();
        } catch (SQLException e) {
            logger.log(Level.INFO, "Error whilst connecting to database : Retrying in 20 : REASON :", e.getSQLState());
        }
    }

    /**
     * @return MysqlConnect Database connection object
     */
    public static synchronized ConnectionUtil getInstance(){
        if ( instance == null ) {
            instance = new ConnectionUtil();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(20), ev -> {
                if(conn==null||statement==null){
                    instance= new ConnectionUtil();
                    System.out.println("Attempting to reconnect");
                }else {
                    try {
                        ResultSet resultSet = conn.prepareStatement("SELECT 1").executeQuery();
                        resultSet.next();
                        isConnected.setValue(true);
                    } catch (SQLException ex) {
                        isConnected.setValue(false);
                        logger.log(Level.INFO, "Connection closed", ex.getSQLState());
                    }
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        return instance;
    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException when you WABALABADUBDUB BITCH
     */
    public ResultSet query(String query) throws SQLException{
        return statement.executeQuery(query);
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException when you WABALABADUBDUB BITCH
     */
    public int insert(String insertQuery) throws SQLException {
        return statement.executeUpdate(insertQuery);
    }
}
