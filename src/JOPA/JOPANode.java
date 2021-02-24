package JOPA;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class JOPANode {

	final int headerHeight = 20;
	final int pointRadius = 5;

	protected Rectangle rect;
	protected String header;
	protected String command;
	protected JOPAFormula formula;

	public JOPANode(Rectangle rect, String header, String command, String formula) {
		this.rect = rect;
		this.header = header;
		this.command = command;
		try {
			this.formula = new JOPAFormula(formula);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			this.formula = null;
		}
	}

	public void draw(Graphics2D g, boolean isSelected) {
		if (isSelected) {
			g.setColor(Color.CYAN);
			g.fillRect(rect.x, rect.y, rect.width, headerHeight);
		}
		g.setColor(Color.BLACK);
		g.drawRect(rect.x, rect.y, rect.width, headerHeight);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.drawString(header, rect.x, rect.y + headerHeight);
		g.drawString(command, rect.x, rect.y + headerHeight * 2);
		if (formula != null) {
			FontRenderContext frc = g.getFontRenderContext();
			Font font = g.getFont();
			int inputsCount = formula.inputs.length;
			int outputsCount = formula.outputs.length;
			float inputsStep = (rect.height - headerHeight) / (float) (inputsCount + 1);
			float outputsStep = (rect.height - headerHeight) / (float) (outputsCount + 1);
			for (int i = 0; i < inputsCount; i++) {
				float h = rect.y + headerHeight + (i + 1) * inputsStep;
				g.drawOval((int) (rect.x - pointRadius), (int) (h - pointRadius), (int) (pointRadius * 2),
						(int) (pointRadius * 2));
				String inputName = formula.inputs[i];
				Rectangle2D r = font.getStringBounds(inputName, frc);
				g.drawString(inputName, (int) (rect.x - r.getWidth() - pointRadius * 2), (int) (h + pointRadius));
			}
			for (int i = 0; i < outputsCount; i++) {
				float h = rect.y + headerHeight + (i + 1) * outputsStep;
				g.drawOval((int) (rect.x + rect.width - pointRadius), (int) (h - pointRadius), (int) (pointRadius * 2),
						(int) (pointRadius * 2));
				String outputName = formula.outputs[i];
				g.drawString(outputName, (int) (rect.x + rect.width + pointRadius * 2), (int) (h + pointRadius));
			}
		}
	}

}