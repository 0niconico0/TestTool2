/** 
 * 顶部菜单（Top） 
 */

package windows;

import data.FileEvent;
import data.RegEx;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class Top extends MenuBar {
	private Menu menuFile, menuHelp;
	private MenuItem itemOpen, itemSave, itemExit, itemAbout;
	private SeparatorMenuItem separator1;	
	
	public Top() {	   		
		// 文件菜单
		menuFile = new Menu("文件");
		
		itemOpen = new MenuItem("打开");
		itemOpen.setOnAction((ActionEvent e) -> {
			FileEvent.openFile();
			if(FileEvent.selectedFile != null) {	
				FileEvent.hightlightKeywords(Center.TEXT_FLOW, Center.TEXT_CODE, RegEx.C_KEYWORD_PATTERN, Color.BLUE);
				FileEvent.showFileTreeView(Left.root, RegEx.VAR_TYPE_PATTERN, FileEvent.fileContent);
//				FileEvent.showFileTreeView(Left.root, RegEx.FUNC_TYPES_PATTERN, Right.TEXT_CODE2.getText());
			}
		});
		
		itemSave = new MenuItem("保存");
		itemSave.setDisable(true);
//		itemSave.setOnAction((ActionEvent e) -> {
//			saveFile();
//		});
		
		itemExit = new MenuItem("退出");
		itemExit.setOnAction((ActionEvent e) -> {
		    System.exit(0);
		});
		
		separator1 = new SeparatorMenuItem();
		
		menuFile.getItems().addAll(itemOpen, itemSave, separator1, itemExit);
		
		// 帮助菜单
		menuHelp = new Menu("帮助");
		itemAbout = new MenuItem("关于");
		itemAbout.setOnAction((ActionEvent e) -> {
			setAlert(AlertType.INFORMATION, "关于", null, "自动化测试用例生成工具");
		});
		
		menuHelp.getItems().addAll(itemAbout);
		
		getMenus().addAll(menuFile, menuHelp);
	}
	
	// 弹出提示框
	public void setAlert(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.showAndWait();
	}
		
}
