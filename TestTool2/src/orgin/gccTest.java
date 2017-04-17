package orgin;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class gccTest  extends Thread {
    public static String  filePath = "";
	public static String  fileName = "";
	public static String  fileNameWithOutType = "";
	public static  Map<String,String> basicMap = new HashMap<String,String>();  
	public static  Map<String,String> newMap = new HashMap<String,String>(); 
	public static  boolean flag = false;
	public static int IterationCount = 0;
	public static int IterationMax = 1000;
	public static int remainCoverRate = 100;
	public static int coverCriteria = 0;
	public static boolean mutipleIteration = false;
	public static int lastCoverage = 100;
	public static String readLine(String filename) throws IOException{
		 fileName = filename;
		
         filePath =  "/Users/niconico/Documents" + filename;
         fileNameWithOutType = fileName.substring(0,fileName.indexOf("."));
         fileName = fileNameWithOutType +"_test.c";
         fileNameWithOutType = fileNameWithOutType + "_test";
         
 		System.out.println("*************");
 		System.out.println(filePath);
 		System.out.println(fileName);
 		System.out.println(fileNameWithOutType);
 		System.out.println("*************");
 		
 		
		 File f = new File(filePath);
		
		 if(f.exists()){ 
		 StringBuffer sb = new StringBuffer("");  
		  
         FileReader reader = new FileReader(filePath);  
         BufferedReader br = new BufferedReader(reader);  

         String str = null;  

         while ((str = br.readLine()) != null)  
         {  
             sb.append(str + "\n");  
         }    
         br.close();  
         reader.close();  
         return sb.toString();
		}
		else{
		    throw new Error("输入的文件不存在");	 
		}
	}

    public void writeFile(String fileContent) throws IOException{ 
       if(IterationCount < IterationMax){
	    	
	         IterationCount ++;
	   }
	   if(IterationCount > IterationMax){
	    	 gccTest.flag = true;
	   }	
      FileWriter writer = new FileWriter(fileName);  
      BufferedWriter bw = new BufferedWriter(writer);  

      bw.write(fileContent);
      bw.close();  
      writer.close();  
    }
    
    public static void deleteFile() throws IOException{
    	File f = new File(fileName);
    	File ftype = new File(fileNameWithOutType);
    	File gcno = new File(fileNameWithOutType + ".gcno");
    	File gcda =  new File(fileNameWithOutType + ".gcda");
    	File gcov =  new File(fileNameWithOutType + ".c.gcov");
    	if(f.exists()){
    	   f.delete();
    	   ftype.delete();
    	   gcno.delete();
    	   gcda.delete();
    	   gcov.delete();
    	}
    }
	public static void generateGcno() throws IOException{
		String str[] = new String[]{"gcc",fileName,"-o",fileNameWithOutType,"-ftest-coverage","-fprofile-arcs"};
		Runtime.getRuntime().exec(str);
	}
	
	public static void generateGCDA() throws IOException{
		
		 Runtime.getRuntime().exec("./" + fileNameWithOutType);
	}
	private static String analyzeGcov(int proportion) throws IOException {
		 String missSentence = "没覆盖的路径:";
		 int suitFunction;
		 int initialBasicMapLength = 0;
		 Map<String, String> tempoMap;
		 if(basicMap.size() == 0){
			 tempoMap = basicMap;
		 }
		 else{
			 tempoMap = newMap;
		 }

		 File f = new File(filePath);
		 int allcodeLength = 0;
		 if(f.exists()){ 
		 StringBuffer sb = new StringBuffer("");  
		  
         FileReader reader = new FileReader(fileNameWithOutType + ".c.gcov");  
         BufferedReader br = new BufferedReader(reader);  

         String str = null;  

         while ((str = br.readLine()) != null)  
         {  
             sb.append(str + "\n");  
         }
         
         String[] programLength = sb.toString().split("\n");
         //去除 前5行 gcov 文件产生的前缀
		 allcodeLength =  programLength.length - 5;
         System.out.println(sb.toString());
         String pattern = "#####:(\\s+)(\\d+)";
		  Pattern function = Pattern.compile(pattern);
		  Matcher functionMatcher = function.matcher(sb.toString());
		  System.out.println("－－－－－－－－－－－－－－－－－－－－－");
		  while(functionMatcher.find()){
			   tempoMap.put(functionMatcher.group(2), "");
			   System.out.println(functionMatcher.group(2));
		  }
		  System.out.println("－－－－－－－－－－－－－－－－－－－－－");
		  
	
         br.close();  
         reader.close();
         
         initialBasicMapLength = basicMap.size();
         //处理新覆盖的单元
         java.util.Iterator<String> iter = basicMap.keySet().iterator();
         while(iter.hasNext()){    
             String key;    
             String value;    
             key = iter.next().toString();    
             value = basicMap.get(key);   
             if(newMap.get(key) == null && newMap.size() >0){
            	 iter.remove();
             }else{
            	 missSentence += key + " ";
             }
            }   
            newMap.clear();
		}
		 
		 //适应度函数
		 System.out.println("IterationCount:" + IterationCount);
		 
//		 if(IterationCount == 1 || (IterationCount == 0 && mutipleIteration)){
//			 cTest.initialDataEvalue[0] = proportion + ""; 
//			 cTest.successDataEvalue[0] = proportion + ""; 
//			 remainCoverRate = 100 - proportion;
//			 if(proportion > 0){
//				 cTest.successPara[cTest.successParaCount] =  cTest.initialData[IterationCount - 1];
//				 cTest.successParaCount ++;
//			 }
//		 }
		 if(IterationCount > 0){
			 suitFunction = 100 * basicMap.size()/allcodeLength;
			 System.out.println(basicMap.size());
			 System.out.println("总长度:" + allcodeLength);
			 cTest.suitFunction = suitFunction;		 
			 // -2 是因为 执行的时候 Iteration 加了1 然后去除第一次随机生成的时候 
			 cTest.initialDataEvalue[IterationCount - 1] = suitFunction + "";
			 if(suitFunction < lastCoverage){
				 remainCoverRate = suitFunction;	
				 cTest.successPara[cTest.successParaCount] = cTest.initialData[IterationCount - 1];
				 cTest.successDataEvalue[cTest.successParaCount] = suitFunction + "";
				 cTest.successParaCount ++;
			 }
			 lastCoverage = suitFunction; 
		 }
		 if(f.exists()){ 
			 if(basicMap.size() == 0 ){
		       	  System.out.println("完全路径覆盖");
		       	  lastCoverage = 100;
		       	  flag = true;
	          }
		 }
		 return missSentence;
	}
	
	public static int generateGcov() throws IOException{
		 Process p = Runtime.getRuntime().exec("gcov " + fileName);
		 String result = loadStream(p.getInputStream());
		 String zz[] = result.split("\n");
	     result = zz[1].substring(zz[1].indexOf(":")+1,zz[1].length());
	     result = result.substring(0,result.indexOf("."));
	     int proportion = Integer.parseInt(result);
	     
	     String missSentence = analyzeGcov(proportion);
	     deleteFile();
	     
	     //将测试结果写入面板右侧
	     System.out.println("***********************");
	     System.out.println(cTest.showRightPanelUnit);
	     System.out.println("***********************");
	     
	     NewFrame.appendRightText(cTest.showRightPanelUnit, (100 - remainCoverRate * 1) * 1);
	     NewFrame.appendRightTextDistance(cTest.getMineArea());
	     NewFrame.appendRightText(missSentence);
	     return proportion;
	}
	
	private static String loadStream(InputStream in) throws IOException {
		// TODO Auto-generated method stub
		 int ptr = 0;
			in = new BufferedInputStream(in);
		    StringBuffer buffer = new StringBuffer();
		    while( (ptr = in.read()) != -1 ) {
		        buffer.append((char)ptr);
			}
		   return buffer.toString();	

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int proportion = 0;
		try {
			generateGcno();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			generateGCDA();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			proportion = generateGcov();
			//评价一轮后的操作
			cTest.GAEvaluate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!flag){
			System.out.println("remainCoverRate:"  + remainCoverRate );
			System.out.println("coverCriteria:"  + coverCriteria );
			if( remainCoverRate > coverCriteria){
		
				    try {
						GCC.run();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			}
			else{
				 GCC.searchCount();
				 NewFrame.appendRightText("集成覆盖已完成", remainCoverRate);
				 NewFrame.appendRightText("有效单元组:");
				 String para = "";
				 for(int i=0; i<cTest.successParaCount; i++){
					   if(i==0){
						   para += cTest.successPara[i] + "   (最初参考集)" + "\n";
					   }
					   else{
						   para += cTest.successPara[i] + "\n";
					   }
				   }
				 
				 NewFrame.appendRightText(para); 	 
				 NewFrame.appendRightText("下一波"); 
				 lastCoverage = 100;
				 //准备下一波;
				 try {
						Thread.sleep(1000);
						
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    try {
						GCC.generateTest(NewFrame.fileContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
			}
		}
		else{
			   if(IterationCount <= IterationMax){
				   GCC.searchCount();
				   NewFrame.appendRightText("集成覆盖已完成",  remainCoverRate);
				   NewFrame.appendRightText("有效单元组:");
				   String para = "";
				   for(int i=0; i<cTest.successParaCount; i++){
					   
					   if(i==0){
						   para += cTest.successPara[i] + "   (最初参考集)" + "\n";
					   }
					   else{
						   para += cTest.successPara[i] + "\n";
					   }
				   }
				   NewFrame.appendRightText(para);
			   }
			   else{
				   GCC.overIteration();
				   NewFrame.appendRightText("迭代次数过多超过基准");
			   }
			     lastCoverage = 100;
				 NewFrame.appendRightText("下一波"); 
				 //准备下一波;
				 try {
						Thread.sleep(1000);
						
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    try {
						GCC.generateTest(NewFrame.fileContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
	}
}
