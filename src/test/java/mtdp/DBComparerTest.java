package mtdp;

import com.alibaba.fastjson.JSON;
import mtdp.ex.DBCompareException;
import mtdp.po.PosPo;
import mtdp.po.dbcompare.DBTableFieldPoCmp;
import mtdp.po.dbcompare.DBTablePoCmp;
import mtdp.po.dbcompare.PosPoCmp;
import mtdp.service.DBComparer;
import mtdp.service.DBInfoFetch;
import mtdp.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by king_luffy on 2017/9/6.
 */
public class DBComparerTest {

    private static final Logger logger = Logger.getLogger(DBInfoFetch.class);

    @Test
    public void dbmybatisTest() throws DBCompareException {

        logger.info("Begin Database Compare :");

        //Fetch Database Info
        DBInfoFetch dbInfoFetch = new DBInfoFetch();
        PosPo posPoBase = dbInfoFetch.fetchDBInfo("jdbc");
        PosPo posPoCmp = dbInfoFetch.fetchDBInfo("jdbc2");
        posPoBase.setPosName("Test1");
        posPoBase.setPosId("1");
        posPoCmp.setPosName("Test2");
        posPoCmp.setPosId("2");

//        printInfo(posPoBase);
//        printInfo(posPoCmp);

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

    private void printDiffInfo(List<DBTablePoCmp> dbTablePoList){
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


    private void printInfo(Object obj){
        System.out.println(JSON.toJSONString(obj,true));
    }

    @Test
    public void connectionTest(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
        try {
            if (sqlSession == null) {
                System.out.println("error");
            } else {
                System.out.println("success");
            }
        }finally {
            SqlSessionUtil.closeSqlSession();
        }
    }

    @Test
    public void connectionTest2(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true,"jdbc2");
        try {
            if (sqlSession == null) {
                System.out.println("error");
            } else {
                System.out.println("success");
            }
        }finally {
            SqlSessionUtil.closeSqlSession();
        }
    }

    @Test
    public void connectionTest3(){
        DBInfoFetch dbInfoFetch = new DBInfoFetch();
        PosPo posPoCmp = dbInfoFetch.fetchDBInfo("jdbc2");
        printInfo(posPoCmp);
    }
}
