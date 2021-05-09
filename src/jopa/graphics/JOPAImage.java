package jopa.graphics;

import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;

public class JOPAImage {

	public int width;
	public int height;
	public int colorDepth;
	public int handle;
	public ByteBuffer data;

	public JOPAImage(int handle, int width, int height) {
		this.width = width;
		this.height = height;
		this.handle = handle;
	}

	public JOPAImage(String fileName) {
		loadImage(fileName);
	}

	private void loadImage(String path) {
		int[] w = new int[1];
		int[] h = new int[1];
		int[] comp = new int[1];

		data = stbi_load(path, w, h, comp, 3);

		width = w[0];
		height = h[0];
		colorDepth = comp[0];
	}

}