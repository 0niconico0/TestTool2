package orgin;
import java.io.IOException;
import java.io.RandomAccessFile;  
  
/** 
 * 
 * @author malik 
 * @version 2011-3-10 下午10:49:41 
 */  
public class file {  
      
    public  static void createFile(){
//    	 try{  
//             File file = new File("addfile.txt");  
//             if(file.createNewFile()){  
//                 System.out.println("Create file successed");  
//             }  
           
    }
    public static void method3(String fileName, String content) {   
        RandomAccessFile randomFile = null;  
        try {     
            // 打开一个随机访问文件流，按读写方式     
            randomFile = new RandomAccessFile(fileName, "rw");     
            // 文件长度，字节数     
            long fileLength = randomFile.length();     
            // 将写文件指针移到文件尾。     
            randomFile.seek(fileLength);     
            randomFile.writeBytes(content);      
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally{  
            if(randomFile != null){  
                try {  
                    randomFile.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }    

}  