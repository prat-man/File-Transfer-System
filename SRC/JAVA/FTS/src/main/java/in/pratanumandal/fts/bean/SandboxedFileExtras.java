package in.pratanumandal.fts.bean;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.google.common.base.Optional;

import ealvatag.audio.AudioFile;
import ealvatag.audio.AudioFileIO;
import ealvatag.tag.Tag;
import ealvatag.tag.images.Artwork;
import in.pratanumandal.fts.util.CommonUtils;
import net.coobird.thumbnailator.Thumbnails;

public class SandboxedFileExtras extends SandboxedFile {
	
	protected String thumbnail;

	public SandboxedFileExtras(File file) throws IOException {
		super(file);
		
		if (file.exists() && !file.isDirectory()) {
			String mimeType = Files.probeContentType(file.toPath());
			
			if (mimeType != null) {
		        String type = mimeType.split("/")[0];
		        
		        if (type.equals("image")) {
					try {
						BufferedImage thumbnailImage = Thumbnails.of(file).size(500, 200).asBufferedImage();
						this.thumbnail = CommonUtils.imageToBase64(thumbnailImage);
					}
					catch (Exception e) {
						// DO NOTHING
					}
		        }
		        else if (type.equals("video")) {
					try (
						FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
						Java2DFrameConverter converter = new Java2DFrameConverter();
					) {
						grabber.start();
						
						grabber.setFrameNumber(grabber.getLengthInFrames() / 2);
						Frame frame = grabber.grabImage();
						BufferedImage frameImage = converter.getBufferedImage(frame);
						
						grabber.stop();
						
						BufferedImage thumbnailImage = Thumbnails.of(frameImage).size(500, 200).asBufferedImage();
						
						this.thumbnail = CommonUtils.imageToBase64(thumbnailImage);
					}
					catch (Exception e) {
						// DO NOTHING
					}
				}
		        else if (type.equals("audio")) {
					try {
						AudioFile audioFile = AudioFileIO.read(file);
						Optional<Tag> optionalTag = audioFile.getTag();
						
						if (optionalTag.isPresent()) {
							Tag tag = optionalTag.get();
							
							Optional<Artwork> optionalArtwork = tag.getFirstArtwork();					
							if (optionalArtwork.isPresent()) {
								Artwork artwork = optionalArtwork.get();
				            	BufferedImage artworkImage = (BufferedImage) artwork.getImage();
				            	BufferedImage thumbnailImage = Thumbnails.of(artworkImage).size(500, 200).asBufferedImage();
								this.thumbnail = CommonUtils.imageToBase64(thumbnailImage);
							}
						}
					} catch (Exception e) {
						// DO NOTHING
						e.printStackTrace();
					}
		        }
			}
		}
	}

	public String getThumbnail() {
		return thumbnail;
	}

	@Override
	public String toString() {
		return "SandboxedFileExtras [thumbnail=" + thumbnail + ", name=" + name + ", path=" + path + ", size=" + size
				+ ", directory=" + directory + ", fileCount=" + fileCount + ", folderCount=" + folderCount
				+ ", creationTime=" + creationTime + ", lastModifiedTime=" + lastModifiedTime + ", lastAccessTime="
				+ lastAccessTime + "]";
	}

}
