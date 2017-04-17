/** 
 * 变量管理（Bottom） 
 */

package windows;

import data.FileEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class Bottom extends FlowPane {
	private Label _varName1, _varName2, _varName3;
	private TextField varName1, varName2, varName3;

	private Button buttonCreate;
	
	public Bottom() {
		_varName1 = new Label("变量1：");
		_varName2 = new Label("变量2：");
		_varName3 = new Label("变量3：");
		
		varName1 = new TextField();
		varName1.setPromptText("var1");
		varName2 = new TextField();
		varName2.setPromptText("var2");
		varName3 = new TextField();
		varName3.setPromptText("var3");
		
		buttonCreate = new Button("开始生成");
		buttonCreate.setPrefSize(100, 20);
		buttonCreate.setOnAction((ActionEvent e) -> {
		    Right.OUTPUT.setText(FileEvent.filePath + "\n");
		});
			
		getChildren().addAll(
				_varName1, varName1, 
				_varName2, varName2, 
				_varName3, varName3, 
				buttonCreate);

		// 设置 FlowPane 属性
		setPadding(new Insets(10, 20, 10, 20));
		setVgap(10);
		setHgap(10);
		setPrefHeight(150); 
		setStyle("-fx-background-color: DAE6F3;");		
	}
}
