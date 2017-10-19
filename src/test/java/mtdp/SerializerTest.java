package mtdp;

import mtdp.po.DBPo;
import mtdp.po.DBTablePo;
import mtdp.util.JsonUtil;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by king_luffy on 2017/9/10.
 */
public class SerializerTest {

    @Test
    public void serializeTest(){
        DBPo dbPo = new DBPo();
        dbPo.setDbId("001");
        dbPo.setDbName("ttt");
        System.out.println(JsonUtil.getJsonString(dbPo,true));

        DBTablePo dbTablePo = new DBTablePo();
        dbTablePo.setDbId("1001");
        dbTablePo.setTableName("tableName");


        try
        {
            ObjectOutputStream o = new ObjectOutputStream( new FileOutputStream("logInfo2.out"));
            o.writeObject(dbTablePo);   //写入数据
            o.close();
        }catch(Exception e) {
            e.printStackTrace();
        }

        try
        {
            ObjectInputStream in =new ObjectInputStream( new FileInputStream("logInfo.out"));
            DBPo dbPo2 = (DBPo)in.readObject();   //读取数据
            System.out.println(JsonUtil.getJsonString(dbPo2,true));
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
