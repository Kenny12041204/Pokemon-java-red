package zandite.Pokered.GFX;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public String path;
	public int width;
	public int height;
	public int[] pixels;
	
	public SpriteSheet(String path) {
		BufferedImage image = null;
		//Error handling.
		try {
			image = ImageIO.read(SpriteSheet.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Backup Error handling.
		if (image == null) return;
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		//Removes Alpha Channels
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (pixels[i] & 0xff)/ 64;
		}
		//Debugging purposes
//		for (int i = 0; i < 8; i++) {
//			System.out.println(pixels[i]);
//		}
		
	}
}
