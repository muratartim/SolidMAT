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
package data;

import java.io.Serializable;

import analysis.Structure;

import element.Element;
import node.Node;
import matrix.DVec;

/**
 * Class for output data object.
 * 
 * @author Murat
 * 
 */
public class OutputData implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * eqn x n matrix, where eqn is the number of equations solved and n is the
	 * number of steps of the analysis. It stores the system nodal unknown
	 * vectors as its columns.
	 */
	private double[][] unknowns_;

	/** Number of steps available. */
	private Integer steps_ = 0;

	/**
	 * Creates output object.
	 * 
	 * @param unknowns
	 *            Unknowns matrix of output object.
	 * @param steps
	 *            Number of steps available.
	 */
	public OutputData(double[][] unknowns, Integer steps) {
		unknowns_ = unknowns;
		steps_ = steps;
	}

	/**
	 * Sets nodal unknowns and reaction forces to nodes depending on the step
	 * number given.
	 * 
	 * @param s
	 *            Structure to be set.
	 * @param n
	 *            The step number of solution.
	 */
	public void setStep(Structure s, int n) {

		// check unknowns matrix
		if (unknowns_ != null) {

			// check demanded step number
			if (n < steps_) {

				// set unknowns to nodes
				setUnknowns(s, n);

				// set reaction forces to nodes
				setReactionForces(s);
			}
		}
	}

	/**
	 * Sets unknowns to node for the given step number.
	 * 
	 * @param node
	 *            Node to be set.
	 * @param n
	 *            The step number.
	 */
	public void setStepToNode(Node node, int n) {

		// check unknowns matrix
		if (unknowns_ != null) {

			// get node's dof numbers array
			int[] dof = node.getDofNumbers();

			// build array for storing node's unknowns
			double[] uNode = new double[6];

			// loop over dofs
			for (int j = 0; j < 6; j++) {

				// check if dof is free
				if (dof[j] != -1) {

					// set unknowns to array
					uNode[j] = unknowns_[dof[j]][n];
				}
			}

			// set unknowns to node
			node.setUnknown(uNode);
		}
	}

	/**
	 * Sets unknowns to element for the given step number.
	 * 
	 * @param e
	 *            Element to be set.
	 * @param n
	 *            The step number.
	 */
	public void setStepToElement(Element e, int n) {

		// check unknowns matrix
		if (unknowns_ != null) {

			// get nodes of element
			Node[] nodes = e.getNodes();

			// loop over element nodes
			for (int i = 0; i < nodes.length; i++) {

				// get node's dof numbers array
				int[] dof = nodes[i].getDofNumbers();

				// build array for storing node's unknowns
				double[] uNode = new double[6];

				// loop over dofs
				for (int j = 0; j < 6; j++) {

					// check if dof is free
					if (dof[j] != -1) {

						// set unknowns to array
						uNode[j] = unknowns_[dof[j]][n];
					}
				}

				// set unknowns to node
				nodes[i].setUnknown(uNode);
			}
		}
	}

	/**
	 * Returns number of available steps.
	 * 
	 * @return Number of available steps.
	 */
	public Integer getNumberOfSteps() {
		return steps_;
	}

	/**
	 * Returns unknowns matrix of output object.
	 * 
	 * @return Unknowns matrix of output object.
	 */
	public double[][] getUnknowns() {
		return unknowns_;
	}

	/**
	 * Sets unknowns to nodes.
	 * 
	 * @param s
	 *            Structure to be set.
	 * @param n
	 *            The step number of solution.
	 */
	private void setUnknowns(Structure s, int n) {

		// loop over nodes
		for (int i = 0; i < s.getNumberOfNodes(); i++) {

			// get node
			Node node = s.getNode(i);

			// get node's dof numbers array
			int[] dof = node.getDofNumbers();

			// build array for storing node's unknowns
			double[] uNode = new double[6];

			// loop over dofs
			for (int j = 0; j < 6; j++) {

				// check if dof is free
				if (dof[j] != -1) {

					// set unknowns to array
					uNode[j] = unknowns_[dof[j]][n];
				}
			}

			// set unknowns to node
			node.setUnknown(uNode);
		}
	}

	/**
	 * Sets reaction forces to nodes.
	 * 
	 * @param s
	 *            Structure to be set.
	 */
	private void setReactionForces(Structure s) {

		// loop over nodes for initializing reaction forces
		for (int i = 0; i < s.getNumberOfNodes(); i++)
			s.getNode(i).setReactionForce(null);

		// loop over elements for element boundary loads
		for (int i = 0; i < s.getNumberOfElements(); i++) {

			// get element
			Element e = s.getElement(i);

			// get boundary load vector of element
			DVec pe = e.getBoundLoadVector();

			// get nodes of element
			Node[] nodes = e.getNodes();

			// build array for storing node's forces
			double[] fNode = new double[6];

			// loop over nodes of element
			for (int j = 0; j < nodes.length; j++) {

				// loop over dofs of node
				for (int k = 0; k < 6; k++) {

					// set node's forces
					fNode[k] = pe.get(6 * j + k);
				}

				// add reactions to node
				nodes[j].addReactionForce(new DVec(fNode));
			}
		}

		// loop over nodes for nodal mechanical loads
		for (int i = 0; i < s.getNumberOfNodes(); i++) {

			// get node
			Node node = s.getNode(i);

			// check if the node has mechanical load
			if (node.getMechLoads().size() != 0) {

				// get mechanical load vector of node
				DVec rn = node.getMechLoadVector().scale(-1.0);

				// add reactions to node
				node.addReactionForce(rn);
			}
		}
	}
}
