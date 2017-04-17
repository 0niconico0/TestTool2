/** 
 * 输出域（Right）
 */

package windows;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class Right extends ScrollPane {
	public static TextArea OUTPUT;
	
	public Right() {					
		OUTPUT = new TextArea();
		OUTPUT.setPromptText("输出域");
		OUTPUT.setPrefSize(300, 623);
		
		// 设置 ScrollPane 属性
		setContent(OUTPUT);
	}
}
