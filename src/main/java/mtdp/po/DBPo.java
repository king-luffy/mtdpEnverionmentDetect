package mtdp.po;

import java.io.Serializable;

/**
 * Created by king_luffy on 2017/9/6.
 */
public class DBPo implements Serializable{
    String posId;
    String version;
    String dbName;
    String dbId;

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }
}
