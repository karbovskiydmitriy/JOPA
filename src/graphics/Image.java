package graphics;

import static org.lwjgl.stb.STBImage.stbi_load;
import static util.OGLUtil.getTextureFormat;
import static util.OGLUtil.getTextureFormatText;

import java.nio.ByteBuffer;

public class Image {

	public int width;
	public int height;
	public int colorDepth;
	public int format;
	public String formatText;
	public int handle;
	public ByteBuffer data;

	public Image(int handle, int width, int height, int format) {
		this.width = width;
		this.height = height;
		this.handle = handle;
		this.format = format;
	}

	public Image(String fileName) {
		loadImage(fileName);
	}

	private void loadImage(String path) {
		int[] w = new int[1];
		int[] h = new int[1];
		int[] depth = new int[1];

		data = stbi_load(path, w, h, depth, 3);

		width = w[0];
		height = h[0];
		colorDepth = depth[0];
		format = getTextureFormat(colorDepth / 8, byte.class, false);
		formatText = getTextureFormatText(format);
	}

}