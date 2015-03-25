package usc.edu.sampleapp;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

public class ImageIcon {

	ImageRegistry ir = new ImageRegistry();
	static ImageIcon imgIcon;

	public ImageDescriptor getImageDescriptor(String key) {
		return ir.getDescriptor(key);
	}

	private ImageIcon() {
		ir.put(ISampleAppConstants.IMAGE_CEAR_CO, ImageDescriptor
				.createFromFile(ImageIcon.class, "/icons/clear_co.gif"));
		ir.put(ISampleAppConstants.IMAGE_CONSOLE_VIEW, ImageDescriptor
				.createFromFile(ImageIcon.class, "/icons/console_view.gif"));
	}

	public static ImageIcon getImageIconFactory() {
		if (imgIcon == null) {
			imgIcon = new ImageIcon();
		}
		return imgIcon;

	}

	public Image getImage(String imageName) {
		return ir.get(imageName);
	}

}
