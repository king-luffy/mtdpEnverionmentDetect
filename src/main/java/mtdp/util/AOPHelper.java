package mtdp.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by king_luffy on 2017/10/10.
 */
public class AOPHelper {
    private ApplicationContext ctx;
    private AOPHelper(){
        ctx = new ClassPathXmlApplicationContext("aop.xml");
    }

    private static AOPHelper INSTANCE;

    public static AOPHelper getInstance(){
        if(INSTANCE==null){
            INSTANCE = new AOPHelper();
        }
        return INSTANCE;
    }

    public <T> T getBean(Class<T> c,String aopName){
        return (T)ctx.getBean(aopName);
    }
}
