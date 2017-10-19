package mtdp.service;

import com.alibaba.fastjson.JSON;
import mtdp.ex.DBCompareException;
import mtdp.po.DBTableFieldPo;
import mtdp.po.DBTablePo;
import mtdp.po.PosPo;
import mtdp.po.dbcompare.DBTableFieldPoCmp;
import mtdp.po.dbcompare.DBTablePoCmp;
import mtdp.po.dbcompare.PosPoCmp;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by king_luffy on 2017/7/25.
 */
public class DBComparer {
    private static final Logger logger = Logger.getLogger(DBComparer.class);

    private PosPoCmp cmp = null;

    public PosPoCmp dbCompare(PosPo basePos,PosPo cmpPos) throws DBCompareException {

        //Parameter Validation
        if(basePos==null || cmpPos==null){
            throw new DBCompareException("Input Pos Info is null!");
        }

        //Result init
        cmp = new PosPoCmp();

        cmp.setTitle(basePos.getPosName()+" and "+cmpPos.getPosName());

        Map<String,DBTablePo> baseDBTablePoMap = basePos.getDbTablePoMap();
        Map<String,DBTablePo> cmpDBTablePoMap = cmpPos.getDbTablePoMap();

        //Compare
        for (DBTablePo baseDBTablePo : baseDBTablePoMap.values()) {
            String baseDBTablePoName = baseDBTablePo.getTableName();

            if(cmpDBTablePoMap.containsKey(baseDBTablePoName)){

                //Match table name, and compare the fields
                cmpTable(baseDBTablePo,cmpDBTablePoMap.get(baseDBTablePoName));

            }else{

                //Table name not Match(base)
                setNotMatchBaseTable(baseDBTablePo);
            }
        }

        //Table name not Match(cmp)
        Map<String,DBTablePoCmp> dbTablePoCmpMap =cmp.getDbCmpTablePoCmpMap();
        for (DBTablePo tablePo :cmpDBTablePoMap.values()) {
            if(!dbTablePoCmpMap.containsKey(tablePo.getTableName())){
                setNotMatchCmpTable(tablePo);
            }
        }

        //calculate the match percent
        calculatePercent();

        return cmp;
    }

    private void calculatePercent(){
        cmp.setMatchPercentBase(calculatePercent(cmp.getDbBaseTablePoCmpMap()));
        cmp.setMatchPercentCmp(calculatePercent(cmp.getDbCmpTablePoCmpMap()));
    }
    private Double calculatePercent(Map<String, DBTablePoCmp> dbTablePoCmpMap){
        int size = 0;
        int matchSize = 0;
        for (DBTablePoCmp tableCmp : dbTablePoCmpMap.values()) {
            size+=tableCmp.getSize();
            matchSize+=tableCmp.getMatchSize();
        }
        Double percent = new Double(matchSize)/new Double(size);
        return percent;
    }

    private void setNotMatchCmpTable(DBTablePo cmpDBTablePo){
        setNotMatchTable(cmpDBTablePo,cmp.getDbCmpTablePoCmpMap());
    }

    private void setNotMatchBaseTable(DBTablePo baseDBTablePo){
        setNotMatchTable(baseDBTablePo,cmp.getDbBaseTablePoCmpMap());
    }

    private void setNotMatchTable(DBTablePo dbTablePo,Map<String, DBTablePoCmp> target){

        //Table级
        DBTablePoCmp dbTablePoCmp = setTableCmpResult2Pos(dbTablePo,target,false);

        //Field级
        Map<String,DBTableFieldPo> dbTableFieldPoMap = dbTablePo.getTableFieldPoMap();
        for (DBTableFieldPo fieldPo : dbTableFieldPoMap.values()) {
            setFieldCmpResult2Table(fieldPo,dbTablePoCmp.getDbTableFieldPoCmpMap(),false);
        }

        //统计信息
        dbTablePoCmp.setSize(dbTableFieldPoMap.size());
        dbTablePoCmp.setMatchSize(0);

    }

    private void cmpTable(DBTablePo baseDBTablePo,DBTablePo cmpDBTablePo){
        //Table级
        DBTablePoCmp baseTablePoCmp = setTableCmpResult2Pos(baseDBTablePo,cmp.getDbBaseTablePoCmpMap(),true);
        DBTablePoCmp cmpTablePoCmp = setTableCmpResult2Pos(cmpDBTablePo,cmp.getDbCmpTablePoCmpMap(),true);

        //统计信息初始化
        int matchSize = 0;

        //Field级
        Map<String,DBTableFieldPo> baseTableFieldPoMap = baseDBTablePo.getTableFieldPoMap();
        Map<String,DBTableFieldPo> cmpTableFieldPoMap = cmpDBTablePo.getTableFieldPoMap();
        Map<String,DBTableFieldPoCmp> baseTableFieldPoCmpMap = baseTablePoCmp.getDbTableFieldPoCmpMap();
        Map<String,DBTableFieldPoCmp> cmpTableFieldPoCmpMap = cmpTablePoCmp.getDbTableFieldPoCmpMap();
        for (Map.Entry<String, DBTableFieldPo> entry :
                baseTableFieldPoMap.entrySet()) {
            String cmpFieldName = entry.getKey();
            if(cmpTableFieldPoMap.containsKey(cmpFieldName)){

                //Base Table Field Match
                setFieldCmpResult2Table(baseTableFieldPoMap.get(cmpFieldName),baseTableFieldPoCmpMap,true);
                setFieldCmpResult2Table(cmpTableFieldPoMap.get(cmpFieldName),cmpTableFieldPoCmpMap,true);
                matchSize++;

            }else{

                //Base Table Field Not Match
                setFieldCmpResult2Table(baseTableFieldPoMap.get(cmpFieldName),baseTableFieldPoCmpMap,false);
            }
        }
        //Cmp Table Field not Match
        for (DBTableFieldPo cmpField :
                cmpTableFieldPoMap.values()) {
            String fieldName = cmpField.getFieldName();
            if(!cmpTableFieldPoCmpMap.containsKey(fieldName)){
                setFieldCmpResult2Table(cmpField,cmpTableFieldPoCmpMap,false);
            }
        }

        //统计信息
        baseTablePoCmp.setMatchSize(matchSize);
        baseTablePoCmp.setSize(baseTableFieldPoMap.size());
        cmpTablePoCmp.setMatchSize(matchSize);
        cmpTablePoCmp.setSize(cmpTableFieldPoMap.size());

    }

    private DBTablePoCmp setTableCmpResult2Pos(DBTablePo table,Map<String,DBTablePoCmp> target,boolean isMatch){
        DBTablePoCmp dbTablePoCmp = new DBTablePoCmp();
        dbTablePoCmp.setMatch(isMatch);
        dbTablePoCmp.setDbTablePo(table);
        //Insert into Result
        target.put(table.getTableName(),dbTablePoCmp);
        return dbTablePoCmp;
    }

    private void setFieldCmpResult2Table(DBTableFieldPo field,Map<String,DBTableFieldPoCmp> target,boolean isMatch){
        DBTableFieldPoCmp fieldPoCmp = new DBTableFieldPoCmp();
        fieldPoCmp.setMatch(isMatch);
        fieldPoCmp.setDbTableFieldPo(field);
        target.put(field.getFieldName(),fieldPoCmp);
    }

    public static void compareTwoDBs() throws DBCompareException {
        logger.info("Begin Database Compare :");

        //Fetch Database Info
        DBInfoFetch dbInfoFetch = new DBInfoFetch();
        String property1 = "jdbc.properties";
        String property2 ="jdbc2.properties";
        PosPo posPoBase = dbInfoFetch.fetchDBInfo(property1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PosPo posPoCmp = dbInfoFetch.fetchDBInfo(property2);

        if(posPoBase==null){
            logger.error("File "+property1 +" db info fetch fail!数据库信息获取失败！");
            return;
        }
        posPoBase.setPosName("Test1");
        posPoBase.setPosId("1");
        printInfo(posPoBase);

        if(posPoCmp==null){
            logger.error("File "+property2 +" db info fetch fail!数据库信息获取失败！");
            return;
        }
        posPoCmp.setPosName("Test2");
        posPoCmp.setPosId("2");
        printInfo(posPoCmp);

        //Compare Database
        DBComparer dbComparer = new DBComparer();
        PosPoCmp posPoCmpResult = dbComparer.dbCompare(posPoBase,posPoCmp);

        //printInfo(posPoCmpResult);

        //Print not Match Table and Field
        List<DBTablePoCmp> dbTablePoBaseList = new ArrayList<>(posPoCmpResult.getDbBaseTablePoCmpMap().values());
        List<DBTablePoCmp> dbTablePoCmpList = new ArrayList<>(posPoCmpResult.getDbCmpTablePoCmpMap().values());

        logger.info("Not Match Detail:");
        logger.info("DB base Table:");
        printDiffInfo(dbTablePoBaseList);
        logger.info("DB cmp Table:");
        printDiffInfo(dbTablePoCmpList);

        printInfo("原数据库匹配目标数据库百分比 ： "+new BigDecimal(posPoCmpResult.getMatchPercentBase()*100).setScale(2,BigDecimal.ROUND_HALF_UP)+" %");
        printInfo("目标数据库匹配原数据库百分比 ： "+new BigDecimal(posPoCmpResult.getMatchPercentCmp()*100).setScale(2,BigDecimal.ROUND_HALF_UP)+" %");

        logger.info("End DatabaseCompare Test");
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    compareTwoDBs();
                } catch (DBCompareException e) {
                    logger.error(e);
                }
            }
        }).start();
    }
    private static void printInfo(Object obj){
        logger.info(JSON.toJSONString(obj));
    }
    private static void printDiffInfo(List<DBTablePoCmp> dbTablePoList){
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (DBTablePoCmp tb :
                dbTablePoList) {
            if(tb.getMatchSize()<tb.getSize()){
                sb.append("Table(");
                sb.append(tb.getDbTablePo().getTableName());
                sb.append(") : [");
                for (DBTableFieldPoCmp fieldPoCmp :
                        tb.getDbTableFieldPoCmpMap().values()) {
                    if(!fieldPoCmp.getMatch()){
                        sb.append(fieldPoCmp.getDbTableFieldPo().getFieldName());
                        sb.append(",");
                    }
                }
                sb.delete(sb.length()-1,sb.length());
                sb.append("]\n");
            }
        }
        logger.info(sb.toString());
    }
}
