package application;

import javafx.scene.paint.Color;
import org.fxmisc.richtext.StyledTextArea;

public class TextArea extends StyledTextArea<Boolean> {

	public TextArea() {
		super(true, (textNode, correct) -> {
			// define boolean Text node style
			if (!correct) {
				textNode.setUnderline(true);
				textNode.setBackgroundFill(Color.TOMATO);
			}
		});
	}
}
