/** 
 * 程序的核心方法
 */

package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import windows.Center;
import windows.Left;
import windows.base;

public class FileEvent {
	public static File selectedFile; // 选中的文件
	public static String fileName = "请选择文件"; // 文件名
	public static String filePath = "请选择文件"; // 文件路径
	public static String fileContent; // 文件内容
	public static String itemValue = null; // 文件结构树的叶子名，即函数或变量
	public static Pattern VAR_NAME_PATTERN; // 获取函数名或变量名的正则模式
	
	// 打开文件
	public static void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("请选择文件");	
		selectedFile = fileChooser.showOpenDialog(null);	
			
		if(selectedFile != null) {		
			
			// 展示文件路径和文件名
			filePath = selectedFile.getAbsolutePath();
			fileName = selectedFile.getName();	
			
			base.BORDER.setLeft(new Left());			
			base.BORDER.setCenter(new Center());
//			base.BORDER.setRight(new Right());
			
			// 读取文件内容
			fileContent = readToString(selectedFile);		
			Center.TEXT_CODE.setText(fileContent);
//			Right.TEXT_CODE2.setText(fileContent);

//		    Pattern pattern = Pattern.compile("\\b(int)\\b");
//			Matcher matcher = pattern.matcher(fileContent);
//			while (matcher.find()) { 
//				Right.TEXT_CODE2.setText(fileContent.replaceAll("\\b" + matcher.group() + "\\b", " * "));
//			}

		}
	}
		
	// 读取文件内容，一次性读取全部内容
	public static String readToString(File file) {  
		String encoding = "gb2312";  
		Long fileLength = file.length();  
	    byte[] filecontent = new byte[fileLength.intValue()];  
	    try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            return "文件打开失败！";
        } catch (IOException e) {  
            e.printStackTrace();  
            return "文件打开失败！";
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
            return "输出不支持此编码：" + encoding;  
        }  
	} 
	
	// 高亮显示关键字（展示的文本流， 原始文本， 匹配模式， 高亮颜色）
	public static void hightlightKeywords(TextFlow textFlow, Text text, Pattern pattern, Color color) {
		Text[] t = new Text[200];
		int  i = 0, j;
		
		// 匹配关键字
		Matcher matcher = pattern.matcher(text.getText());
		
		// 以关键字为分割点，逐次输出文本
		while (matcher.find()) {  
//		    System.out.println(matcher.group());  	  
			
		    // 根据本次关键字分割文本
			String[] strs = text.getText().split("\\b(" + matcher.group() + ")\\b");
			// matcher.group() 并不包含 \b，需要在分割时再次添加
		
			// 将本次关键字前的文本输出
			textFlow.getChildren().add(new Text(strs[0])); 			
//			System.out.println(strs[0]);
			
			// 将本次关键字高亮输出
			t[i] = new Text();
			t[i].setFill(color);
			t[i].setText(matcher.group());
			textFlow.getChildren().add(t[i]); 
//			System.out.println(t[i].getText());
			i++;
		
			// 将本次关键字后的文本作为下一次循环的文本
			String s = "";
			for (j = 1; j < strs.length - 1; j++) {
				s = s + strs[j] + matcher.group();
			}
			s += strs[j];
			text.setText(s);
		}  
		
		// 将最后一个关键字之后的剩余文本输出
		textFlow.getChildren().add(new Text(text.getText())); 
//		System.out.println(Center.TEXT_CODE.getText());
		text.setText(null);
	}
	
	// 高亮显示关键字，并高亮显示选中变量或函数（展示的文本流， 原始文本， 匹配模式， 选中的字段， 选中的字段的高亮颜色， 关键字的高亮颜色）
	public static void hightlight(TextFlow textFlow, Text text, Pattern pattern, String selectedWord, Color color1, Color color2) {
		Text[] t = new Text[200];
		int  i = 0, j;
		
		// 匹配关键字
		Matcher matcher = pattern.matcher(text.getText());
		
		// 以关键字为分割点，逐次输出文本
		while (matcher.find()) {  
		  			
		    // 根据本次循环匹配到的关键字分割文本
			String[] strs = text.getText().split("\\b(" + matcher.group() + ")\\b");
			/** 此语句中 matcher.group() 并不包含 \b，需要在分割时再次添加 */
		
			// 将关键字之前的文本以普通形式输出
			textFlow.getChildren().add(new Text(strs[0])); 			
//			System.out.println(strs[0]);
			
			// 将本次关键字高亮输出
			t[i] = new Text();
			if (matcher.group().matches(selectedWord)) {
				t[i].setFill(color1); // 选中的变量或函数以一种颜色输出
			}
			else {
				t[i].setFill(color2); // 关键字以另一种颜色输出
			}
			t[i].setText(matcher.group());
			textFlow.getChildren().add(t[i]); 
//			System.out.println(t[i].getText());
			i++;
		
			// 将之前被分割的剩余文本合并，作为下一次循环匹配所使用的文本
			String s = "";
			for (j = 1; j < strs.length - 1; j++) {
				s = s + strs[j] + matcher.group();
			}
			s += strs[j];
			text.setText(s);
		}  
		
		// 将最后一次匹配到的关键字后面的剩余文本输出
		textFlow.getChildren().add(new Text(text.getText())); 
//		System.out.println(Center.TEXT_CODE.getText());
		text.setText(null);
	}
	
	// 匹配函数和变量，并在文件结构中展示（文件结构树根， 匹配模式， 文本内容）
	public static void showFileTreeView(TreeItem<String> root, Pattern pattern, String content) {
		TreeItem<String> treeItem;
		
		// 匹配函数和变量
		Matcher matcher = pattern.matcher(content);
		
		// 每匹配到一个关键字，就新建一个叶子
		while (matcher.find()) {  
//		    System.out.println(matcher.group()); 
		    treeItem = new TreeItem<> (matcher.group());	   	    
		    root.getChildren().add(treeItem); 	    
		}
		
		// 为文件结构树添加监听
		selectedItem(Left.treeView);
	}	
	
	// 获取 TreeView 中选中的节点
	public static void selectedItem(TreeView<String> treeView) {
		treeView.getSelectionModel().selectedItemProperty().addListener(   
                new ChangeListener<TreeItem<String>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<String>> observableValue,
                    TreeItem<String> oldItem, TreeItem<String> newItem) {       
                    	
                    	// 获取函数名或变量名
                        itemValue = newItem.getValue().replaceAll(RegEx.VarName, "");
                        String varName = "\\b(" + itemValue + ")\\b"; 
                        
                        // 将匹配模式设置为关键字和函数名或变量名的并集
                	    Pattern pattern = Pattern.compile(RegEx.CKeyWord + "|" + varName);
                        
                        // 刷新中心文本域
                        base.BORDER.setCenter(new Center());
                        Center.TEXT_CODE.setText(fileContent);             	
                		
                		// 高亮显示
                	    hightlight(Center.TEXT_FLOW, Center.TEXT_CODE, pattern, itemValue, Color.RED, Color.BLUE);
                		
//                        System.out.println(itemValue);
                    }
                });
	}

/** 以下为曾使用过的代码 */
//	// 读取文件内容，逐行读取
//	public void readFile() {
//		BufferedReader br = null;
//		String str;
//		try {
//			br = new BufferedReader(new FileReader(selectedFile)); // 此时获取到的br就是整个文件的缓存流
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			Center.TEXT_CODE.setText("读取发生错误！"); 
//		}
//		try {
//			Center.TEXT_CODE.setText("");
//			while ((str = br.readLine())!= null) // 判断最后一行不存在，为空结束循环
//			{
//				Center.TEXT_CODE.appendText(str + "\n"); // 此处将内容写入到TextArea即可
//			}
//			br.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Center.TEXT_CODE.appendText("读取发生错误！"); 
//		}	
//	}	
	
//	// 保存文件
//	public void saveFile() {
//		selectedFile = new File(Bottom.LABEL_FILE_PATH.getText());  
//        
//        // 写入文件内容
//        try {
//        	String str = Center.TEXT_CODE.getText();
//        	FileOutputStream os = new FileOutputStream(selectedFile);
//        	PrintStream ps = new PrintStream(os);
//        	ps.print(str);
//        	//System.out.print(str);
//        	ps.close();
//        	os.close();
//        	setAlert(AlertType.INFORMATION, "保存成功", null, "文件已保存！");
//        } catch(Exception ex) {
//			setAlert(AlertType.INFORMATION, "保存失败", null, "出现未知错误，保存失败！");
//        }
//	}
		
//	// 关键字高亮
//	public static void matchKeyWords() {
//		Text[] t = new Text[200];
//		int  i = 0, j;
//		
//		// 匹配关键字
//		Matcher matcher = RegEx.C_KEYWORD_PATTERN.matcher(Center.TEXT_CODE.getText());
//		
//		// 以关键字为分割点，逐次输出文本
//		while (matcher.find()) {  
////		    System.out.println(matcher.group());  	  
//			
//		    // 根据本次关键字分割文本
//			String[] strs = Center.TEXT_CODE.getText().split(matcher.group());
//			
//			// 将本次关键字前的文本输出
//			Center.TEXT_FLOW.getChildren().add(new Text(strs[0])); 			
////			System.out.println(strs[0]);
//			
//			// 将本次关键字高亮输出
//			t[i] = new Text();
//			t[i].setFill(Color.BLUE);
//			t[i].setText(matcher.group());
//			Center.TEXT_FLOW.getChildren().add(t[i]); 
////			System.out.println(t[i].getText());
//			i++;
//		
//			// 将本次关键字后的文本作为原始文本
//			String s = "";
//			for (j = 1; j < strs.length - 1; j++) {
//				s = s + strs[j] + matcher.group();
//			}
//			s += strs[j];
//			Center.TEXT_CODE.setText(s);
//		}  
//		
//		// 将最后一个关键字后的文本输出
//		Center.TEXT_FLOW.getChildren().add(new Text(Center.TEXT_CODE.getText())); 
////		System.out.println(Center.TEXT_CODE.getText());
//		Center.TEXT_CODE.setText(null);
//	}
	
//	// 高亮显示选中函数或变量
//	public static void highlight() {
//		Text[] t = new Text[200];
//		int  i = 0, j;
//		
//		base.BORDER.setRight(new Right());
//		Right.TEXT_CODE2.setText(fileContent);
//		
//		// 匹配关键字		
//		Matcher matcher = VAR_NAME_PATTERN.matcher(Right.TEXT_CODE2.getText());
//		
//		// 以关键字为分割点，逐次输出文本
//		while (matcher.find()) {  
////		    System.out.println(matcher.group());  	  
//			
//		    // 根据本次关键字分割文本
//			String[] strs = Right.TEXT_CODE2.getText().split(matcher.group());
//			
//			// 将本次关键字前的文本输出
//			Right.TEXT_FLOW2.getChildren().add(new Text(strs[0])); 			
////			System.out.println(strs[0]);
//			
//			// 将本次关键字高亮输出
//			t[i] = new Text();
//			t[i].setFill(Color.RED);
//			t[i].setText(matcher.group());
//			Right.TEXT_FLOW2.getChildren().add(t[i]); 
////			System.out.println(t[i].getText());
//			i++;
//		
//			// 将本次关键字后的文本作为原始文本
//			String s = "";
//			for (j = 1; j < strs.length - 1; j++) {
//				s = s + strs[j] + matcher.group();
//			}
//			s += strs[j];
//			Right.TEXT_CODE2.setText(s);
//		}  
//		
//		// 将最后一个关键字后的文本输出
//		Right.TEXT_FLOW2.getChildren().add(new Text(Center.TEXT_CODE.getText())); 
////		System.out.println(Center.TEXT_CODE.getText());
//		Right.TEXT_CODE2.setText(null);
//	}
/** 曾使用过的代码 */	

}
