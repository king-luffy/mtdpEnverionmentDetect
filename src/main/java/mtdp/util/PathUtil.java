package mtdp.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by king_luffy on 2017/9/20.
 */
public class PathUtil {

    private static Logger logger = Logger.getLogger(PathUtil.class);

    /**
     * 获取源文件根路径下资源
     * @param bundleName
     * @return
     */
    public static ResourceBundle getBundle(String bundleName){
        ResourceBundle bundle= ResourceBundle.getBundle(bundleName);
        return bundle;
    }

    /**
     * 获取项目根目录下Properties文件
     * @param propertyName
     * @return
     */
    public static Properties getProperties(String propertyName){
        File file = new File(propertyName);
        if(!file.exists()) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
        return properties;
    }

    /**
     * 获取项目根目录路径
     * @return
     */
    public static String getRootPath(){
        return System.getProperty("user.dir");
    }

    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 复制文件，通过FileInputStream
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFlie(FileInputStream source,File dest)throws IOException{
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = source.getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * 复制文件
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(InputStream source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = source;
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    /**
     * 获取jar包内的文件
     * @param c
     * @param filePath
     * @return
     */
    public static InputStream getJarFile(Class c,String filePath){
        InputStream is = c.getResourceAsStream(filePath);
        return is;
    }

    /**
     * 获取class根目录
     * @param c
     * @return
     */
    public static String getClassFolderPath(Class c){
        return c.getResource("/").getPath();
    }
    /**
     * 获取class根目录下资源文件的路径
     * @param c
     * @param srcFileName
     * @return
     */
    public static String getClassFolderPath(Class c,String srcFileName){
        return c.getResource("/"+srcFileName).getPath();
    }
    public static void main(String[] args) throws IOException {
//        File file = new File("test.properties");
//        logger.info(file.exists());
//
//        Properties properties = new Properties();
//        properties.load(new FileInputStream(file));
//
//        logger.info(properties.getProperty("url"));
        String tmp = getClassFolderPath(PathUtil.class);
        logger.info(tmp);
    }
}
