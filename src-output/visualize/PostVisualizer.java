/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package visualize;

import java.util.Locale;

import node.Node;
import main.SolidMAT;
import matrix.DVec;
import analysis.Structure;
import analysis.Analysis;

/**
 * Class for visualizing postprocessing information.
 * 
 * @author Murat
 * 
 */
public class PostVisualizer {

	/**
	 * Static variable for the result option of drawing. If something wanted to
	 * be added, then check method should be modified.
	 */
	public static final int nodalDisp_ = 0, reactionForces_ = 1,
			elementDisp1D_ = 2, elasticStrains1D_ = 3, stresses1D_ = 4,
			internalForces1D_ = 5, principalStrains1D_ = 6,
			principalStresses1D_ = 7, misesStress1D_ = 8, elementDisp2D_ = 9,
			elasticStrains2D_ = 10, stresses2D_ = 11, internalForces2D_ = 12,
			principalStrains2D_ = 13, principalStresses2D_ = 14,
			misesStress2D_ = 15, noOption_ = 16, elementDisp3D_ = 17,
			elasticStrains3D_ = 18, stresses3D_ = 19, principalStrains3D_ = 20,
			principalStresses3D_ = 21, misesStress3D_ = 22;

	/** Static variable for drawing coordinate system. */
	public static final int global_ = 0, local_ = 1;

	/** The coordinate system for drawing numerical values. */
	private int coordinateSystem_ = PostVisualizer.global_;

	/** Static variable for drawing undeformed or deformed shape of structure. */
	public static final int undeformed_ = 0, deformed_ = 1;

	/** The structure to be visualized. */
	private Structure structure_;

	/** Node and node related results drawer of visualizer. */
	private PostNodeDisplay nodeDisplay_;

	/**
	 * One dimensional elements and element related results drawer of
	 * visualizer.
	 */
	private PostElement1DDisplay element1DDisplay_;

	/**
	 * Two dimensional elements and element related results drawer of
	 * visualizer.
	 */
	private PostElement2DDisplay element2DDisplay_;

	/**
	 * Three dimensional elements and element related results drawer of
	 * visualizer.
	 */
	private PostElement3DDisplay element3DDisplay_;

	/** Maximum and minimum distances between nodes. Used for scaling objects. */
	private double maxDistance_ = 0.0, minDistance_ = Math.pow(10, 8);

	/** Maximum displacement of all nodes. Used for scaling displacements. */
	private double maxDisplacement_ = 0.0;

	/**
	 * Scaling factor used for scaling displacements when automatic scaling is
	 * not used.
	 */
	private Double scale_ = null;

	/**
	 * Array storing the scaling factors of drawing. The sequence is; radius of
	 * nodes, radius of lines, diagram height.
	 */
	private double[] factors_ = { 1.0, 1.0, 1.0 };

	/**
	 * Sets structure to post-visualizer.
	 * 
	 * @param structure
	 *            The stucture to be drawn.
	 */
	public void setStructure(Structure structure) {

		// set structure
		structure_ = structure;

		// compute scaling magnitudes
		computeScalors();

		// create node display object
		nodeDisplay_ = new PostNodeDisplay(structure_, maxDistance_,
				minDistance_, maxDisplacement_, factors_);

		// create 1D element display object
		element1DDisplay_ = new PostElement1DDisplay(structure_, maxDistance_,
				minDistance_, maxDisplacement_, factors_);

		// create 2D element display object
		element2DDisplay_ = new PostElement2DDisplay(structure_, maxDistance_,
				minDistance_, maxDisplacement_);

		// create 3D element display object
		element3DDisplay_ = new PostElement3DDisplay(structure_, maxDistance_,
				minDistance_, maxDisplacement_);
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
	 * Sets factor for scaling displacements when automatic scaling is not used.
	 * 
	 * @param scale
	 *            Scaling factor.
	 */
	public void setScalingFactor(Double scale) {
		scale_ = scale;
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
	 * Returns the scaling factors array of drawing.
	 * 
	 * @return Array storing the scaling factors.
	 */
	public double[] getScalingFactors() {
		return factors_;
	}

	/**
	 * Draws results on undeformed shape of the structure.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component to be drawn.
	 */
	public void drawUndeformedShape(SolidMAT owner, int option, int[] comp) {

		// check parameters
		check(option, comp);

		// set coordinate system for nodes
		nodeDisplay_.setCoordinateSystem(coordinateSystem_);

		// visualize nodal results
		if (option == PostVisualizer.nodalDisp_
				|| option == PostVisualizer.reactionForces_)
			nodeDisplay_.draw(owner, PostNodeDisplay.undeformed_, option, comp,
					false);

		// visualize one dimensional elements and results
		element1DDisplay_.draw(owner, PostVisualizer.undeformed_, option, comp,
				false);

		// visualize two dimensional elements and results
		element2DDisplay_.draw(owner, PostVisualizer.undeformed_, option, comp,
				false);

		// visualize three dimensional elements and results
		element3DDisplay_.draw(owner, PostVisualizer.undeformed_, option, comp,
				false);
	}

	/**
	 * Draws results on deformed shape of the structure.
	 * 
	 * @param owner
	 *            The owner frame of drawing.
	 * @param option
	 *            The result option for drawing results.
	 * @param comp
	 *            The component to be drawn.
	 * @param isScaled
	 *            True if displacements are scaled, false if not.
	 * @param step
	 *            The step number of drawing.
	 */
	public void drawDeformedShape(SolidMAT owner, int option, int[] comp,
			boolean isScaled, int step) {

		// check parameters
		check(option, comp);

		// set coordinate system for nodes
		nodeDisplay_.setCoordinateSystem(coordinateSystem_);

		// set displacement scaling factor
		nodeDisplay_.setScalingFactor(scale_);
		element1DDisplay_.setScalingFactor(scale_);
		element2DDisplay_.setScalingFactor(scale_);
		element3DDisplay_.setScalingFactor(scale_);

		// visualize nodal results
		if (option == PostVisualizer.nodalDisp_
				|| option == PostVisualizer.reactionForces_)
			nodeDisplay_.draw(owner, PostNodeDisplay.deformed_, option, comp,
					isScaled);

		// visualize one dimensional elements and results
		element1DDisplay_.draw(owner, PostVisualizer.deformed_, option, comp,
				isScaled);

		// visualize two dimensional elements and results
		element2DDisplay_.draw(owner, PostVisualizer.deformed_, option, comp,
				isScaled);

		// visualize three dimensional elements and results
		element3DDisplay_.draw(owner, PostVisualizer.deformed_, option, comp,
				isScaled);

		// draw contour scalor for deformed shape
		if (option == PostVisualizer.noOption_)
			drawScalor(owner, step);
	}

	/**
	 * Draws contour scalor for the deformed shape option.
	 * 
	 * @param owner
	 *            The owner of contour scalor.
	 * @param step
	 *            The step number of drawing.
	 */
	private void drawScalor(SolidMAT owner, int step) {

		// set name
		String name = "Deformed Shape";

		// set type
		int type = ContourScalor.deformed_;

		// initialize values array
		String[] values = { "Step " + step };

		// get analysis type
		int anTyp = (Integer) structure_.getAnalysisInfo().get(1);

		// modal analysis
		if (anTyp == Analysis.modal_) {

			// get cyclic frequency of the recent step
			double[] nf = (double[]) structure_.getAnalysisInfo().get(6);
			values[0] += ", Frequency : " + formatter(nf[step]);
		}

		// buckling analysis
		else if (anTyp == Analysis.linearBuckling_) {

			// get load factor of the recent step
			double[] lf = (double[]) structure_.getAnalysisInfo().get(5);
			values[0] += ", Load Factor : " + formatter(lf[step]);
		}

		// draw contour scalor
		owner.setContourScalor(true, name, type, values);
	}

	/**
	 * Computes maximum and minimum distances between nodes of structure. These
	 * distances are used for scaling objects.
	 * 
	 */
	private void computeScalors() {

		// loop over nodes
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get base node
			Node node = structure_.getNode(i);

			// get base node's position vector
			DVec pos1 = node.getPosition();

			// loop over nodes
			for (int j = 0; j < structure_.getNumberOfNodes(); j++) {

				// get target node's position vector
				DVec pos2 = structure_.getNode(j).getPosition();

				// compute resultant distance
				DVec dis = pos1.subtract(pos2);
				double length = dis.l2Norm();

				// assign minimum distance
				if (length < minDistance_ && length > 0.0)
					minDistance_ = length;

				// assign maximum distance
				if (length > maxDistance_)
					maxDistance_ = length;
			}

			// get base node's displacement vector
			DVec dispVec = node.getUnknown(Node.global_);
			dispVec = dispVec.getSubVector(0, 2);
			double dispVal = dispVec.l2Norm();

			// assign maximum displacement
			if (dispVal > maxDisplacement_)
				maxDisplacement_ = dispVal;
		}
	}

	/**
	 * Checks given parameters for graphics.
	 * 
	 * @param option
	 *            Result option.
	 * @param comp
	 *            The demanded component of result.
	 */
	private void check(int option, int[] comp) {

		// check option
		if (option < 0 || option > misesStress3D_)
			exceptionHandler("Illegal result option for display!");

		// check dimension of component array
		if (comp.length > 2)
			exceptionHandler("Illegal result component demanded!");
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
	 * Throws exception with the related message.
	 * 
	 * @param message
	 *            The message to be displayed.
	 */
	private void exceptionHandler(String message) {
		throw new IllegalArgumentException(message);
	}
}
