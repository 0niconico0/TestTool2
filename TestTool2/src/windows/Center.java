/** 
 * 中心文本域（Center） 
 */

package windows;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Center extends ScrollPane {	
	private FlowPane fp;
	public static TextFlow TEXT_FLOW;
	public static Text TEXT_CODE;
	
	public Center() {					
		TEXT_CODE = new Text("请选择文件");
		TEXT_CODE.setFont(Font.font("Dialog", 20));
		TEXT_CODE.setFill(Color.BLUE);
		
		TEXT_FLOW = new TextFlow(TEXT_CODE);
		
		// 设置 FlowPane 属性
		fp = new FlowPane();
		fp.getChildren().add(TEXT_FLOW);
		fp.setStyle("-fx-background-color: white;");	
		
		// 设置 ScrollPane 属性
		setContent(fp);
		setFitToHeight(true);
		setFitToWidth(true);
//		setStyle(" -fx-background-color: white; ");	
	}
}
