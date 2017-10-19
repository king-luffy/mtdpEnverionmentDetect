package mtdp.po;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by king_luffy on 2017/8/2.
 */
public class DBTablePo implements Serializable {
    private String dbId;
    private String tableId;
    private String tableName;
    private Integer tableRows;
    private Map<String,DBTableFieldPo> tableFieldPoMap = new HashMap<>();//Key : TableFieldName, Value : DBTableFieldPo

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getTableRows() {
        return tableRows;
    }

    public void setTableRows(Integer tableRows) {
        this.tableRows = tableRows;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Map<String, DBTableFieldPo> getTableFieldPoMap() {
        return tableFieldPoMap;
    }

    public void setTableFieldPoMap(Map<String, DBTableFieldPo> tableFieldPoMap) {
        this.tableFieldPoMap = tableFieldPoMap;
    }
}
