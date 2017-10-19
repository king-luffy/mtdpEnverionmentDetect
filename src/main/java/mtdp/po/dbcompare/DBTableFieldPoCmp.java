package mtdp.po.dbcompare;

import mtdp.po.DBTableFieldPo;

import java.io.Serializable;

/**
 * Created by king_luffy on 2017/9/10.
 */
public class DBTableFieldPoCmp implements Serializable {
    private DBTableFieldPo dbTableFieldPo;
    private Boolean isMatch;

    public Boolean getMatch() {
        return isMatch;
    }

    public void setMatch(Boolean match) {
        isMatch = match;
    }

    public DBTableFieldPo getDbTableFieldPo() {
        return dbTableFieldPo;
    }

    public void setDbTableFieldPo(DBTableFieldPo dbTableFieldPo) {
        this.dbTableFieldPo = dbTableFieldPo;
    }
}
