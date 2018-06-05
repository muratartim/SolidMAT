package visualize;

import node.Node;
import main.SolidMAT;
import matrix.DVec;
import analysis.Structure;
import inf.v3d.obj.BoundingBox;
import inf.v3d.obj.Sphere;
import java.util.Locale;
import math.MathUtil;

/**
 * Class for drawing node results for the post-visualizer.
 * 
 * @author Murat
 * 
 */
public class PostNodeDisplay {

	/** Static variable for drawing undeformed or deformed shape of structure. */
	public static final int undeformed_ = 0, deformed_ = 1;

	/** Static variable for drawing coordinate system. */
	private static final int global_ = 0, local_ = 1;

	/** The structure to be drawn. */
	private Structure structure_;

	/** The coordinate system for drawing numerical values. */
	private int coordinateSystem_ = PostNodeDisplay.global_;

	/** The scaled values of the drawing. */
	private double radius_, dispScale_, maxVal_, minVal_, maxDisplacement_;

	/**
	 * Scaling factor used for scaling displacements when automatic scaling is
	 * not used.
	 */
	private Double scale_ = null;

	/**
	 * Creates PostNodeDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 * @param maxDisplacement
	 *            The maximum displacement of nodes.
	 */
	public PostNodeDisplay(Structure structure, double maxDistance,
			double minDistance, double maxDisplacement, double[] factors) {

		// set structure
		structure_ = structure;

		// set maximum displacement
		maxDisplacement_ = maxDisplacement;

		// compute scaled values
		computeScaledValues(maxDistance, minDistance, factors);
	}

	/**
	 * Sets coordinate system for drawing numerical values.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of drawing.
	 */
	protected void setCoordinateSystem(int coordinateSystem) {
		coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Sets factor for scaling nodal displacements.
	 * 
	 * @param scale
	 *            The scaling factor.
	 */
	protected void setScalingFactor(Double scale) {
		scale_ = scale;
	}

	/**
	 * Draws node results for post visualizer.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param shape
	 *            Parameter for drawing deformed or undeformed shape.
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of the result to be drawn.
	 * @param isScaled
	 *            True if displacements are scaled, false if not.
	 */
	protected void draw(SolidMAT owner, int shape, int option, int[] comp,
			boolean isScaled) {

		// compute nodal values
		double[] values = computeValues(option, comp);

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// get position vector of node
			DVec pos = node.getPosition();

			// deformed shape demanded
			if (shape == PostNodeDisplay.deformed_) {

				// get unknown vector in global coordinates
				DVec disp = node.getUnknown(Node.global_);

				// get displacements
				disp = disp.getSubVector(0, 2);

				// scale displacements if demanded
				if (isScaled)
					if (maxDisplacement_ != 0.0) {
						if (scale_ != null)
							disp = disp.scale(scale_);
						else
							disp = disp.scale(dispScale_ / maxDisplacement_);
					}

				// add displacements to position vector
				pos = pos.add(disp);
			}

			// draw result
			drawResult(pos, values[i]);
		}

		// draw contour scalor
		drawContourScalor(owner, checkOption(option, comp));
	}

	/**
	 * Draws contour scalor for numerical results.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param option
	 *            The result option to be displayed.
	 * 
	 */
	private void drawContourScalor(SolidMAT owner, String option) {

		// set values array
		String[] values = { formatter(minVal_),
				formatter((maxVal_ + minVal_) / 2.0), formatter(maxVal_) };

		// set contour scalor
		owner.setContourScalor(true, option, ContourScalor.result_, values);
	}

	/**
	 * Draws nodes as sphere.
	 * 
	 * @param pos
	 *            The position vector of node.
	 * @param val
	 *            The result value.
	 */
	private void drawResult(DVec pos, double val) {

		// create sphere
		Sphere sphere = new Sphere();

		// set center
		double x = pos.get(0);
		double y = pos.get(1);
		double z = pos.get(2);
		sphere.setCenter(x, y, z);

		// set radius
		sphere.setRadius(radius_);

		// create bounding box
		BoundingBox box = new BoundingBox(sphere);

		// unregister sphere
		sphere.unregister();

		// set color
		box.setColor(getColor(val));
	}

	/**
	 * Returns the color related with the result value.
	 * 
	 * @param val
	 *            The result value.
	 * @return Color name.
	 */
	private String getColor(double val) {

		// all values equal
		if (minVal_ == maxVal_)
			return "blue";

		// compute stationary points
		double two = minVal_ + 0.2 * (maxVal_ - minVal_);
		double three = minVal_ + 0.4 * (maxVal_ - minVal_);
		double four = minVal_ + 0.6 * (maxVal_ - minVal_);
		double five = minVal_ + 0.8 * (maxVal_ - minVal_);

		// blue
		if (minVal_ <= val && val < two)
			return "blue";

		// cyan
		else if (two <= val && val < three)
			return "cyan";

		// green
		else if (three <= val && val < four)
			return "green";

		// yellow
		else if (four <= val && val < five)
			return "yellow";

		else
			return "red";
	}

	/**
	 * Computes the demanded result values and the extremes for the graphics.
	 * 
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of demanded result to be visualized.
	 */
	private double[] computeValues(int option, int[] comp) {

		// get number of nodes
		int nn = structure_.getNumberOfNodes();

		// initialize values array
		double[] val = new double[nn];

		// loop over nodes
		for (int i = 0; i < nn; i++) {

			// get node
			Node node = structure_.getNode(i);

			// displacements
			if (option == PostVisualizer.nodalDisp_) {

				// resultant displacement
				if (comp[0] == 6)
					val[i] = node.getUnknown(coordinateSystem_).getSubVector(0,
							2).l2Norm();

				// resultant rotation
				else if (comp[0] == 7)
					val[i] = node.getUnknown(coordinateSystem_).getSubVector(3,
							5).l2Norm();

				// componet
				else
					val[i] = node.getUnknown(coordinateSystem_).get(comp[0]);
			}

			// reaction forces
			else if (option == PostVisualizer.reactionForces_) {

				// resultant force
				if (comp[0] == 6)
					val[i] = node.getReactionForce(coordinateSystem_)
							.getSubVector(0, 2).l2Norm();

				// resultant moment
				else if (comp[0] == 7)
					val[i] = node.getReactionForce(coordinateSystem_)
							.getSubVector(3, 5).l2Norm();

				// componet
				else
					val[i] = node.getReactionForce(coordinateSystem_).get(
							comp[0]);
			}
		}

		// assign maximum and minimum values
		maxVal_ = MathUtil.maxVal(val);
		minVal_ = MathUtil.minVal(val);

		// return the computed values array
		return val;
	}

	/**
	 * Computes scaled values of the drawing.
	 * 
	 * @param minDistance
	 *            The minimum distance between nodes.
	 * @param maxDistance
	 *            The maximum distnace between nodes.
	 */
	private void computeScaledValues(double maxDistance, double minDistance,
			double[] factors) {

		// compute drawing dependent scale
		double scale = minDistance / 70.0 + maxDistance / 1000.0;

		// compute radius of node
		radius_ = scale * factors[0];

		// compute displacement scale
		dispScale_ = 10.0 * scale;
	}

	/**
	 * Formats given real number to scientific form and returns string.
	 * 
	 * @param number
	 *            The number to be formatted.
	 * @return The formatted string.
	 */
	private String formatter(double number) {

		// format number
		String value = String.format(Locale.US, "%." + 2 + "E", number);
		if (value.length() == 9)
			value = String.format(Locale.US, "%." + 1 + "E", number);
		else if (value.length() == 10)
			value = String.format(Locale.US, "%." + "E", number);

		// return formatted value
		return value;
	}

	/**
	 * Checks result option.
	 * 
	 * @param option
	 *            The result option to be drawn.
	 * @param comp
	 *            The component of the result to be displayed.
	 * @return Result option if available, null vice versa.
	 */
	private String checkOption(int option, int[] comp) {

		// initialize string to be returned
		String val = null;

		// set option
		switch (option) {

		// displacements
		case PostVisualizer.nodalDisp_: {

			// set option
			val = "Nodal ";

			// set component
			switch (comp[0]) {
			case 0: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "displacement 1";
				else
					val += "displacement x";
				break;
			}
			case 1: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "displacement 2";
				else
					val += "displacement y";
				break;
			}
			case 2: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "displacement 3";
				else
					val += "displacement z";
				break;
			}
			case 3: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "rotation 1";
				else
					val += "rotation x";
				break;
			}
			case 4: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "rotation 2";
				else
					val += "rotation y";
				break;
			}
			case 5: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "rotation 3";
				else
					val += "rotation z";
				break;
			}
			case 6: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "resultant displacement (local)";
				else
					val += "resultant displacement (global)";
				break;
			}
			case 7: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "resultant rotation (local)";
				else
					val += "resultant rotation (global)";
				break;
			}
			}

			// return value
			return val;
		}

			// reaction forces
		case PostVisualizer.reactionForces_: {

			// set option
			val = "Nodal ";

			// set component
			switch (comp[0]) {
			case 0: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "force 1";
				else
					val += "force x";
				break;
			}
			case 1: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "force 2";
				else
					val += "force y";
				break;
			}
			case 2: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "force 3";
				else
					val += "force z";
				break;
			}
			case 3: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "moment 1";
				else
					val += "moment x";
				break;
			}
			case 4: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "moment 2";
				else
					val += "moment y";
				break;
			}
			case 5: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "moment 3";
				else
					val += "moment z";
				break;
			}
			case 6: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "resultant force (local)";
				else
					val += "resultant force (global)";
				break;
			}
			case 7: {
				if (coordinateSystem_ == PostNodeDisplay.local_)
					val += "resultant moment (local)";
				else
					val += "resultant moment (global)";
				break;
			}
			}

			// return value
			return val;
		}

			// no option
		default:
			return val;
		}
	}
}
