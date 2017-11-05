/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: ThumbUtil.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.utils.common;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.lyx.file.Constants;
/**
 * 
 *<pre><b><font color="blue">ThumbUtil</font></b></pre>
 *
 *<pre><b>缩略图工具类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   ThumbUtil obj = new ThumbUtil();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class ThumbUtil {
	private File inPutFile;
	private File outPutFile;
	private int width;
	private int height;

	boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

	public ThumbUtil() {
		super();
	}

	/**
	 * 
	 * @param inPutFile
	 *            需要转换的文件
	 * @param outPutFile
	 *            输出文件
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param proportion
	 *            是否需要等比缩放(默认为等比缩放)
	 */
	public ThumbUtil(File inPutFile, File outPutFile, int width, int height,
			boolean proportion) {
		this.inPutFile = inPutFile;
		this.outPutFile = outPutFile;
		this.width = width;
		this.height = height;
		this.proportion = proportion;
	}

	/**
	 * 
	 * @param inPutFile
	 *            需要转换的文件
	 * @param outPutFile
	 *            输出文件
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 */
	public ThumbUtil(File inPutFile, File outPutFile, int width, int height) {
		this(inPutFile, outPutFile, width, height, true);
	}

	public static String getThumbImagePath(String filePath) {
		int position = filePath.lastIndexOf(".");
		String suffix = filePath.substring(position);
		String thumbPath = filePath.substring(0, position)
				+ Constants.THUMB_SUFFIX + suffix;
		return thumbPath;
	}

	@Deprecated
	public static void createThumbImage(File inPutFile, File outPutFile,
			int width, int height) {
		try {
			BufferedImage source = ImageIO.read(inPutFile);
			if (source == null) {
				return;
			}
			if (source.getWidth() / source.getHeight() >= width / height) {
				if (source.getWidth() > width) {
					height = source.getHeight() * width / source.getWidth();
				} else {
					width = source.getWidth();
					height = source.getHeight();
				}
			} else if (source.getHeight() > height) {
				width = source.getWidth() * height / source.getHeight();
			} else {
				width = source.getWidth();
				height = source.getHeight();
			}
			double hx = height / source.getHeight();
			double wy = width / source.getWidth();
			if (hx <= wy) {
				wy = hx;
				width = (int) (source.getWidth() * wy);
			} else {
				hx = wy;
				height = (int) (source.getHeight() * hx);
			}

			int type = source.getType();
			BufferedImage target = null;
			if (type == BufferedImage.TYPE_CUSTOM) {
				ColorModel cm = source.getColorModel();
				WritableRaster raster = cm.createCompatibleWritableRaster(
						width, height);

				boolean alphaPremultiplied = cm.isAlphaPremultiplied();
				target = new BufferedImage(cm, raster, alphaPremultiplied, null);
			} else {
				target = new BufferedImage(width, height, type);
			}
			Graphics2D g = target.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			g.drawRenderedImage(source,
					AffineTransform.getScaleInstance(wy, hx));
			g.dispose();
			ImageIO.write(target, "JPEG", outPutFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 利用Thumbnailator生成缩略图
	 * 
	 * @author liuyuanxian
	 */
	public void createThumbImage() {
		// asBufferedImage() 返回BufferedImage
		try {
			BufferedImage thumbnail = Thumbnails.of(inPutFile)
					.size(width, height).keepAspectRatio(proportion)
					.asBufferedImage();
			ImageIO.write(thumbnail, "jpg", outPutFile);
		} catch (IOException e) {
			e.printStackTrace();
			/*try {
				ThumbnailConvert tc = new ThumbnailConvert();
				tc.setCMYK_COMMAND(inPutFile.getPath());
				Image image = null;
				image = Toolkit.getDefaultToolkit().getImage(inPutFile.getPath());
				MediaTracker mediaTracker = new MediaTracker(new Container());
				mediaTracker.addImage(image, 0);
				mediaTracker.waitForID(0);
				image.getWidth(null);
				image.getHeight(null);
				System.out.println(image.getGraphics());
			} catch (Exception e1) {
				e1.printStackTrace();
			}*/
		}

	}

	public static void main(String[] args) {
		new ThumbUtil(new File("D:\\IMG_4531.JPG"),
				new File("D:\\temp\\2.jpg"), 100, 100).createThumbImage();
	}
}
