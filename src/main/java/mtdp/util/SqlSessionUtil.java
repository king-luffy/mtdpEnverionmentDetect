package mtdp.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by king_luffy on 2017/9/15.
 */
public class SqlSessionUtil {
    private static Logger LOG = Logger.getLogger(SqlSessionUtil.class);
    private static ThreadLocal<SqlSession> sqlSessionThreadLocal = new ThreadLocal();
    private static final String DRIVER_JTDS = "net.sourceforge.jtds.jdbc.Driver";
    private static final String DRIVER_ODBC = "sun.jdbc.odbc.JdbcOdbcDriver";

    public SqlSessionUtil() {
    }

    public static SqlSession getSqlSession(boolean autoCommit) {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession == null) {

            SqlSessionFactory sqlSessionFactory = null;
            try {
                sqlSessionFactory = SqlSessionFactoryHelper.loadProperties();
            } catch (Exception e) {
                LOG.error(e);
            }
            sqlSession = openSession(sqlSessionFactory,autoCommit);
        }

        return sqlSession;
    }
    public static SqlSession getSqlSession(boolean autoCommit,String propertyName) {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession == null) {
            SqlSessionFactory sqlSessionFactory = null;
            try {
                sqlSessionFactory = SqlSessionFactoryHelper.loadProperties(propertyName);
            } catch (Exception e) {
                LOG.error(e);
            }
            sqlSession = openSession(sqlSessionFactory,autoCommit);
        }

        return sqlSession;
    }

    public static SqlSession getSqlSession(boolean autoCommit,Properties properties) {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession == null) {
            SqlSessionFactory sqlSessionFactory = null;
            try {
                sqlSessionFactory = SqlSessionFactoryHelper.loadProperties(properties);
            } catch (Exception e) {
                LOG.error(e);
            }
            sqlSession = openSession(sqlSessionFactory,autoCommit);
        }

        return sqlSession;
    }

    private static SqlSession openSession(SqlSessionFactory sqlSessionFactory,boolean autoCommit){
        if(sqlSessionFactory == null) {
            return null;
        }
        try {
            Class.forName(DRIVER_JTDS);
            Class.forName(DRIVER_ODBC);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession(autoCommit);
        boolean isConnected = testGetConnection(sqlSession);
        if(!isConnected) {
            return null;
        }

        sqlSessionThreadLocal.set(sqlSession);

        return sqlSession;
    }

    private static boolean testGetConnection(SqlSession sqlSession) {
        Connection connection = null;

        try {
            connection = sqlSession.getConnection();
            if(connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (Exception var3) {
            LOG.error("direct connect test sql error", var3);
        }

        return false;
    }

    public static void closeSqlSession() {
        SqlSession sqlSession = (SqlSession)sqlSessionThreadLocal.get();
        if(sqlSession != null) {
            try {
                sqlSessionThreadLocal.remove();
            } catch (Exception var5) {
                LOG.error("closeSqlSession error", var5);
            } finally {
                sqlSession.close();
            }
        }

    }

    private static class SqlSessionFactoryHelper {
        static SqlSessionFactory sqlSessionFactory;

        private SqlSessionFactoryHelper() {
        }

        public static SqlSessionFactory loadProperties() throws Exception {
            Properties properties = getProperties(null);
            return (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsReader("mybatis-config.xml"), properties);
        }
        public static SqlSessionFactory loadProperties(String bundleName) throws Exception {
            Properties properties = getProperties(bundleName);
            return (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsReader("mybatis-config.xml"), properties);
        }
        public static SqlSessionFactory loadProperties(Properties properties) throws IOException {
            properties.setProperty("db.url", properties.getProperty("url"));
            properties.setProperty("db.username", properties.getProperty("username"));
            properties.setProperty("db.password", properties.getProperty("password"));
            properties.setProperty("db.driver", "jdbc:odbc:MTDP".equals(properties.getProperty("url"))?"sun.jdbc.odbc.JdbcOdbcDriver":"net.sourceforge.jtds.jdbc.Driver");
            return (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsReader("mybatis-config.xml"), properties);
        }

        private static Properties getProperties(String propertyName){
            if(StringUtils.isEmpty(propertyName))
                propertyName = "jdbc";
            ResourceBundle bundle = ResourceBundle.getBundle(propertyName);
            String dbUrl = System.getProperty("PluginDBURL", bundle.getString("url")).trim();
            String dbUserName = System.getProperty("PluginDBUser", bundle.getString("username")).trim();
            String dbPassword = System.getProperty("PluginDBPWD", bundle.getString("password")).trim();
            Properties properties = new Properties();
            properties.setProperty("db.url", dbUrl);
            properties.setProperty("db.username", dbUserName);
            properties.setProperty("db.password", dbPassword);
            properties.setProperty("db.driver", "jdbc:odbc:MTDP".equals(dbUrl)?"sun.jdbc.odbc.JdbcOdbcDriver":"net.sourceforge.jtds.jdbc.Driver");
            return properties;
        }

    }
}
