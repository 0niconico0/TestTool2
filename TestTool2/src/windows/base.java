package windows;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class base extends Application {
	public static BorderPane BORDER;
	private Scene scene;
	
	public void start(Stage stage) {	
		BORDER = new BorderPane();   
				
		// 初始化顶部菜单（Top）
		BORDER.setTop(new Top());
		
		// 初始化文件结构树（Left）
		BORDER.setLeft(new Left());

		// 初始化中心文本域（Center）
		BORDER.setCenter(new Center());
		
		// 初始化变量管理（Bottom）
		BORDER.setBottom(new Bottom());
		
		// 初始化输出域（Right）
		BORDER.setRight(new Right());

		// 设置主窗体属性     
		scene = new Scene(BORDER, 1200, 800);
		stage.setTitle("自动化测试用例生成工具");
		stage.setScene(scene);
		stage.show();
	}
   
	public static void main(String[] args) {
		launch(args);
	}
}
