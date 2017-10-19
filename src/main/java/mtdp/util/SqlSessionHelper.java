package mtdp.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by king_luffy on 2017/9/20.
 */
public class SqlSessionHelper {
    private static Logger LOGGER = Logger.getLogger(SqlSessionHelper.class);
    public static final int DEFAULT_DB_LOGIN_TIMEOUT_SEC = 3;
    private static final String JDBC_CONFIG = "jdbc";
    private static final String MYBATIS_CONFIG = "mybatis-config.xml";
    private static int DB_LOGIN_TIMEOUT_SEC = 3;
    private static ThreadLocal<SqlSession> sqlSessionThreadLocal = new ThreadLocal();

    public <T> T getMapper(Class<T> type) {
        SqlSession sqlSession = openSqlSession(false);
        return sqlSession == null?null:sqlSession.getMapper(type);
    }

    public static SqlSession openSqlSession(boolean autoCommit) {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession == null) {
            SqlSessionFactory sqlSessionFactory = FactorySingleton.INSTANCE.getSqlSessionFactory();
            if(sqlSessionFactory == null) {
                return null;
            }

            sqlSession = sqlSessionFactory.openSession(autoCommit);
            boolean isConnected = testGetConnection(sqlSession);
            if(!isConnected) {
                return null;
            }

            sqlSessionThreadLocal.set(sqlSession);
        }

        return sqlSession;
    }
    public static SqlSession openSqlSession(boolean autoCommit,String property){
        return openSqlSession(autoCommit,property,null);
    }
    public static SqlSession openSqlSession(boolean autoCommit,Properties properties){
        return openSqlSession(autoCommit,null,properties);
    }
    public static SqlSession openSqlSession(boolean autoCommit,String property,Properties properties) {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession == null) {
            SqlSessionFactory sqlSessionFactory = null;
            if(property!=null){
                sqlSessionFactory = FactorySingleton.INSTANCE.getSqlSessionFactory(property);
            }else if(properties!=null){
                sqlSessionFactory = FactorySingleton.INSTANCE.getSqlSessionFactory(properties);
            }else{
                sqlSessionFactory = FactorySingleton.INSTANCE.getSqlSessionFactory();
            }

            if(sqlSessionFactory == null) {
                return null;
            }

            sqlSession = sqlSessionFactory.openSession(autoCommit);
            boolean isConnected = testGetConnection(sqlSession);
            if(!isConnected) {
                return null;
            }

            sqlSessionThreadLocal.set(sqlSession);
        }

        return sqlSession;
    }

    public static SqlSession openSqlSession(boolean autoCommit, int dbLoginTimeoutSec) {
        DB_LOGIN_TIMEOUT_SEC = dbLoginTimeoutSec;
        return openSqlSession(autoCommit);
    }

    private static boolean testGetConnection(SqlSession sqlSession) {
        Connection connection = null;

        try {
            connection = sqlSession.getConnection();
            if(connection != null) {
                return true;
            }
        } catch (Exception var3) {
            LOGGER.error("test sql error", var3);
        }

        return false;
    }

    public static void closeSqlSession() {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession != null) {
            sqlSession.close();
            sqlSessionThreadLocal.remove();
        }

    }

    private SqlSessionHelper() {
    }

    public static final SqlSessionHelper getInstance() {
        return SqlSessionHelperHolder.INSTANCE;
    }

    private static class SqlSessionHelperHolder {
        private static final SqlSessionHelper INSTANCE = new SqlSessionHelper();

        private SqlSessionHelperHolder() {
        }
    }

    private static enum FactorySingleton {
        INSTANCE;

        private SqlSessionFactory sqlSessionFactory;


        public SqlSessionFactory getSqlSessionFactory() {
            setSqlSessionFactory("jdbc");
            return this.sqlSessionFactory;
        }
        public SqlSessionFactory getSqlSessionFactory(String property) {
            setSqlSessionFactory(property);
            return this.sqlSessionFactory;
        }
        public SqlSessionFactory getSqlSessionFactory(Properties properties) {
            try {
                this.sqlSessionFactory = (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsReader("mybatis-config.xml"), properties);
                if(DriverManager.getLoginTimeout() <= 0) {
                    DriverManager.setLoginTimeout(SqlSessionHelper.DB_LOGIN_TIMEOUT_SEC);
                }
            } catch (Exception var9) {
                LOGGER.error("init SqlSessionFactory error", var9);
            }
            return this.sqlSessionFactory;
        }
        private void setSqlSessionFactory(String property){
            try {
                Properties propertiesIn = PathUtil.getProperties(property);

                String driverName = propertiesIn.getProperty("driver").trim();
                String url = propertiesIn.getProperty("url").trim();
                String userName = propertiesIn.getProperty("username").trim();
                String password = propertiesIn.getProperty("password").trim();
                Properties properties = new Properties();
                properties.setProperty("db.driver", driverName);
                properties.setProperty("db.url", url);
                properties.setProperty("db.username", userName);
                properties.setProperty("db.password", password);
                this.sqlSessionFactory = (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsReader("mybatis-config.xml"), properties);
                if(DriverManager.getLoginTimeout() <= 0) {
                    DriverManager.setLoginTimeout(SqlSessionHelper.DB_LOGIN_TIMEOUT_SEC);
                }
            } catch (Exception var9) {
                LOGGER.error("init SqlSessionFactory error", var9);
            }
        }
    }
}
