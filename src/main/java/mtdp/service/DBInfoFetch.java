package mtdp.service;

import com.alibaba.fastjson.JSON;
import mtdp.dao.DBInfoFetchMapper;
import mtdp.po.DBTableFieldPo;
import mtdp.po.DBTablePo;
import mtdp.po.PosPo;
import mtdp.util.SqlSessionHelper;
import mtdp.util.SqlSessionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by king_luffy on 2017/9/10.
 */
public class DBInfoFetch {
    private static final Logger logger = Logger.getLogger(DBInfoFetch.class);

    public static final String ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";
    public static final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";


    public static final SqlSession getSession(boolean autoCommit){
        loadJDBCDriver();
        SqlSession sqlSession = SqlSessionHelper.openSqlSession(autoCommit);
        if(sqlSession==null){
            logger.error("can not connect db");
            return null;
        }
        return sqlSession;
    }

    public static final SqlSession getSession(boolean autoCommit,String propertyFile){
        loadJDBCDriver();
        SqlSession sqlSession = SqlSessionHelper.openSqlSession(autoCommit,propertyFile);
        if(sqlSession==null){
            logger.error("can not connect db");
            return null;
        }
        return sqlSession;
    }
    public static final SqlSession getSession(boolean autoCommit, Properties properties){
        loadJDBCDriver();
        SqlSession sqlSession = SqlSessionHelper.openSqlSession(autoCommit,properties);
        if(sqlSession==null){
            logger.error("can not connect db");
            return null;
        }
        return sqlSession;
    }
    private static final void loadJDBCDriver(){
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        }
    }

    public List<DBTablePo> fetchDBTables(DBInfoFetchMapper dbInfoFetchMapper){

        List<DBTablePo> result = null;
        try{
            result = dbInfoFetchMapper.getDBTableInfo();
        }catch (Exception e){
            logger.error(e);
        }

        return result;
    }

    public List<DBTableFieldPo> fetchDBTableFields(DBInfoFetchMapper dbInfoFetchMapper, String tableName){

        List<DBTableFieldPo> result = null;
        try{
            result = dbInfoFetchMapper.getDBTableFieldsInfo(tableName);
        }catch (Exception e){
            logger.error(e);
        }

        return result;
    }

    /**
     * 获取一个db的Table及Table下所有字段的信息
     * @param propertyFileName
     * @return
     */
    public PosPo fetchDBInfo(String propertyFileName){
        SqlSession sqlSession = DBInfoFetch.getSession(true,propertyFileName);
        return fetchDBInfo(sqlSession);
    }

    public PosPo fetchDBInfo(Properties properties){
        SqlSession sqlSession = DBInfoFetch.getSession(true,properties);
        return fetchDBInfo(sqlSession);
    }

    private PosPo fetchDBInfo(SqlSession sqlSession){
        if(sqlSession==null){
            System.out.println("error connect db !!");
            return null;
        }

        //Result Initial
        PosPo posPo = new PosPo();
        Map<String,DBTablePo> dbTablePoMap = posPo.getDbTablePoMap();

        try {
            DBInfoFetch dbInfoFetcher = new DBInfoFetch();
            DBInfoFetchMapper dbInfoFetchMapper = sqlSession.getMapper(DBInfoFetchMapper.class);

            //Table Level : Table Info Fetch
            List<DBTablePo> dbTablePos = dbInfoFetcher.fetchDBTables(dbInfoFetchMapper);

            if(CollectionUtils.isEmpty(dbTablePos)){
                return null;
            }

            //Field Level : Field Info Fetch
            for (DBTablePo dbTablePo:dbTablePos) {

                //Insert Table Info into PosPo
                dbTablePoMap.put(dbTablePo.getTableName(),dbTablePo);

                //Fetch and Insert Field Info into TablePo
                List<DBTableFieldPo> dbTableFieldPos =
                        dbInfoFetcher.fetchDBTableFields(dbInfoFetchMapper,dbTablePo.getTableName());
                Map<String,DBTableFieldPo> tableFieldPoMap = dbTablePo.getTableFieldPoMap();
                for (DBTableFieldPo fieldPo :
                        dbTableFieldPos) {
                    tableFieldPoMap.put(fieldPo.getFieldName(), fieldPo);
                }

            }

        }finally {
            SqlSessionHelper.closeSqlSession();
        }

        return posPo;
    }
    private void printInfo(Object obj){
        System.out.println(JSON.toJSONString(obj,true));
    }
}
