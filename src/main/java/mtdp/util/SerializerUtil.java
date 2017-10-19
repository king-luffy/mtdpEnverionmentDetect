package mtdp.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

import org.apache.log4j.Logger;

/**
 * Created by king_luffy on 2017/9/9.
 */
public class SerializerUtil {

    private static Logger logger = Logger.getLogger(SerializerUtil.class);

    /**
     * 把obj写入到本地path位置
     * @param obj
     * @param path
     * @return
     */
    public static boolean serialization(Object obj,String path){

        if(obj == null){
            logger.error("Serialization object is null");
            return false;
        }

        if(!(obj instanceof Serializable)){
            logger.error("Object is not Serializable");
            return false;
        }

        if(StringUtils.isEmpty(path)){
            logger.error("Serialization path is null");
            return false;
        }

        ObjectOutputStream o = null;
        try
        {
            o = new ObjectOutputStream( new FileOutputStream(path));
            o.writeObject(obj);   //写入数据
        }catch(Exception e) {
            logger.error("Serialization fail : ",e);
        }finally {
            if(o!=null){
                try {
                    o.close();
                } catch (IOException e) {
                    logger.error("OutputSream close error :",e);
                }
            }
        }

        return true;
    }

    /**
     * 从本地path位置读取对象到内存
     * @param path
     * @param <T>
     * @return
     */
    public static <T> T deserialization(String path){
        File file = new File(path);

        if(file==null || !file.exists()){
            logger.error("File "+path+" not exist!");
            return null;
        }
        ObjectInputStream in = null;
        try
        {
            in =new ObjectInputStream( new FileInputStream(path));
            T obj = (T)in.readObject(); //读取数据

            return obj;
        }catch(Exception e) {
            logger.error("Read serialization object fail!");
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("Close input stream fail!");
                }
            }
        }

        return null;
    }


}
