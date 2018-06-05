package math;

import inf.v3d.obj.Circle;
import inf.v3d.obj.Line;
import inf.v3d.obj.Text;
import inf.v3d.obj.Polyline;
import inf.v3d.obj.Polygons;
import inf.v3d.obj.PolygonOutlines;
import inf.v3d.view.Viewer;

import java.awt.Color;

/**
 * Class for plotting two dimensional graphs.
 * 
 * @author Murat Artim
 * 
 */
public class GraphPlot2D {

	/**
	 * The writing tolerance. Absolute values smaller than this value will be
	 * considered as zero.
	 */
	private static final double tolerance_ = 1.00E-10;

	/** Static values for the height and width of the plot. */
	private static final double height_ = 10.0, width_ = 20.0;

	/** Static variable for the radii of spheres used for representing points. */
	private static final double radius_ = height_ / 100.0;

	/** Static variable for the text height. */
	private static final double textHeight_ = 2.0 * radius_;

	/** Static variable for the text position. */
	private static final double textPosition_ = textHeight_;

	/** Colors of points, lines and background of the plot. */
	private Color pointColor_ = Color.orange, lineColor_ = Color.black,
			backgroundColor_ = Color.lightGray;

	/** X and y value arrays of the plotter. */
	private double[] xVal_, yVal_;

	/** Labels for the x and y values. */
	private String xLabel_ = "", yLabel_ = "";

	/**
	 * Boolean values for drawing guidance lines, data labels for x and y values
	 * and points
	 */
	private boolean guidanceLines_ = true, dataLabelX_ = false,
			dataLabelY_ = false, point_ = true;

	/**
	 * Sets x and y value arrays to plotter.
	 * 
	 * @param xVal
	 *            Array storing the x values.
	 * @param yVal
	 *            Array storing the y values.
	 */
	public void setValues(double[] xVal, double[] yVal) {

		// check dimensions
		if (xVal.length == 0 || yVal.length == 0)
			exceptionHandler("Illegal dimensions for plot values!");
		if (xVal.length != yVal.length)
			exceptionHandler("Illegal dimensions for plot values!");

		// set values
		xVal_ = xVal;
		yVal_ = yVal;
	}

	/**
	 * Sets x and y value labels of the plot.
	 * 
	 * @param xLabel
	 *            X value label.
	 * @param yLabel
	 *            Y value label.
	 */
	public void setLabels(String xLabel, String yLabel) {
		xLabel_ = xLabel;
		yLabel_ = yLabel;
	}

	/**
	 * Sets guidance lines to plot.
	 * 
	 * @param arg0
	 *            True if guidance lines demanded, False vice versa.
	 */
	public void setGuidanceLines(boolean arg0) {
		guidanceLines_ = arg0;
	}

	/**
	 * Sets data labels for x values of the plot.
	 * 
	 * @param arg0
	 *            True if x data labels are demanded, False vice versa.
	 */
	public void setDataLabelX(boolean arg0) {
		dataLabelX_ = arg0;
	}

	/**
	 * Sets data labels for y values of the plot.
	 * 
	 * @param arg0
	 *            True if y data labels are demanded, False vice versa.
	 */
	public void setDataLabelY(boolean arg0) {
		dataLabelY_ = arg0;
	}

	/**
	 * Sets points visible or not.
	 * 
	 * @param arg0
	 *            True if visible, False vice versa.
	 */
	public void setPointsVisible(boolean arg0) {
		point_ = arg0;
	}

	/**
	 * Sets color for points of the plot.
	 * 
	 * @param color
	 *            The color to be set.
	 */
	public void setPointColor(Color color) {
		pointColor_ = color;
	}

	/**
	 * Sets color for lines of the plot.
	 * 
	 * @param color
	 *            The color to be set.
	 */
	public void setLineColor(Color color) {
		lineColor_ = color;
	}

	/**
	 * Sets color for background of the plot.
	 * 
	 * @param color
	 *            The color to be set.
	 */
	public void setBackgroundColor(Color color) {
		backgroundColor_ = color;
	}

	/**
	 * Returns x values array of plot.
	 * 
	 * @return X values array.
	 */
	public double[] getXValues() {
		return xVal_;
	}

	/**
	 * Returns y values array of plot.
	 * 
	 * @return Y values array.
	 */
	public double[] getYValues() {
		return yVal_;
	}

	/**
	 * Returns x value label of the plot.
	 * 
	 * @return The x value label of the plot.
	 */
	public String getXLabel() {
		return xLabel_;
	}

	/**
	 * Returns y value label of the plot.
	 * 
	 * @return The y value label of the plot.
	 */
	public String getYLabel() {
		return yLabel_;
	}

	/**
	 * Returns color of the points in the plot.
	 * 
	 * @return The color of the points.
	 */
	public Color getPointColor() {
		return pointColor_;
	}

	/**
	 * Returns color of the lines in the plot.
	 * 
	 * @return The color of the lines.
	 */
	public Color getLineColor() {
		return lineColor_;
	}

	/**
	 * Returns background color of the plot.
	 * 
	 * @return Background color of the plot.
	 */
	public Color getBackgroundColor() {
		return backgroundColor_;
	}

	/**
	 * Returns True if guidance lines are set, False vice versa.
	 * 
	 * @return True if guidance lines are set, False vice versa.
	 */
	public boolean areGuidanceLinesSet() {
		return guidanceLines_;
	}

	/**
	 * Returns True if data labels for x values are set, False vice versa.
	 * 
	 * @return True if data labels for x values are set, False vice versa.
	 */
	public boolean areDataLabelsXSet() {
		return dataLabelX_;
	}

	/**
	 * Returns True if data labels for y values are set, False vice versa.
	 * 
	 * @return True if data labels for y values are set, False vice versa.
	 */
	public boolean areDataLabelsYSet() {
		return dataLabelY_;
	}

	/**
	 * Returns True if points are visible, False vice versa.
	 * 
	 * @return True if points are visible, False vice versa.
	 */
	public boolean arePointsVisible() {
		return point_;
	}

	/**
	 * Plots the two dimensional graph.
	 * 
	 * @param viewer
	 *            The viewer to be plotted.
	 */
	public void plot(Viewer viewer) {

		// clear viewer
		viewer.clear();

		// compute minimum values
		double minValX = MathUtil.minVal(xVal_);
		double minValY = MathUtil.minVal(yVal_);

		// compute maximum values
		double maxValX = MathUtil.maxVal(xVal_);
		double maxValY = MathUtil.maxVal(yVal_);

		// compute the maximum differences
		double maxDifX = Math.abs(maxValX - minValX);
		double maxDifY = Math.abs(maxValY - minValY);

		// get colors of points and lines
		double rp = pointColor_.getRed() / 255.0;
		double gp = pointColor_.getGreen() / 255.0;
		double bp = pointColor_.getBlue() / 255.0;
		double rl = lineColor_.getRed() / 255.0;
		double gl = lineColor_.getGreen() / 255.0;
		double bl = lineColor_.getBlue() / 255.0;

		// create Polyline for drawing lines
		Polyline line = new Polyline();

		// loop over array values
		for (int i = 0; i < xVal_.length; i++) {

			// compute current differences
			double currDifX = Math.abs(xVal_[i] - minValX);
			double currDifY = Math.abs(yVal_[i] - minValY);

			// compute coordinates of the point
			double x = width_ * 0.5;
			double y = height_ * 0.5;
			if (maxDifX != 0.0)
				x = (currDifX * width_ * 0.8) / maxDifX + width_ * 0.1;
			if (maxDifY != 0.0)
				y = (currDifY * height_ * 0.8) / maxDifY + height_ * 0.1;

			// draw circle
			if (point_) {
				Circle point = new Circle(radius_);
				point.setCenter(x, y, 0.0);

				// set color
				point.setColor(rp, gp, bp);
			}

			// draw data labels
			if (dataLabelX_ || dataLabelY_)
				drawDataLabels(i, x, y);

			// draw line
			line.addVertex(x, y, 0.0);

			// set color
			line.setColor(rl, gl, bl);
		}

		// draw frame for graph
		drawFrame(minValX, maxDifX, minValY, maxDifY);
	}

	/**
	 * Draws frame for the plot.
	 * 
	 */
	private void drawFrame(double minValX, double maxDifX, double minValY,
			double maxDifY) {

		// create polygon
		Polygons poly = new Polygons();

		// set color
		double r = backgroundColor_.getRed() / 255.0;
		double g = backgroundColor_.getGreen() / 255.0;
		double b = backgroundColor_.getBlue() / 255.0;
		poly.setColored(false);
		poly.setColor(r, g, b);

		// set opacity
		poly.setOpacity(0.4);

		// set outline
		PolygonOutlines outlines = new PolygonOutlines();
		outlines.setPolygons(poly);

		// insert cells
		poly.insertNextCell(4);
		poly.insertCellPoint(0.0, 0.0, 0.0, 0.0);
		poly.insertCellPoint(width_, 0.0, 0.0, 0.0);
		poly.insertCellPoint(width_, height_, 0.0, 0.0);
		poly.insertCellPoint(0.0, height_, 0.0, 0.0);

		// draw major ticks
		for (int i = 0; i < 11; i++) {

			// draw in x direction
			double xx = i * width_ / 10.0;
			double yx = -height_ / 50.0;
			Line tickXd = new Line(xx, 0, 0, xx, yx, 0);
			Line tickXu = new Line(xx, height_, 0, xx, height_ - yx, 0);

			// draw in y direction
			double xy = -width_ / 100.0;
			double yy = i * height_ / 10.0;
			Line tickYl = new Line(0, yy, 0, xy, yy, 0);
			Line tickYr = new Line(width_, yy, 0, width_ - xy, yy, 0);

			// set color
			tickXd.setColor("black");
			tickXu.setColor("black");
			tickYl.setColor("black");
			tickYr.setColor("black");

			// compute x and y values
			String xVal = formatter(minValX + 0.125 * maxDifX * (i - 1));
			String yVal = formatter(minValY + 0.125 * maxDifY * (i - 1));

			// draw values in x and y direction
			Text labelXd = new Text(xVal);
			Text labelXu = new Text(xVal);
			Text labelYl = new Text(yVal);
			Text labelYr = new Text(yVal);

			// set text heights
			labelXd.setHeight(textHeight_);
			labelXu.setHeight(textHeight_);
			labelYl.setHeight(textHeight_);
			labelYr.setHeight(textHeight_);

			// set origins
			labelXd.setOrigin(xx - 3 * textPosition_, yx - 2 * textPosition_,
					0.0);
			labelXu.setOrigin(xx - 3 * textPosition_, height_ - yx
					+ textPosition_, 0.0);
			labelYl.setOrigin(xy - 8 * textPosition_, yy - 0.5 * textPosition_,
					0.0);
			labelYr.setOrigin(width_ - xy + textPosition_, yy - 0.5
					* textPosition_, 0.0);

			// set color
			labelXd.setColor("black");
			labelXu.setColor("black");
			labelYl.setColor("black");
			labelYr.setColor("black");

			// draw labels
			if (i == 5) {

				// draw labels
				Text axislabelXd = new Text(xLabel_);
				Text axislabelXu = new Text(xLabel_);
				Text axislabelYl = new Text(yLabel_);
				Text axislabelYr = new Text(yLabel_);

				// set text heights
				axislabelXd.setHeight(2 * textHeight_);
				axislabelXu.setHeight(2 * textHeight_);
				axislabelYl.setHeight(2 * textHeight_);
				axislabelYr.setHeight(2 * textHeight_);

				// set origins
				axislabelXd.setOrigin(xx - 2 * textPosition_, yx - 5
						* textPosition_, 0.0);
				axislabelXu.setOrigin(xx - 2 * textPosition_, height_ - yx + 4
						* textPosition_, 0.0);
				axislabelYl.setOrigin(xy - 19 * textPosition_, yy - 0.5
						* textPosition_, 0.0);
				axislabelYr.setOrigin(width_ - xy + 9 * textPosition_
						+ textPosition_, yy - 0.5 * textPosition_, 0.0);

				// set color
				axislabelXd.setColor("black");
				axislabelXu.setColor("black");
				axislabelYl.setColor("black");
				axislabelYr.setColor("black");
			}

			// draw guidance lines
			if (guidanceLines_) {

				// check if first or last index
				if (i != 0 && i != 10) {

					// draw
					Line guidanceX = new Line(xx, 0, 0, xx, height_, 0);
					Line guidanceY = new Line(0, yy, 0, width_, yy, 0);

					// set color
					guidanceX.setColor("gray");
					guidanceY.setColor("gray");

					// set opacity
					guidanceX.setOpacity(0.4);
					guidanceY.setOpacity(0.4);
				}
			}
		}
	}

	/**
	 * Draws data labels.
	 * 
	 * @param i
	 *            The value index.
	 * @param x
	 *            X coordinate of point.
	 * @param y
	 *            Y coordinate of point.
	 */
	private void drawDataLabels(int i, double x, double y) {

		// create text
		String text = "(";
		if (dataLabelX_)
			text += formatter(xVal_[i]);
		if (dataLabelY_) {
			if (dataLabelX_)
				text += ", " + formatter(yVal_[i]);
			else
				text += formatter(yVal_[i]);
		}
		text += ")";
		Text label = new Text(text);

		// set text height
		label.setHeight(textHeight_);

		// set position
		double xt = x + textPosition_;
		double yt = y + textPosition_;
		label.setOrigin(xt, yt, 0.0);

		// set color
		label.setColor("black");
	}

	/**
	 * Formats given real number to scientific form and returns string.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return The formatted string.
	 */
	private String formatter(double number) {

		// check number
		if (Math.abs(number) < tolerance_)
			number = 0.0;

		// format number
		String value = String.format("%." + 2 + "E", number);
		if (value.length() == 9)
			value = String.format("%." + 1 + "E", number);
		else if (value.length() == 10)
			value = String.format("%." + "E", number);

		// return formatted value
		return value;
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param arg0
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String arg0) {

		// throw exception with the related message
		throw new IllegalArgumentException(arg0);
	}
}
