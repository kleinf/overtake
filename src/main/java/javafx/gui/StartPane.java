package javafx.gui;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.util.ImageLoader;

/**
 * @author Administrator
 * 
 */
public class StartPane extends Pane {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public StartPane() {
		super();
		Image img = ImageLoader.getImage("images/backgrounds/HP.GIF");
		setBackground(new Background(new BackgroundImage(img,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
		setWidth(img.getWidth());
		setHeight(img.getHeight());
	}
}