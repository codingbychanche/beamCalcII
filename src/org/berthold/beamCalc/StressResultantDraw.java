package org.berthold.beamCalc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Draws the graphical representation from a {@link StressResultantTable}.
 *  
 * @author Berthold
 *
 */

public class StressResultantDraw {

	private String name;
	private Beam beam;
	private StressResultantTable stressResultantsTable;
	private int height_px, width_px, padX_px, padY_px;
	private int y0_px;
	double yMax, yMin, xMax;

	double leftSupportX, rightSupportX;
	
	
	/**
	 * Creates a new image.
	 * 
	 * @param name File name.
	 * @param beam The asociated {@link Beam}- object.
	 * @param stressResultants	The stress resultants to be drawn ({@link StressResultantTable}).
	 * @param height_px	Height of the image in pixels.
	 * @param width_px	The width of the image in pixels.
	 * @param padX_px	Padding in pixels at the left and right side of the image.
	 * @param padY_px	Padding in pixels at the top and bottom of the image.
	 */
	public StressResultantDraw(String name,Beam beam,StressResultantTable stressResultants, int height_px, int width_px, int padX_px,
			int padY_px) {
		super();
		
		this.name=name;
		this.beam=beam;
		this.stressResultantsTable=stressResultants;
		this.height_px = height_px;
		this.width_px = width_px;
		this.padX_px = padX_px;
		this.padY_px = padY_px;

		// Beam
		leftSupportX=beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(0).getDistanceFromLeftEndOfBeam_m();
		rightSupportX=beam.getSupportsSortedByDistanceFromLeftEndOfBeamDesc().get(1).getDistanceFromLeftEndOfBeam_m();
		
		// Constants efining the gfx- window
		y0_px = height_px / 2;
		yMax = stressResultantsTable.getAbsMax();
		yMin =  stressResultantsTable.getAbsMin();
		xMax =  stressResultantsTable.getAbsMaxX();
		
		System.out.println("YM "+this.yMax+" YMIN"+ this.yMin+"   XM "+this.xMax+ "    y0="+this.y0_px);
	}

	public void draw() {
		try {
			// Create an in memory Image
			BufferedImage img = new BufferedImage(width_px,height_px, BufferedImage.TYPE_INT_ARGB);

			// Grab the graphics object off the image
			Graphics2D graphics = img.createGraphics();
			
			Color color = new Color(255, 255, 255);
			graphics.setBackground(color);

			color = new Color(50, 50, 50);
			Stroke stroke = new BasicStroke(1f);
			graphics.setStroke(stroke);
			graphics.setColor(color);
			
			// Datum
			graphics.drawLine(padX_px,y0_px,width_px-padX_px,y0_px);
			
			// Supports
			int xTLeft=(int)getXT(leftSupportX);
			int xTRight=(int)getXT(rightSupportX);
			graphics.drawLine(xTLeft,(int)padY_px,xTLeft,(int)height_px-padY_px);
			graphics.drawLine(xTRight,(int)padY_px,xTRight,(int)height_px-padY_px);
			
			// Draw stress resultants.
			for (StressResultant r: stressResultantsTable.sfValues) {
				// Stress resultant
				color = new Color(200,0,0);
				graphics.setColor(color);
				
				// Transform
				double y=r.getShearingForce();
				double x=r.getX_m();
				
				graphics.drawLine((int)getXT(x),(int) getYT(y),(int) getXT(x),(int) getYT(y));
				
				if (r.isDiscontiunuity()) {
					color = new Color(0,0,0);
					graphics.setColor(color);
					graphics.drawLine((int)getXT(x),padY_px,(int)getXT(x),height_px-padY_px);
					
					graphics.drawString(r.getShearingForce()+" --",(int)getXT(x),(int)getYT(y));
				}
			}
			
			// Save to file.
			File outputfile = new File(name+".png");
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e) {
		}
	}

	/**
	 * Transforms a y- koordinate.
	 * 
	 * @param y y- koordinate
	 * @return Transformed y- koordinate.
	 */
	private double getYT(double y) {

		if (y > 0)
			return y0_px - ((y0_px - padY_px) / yMax) * y;
		if (y < 0)
			return y0_px + ((height_px - padY_px - y0_px) / yMin) * y;

		return (double) y0_px;
	}

	/**
	 * Transforms a x- koordinate.
	 * 
	 * @param x
	 * @return Transformed x- koordinate.
	 */
	private double getXT(double x) {
		return x * ((width_px - 2 * padX_px) / xMax) + padX_px;
	}
}