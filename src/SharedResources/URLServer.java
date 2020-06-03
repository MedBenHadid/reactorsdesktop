package SharedResources;

public class URLServer {

    public final static String url = "http://localhost/reactors/web/uploads/";

    public final static String userImage = url+"user/images/";

    public final static String ftpPassword = "3PoKloiAz&";
    public final static String ftpUser = "javaReactorsFx";
    public final static String ftpServerLink = "localhost";
    public final static int ftpSocketPort = 21;

    // FTP Upload directories {used for uploading}
    public final static String userImageDir = "user/images/";
    public final static String associationImageDir = "association/images/";
    public final static String associationPieceDir = "association/pieces/";
    //
    public final static String missionImageDir = "mission/images/";


    public final static String DATABASE_DRIVER = "jdbc:mysql:";
    public final static String DATABASE_HOST = "//localhost";
    public final static String DATABASE_NAME = "/reactorsdb";
    public final static String DATABASE_UTC_SUPPORT = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public final static String DATABASE_USERNAME = "root";
    public final static String DATABASE_PORT = ":3306";
    public final static String DATABASE_PASSWORD = "";
    public final static String DATABASE_CJ_DRIVER = "com.mysql.cj.jdbc.Driver";

}
