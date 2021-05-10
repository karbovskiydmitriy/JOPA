package jopa.graphics;

import static jopa.util.JOPAOGLUtil.getTextureFormat;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;

public class JOPAImage {

	public int width;
	public int height;
	public int colorDepth;
	public int format;
	public int handle;
	public ByteBuffer data;

	public JOPAImage(int handle, int width, int height, int format) {
		this.width = width;
		this.height = height;
		this.handle = handle;
		this.format = format;
	}

	public JOPAImage(String fileName) {
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
	}

}