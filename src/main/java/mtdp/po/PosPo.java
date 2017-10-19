package mtdp.po;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by king_luffy on 2017/9/6.
 */
public class PosPo implements Serializable{
    private String posId;
    private String posName;
    private Map<String,DBTablePo> dbTablePoMap = new HashMap<>();//Key : TableName, Value : DBTablePo

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Map<String, DBTablePo> getDbTablePoMap() {
        return dbTablePoMap;
    }

    public void setDbTablePoMap(Map<String, DBTablePo> dbTablePoMap) {
        this.dbTablePoMap = dbTablePoMap;
    }
}
