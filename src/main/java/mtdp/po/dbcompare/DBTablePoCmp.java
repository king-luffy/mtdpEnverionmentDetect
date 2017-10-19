package mtdp.po.dbcompare;

import mtdp.po.DBTablePo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by king_luffy on 2017/9/10.
 */
public class DBTablePoCmp implements Serializable {
    public DBTablePoCmp(){
        setMatch(false);
        setDbTableFieldPoCmpMap(new HashMap<String, DBTableFieldPoCmp>());
    }
    private Boolean isMatch;
    private DBTablePo dbTablePo;
    private int size;
    private int matchSize;
    private Map<String,DBTableFieldPoCmp> dbTableFieldPoCmpMap;

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public DBTablePo getDbTablePo() {
        return dbTablePo;
    }

    public void setDbTablePo(DBTablePo dbTablePo) {
        this.dbTablePo = dbTablePo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMatchSize() {
        return matchSize;
    }

    public void setMatchSize(int matchSize) {
        this.matchSize = matchSize;
    }

    public Map<String, DBTableFieldPoCmp> getDbTableFieldPoCmpMap() {
        return dbTableFieldPoCmpMap;
    }

    public void setDbTableFieldPoCmpMap(Map<String, DBTableFieldPoCmp> dbTableFieldPoCmpMap) {
        this.dbTableFieldPoCmpMap = dbTableFieldPoCmpMap;
    }
}
