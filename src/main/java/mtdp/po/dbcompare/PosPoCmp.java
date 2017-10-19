package mtdp.po.dbcompare;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by king_luffy on 2017/9/10.
 */
public class PosPoCmp implements Serializable {

    public PosPoCmp(){
        setTitle("");
        setMatchPercentBase(0.0);
        setDbBaseTablePoCmpMap(new HashMap<String, DBTablePoCmp>());
        setDbCmpTablePoCmpMap(new HashMap<String, DBTablePoCmp>());
    }

    private String title;
    private Double matchPercentBase;
    private Double matchPercentCmp;
    private Map<String,DBTablePoCmp> dbBaseTablePoCmpMap;//key : tableName
    private Map<String,DBTablePoCmp> dbCmpTablePoCmpMap;//key : tableName

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getMatchPercentBase() {
        return matchPercentBase;
    }

    public void setMatchPercentBase(Double matchPercentBase) {
        this.matchPercentBase = matchPercentBase;
    }

    public Double getMatchPercentCmp() {
        return matchPercentCmp;
    }

    public void setMatchPercentCmp(Double matchPercentCmp) {
        this.matchPercentCmp = matchPercentCmp;
    }

    public Map<String, DBTablePoCmp> getDbBaseTablePoCmpMap() {
        return dbBaseTablePoCmpMap;
    }

    public void setDbBaseTablePoCmpMap(Map<String, DBTablePoCmp> dbBaseTablePoCmpMap) {
        this.dbBaseTablePoCmpMap = dbBaseTablePoCmpMap;
    }

    public Map<String, DBTablePoCmp> getDbCmpTablePoCmpMap() {
        return dbCmpTablePoCmpMap;
    }

    public void setDbCmpTablePoCmpMap(Map<String, DBTablePoCmp> dbCmpTablePoCmpMap) {
        this.dbCmpTablePoCmpMap = dbCmpTablePoCmpMap;
    }
}
