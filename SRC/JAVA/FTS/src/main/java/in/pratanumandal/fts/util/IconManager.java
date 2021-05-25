package in.pratanumandal.fts.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class IconManager {
	
	private static final int CACHE_VALIDITY = 3600000;
	
	private static final Map<String, CachedIcon> ICON_CACHE = new HashMap<>();
	
	public static String getIcon(File file) {
		if (file == null) return null;
		
		CachedIcon cachedIcon = ICON_CACHE.get(file.getAbsolutePath());
		if (cachedIcon != null && !cachedIcon.isExpired()) return cachedIcon.getIcon();
		
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
				String icon = CommonUtils.imageToBase64PNG(image);
				
				ICON_CACHE.put(file.getAbsolutePath(), new CachedIcon(icon));
				return icon;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static class CachedIcon {
		
		private String icon;
		private long time;
		
		public CachedIcon(String icon) {
			this.icon = icon;
			this.time = System.currentTimeMillis();
		}
		
		public String getIcon() {
			return icon;
		}

		public boolean isExpired() {
			return System.currentTimeMillis() - time > CACHE_VALIDITY;
		}
		
	}

}
