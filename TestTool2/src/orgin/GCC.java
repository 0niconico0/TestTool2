package orgin;

import java.io.IOException;
import java.util.HashMap;


public class GCC {
	 static gccTest gTest;
	 static cTest ctest;
	 static int count = 0;
	 static long t1;
	 // public static void main(String[] args) throws IOException {  
	 public static String getFile(String filePath) throws IOException{
		 t1 = System.currentTimeMillis(); 
		 gTest = new gccTest();
//		 Scanner in = new Scanner(System.in);
//		 String   str = in.next();
//		 System.out.println(str);
		 String fileContent = gTest.readLine(filePath);
	
         return fileContent;
	 }
	 public static void generateTest(String fileContent) throws IOException{
		 count = 0;
		 count ++;
		 //第二轮迭代
		 if(gccTest.IterationCount > 0) {
			 System.out.println("第二轮迭代了");
			 //第一轮要预先write所以count为0
		     gccTest.IterationCount = 0;
		     //调整 交叉 变异的参数
		     cTest.setcrossoverRate();
		     cTest.setvariationRate();
		     cTest.restPara();
		     //重置初始覆盖额
		     gccTest.remainCoverRate = 100;
		     //重置时间
		     t1 = System.currentTimeMillis(); 
		     //重置步长
		     cTest.iniatiateMineArea();
		     count = 0; 
		     //开启多次迭代开关
		     gTest.mutipleIteration = true;
		     //重置覆盖率map
		     gccTest.basicMap = new HashMap<String,String>();
		     gccTest.newMap = new HashMap<String,String>();
		     
		     ctest = new cTest();
		     String testFile = ctest.cParse(fileContent);
		     GCC.run();
		 }
		 
		 if(gccTest.IterationCount == 0) {
		     ctest = new cTest();
			 String testFile = ctest.cParse(fileContent);
			 gTest.writeFile(testFile);
			 gTest.start();
		 }
	 }
	 public static void run() throws IOException{
		 count ++;
		 String testFile = ctest.generateTestcase();
		 gTest.writeFile(testFile);
         gTest.run();	 
	 }
	 public static void searchCount(){
		 System.out.println("生成次数为:" + count);
		 long t2 = System.currentTimeMillis(); 
		 long interval = t2 - t1;
		 System.out.println("花费时间:"+ interval + "ms");
		 NewFrame.appendRightText("生成次数为:" + count);
		 NewFrame.appendRightText("花费时间:"+ interval + "ms");
		 int coverCriteria = 100 - gccTest.coverCriteria;
		 String para = "coverCriteria:" + coverCriteria + "\n" + "generateCount:" + count + "\n" + "time:"+ interval + "ms" + "\n"
		         +"crossoverRate:"+ cTest.crossoverRate + "\n" + "varitionRate:" + cTest.variationRate + "\n" + "allCollision:" + cTest.allCollision + 
		         "\n" + "mineAreaUnit:" +  cTest.mineAreaUnit +
				 "\n\n";
		 file.method3("addfile.txt", para);
		 
	 }
	public static void overIteration() {
		// TODO Auto-generated method stub
		System.out.println("迭代次数超过基准");
		NewFrame.appendRightText("迭代次数超过基准");
	}
}
