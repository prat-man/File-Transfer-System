package in.pratanumandal.fts.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class IconManager {
	
	public static String getIcon(File file) {
		String mimeType = null;
		
		if (file.isDirectory()) {
			mimeType = "folder";
		}
		else {
			try {
				mimeType = CommonUtils.getMimeType(file);
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
		
		if (mimeType == null) {
			mimeType = "unknown";
		}
		
		try {
			InputStream in = null;
			
			in = IconManager.class.getResourceAsStream("/static/img/mimetypes/" + mimeType.replace('/', '-') + ".png");
			
			if (in == null) {
				in = IconManager.class.getResourceAsStream("/static/img/mimetypes/" + mimeType.split("/")[0] + "-x-generic.png");
			}
			
			if (in == null && mimeType.endsWith("-compressed")) {
				in = IconManager.class.getResourceAsStream("/static/img/mimetypes/" + mimeType.replace('/', '-').substring(0, mimeType.length() - "-compressed".length()) + ".png");
			}
			
			if (in == null) {
				in = IconManager.class.getResourceAsStream("/static/img/mimetypes/unknown.png");
			}
			
			if (in != null) {
				BufferedImage image = ImageIO.read(in);
				return CommonUtils.imageToBase64PNG(image);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
