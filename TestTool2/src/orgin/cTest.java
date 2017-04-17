package orgin;
/*
 *  简单的单元测试驱动
 *  规范 如下 int main -> void function
 *  逻辑语句 请写成 变量在做 例如 a>1
 *  最大的测试用例为 1000 个
 *  
 */
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cTest {
	  public static String originModel; 
	  public static String[] functionName = new String[100];
	  public static int functionCount = 0;
	  public static String noSpaceModel;
	  public static String[] IfParts;
	  static boolean logicFlag = true;
	  public static String showRightPanelUnit = "";
	  public static String initialData[] = {};
	  public static String initialDataEvalue[] = {};
	  public static String successDataEvalue[] = {};
	  public static String IfBodySplitParts = "";
	  public static int suitFunction = 0;
	  static String firstCase[] = {};
	  static float crossoverRate =  (float) 0.03;
	  static float variationRate = (float) 0.02;
	  static int mineAreaUnit;
	  public static String successPara[] = new String[100];
	  public static int successParaCount = 0;
	  public static int coverage = 20;
	  public static final int baseCoverage = 20;
	  public static final int baseMineAreaUnit = 10;
	  public static int allCollision = 0;
	  public static int collisionCount = 0;
	  
	  public static void restPara(){
		   successParaCount = 0;
		   coverage = 20;
		   collisionCount = 0;
		   allCollision =0;
		   successPara = new String[100];
		   functionCount = 0;
		   functionName = new String[100];
		   suitFunction = 0;
		   logicFlag = true;
		   initialData  = new  String[1000]; 
		   initialDataEvalue = new String[1000];
		   successDataEvalue = new String [1000];
	  }
	  
	  public static void setcrossoverRate(){
		  crossoverRate = (float) (crossoverRate + 0.01 * Math.random() * 10 );
		  if(crossoverRate < 0.03){
			  crossoverRate = (float) 0.03;  
		  }
	  }
	  
	  public static void setvariationRate(){
		  variationRate = (float)(variationRate + 0.01 * Math.random());
		  if(variationRate < 0.02){
			  variationRate = (float) 0.02;  
		  }
	  }
	 
	  public static String getFunctionBody(String tempoText){
		  int bracketNum = 0;
		  String result= "";
		  do{
			    char functionBody[] = tempoText.toCharArray();
			    int i;
			    for(i=0; i<tempoText.length(); i++){
			    	if(functionBody[i] == '{'){
			    		bracketNum ++;
			    	}
			    	if(functionBody[i] == '}'){
			    		bracketNum --;
			    		if(bracketNum == 0){
			    			result = tempoText.substring(0, i+1);
			    			break;
			    		}
			    	}
			    }
		   }while(bracketNum > 0);
		  return result;
	  }
	  
	  public static String deleteLogicBracket(String tempoText){
		  int bracketNum = 0;
		  String result= "";
		  System.out.println(tempoText);
		  do{
			    char functionBody[] = tempoText.toCharArray();
			    int i;
			    for(i=0; i<tempoText.length(); i++){
			    	if(functionBody[i] == '('){
			    		bracketNum ++;
			    	}
			    	if(functionBody[i] == ')'){
			    		bracketNum --;
			    		if(bracketNum == 0){
			    			result = tempoText.substring(1, i);
			    			break;
			    		}
			    	}
			    }
		   }while(bracketNum > 0);
		  return result;
	  }
	  
	  public static String deleteLogic(String tempoBody){
		  String pattern = "\\!";
		  String needBody = "";
		  int clearLogic = 0;
		  Pattern function = Pattern.compile(pattern);
		  Matcher functionMatcher = function.matcher(tempoBody);
		  while(functionMatcher.find()){
			  //再!紧跟着(
			  if(tempoBody.substring(functionMatcher.start()+1, functionMatcher.start()+2).equals("(")){
				  String result = deleteLogicBracket(tempoBody.substring(functionMatcher.start()+1));
				  tempoBody = tempoBody.replace("!(" + result + ")", result);
			  };
		  }
		  return tempoBody;
	  }
	  //取出if 里的选择 语句
	  public static String getIfBody(String ifBody){
		   String loginKeys[] = {"!","&&","||"};
		   String mathKeys[] = {">","<"};
		   String pattern = "if\\((.+?)\\)[^)]";
		   String ifPara = "";
		   Pattern function = Pattern.compile(pattern);
		   Matcher functionMatcher = function.matcher(ifBody);
		   while(functionMatcher.find()){
			   System.out.println("Found value: " + functionMatcher.group(0));
			   System.out.println("Found value: " + functionMatcher.group(1));
			   ifPara = functionMatcher.group(1);
			   logicFlag = true;
			   for(int i=0; i<loginKeys.length;i++){
				   String logicRegex = loginKeys[i];
				   if(ifPara.contains(logicRegex)){
					   logicFlag = false;
			           if(logicRegex == "||") logicRegex = "\\|\\|";
			           if(logicRegex == "!")  ifPara = deleteLogic(ifPara);
			           if(logicRegex != "!"){
			        	   String partIfOption[] = ifPara.split(logicRegex);
			        	   for(int j=0; j<partIfOption.length; j++){
			        		   System.out.println("－－" + partIfOption[j]);
			        		   IfBodySplitParts += partIfOption[j] + ",";
			        	   }
			           }
				   }
			   }
			   if(logicFlag){
				   IfBodySplitParts += functionMatcher.group(1) + ",";
			   }
		   }
		   if(IfBodySplitParts.substring(IfBodySplitParts.length()-1, IfBodySplitParts.length()).equals(",")){
			   IfBodySplitParts = IfBodySplitParts.substring(0, IfBodySplitParts.length() - 1);
			   System.out.println(IfBodySplitParts);
		   }
		   return  IfBodySplitParts; 
	  }
	  
	  public static void dealWithIfParts(String IfPart, String functionPara, String functionBody){
		  //当左右只有1个变量的时候
		  System.out.println("****************");
		  System.out.println("收入的变量条件:" + IfPart);
		  IfParts = IfPart.split(",");
		  for(int i=0;i<IfParts.length; i++){
			  System.out.println("零散的变量:" + IfParts[i]);
		  }
		  System.out.println("****************");
	  }
     
	  public static String keyValue(String model){
		  //save关键字 给关键字 插入@
		  String keys[] = {"int","char","String","const","return"};
		  System.out.println(model);
		  String pattern = "include(?:(\\s+)<(.+?)>)";
		  Pattern function = Pattern.compile(pattern);
		  Matcher functionMatcher = function.matcher(model);
		  while(functionMatcher.find()){
			   System.out.println("Found value: " + functionMatcher.group(0));
			   System.out.println("Found value: " + functionMatcher.group(1));
			   System.out.println("Found value: " + functionMatcher.group(2));
			   model = model.substring(0,functionMatcher.end()) + "\n" +  model.substring(functionMatcher.end(), model.length());
		  }
		  
		 
		  for(int i=0; i<keys.length; i++){
			  model = model.replaceAll(keys[i] + " ",keys[i] + " @");
		  }
		  return model;
	  }
	  
	  public static String randomParaString(int i){
			
		  return "";
	  }
	  
	  public static int randomParaInt(int position, int functionCount,int length){
		  //第一次
		  if(gccTest.IterationCount == 0 && initialData.length == 0){
			  int functionPara = functionCount * 3 + 1;
			  String para = functionName[functionPara];
			  String paranum[] = para.split(",");
			  for(int i=0; i<paranum.length; i++){
				  System.out.println("输入变量的值:" + paranum[i]);
			  }
			  //添加用户选择输入的变量
			  System.out.println(NewFrame.text[0].getText());
			  System.out.println(NewFrame.text[1].getText());
			  System.out.println(NewFrame.text[2].getText());
			  System.out.println(NewFrame.text[3].getText());
			  System.out.println(NewFrame.text[4].getText());
			  
			  if(!NewFrame.text[3].getText().equals(""))
			  gccTest.coverCriteria = 100 - Integer.parseInt(NewFrame.text[4].getText());
			  else{
				  gccTest.coverCriteria = 0;
			  }
			  
			  System.out.println("要求测试覆盖度为:" +  gccTest.coverCriteria);
			  coverage = Integer.parseInt(NewFrame.text[3].getText());
			  
			  //重置覆盖率
			  if(!NewFrame.text[3].getText().equals(""))
			  iniatiateMineArea();

			  generateInitialData(functionCount);
			  
			  String tempoData[] = initialData[0].split(",");
			  initialData[0] = "";
			  for(int i=0;i<tempoData.length; i++){
				  if(!NewFrame.text[i].getText().equals("")){
					  initialData[0] += NewFrame.text[i].getText() + ",";
				  }
				  else{
					  initialData[0] += tempoData[i] + ",";
				  }
			  }
			  initialData[0] = initialData[0].substring(0,initialData[0].length()-1); 
			  System.out.println("看看新鲜的数据:" + initialData[0]);
			  
			  firstCase = initialData[0].split(",");
		  }
		  if(gccTest.IterationCount == 0){
			  return Integer.parseInt(firstCase[position]);
		  }
		  if(gccTest.IterationCount > 0){
			 String analyseIfpart = IfParts[position];
//			 if(position == length - 1)
//			 int paraResult = GAGeneratePara(position);
			 int basePara = Integer.parseInt(firstCase[position]) + (int)(Math.random() * coverage - coverage/2);    //Math.random()  [0,1]
			 return basePara;
		  }
		  return (Integer) null;
	  }
	  

	public static void GAEvaluate() {
		// TODO Auto-generated method stub
		System.out.println("****************"); 
		System.out.println("适应度函数");
		System.out.println(suitFunction);
		System.out.println("*****************");
	}

	/* 生成初始数据 开始步幅为 [-100 100]*/
	  private static void generateInitialData(int functionCount) {
		// TODO Auto-generated method stub
	  
	    int functionPara = functionCount * 3 + 1;
	    String para = functionName[functionPara];
	    String paranum[] = para.split(",");
	    for(int i=0; i<paranum.length; i++){
	    	paranum[i] = paranum[i].replace("int","").replace(" ","");
	    }
	    
	    Map<String,String> map=new HashMap<String,String>();  
		int paraNum = paranum.length;
		// 最大只有 1000 个 用例
		initialData  = new  String[1000]; 
		initialDataEvalue = new String[1000];
		successDataEvalue = new String [1000];
		
		for(int i=0; i<1000; i++){
			initialData[i] = "";
		}
		//解析参数
		//初始化 开始 的 map 参数
		for(int i=0; i<paraNum; i++){
			map.put(paranum[i],  (int)(Math.random() * coverage - coverage/2) + "");
		}
		for(int i=0; i<IfParts.length; i++){
			Pattern pattern = Pattern.compile("(\\w+)(.?+)(\\d+)?");
			Matcher matcher = pattern.matcher(IfParts[i]);
			while(matcher.find()) {
				  System.out.println("Found value: " + matcher.group(0));
				  System.out.println("Found value: " + matcher.group(1));
				  System.out.println("Found value: " + matcher.group(2));
				  System.out.println("Found value: " + matcher.group(3));
				  if(matcher.group(3) != null){
					  map.put(matcher.group(1),matcher.group(3));
				  }
				  break;
			}
		}
		
		System.out.println("生成变量的参照:");
		java.util.Iterator<String> iter = map.keySet().iterator();
         while(iter.hasNext()){    
             String key;    
             String value;    
             key = iter.next().toString();    
             value = map.get(key);    
             System.out.println(key + "---" + value);  
          }   
		
		
			//需要在 para 里 获取变量的信息
			for(int j=0; j<paraNum; j++){
				initialData[0] += Integer.parseInt(map.get(paranum[j])) + ","; 
			}
			initialData[0] = initialData[0].substring(0,initialData[0].length()-1); 
		
		
		System.out.println("*****************");
		System.out.println("开始的随机变量");
		System.out.println(initialData[0]);
		System.out.println("*****************");
	}

	public static char randomParaChar(int i){
		  
		  return 'a';
	  }
	  
	  public static String generateTestcase(){
		//替换文本中的函数调用
		   boolean stepMine = false;
		   String loadPara = "";
		   showRightPanelUnit = "";
		   
		   for(int i=0; i<functionCount; i=i+3){
			   String Name = functionName[i]; 
			   String para = functionName[i+1];
			   String pattern = "[^\\s]"+functionName[i]+"\\((.+?)\\)";   
		       Pattern function = Pattern.compile(pattern);
		       Matcher functionMatcher = function.matcher(noSpaceModel);
			   
			   while(functionMatcher.find()){
				   String paranum[] = para.split(",");
				   for(int j=0; j<paranum.length; j++){
					   //随机生成变量
					   if(para.substring(0, para.indexOf(" ")).equals("int")){
					       loadPara += randomParaInt(j,i,paranum.length) + ",";   
					   }
				   }
				   
				  loadPara = loadPara.substring(0, loadPara.length() - 1);
				  System.out.println("装载的变量:");
				  System.out.println(loadPara);
				  
				  //选择
				  boolean crossover = false;
				  boolean variation = false;
				  if(Math.random() < crossoverRate){
					  crossover = true;
				  }
				  if(Math.random() < variationRate){
					  variation = true;
				  }
				  if(crossover || variation){
					  System.out.println("交叉变异");
					  loadPara = crossoverAndvariation(loadPara,crossover,variation);
				  }
				  if(!crossover && !variation){
					System.out.println("不需要交叉变异");  
				  }
				  
				  stepMine = checkRepeatIntialData(loadPara);
				  
				  if(!stepMine){
					  collisionCount = 0;
					  initialData[gccTest.IterationCount] = loadPara; 
					  //制作现实在面板里的单元
					  showRightPanelUnit += functionName[i+1] + "\n" + loadPara;
					  
					  originModel = originModel.replaceAll(functionName[i]+"\\((.+?)\\)(\\s+)?[^\\{]", functionName[i] + "(" + loadPara + ");");
				  }
				  else{
					  collisionCount ++ ;
					  allCollision ++;
					  System.out.println("踩中了雷区-生成失败");
					  NewFrame.appendRightText("踩中了雷区(" + loadPara + "),  次数:" + collisionCount);
					  if(collisionCount > 20){
						  collisionCount = 0;
						  setMineArea();
						  NewFrame.appendRightText("步长:" + mineAreaUnit);
					  }
					  generateTestcase();
				  }
      		   }
		   }

		  return  originModel;
	  }
	private static String crossoverAndvariation(String loadPara,boolean crossover, boolean variation) {
		  String para[] = loadPara.split(",");
		  String newString = "";
		//交叉
			if(crossover){
				int accumulateAdd = 0;
				//生成一位随机数 确定 交叉
				java.util.Random random = new java.util.Random();
				int crossoverNum = random.nextInt(para.length -1) + 1;
				for(int j=0; j<crossoverNum; j++){
					int result = random.nextInt(para.length - 1) + 1;// 返回[0,参数长度)集合中的整数
					int rate = random.nextInt(100);
					int startPosition = 0;
					if(gccTest.IterationCount == 0){
						startPosition = 0;
					}
					else{
						random.nextInt(gccTest.IterationCount);
					}
					int count = 0;
					for(int i=startPosition; count<gccTest.IterationCount; i++){
						String initialDataPart[] = initialData[i].split(",");
						accumulateAdd += Integer.parseInt(initialDataEvalue[i]);
						if(accumulateAdd > rate){
							para[result] = initialDataPart[result];
							break;
						}
						if(i == gccTest.IterationCount - 1){
							  i = -1;
						}
						  count ++;
					}
				}
			}
			//变异产生雷区内的点
			if(variation){
				int accumulateAdd = 0;
				//生成一位随机数 确定 交叉
				java.util.Random random = new java.util.Random();
				int result = random.nextInt(para.length);// 返回[0,3)集合中的整数
				int rate = random.nextInt(100);
				int count = 0;
				int startPosition = 0;
				if(gccTest.IterationCount == 0){
					startPosition = 0;
				}
				else{
					random.nextInt(gccTest.IterationCount);
				}
				for(int i=startPosition; count<gccTest.IterationCount; i++){
					String initialDataPart[] = initialData[i].split(",");
					accumulateAdd += Integer.parseInt(initialDataEvalue[i]);
					if(accumulateAdd > rate){
						para[result] = initialDataPart[result] + random.nextInt(mineAreaUnit);
						break;
					}
					if(i == gccTest.IterationCount - 1){
						  i = -1;
					}
					  count ++;
				}
				
			}
			
			for(int j=0; j<para.length; j++){
				newString += para[j] + ",";
			}
			
			newString = newString.substring(0, newString.length() -1);
			boolean repeatFlag = checkRepeat(newString);
			if(!repeatFlag){
				return newString;
			}
			return loadPara;
	}

	private static boolean checkRepeat(String newString) {
		// TODO Auto-generated method stub
		boolean repeatFlag = false;
		for(int i=0; i<gccTest.IterationCount; i++){
		   if(initialData[i].equals(newString)){
			   repeatFlag = true;
		   }
		}
		return repeatFlag;
	}

	//检测随机生成的变量组是否会出现
	  private static boolean checkRepeatIntialData(String loadPara) {
	    String para[] = loadPara.split(",");
		int RandomParaSet[] = new int[para.length];
		int count = 0;
//		mineAreaUnit = 6;
		boolean stepMine = false;
		System.out.println("*************************"); 	
		System.out.println("遗传算法生成变量");
		System.out.println("IterationCount:" + gccTest.IterationCount);
		System.out.println("cTest.initialDataEvalue:" + cTest.initialDataEvalue[0]);
//		boolean repeatFlag = checkRepeat(loadPara);
		
		for(int j = 0; j < para.length; j++){
			RandomParaSet[j] = Integer.parseInt(para[j]);
		}
		
//		for(int j = 0; j<gccTest.IterationCount; j++ )
//		{	
//			System.out.println("AAAAA" + cTest.initialDataEvalue[j] + "AAAAA");
//			
//		}
			//校验生成的变量在雷区
				for(int i=0; i<gccTest.IterationCount; i++){
//					System.out.println("生成的数据:" + initialData[i]);
					String initialDataPart[] = initialData[i].split(",");
					count = 0;
					for(int j=0; j < para.length; j++ ){
						int initialDataPartPara = RandomParaSet[j];
	//					System.out.println("生成的评价:"+ initialDataEvalue[i]);
						//变异 变异的意义在于 给一定的机率踩入雷区
						int caculatMineArea = (mineAreaUnit * (100 - Integer.parseInt(cTest.initialDataEvalue[i])))/100;
						//踩入雷区
	//					System.out.println("雷点是" + initialDataPartPara);
					   
	//					System.out.println("雷点作用域:" + caculatMineArea);
						if(Math.abs(Integer.parseInt(initialDataPart[j]) - initialDataPartPara) < caculatMineArea){   
							count ++;
						}
					}
					if(count == para.length){
						stepMine = true;
						break;
					}
				}
		    
		//第一组变量生成的时候 默认不踩雷
		if(gccTest.IterationCount == 0)	
		stepMine = false;
		
		System.out.println("*************************"); 
		return stepMine;
		
	}

	public static String cParse(String Filecontent){   	
		
	   iniatiateMineArea();	 	
	   originModel = Filecontent;	  
	   String model = Filecontent.replaceAll("\n", "");	 

	   //给关键字隔离出空格
	   model = keyValue(model);

	   //截取function正则   
	   String pattern = "void(?:(\\s+)(\\w+)\\((.+?))\\)";
	   Pattern function = Pattern.compile(pattern);
	   Matcher functionMatcher = function.matcher(model);
       String IfBodyParts = "";
	   int count = 0;
	   functionCount = 0;
	   int startClearSpace = 0;
	   noSpaceModel = "";
	   int endClearSpace = 0;
	   
	   while(functionMatcher.find()){
		   System.out.println("Found value: " + functionMatcher.group(0));
		   System.out.println("Found value: " + functionMatcher.group(1));
		   System.out.println("Found value: " + functionMatcher.group(2));
		   System.out.println("Found value: " + functionMatcher.group(3));
		   //function 名
		   functionName[functionCount] = functionMatcher.group(2).replace(" ", "");
		   functionCount ++;
		   //变量名
		   functionName[functionCount] = functionMatcher.group(3).replace("@", " ");
		   functionCount ++;
		   String tempoModel = model.substring(startClearSpace,functionMatcher.start());
		   int start =functionMatcher.start() + functionMatcher.group(0).length();   
		   String tempoText = model.substring(start).replaceAll(" ", "");
		   String functionBody = getFunctionBody(tempoText);
		   
		   System.out.println(functionBody);
		   
		   
		   //方法体
		   functionName[functionCount] = functionBody.replace("@", " ");;
		   functionCount ++;
		   //去除 function Test(int a ,int b,int  c) 之间的空格
		   tempoModel = tempoModel.replaceAll(" ","");
		   startClearSpace = functionMatcher.end();
		   noSpaceModel += tempoModel + model.substring(functionMatcher.start(),functionMatcher.end());
		   endClearSpace = functionMatcher.end();
	       count++;
	   }
	   //清掉最后1个function后的空格
	   noSpaceModel += model.substring(endClearSpace, model.length()).replaceAll(" ", "");
	   noSpaceModel = noSpaceModel.replaceAll("@"," ");
	   System.out.println("去除空格的字符串 :" + "//" + noSpaceModel + "//");
	   //找出调用的语句
	   
	   System.out.println("****************");
	   System.out.println("函数结构");
	   for(int i=0; i<functionCount; i++){
		   System.out.println(functionName[i]);
	   }
	   System.out.println("****************");
	   
	   for(int i=0; i<functionCount; i=i+3){
		   String Name = functionName[i]; 
		   String para = functionName[i+1];
		   String functionBody = functionName[i+2];
		   //解析 指定 function 内的 参数 变量
		   pattern = "[^\\s]"+functionName[i]+"\\((.+?)\\)";   
	       function = Pattern.compile(pattern);
		   functionMatcher = function.matcher(noSpaceModel);
		   while(functionMatcher.find()){
			   //调用语句
			   System.out.println("Found value: " + functionMatcher.group(0).substring(1, functionMatcher.group(0).length()));
			   IfBodyParts = getIfBody(functionBody);
		   }
		   //处理if 语句里的变量信息
		   dealWithIfParts(IfBodyParts, para, functionBody);
	     }
	   originModel = generateTestcase();
	   return   originModel;
	}

	static void iniatiateMineArea() {
		// TODO Auto-generated method stub
		mineAreaUnit = (int) coverage / 2;
		
		if(mineAreaUnit <= 1){
			mineAreaUnit = 1;
		}
		
		System.out.println("初始雷区半径:" + mineAreaUnit);
	}
	private static void setMineArea() {
			// TODO Auto-generated method stub
			mineAreaUnit = mineAreaUnit/2;
			if(mineAreaUnit <= 1){
				mineAreaUnit = 1;
			}
			System.out.println("调整后的雷区半径:" + mineAreaUnit);
	}
	static int getMineArea(){
		return mineAreaUnit;
	}
}

