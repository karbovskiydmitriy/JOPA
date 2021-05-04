package jopa.main;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class JOPAGraphics2D extends Graphics2D {

	public float scale = 1.0f;
	public Graphics2D implementation;

	@Override
	public void addRenderingHints(Map<?, ?> hints) {
		implementation.addRenderingHints(hints);
	}

	@Override
	public void clip(Shape s) {
		implementation.clip(s);
	}

	@Override
	public void draw(Shape s) {
		implementation.draw(s);
	}

	@Override
	public void drawGlyphVector(GlyphVector g, float x, float y) {
		implementation.drawGlyphVector(g, x, y);
	}

	@Override
	public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
		return implementation.drawImage(img, xform, obs);
	}

	@Override
	public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
		implementation.drawImage(img, op, x, y);
	}

	@Override
	public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
		implementation.drawRenderableImage(img, xform);
	}

	@Override
	public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
		implementation.drawRenderedImage(img, xform);
	}

	@Override
	public void drawString(String str, int x, int y) {
		implementation.drawString(str, x, y);
	}

	@Override
	public void drawString(String str, float x, float y) {
		implementation.drawString(str, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, int x, int y) {
		implementation.drawString(iterator, x, y);
	}

	@Override
	public void drawString(AttributedCharacterIterator iterator, float x, float y) {
		implementation.drawString(iterator, x, y);
	}

	@Override
	public void fill(Shape s) {
		implementation.fill(s);
	}

	@Override
	public Color getBackground() {
		return implementation.getBackground();
	}

	@Override
	public Composite getComposite() {
		return implementation.getComposite();
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		return implementation.getDeviceConfiguration();
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		return implementation.getFontRenderContext();
	}

	@Override
	public Paint getPaint() {
		return implementation.getPaint();
	}

	@Override
	public Object getRenderingHint(Key hintKey) {
		return implementation.getRenderingHint(hintKey);
	}

	@Override
	public RenderingHints getRenderingHints() {
		return implementation.getRenderingHints();
	}

	@Override
	public Stroke getStroke() {
		return implementation.getStroke();
	}

	@Override
	public AffineTransform getTransform() {
		return implementation.getTransform();
	}

	@Override
	public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
		return implementation.hit(rect, s, onStroke);
	}

	@Override
	public void rotate(double theta) {
		implementation.rotate(theta);
	}

	@Override
	public void rotate(double theta, double x, double y) {
		implementation.rotate(theta, x, y);
	}

	@Override
	public void scale(double sx, double sy) {
		implementation.scale(sx, sy);
	}

	@Override
	public void setBackground(Color color) {
		implementation.setBackground(color);
	}

	@Override
	public void setComposite(Composite comp) {
		implementation.setComposite(comp);
	}

	@Override
	public void setPaint(Paint paint) {
		implementation.setPaint(paint);
	}

	@Override
	public void setRenderingHint(Key hintKey, Object hintValue) {
		implementation.setRenderingHint(hintKey, hintValue);
	}

	@Override
	public void setRenderingHints(Map<?, ?> hints) {
		implementation.setRenderingHints(hints);
	}

	@Override
	public void setStroke(Stroke s) {
		implementation.setStroke(s);
	}

	@Override
	public void setTransform(AffineTransform Tx) {
		implementation.setTransform(Tx);
	}

	@Override
	public void shear(double shx, double shy) {
		implementation.shear(shx, shy);
	}

	@Override
	public void transform(AffineTransform Tx) {
		implementation.transform(Tx);
	}

	@Override
	public void translate(int x, int y) {
		implementation.translate(x, y);
	}

	@Override
	public void translate(double tx, double ty) {
		implementation.translate(tx, ty);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		implementation.clearRect(x, y, width, height);
	}

	@Override
	public void clipRect(int x, int y, int width, int height) {
		implementation.clipRect(x, y, width, height);
	}

	@Override
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		implementation.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public Graphics create() {
		return implementation.create();
	}

	@Override
	public void dispose() {
		implementation.dispose();
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		implementation.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
		return implementation.drawImage(img, x, y, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
		return implementation.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
		return implementation.drawImage(img, x, y, width, height, observer);
	}

	@Override
	public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
		return implementation.drawImage(img, x, y, width, height, bgcolor, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			ImageObserver observer) {
		return implementation.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
	}

	@Override
	public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
			Color bgcolor, ImageObserver observer) {
		return implementation.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		implementation.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(int x, int y, int width, int height) {
		implementation.drawOval(x, y, width, height);
	}

	@Override
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		implementation.drawPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
		implementation.drawPolyline(xPoints, yPoints, nPoints);
	}

	@Override
	public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		implementation.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		implementation.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillOval(int x, int y, int width, int height) {
		implementation.fillOval(x, y, width, height);
	}

	@Override
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		implementation.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public void fillRect(int x, int y, int width, int height) {
		implementation.fillRect(x, y, width, height);
	}

	@Override
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		implementation.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public Shape getClip() {
		return implementation.getClip();
	}

	@Override
	public Rectangle getClipBounds() {
		return implementation.getClipBounds();
	}

	@Override
	public Color getColor() {
		return implementation.getColor();
	}

	@Override
	public Font getFont() {
		return implementation.getFont();
	}

	@Override
	public FontMetrics getFontMetrics(Font arg0) {
		return implementation.getFontMetrics();
	}

	@Override
	public void setClip(Shape clip) {
		implementation.setClip(clip);
	}

	@Override
	public void setClip(int x, int y, int width, int height) {
		implementation.setClip(x, y, width, height);
	}

	@Override
	public void setColor(Color c) {
		implementation.setColor(c);
	}

	@Override
	public void setFont(Font font) {
		implementation.setFont(font);
	}

	@Override
	public void setPaintMode() {
		implementation.setPaintMode();
	}

	@Override
	public void setXORMode(Color c1) {
		implementation.setXORMode(c1);
	}

}