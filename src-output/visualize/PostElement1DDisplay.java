package visualize;

import java.util.Vector;
import java.util.Locale;

import node.Node;

import main.SolidMAT;
import math.MathUtil;
import matrix.DMat;
import matrix.DVec;

import analysis.Structure;

import inf.v3d.obj.Cylinder;
import inf.v3d.obj.Polygons;

import element.Element;
import element.Element1D;
import element.ElementLibrary;

/**
 * Class for drawing 1D element results for the post-visualizer.
 * 
 * @author Murat
 * 
 */
public class PostElement1DDisplay {

	/** Static variable for the number of stationary points. */
	private static final int numberOfStations_ = 50;

	/** Polygons used to draw the result diagrams. */
	private Polygons polys_ = new Polygons();

	/** The structure to be drawn. */
	private Structure structure_;

	/** Vector for storing demanded result values of all elements. */
	private Vector<double[]> values_;

	/** Maximum and minimum values for scaling graphics. */
	private double maxDisp_, maxVal_ = -Math.pow(10, 8), minVal_ = Math.pow(10,
			8), elMax_ = -Math.pow(10, 8), elMin_ = Math.pow(10, 8);

	/** The scaled values of the drawing. */
	private double radius_, diagramHeight_, dispScale_;

	/**
	 * Scaling factor used for scaling displacements when automatic scaling is
	 * not used.
	 */
	private Double scale_ = null;

	/**
	 * Creates PostElement1DDisplay object.
	 * 
	 * @param structure
	 *            The structure to be drawn.
	 * @param maxDistance
	 *            The maximum distance of the structure.
	 * @param minDistance
	 *            The minimum distance of the structure.
	 * @param maxDisplacement
	 *            The maximum displacement of the structure.
	 */
	public PostElement1DDisplay(Structure structure, double maxDistance,
			double minDistance, double maxDisplacement, double[] factors) {

		// set structure
		structure_ = structure;

		// set maximum displacement
		maxDisp_ = maxDisplacement;

		// compute scaled values
		computeScaledValues(maxDistance, minDistance, factors);
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
	 * Draws one dimensional elements and related results.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param shape
	 *            Parameter for drawing deformed or undeformed shape.
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of demanded result to be visualized.
	 * @param isScaled
	 *            True if displacements are scaled, false if not.
	 */
	protected void draw(SolidMAT owner, int shape, int option, int[] comp,
			boolean isScaled) {

		// get result option
		String opt = checkOption(option, comp);

		// compute demanded result values and extremes
		if (opt != null) {

			// compute nodal values
			computeValues(option, comp);

			// compute average values
			if (option != PostVisualizer.elementDisp1D_)
				computeAverage();
		}

		// loop over elements
		int k = 0;
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// one dimensional elements
			if (e.getDimension() == ElementLibrary.oneDimensional_) {

				// get one dimensional element
				Element1D e1D = (Element1D) e;

				// get nodes of element
				Node[] nodes = e1D.getNodes();

				// create array for storing nodal position vectors
				DVec[] pos = new DVec[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodes.length; j++)
					pos[j] = nodes[j].getPosition();

				// deformed shape demanded
				if (shape == PostVisualizer.deformed_) {

					// loop over nodes of element
					for (int j = 0; j < nodes.length; j++) {

						// get unknown vector in global coordinates
						DVec disp = nodes[j].getUnknown(Node.global_);

						// get displacements
						disp = disp.getSubVector(0, 2);

						// scale displacements if demanded
						if (isScaled)
							if (maxDisp_ != 0.0) {
								if (scale_ != null)
									disp = disp.scale(scale_);
								else
									disp = disp.scale(dispScale_ / maxDisp_);
							}

						// add displacements to position vector
						pos[j] = pos[j].add(disp);
					}
				}

				// draw element
				drawElement(pos);

				// draw result diagram
				if (opt != null)
					drawDiagram(e1D, k, pos);
				k++;
			}
		}

		// draw contour scalor
		if (opt != null)
			drawContourScalor(owner, opt);

		// create contour colours
		polys_.createColors();

		// delete values vector
		values_ = null;
	}

	/**
	 * Draws one dimensional elements as lines.
	 * 
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 */
	private void drawElement(DVec[] pos) {

		// loop over nodes of element
		for (int i = 0; i < pos.length - 1; i++) {

			// get positions of recent node
			double x1 = pos[i].get(0);
			double y1 = pos[i].get(1);
			double z1 = pos[i].get(2);

			// get positions of next node
			double x2 = pos[i + 1].get(0);
			double y2 = pos[i + 1].get(1);
			double z2 = pos[i + 1].get(2);

			// create cylinder
			Cylinder cylinder = new Cylinder(x1, y1, z1, x2, y2, z2);

			// set radius
			cylinder.setRadius(radius_);

			// set color
			cylinder.setColor("yellow");
		}
	}

	/**
	 * Draws result diagram.
	 * 
	 * @param e1D
	 *            One dimensional element that the diagram will be drawn on.
	 * @param index
	 *            The index of element.
	 * @param pos
	 *            Array storing the nodal position vectors of element.
	 * 
	 */
	private void drawDiagram(Element1D e1D, int index, DVec[] pos) {

		// create array for storing position vectors of stations
		DVec[] posL = new DVec[numberOfStations_];
		for (int i = 0; i < posL.length; i++)
			posL[i] = new DVec(3);

		// compute positions of stationary points
		int ns = posL.length;
		for (int i = 0; i < 3; i++) {

			// get component of starting and end nodes
			double value1 = pos[0].get(i);
			double value2 = pos[pos.length - 1].get(i);

			// loop over stationary points
			for (int j = 0; j < ns; j++) {

				// compute positions of points
				double value = Math.abs(value2 - value1) * j / (ns - 1);
				if (value1 <= value2)
					posL[j].set(i, value1 + value);
				else
					posL[j].set(i, value1 - value);
			}
		}

		// compute absolute maximum value
		double maxVal = Math.max(Math.abs(maxVal_), Math.abs(minVal_));

		// get three dimensional transformation matrix of element
		DMat tr = e1D.getTransformation();

		// draw diagram
		for (int i = 0; i < numberOfStations_ - 1; i++) {

			// compute scaled values of successive stationary points
			double value1 = values_.get(index)[i];
			double value2 = values_.get(index)[i + 1];

			// create vectors in x3 local axis for end points
			DVec vec1 = new DVec(3);
			DVec vec2 = new DVec(3);
			vec1.set(2, diagramHeight_ * value1 / maxVal);
			vec2.set(2, diagramHeight_ * value2 / maxVal);
			vec1 = vec1.transform(tr, DMat.toGlobal_);
			vec2 = vec2.transform(tr, DMat.toGlobal_);

			// compute edge points of polygon
			DVec e1 = posL[i].add(vec1);
			DVec e2 = posL[i + 1].add(vec2);

			// draw polygon
			polys_.insertNextCell(4);
			polys_.insertCellPoint(posL[i].get1DArray(), value1);
			polys_.insertCellPoint(posL[i + 1].get1DArray(), value2);
			polys_.insertCellPoint(e2.get1DArray(), value2);
			polys_.insertCellPoint(e1.get1DArray(), value1);
		}
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
	 * Computes the demanded result values and the extremes for the graphics.
	 * 
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component of demanded result to be visualized.
	 */
	private void computeValues(int option, int[] comp) {

		// initialize values vector
		values_ = new Vector<double[]>();
		DVec vec = new DVec(3);

		// compute stationary points in natural coordinates
		double distance = 2.0 / (numberOfStations_ - 1.0);
		double[] eps1 = new double[numberOfStations_];
		eps1[0] = -1.0;
		for (int j = 0; j < numberOfStations_ - 1; j++)
			eps1[j + 1] = eps1[j] + distance;

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// one dimensional elements
			if (e.getDimension() == ElementLibrary.oneDimensional_) {

				// get one dimensional element
				Element1D e1D = (Element1D) e;

				// compute values of element and set extreme values
				double[] elValues = new double[numberOfStations_];
				for (int j = 0; j < numberOfStations_; j++) {

					// element displacements
					if (option == PostVisualizer.elementDisp1D_) {

						// resultant displacement
						if (comp[0] == 6)
							elValues[j] = e1D
									.getDisplacement(eps1[j], 0.0, 0.0)
									.getSubVector(0, 2).l2Norm();

						// resultant rotation
						else if (comp[0] == 7)
							elValues[j] = e1D
									.getDisplacement(eps1[j], 0.0, 0.0)
									.getSubVector(3, 5).l2Norm();

						// component
						else
							elValues[j] = e1D
									.getDisplacement(eps1[j], 0.0, 0.0).get(
											comp[0]);
					}

					// elastic strain
					else if (option == PostVisualizer.elasticStrains1D_) {

						// get strain tensor
						DMat strain = e1D.getStrain(eps1[j], 0.0, 0.0);

						// normal strain
						if (comp[0] == 3) {
							vec.set(0, strain.get(0, 0));
							vec.set(1, strain.get(1, 1));
							vec.set(2, strain.get(2, 2));
							elValues[j] = vec.l2Norm();
						}

						// shear strain
						else if (comp[0] == 4) {
							vec.set(0, strain.get(0, 1));
							vec.set(1, strain.get(0, 2));
							vec.set(2, strain.get(1, 2));
							elValues[j] = vec.l2Norm();
						}

						// component
						else
							elValues[j] = strain.get(comp[0], comp[1]);
					}

					// stress
					else if (option == PostVisualizer.stresses1D_) {

						// get stress tensor
						DMat stress = e1D.getStress(eps1[j], 0.0, 0.0);

						// normal stress
						if (comp[0] == 3) {
							vec.set(0, stress.get(0, 0));
							vec.set(1, stress.get(1, 1));
							vec.set(2, stress.get(2, 2));
							elValues[j] = vec.l2Norm();
						}

						// shear stress
						else if (comp[0] == 4) {
							vec.set(0, stress.get(0, 1));
							vec.set(1, stress.get(0, 2));
							vec.set(2, stress.get(1, 2));
							elValues[j] = vec.l2Norm();
						}

						// component
						else
							elValues[j] = stress.get(comp[0], comp[1]);
					}

					// internal forces
					else if (option == PostVisualizer.internalForces1D_) {
						elValues[j] = e1D.getInternalForce(comp[0], eps1[j],
								0.0, 0.0);
					}

					// principle strain
					else if (option == PostVisualizer.principalStrains1D_)
						elValues[j] = e1D.getPrincipalStrain(eps1[j], 0.0, 0.0,
								comp[0]);

					// principle stress
					else if (option == PostVisualizer.principalStresses1D_)
						elValues[j] = e1D.getPrincipalStress(eps1[j], 0.0, 0.0,
								comp[0]);

					// Mises stress
					else if (option == PostVisualizer.misesStress1D_)
						elValues[j] = e1D.getVonMisesStress(eps1[j], 0.0, 0.0);

					// assign minimum value
					if (elValues[j] < elMin_)
						elMin_ = elValues[j];

					// assign maximum value
					if (elValues[j] > elMax_)
						elMax_ = elValues[j];
				}

				// set element values to values array
				values_.add(elValues);

				// assign minimum value
				if (elMin_ < minVal_)
					minVal_ = elMin_;

				// assign maximum value
				if (elMax_ > maxVal_)
					maxVal_ = elMax_;
			}
		}
	}

	/**
	 * Computes nodal average for the demanded result values.
	 */
	private void computeAverage() {

		// create vector for storing the node ids of elements
		Vector<int[]> ids = new Vector<int[]>();

		// store node id arrays of every element in a vector
		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// one dimensional elements
			if (e.getDimension() == ElementLibrary.oneDimensional_) {

				// get two dimensional element
				Element1D e1D = (Element1D) e;

				// get nodes of element
				Node[] nodes = e1D.getNodes();

				// initialize ids of nodes
				int[] nodeIds = new int[nodes.length];

				// loop over nodes of element
				for (int j = 0; j < nodeIds.length; j++)
					nodeIds[j] = structure_.indexOfNode(nodes[j]);

				// set element node ids to ids vector
				ids.add(nodeIds);
			}
		}

		// compute and set mean values for each node
		// initialize node ids and node values vectors
		Vector<Integer> nodeIds = new Vector<Integer>();
		Vector<Double> nodeValues = new Vector<Double>();

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// loop over elements
			int k = 0;
			for (int j = 0; j < ids.size(); j++) {

				// get the index of node in the element
				int index = MathUtil.indexOf(ids.get(j), i);

				// set value if contains
				if (index != -1) {

					// increase capacity if necessary
					if (i >= nodeValues.size())
						nodeValues.setSize(i + 1);

					// set value
					Double val = nodeValues.get(i);
					if (val != null)
						nodeValues.set(i, val + values_.get(j)[index]);
					else
						nodeValues.set(i, values_.get(j)[index]);
					k++;
				}
			}

			// compute mean value
			if (k > 0) {
				nodeIds.add(i);
				nodeValues.set(i, nodeValues.get(i) / k);
			}
		}

		// set the mean values to elements
		// loop over node ids
		for (int i = 0; i < nodeIds.size(); i++) {

			// loop over element node ids
			for (int j = 0; j < ids.size(); j++) {

				// check if node is contained
				int index = MathUtil.indexOf(ids.get(j), nodeIds.get(i));
				if (index != -1)
					values_.get(j)[index] = nodeValues.get(nodeIds.get(i));
			}
		}
	}

	/**
	 * Computes scaled values of the drawing.
	 * 
	 * @param maxDistance
	 *            The maximum distnace between nodes.
	 * @param minDistance
	 *            The minimum distance between nodes.
	 */
	private void computeScaledValues(double maxDistance, double minDistance,
			double[] factors) {

		// compute drawing dependent scale
		double scale = minDistance / 70.0 + maxDistance / 1000.0;

		// compute radius of line
		radius_ = 0.25 * scale * factors[1];

		// compute diagram height
		diagramHeight_ = 15.0 * scale * factors[2];

		// compute displacement scale
		dispScale_ = 10.0 * scale;
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

		// element displacements
		case PostVisualizer.elementDisp1D_: {

			// set option
			val = "1D element ";

			// set component
			switch (comp[0]) {
			case 0:
				val += "displacement 1";
				break;
			case 1:
				val += "displacement 2";
				break;
			case 2:
				val += "displacement 3";
				break;
			case 3:
				val += "rotation 1";
				break;
			case 4:
				val += "rotation 2";
				break;
			case 5:
				val += "rotation 3";
				break;
			case 6:
				val += "resultant displacement";
				break;
			case 7:
				val += "resultant rotation";
				break;
			}

			// return value
			return val;
		}

			// elastic strains
		case PostVisualizer.elasticStrains1D_: {

			// set option
			val = "1D element ";

			// normal strain
			if (comp[0] == 3)
				val += "resultant normal strain";

			// shear strain
			else if (comp[0] == 4)
				val += "resultant shear strain";

			// set normal component
			else if (comp[0] == comp[1])
				val += "normal strain " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// set shear component
			else if (comp[0] != comp[1] && comp[0] < 3)
				val += "shear strain " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// return value
			return val;
		}

			// stresses
		case PostVisualizer.stresses1D_: {

			// set option
			val = "1D element ";

			// normal strain
			if (comp[0] == 3)
				val += "resultant normal stress";

			// shear strain
			else if (comp[0] == 4)
				val += "resultant shear stress";

			// set normal component
			else if (comp[0] == comp[1])
				val += "normal stress " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// set shear component
			else if (comp[0] != comp[1] && comp[0] < 3)
				val += "shear stress " + Integer.toString(comp[0] + 1)
						+ Integer.toString(comp[1] + 1);

			// return value
			return val;
		}

			// internal forces
		case PostVisualizer.internalForces1D_: {

			// set option
			val = "1D element ";

			// set component
			switch (comp[0]) {
			case Element1D.N1_:
				val += "axial force";
				break;
			case Element1D.V2_:
				val += "shear force 2";
				break;
			case Element1D.V3_:
				val += "shear force 3";
				break;
			case Element1D.T1_:
				val += "torsion";
				break;
			case Element1D.M2_:
				val += "bending moment 2";
				break;
			case Element1D.M3_:
				val += "bending moment 3";
				break;
			}

			// return value
			return val;
		}

			// principal strains
		case PostVisualizer.principalStrains1D_: {

			// set option
			val = "1D element ";

			// set component
			switch (comp[0]) {
			case Element.minPrincipal_:
				val += "min principal strain";
				break;
			case Element.midPrincipal_:
				val += "mid principal strain";
				break;
			case Element.maxPrincipal_:
				val += "max principal strain";
				break;
			}

			// return value
			return val;
		}

			// principal stresses
		case PostVisualizer.principalStresses1D_: {

			// set option
			val = "1D element ";

			// set component
			switch (comp[0]) {
			case Element.minPrincipal_:
				val += "min principal stress";
				break;
			case Element.midPrincipal_:
				val += "mid principal stress";
				break;
			case Element.maxPrincipal_:
				val += "max principal stress";
				break;
			}

			// return value
			return val;
		}

			// mises stress
		case PostVisualizer.misesStress1D_: {

			// set option
			val = "1D element mises stress";

			// return value
			return val;
		}

			// no option
		default:
			return val;
		}
	}
}
