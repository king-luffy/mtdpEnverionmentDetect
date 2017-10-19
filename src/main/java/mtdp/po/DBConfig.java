package mtdp.po;

/**
 * Created by king_luffy on 2017/8/2.
 */
public class DBConfig {

    private static final String URL_FROMAT = "jdbc:jtds:sqlserver://%s:%s;DatabaseName=%s";

    private String dbIp = "";
    private String dbName = "";
    private String userName = "";
    private String userPwd = "";
    private String url ="";
    private String port = "";

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    public static String formateUrl(DBConfig config){
        return String.format(URL_FROMAT, config.getDbIp(), config.getPort(), config.getDbName());
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DBConfig{");
        sb.append("dbIp='").append(dbIp).append('\'');
        sb.append(", dbName='").append(dbName).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", userPwd='").append(userPwd).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
