package visualize;

import java.util.Vector;

import main.SolidMAT;
import matrix.DVec;

import boundary.BoundaryCase;

import analysis.Structure;
import element.Element;
import node.Node;
import data.Group;

import java.awt.Color;

/**
 * Class for visualizing preprocessing information.
 * 
 * @author Murat
 * 
 */
public class PreVisualizer {

	/** Static variable for drawing coordinate system. */
	public static final int global_ = 0, local_ = 1;

	/** Static variable for drawing mechanical loads of 2D/3D elements. */
	public static final int fx_ = 0, fy_ = 1, fz_ = 2, mx_ = 3, my_ = 4,
			mz_ = 5;

	/** The structure to be visualized. */
	private Structure structure_;

	/** Node and node related objects drawer of visualizer. */
	private PreNodeDisplay nodeDisplay_;

	/**
	 * One dimensional elements and element related objects drawer of
	 * visualizer.
	 */
	private PreElement1DDisplay element1DDisplay_;

	/**
	 * Two dimensional elements and element related objects drawer of
	 * visualizer.
	 */
	private PreElement2DDisplay element2DDisplay_;

	/**
	 * Three dimensional elements and element related objects drawer of
	 * visualizer.
	 */
	private PreElement3DDisplay element3DDisplay_;

	/**
	 * Boolean array for storing node display options. The option sequence is;
	 * visibility, labels, restraints, springs, mechanical loads, displacement
	 * loads, masses, local axes, initial displacements, initial velocities,
	 * groups.
	 */
	private boolean[] nodeOptions_ = { false, false, false, false, false,
			false, false, false, false, false, false };

	/**
	 * Double array for storing the visualization option of nodes. The first
	 * three values are the rgb values of the color (which is light gray as
	 * default), the last value is the opacity (which is no-transparent by
	 * default).
	 */
	private double[] nodeVizOptions_ = { 0.752941, 0.752941, 0.752941, 1.0 };

	/**
	 * Boolean array for storing element display options. The option sequence
	 * is; visibility, labels, extrude, mechanical loads, temperature loads,
	 * masses, materials, sections, springs, local axes, element types, groups,
	 * upside down, inside out, zero volume, aspect ratio.
	 */
	private boolean[] elementOptions_ = { true, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false };

	/**
	 * Double array for storing the visualization option of elements. The first
	 * three values are the rgb values of the color (which is light gray as
	 * default), the last value is the opacity (which is no-transparent by
	 * default).
	 */
	private double[] elementVizOptions_ = { 0.752941, 0.752941, 0.752941, 1.0 };

	/** Maximum and minimum distances between nodes. Used for scaling objects. */
	private double maxDistance_ = 0.0, minDistance_ = Math.pow(10, 8);

	/** The boundary cases to be displayed for boundaries. */
	private Vector<BoundaryCase> bCases_ = new Vector<BoundaryCase>();

	/** The coordinate system for drawing numerical values. */
	private int coordinateSystem_ = PreVisualizer.global_;

	/** The component to be displayed fot drawing mechanical loads. */
	private int component_ = PreVisualizer.fx_;

	/** Treshold value for displaying bad aspect ratio elements. */
	private double treshold_ = 0.5;

	/**
	 * Array storing the scaling factors of drawing. The sequence is; radius of
	 * nodes, radius of lines, radius of arrows, length of arrows, text height,
	 * writing tolerance.
	 */
	private double[] factors_ = { 1.0, 1.0, 1.0, 1.0, 1.0, 1.00E-10 };

	/**
	 * Sets structure to pre-visualizer.
	 * 
	 * @param structure
	 *            The structure to be set.
	 */
	public void setStructure(Structure structure) {

		// set structure
		structure_ = structure;

		// compute scaling magnitudes
		computeScalors();

		// create node display object
		nodeDisplay_ = new PreNodeDisplay(structure_, maxDistance_,
				minDistance_, factors_);

		// create one dimensional element display object
		element1DDisplay_ = new PreElement1DDisplay(structure_, maxDistance_,
				minDistance_, factors_);

		// create two dimensional element display object
		element2DDisplay_ = new PreElement2DDisplay(structure_, maxDistance_,
				minDistance_, factors_);

		// create three dimensional element display object
		element3DDisplay_ = new PreElement3DDisplay(structure_, maxDistance_,
				minDistance_, factors_);
	}

	/**
	 * Sets node display options.
	 * 
	 * @param options
	 *            The boolean array for storing the node display options. The
	 *            option sequence is; visibility, labels, constraints, springs,
	 *            mechanical loads, displacement loads, masses, local axes.
	 */
	public void setNodeOptions(boolean[] options) {

		// check dimensions
		if (options.length != nodeOptions_.length)
			exceptionHandler("Illegal number of node options for display!");

		// set options
		nodeOptions_ = options;
	}

	/**
	 * Sets nodal visualization options.
	 * 
	 * @param vizOptions
	 *            Array storing the nodal visualization options. The first three
	 *            values are the rgb values of the color (which is light gray as
	 *            default), the last value is the opacity (which is
	 *            no-transparent by default).
	 */
	public void setNodeVizOptions(double[] vizOptions) {

		// check dimensions
		if (vizOptions.length != nodeVizOptions_.length)
			exceptionHandler("Illegal number of node visualization options for display!");

		// set options
		nodeVizOptions_ = vizOptions;
	}

	/**
	 * Sets element display options.
	 * 
	 * @param options
	 *            The boolean array for storing the element display options. The
	 *            option sequence is; visibility, labels, extrude, mechanical
	 *            loads, temperature loads, masses, materials, sections,
	 *            springs, local axes.
	 */
	public void setElementOptions(boolean[] options) {

		// check dimensions
		if (options.length != elementOptions_.length)
			exceptionHandler("Illegal number of element options for display!");

		// set options
		elementOptions_ = options;
	}

	/**
	 * Sets element visualization options.
	 * 
	 * @param vizOptions
	 *            Array storing the element visualization options. The first
	 *            three values are the rgb values of the color (which is light
	 *            gray as default), the last value is the opacity (which is
	 *            no-transparent by default).
	 */
	public void setElementVizOptions(double[] vizOptions) {

		// check dimensions
		if (vizOptions.length != elementVizOptions_.length)
			exceptionHandler("Illegal number of element visualization options for display!");

		// set options
		elementVizOptions_ = vizOptions;
	}

	/**
	 * Sets coordinate system for drawing numerical values.
	 * 
	 * @param coordinateSystem
	 *            The coordinate system of numerical values.
	 */
	public void setCoordinateSystem(int coordinateSystem) {

		// check coordinate system
		if (coordinateSystem < 0 || coordinateSystem > 1)
			exceptionHandler("Illegal coordinate system for visualizer!");

		// set
		coordinateSystem_ = coordinateSystem;
	}

	/**
	 * Sets component to be displayed for drawing mechanical loads of 2D/3D
	 * elements.
	 * 
	 * @param comp
	 *            The component to be displayed.
	 */
	public void setComponent(int comp) {

		// check component
		if (comp < 0 || comp > 5)
			exceptionHandler("Illegal component for visualizer!");

		// set
		component_ = comp;
	}

	/**
	 * Sets treshold value for displaying bad aspect ratio elements.
	 * 
	 * @param treshold
	 *            Treshold value to be set.
	 */
	public void setTreshold(double treshold) {
		treshold_ = treshold;
	}

	/**
	 * Sets scaling factors array for scaling the objects in the drawing.
	 * 
	 * @param factors
	 *            The scaling factors array to be set.
	 */
	public void setScalingFactors(double[] factors) {
		factors_ = factors;
	}

	/**
	 * Sets boundary cases for drawing boundaries.
	 * 
	 * @param bCases
	 *            The boundary cases to be displayed for boundaries.
	 */
	public void setBoundaryCases(Vector<BoundaryCase> bCases) {
		bCases_ = bCases;
	}

	/**
	 * Returns node display options.
	 * 
	 * @return Node display options.
	 */
	public boolean[] getNodeOptions() {
		return nodeOptions_;
	}

	/**
	 * Returns True if any of the node visibility options are true;
	 * 
	 * @return True if any of the node visibility options are true;
	 */
	public boolean areNodesVisible() {
		for (int i = 0; i < nodeOptions_.length; i++)
			if (nodeOptions_[i])
				return true;
		return false;
	}

	/**
	 * Returns node visualization options.
	 * 
	 * @return Node visualization options.
	 */
	public double[] getNodeVizOptions() {
		return nodeVizOptions_;
	}

	/**
	 * Returns element display options.
	 * 
	 * @return Element display options.
	 */
	public boolean[] getElementOptions() {
		return elementOptions_;
	}

	/**
	 * Returns True if any of the element visibility options are true;
	 * 
	 * @return True if any of the element visibility options are true;
	 */
	public boolean areElementsVisible() {
		for (int i = 0; i < elementOptions_.length; i++)
			if (elementOptions_[i])
				return true;
		return false;
	}

	/**
	 * Returns element visualization options.
	 * 
	 * @return Element visualization options.
	 */
	public double[] getElementVizOptions() {
		return elementVizOptions_;
	}

	/**
	 * Returns the scaling factors array of drawing.
	 * 
	 * @return Array storing the scaling factors.
	 */
	public double[] getScalingFactors() {
		return factors_;
	}

	/**
	 * Sets colored element and node options false.
	 */
	public void disableColoredOptions() {

		// set node options
		nodeOptions_[10] = false;

		// set element options
		elementOptions_[6] = false;
		elementOptions_[7] = false;
		elementOptions_[10] = false;
		elementOptions_[11] = false;
		elementOptions_[12] = false;
		elementOptions_[13] = false;
		elementOptions_[14] = false;
		elementOptions_[15] = false;
	}

	/**
	 * Draws undeformed shape of the structure depending on the display options
	 * for nodes and elements selected.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 */
	public void draw(SolidMAT owner) {

		// set coordinate system
		nodeDisplay_.setCoordinateSystem(coordinateSystem_);
		element1DDisplay_.setCoordinateSystem(coordinateSystem_);
		element2DDisplay_.setCoordinateSystem(coordinateSystem_);

		// set boundary cases
		nodeDisplay_.setBoundaryCases(bCases_);
		element1DDisplay_.setBoundaryCases(bCases_);
		element2DDisplay_.setBoundaryCases(bCases_);
		element3DDisplay_.setBoundaryCases(bCases_);

		// set component for two/three dimensional elements
		element2DDisplay_.setComponent(component_);
		element3DDisplay_.setComponent(component_);

		// set treshold for two/three dimensional elements
		element2DDisplay_.setTreshold(treshold_);
		element3DDisplay_.setTreshold(treshold_);

		// get material assigns array if selected
		Vector<Object>[] assigns = getColoredAssigns(owner);

		// visualize nodes and related objects
		nodeDisplay_.draw(owner, nodeOptions_, nodeVizOptions_, assigns);

		// visualize one dimensional elements and related objects
		element1DDisplay_.draw(owner, elementOptions_, elementVizOptions_,
				assigns);

		// visualize two dimensional elements and related objects
		element2DDisplay_.draw(owner, elementOptions_, elementVizOptions_,
				assigns);

		// visualize three dimensional elements and related objects
		element3DDisplay_.draw(owner, elementOptions_, elementVizOptions_,
				assigns);

		// draw contour scalor
		drawScalor(owner, assigns);
	}

	/**
	 * Draws contour scalor for the deformed shape option.
	 * 
	 * @param owner
	 *            The owner of contour scalor.
	 * @param assigns
	 *            Array storing vectors holding colored assign names and colors.
	 */
	private void drawScalor(SolidMAT owner, Vector<Object>[] assigns) {

		// draw contour scalor if colored assigns selected
		if (elementOptions_[6] || elementOptions_[7] || elementOptions_[10]
				|| elementOptions_[11] || nodeOptions_[10]
				|| elementOptions_[12] || elementOptions_[13]
				|| elementOptions_[14] || elementOptions_[15]) {

			// set name
			String name = null;
			if (elementOptions_[6])
				name = "Materials";
			else if (elementOptions_[7])
				name = "Sections";
			else if (elementOptions_[10])
				name = "Types";
			else if (elementOptions_[11] || nodeOptions_[10])
				name = "Groups";
			else if (elementOptions_[12] || elementOptions_[13]
					|| elementOptions_[14] || elementOptions_[15])
				name = "Check";

			// set type
			int type = ContourScalor.assignment_;

			// draw contour scalor
			owner.setContourScalor(true, name, type, assigns);
		}
	}

	/**
	 * Stores colored assign names and related colors in vectors and returns
	 * array storing these vectors. The colored assigns are materials, sections,
	 * element types, element groups or node groups.
	 * 
	 * @return Array storing vectors holding colored assign names and colors.
	 */
	private Vector<Object>[] getColoredAssigns(SolidMAT owner) {

		// materials, sections or element types selected
		if (elementOptions_[6] || elementOptions_[7] || elementOptions_[10])
			return getAssigns(owner);

		// element group assignments seleceted
		else if (elementOptions_[11])
			return getElementGroupAssigns(owner);

		// node group assignments seleceted
		else if (nodeOptions_[10])
			return getNodeGroupAssigns(owner);

		// element upside down option selected
		else if (elementOptions_[12] || elementOptions_[13]
				|| elementOptions_[14] || elementOptions_[15])
			return getCheckAssigns(owner);
		return null;
	}

	/**
	 * Stores colored check assign names and related colors in vectors and
	 * returns array storing these vectors. The check assigns are upside down,
	 * inside out, zero volume and aspect ratio elements.
	 * 
	 * @return Array storing vectors holding colored assign names and colors.
	 */
	private Vector<Object>[] getCheckAssigns(SolidMAT owner) {

		// initialize assignment names, color vectors
		Vector<String> assigns = new Vector<String>();
		Vector<Color> colors = new Vector<Color>();

		// set names
		if (elementOptions_[12])
			assigns.add("Upside down");
		else if (elementOptions_[13])
			assigns.add("Inside out");
		else if (elementOptions_[14])
			assigns.add("Zero volume");
		else if (elementOptions_[15])
			assigns.add("Bad aspect ratio");
		assigns.add("OK");

		// set colors
		colors.add(Color.RED);
		colors.add(Color.LIGHT_GRAY);

		// store vectors in array and return
		Vector[] vectors = { assigns, colors };
		return vectors;
	}

	/**
	 * Stores colored assign names and related colors in vectors and returns
	 * array storing these vectors. The colored assigns are materials, sections,
	 * element types.
	 * 
	 * @return Array storing vectors holding colored assign names and colors.
	 */
	private Vector<Object>[] getAssigns(SolidMAT owner) {

		// initialize assignment names vector
		Vector<String> assigns = new Vector<String>();
		Vector<Color> colors = new Vector<Color>();

		// initialize variable for number of elements without assign
		int n = 0;

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// get assign
			Object a = null;
			if (elementOptions_[6])
				a = e.getMaterial();
			else if (elementOptions_[7])
				a = e.getSection();
			else if (elementOptions_[10])
				a = e.getType();

			// element has assignment
			if (a != null) {

				// get name of assign
				String name = null;
				if (elementOptions_[6])
					name = e.getMaterial().getName();
				else if (elementOptions_[7])
					name = e.getSection().getName();
				else if (elementOptions_[10])
					name = "Type " + Integer.toString(e.getType());

				// check if exists in list
				if (assigns.contains(name) == false) {

					// add to names list
					assigns.add(name);

					// create a random color and add it to colors list
					float r = new Double(Math.random()).floatValue();
					float g = new Double(Math.random()).floatValue();
					float b = new Double(Math.random()).floatValue();
					Color c = new Color(r, g, b);
					colors.add(c);
				}
			}

			// element has no assign
			else
				n++;
		}

		// elements without assign exist
		if (n > 0) {
			assigns.add("None");
			colors.add(Color.LIGHT_GRAY);
		}

		// store vectors in array and return
		Vector[] vectors = { assigns, colors };
		return vectors;
	}

	/**
	 * Stores colored element group assign names and related colors in vectors
	 * and returns array storing these vectors.
	 * 
	 * @return Array storing vectors holding colored assign names and colors.
	 */
	private Vector<Object>[] getElementGroupAssigns(SolidMAT owner) {

		// get groups vector
		Vector<Group> groups = owner.inputData_.getGroup();

		// initialize assignment names vector
		Vector<String> assigns = new Vector<String>();
		Vector<Color> colors = new Vector<Color>();

		// initialize variable for number of elements without assign
		int n1 = 0;

		// loop over elements
		for (int i = 0; i < structure_.getNumberOfElements(); i++) {

			// get element
			Element e = structure_.getElement(i);

			// initialize variable for number of elements without assign
			int n2 = 0;

			// loop over groups
			for (int j = 0; j < groups.size(); j++) {

				// get group
				Group group = groups.get(j);

				// element is contained in the group
				if (group.containsElement(e)) {

					// increase index
					n2++;

					// get name of group
					String name = group.getName();

					// check if exists in list
					if (assigns.contains(name) == false) {

						// add to names list
						assigns.add(name);

						// create a random color and add it to colors list
						float r = new Double(Math.random()).floatValue();
						float g = new Double(Math.random()).floatValue();
						float b = new Double(Math.random()).floatValue();
						Color c = new Color(r, g, b);
						colors.add(c);
						break;
					}
				}
			}

			// element has no group
			if (n2 == 0)
				n1++;
		}

		// elements without assign exist
		if (n1 > 0) {
			assigns.add("None");
			colors.add(Color.LIGHT_GRAY);
		}

		// store vectors in array and return
		Vector[] vectors = { assigns, colors };
		return vectors;
	}

	/**
	 * Stores colored node group assign names and related colors in vectors and
	 * returns array storing these vectors.
	 * 
	 * @return Array storing vectors holding colored assign names and colors.
	 */
	private Vector<Object>[] getNodeGroupAssigns(SolidMAT owner) {

		// get groups vector
		Vector<Group> groups = owner.inputData_.getGroup();

		// initialize assignment names vector
		Vector<String> assigns = new Vector<String>();
		Vector<Color> colors = new Vector<Color>();

		// initialize variable for number of elements without assign
		int n1 = 0;

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get node
			Node node = structure_.getNode(i);

			// initialize variable for number of elements without assign
			int n2 = 0;

			// loop over groups
			for (int j = 0; j < groups.size(); j++) {

				// get group
				Group group = groups.get(j);

				// node is contained in the group
				if (group.containsNode(node)) {

					// increase index
					n2++;

					// get name of group
					String name = group.getName();

					// check if exists in list
					if (assigns.contains(name) == false) {

						// add to names list
						assigns.add(name);

						// create a random color and add it to colors list
						float r = new Double(Math.random()).floatValue();
						float g = new Double(Math.random()).floatValue();
						float b = new Double(Math.random()).floatValue();
						Color c = new Color(r, g, b);
						colors.add(c);
						break;
					}
				}
			}

			// node has no group
			if (n2 == 0)
				n1++;
		}

		// nodes without assign exist
		if (n1 > 0) {
			assigns.add("None");
			colors.add(Color.LIGHT_GRAY);
		}

		// store vectors in array and return
		Vector[] vectors = { assigns, colors };
		return vectors;
	}

	/**
	 * Computes maximum and minimum distances between nodes of structure. These
	 * distances are used for scaling objects.
	 * 
	 */
	private void computeScalors() {

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get base node's position vector
			DVec pos1 = structure_.getNode(i).getPosition();

			// loop over nodes
			for (int j = 0; j < structure_.getNumberOfNodes(); j++) {

				// get target node's position vector
				DVec pos2 = structure_.getNode(j).getPosition();

				// compute distance between base and target nodes
				double length = pos1.subtract(pos2).l2Norm();

				// assign minimum distance
				if (length < minDistance_ && length > 0.0)
					minDistance_ = length;

				// assign maximum distance
				if (length > maxDistance_)
					maxDistance_ = length;
			}
		}
	}

	/**
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
