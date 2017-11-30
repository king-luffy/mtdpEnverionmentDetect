package mtdp;

import mtdp.dao.DBSessionMapper;
import mtdp.util.SqlSessionHelper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * Created by king_luffy on 2017/11/3.
 */
public class DBSessionTest {

    @Test
    public void sessionTest(){
        SqlSession sqlSession=null;
        try {
            //SqlSessionHelper.openSqlSession()
            sqlSession = SqlSessionHelper.openSqlSession(false);

            DBSessionMapper sessionMapper = sqlSession.getMapper(DBSessionMapper.class);

            int result = 0;
            //合法操作
            result = sessionMapper.updateLegal();

            //事务提交
            sqlSession.commit();

            System.out.println("合法操作结果:"+result);
            //非法操作
            result = sessionMapper.undateIlligal();

            System.out.println("非法操作结果:"+result);

            //事务提交
            sqlSession.commit();


        }catch (Exception e){
            System.out.println(e);
            if(sqlSession!=null){
                sqlSession.rollback();
            }
        }finally {
            SqlSessionHelper.closeSqlSession();
        }



    }
}
