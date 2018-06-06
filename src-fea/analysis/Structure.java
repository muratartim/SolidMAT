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
package analysis;

import java.io.Serializable;
import java.util.Vector;

import node.Node;
import boundary.BoundaryCase;
import element.Element;
import element.ElementLibrary;
import matrix.DVec;

import data.OutputData;
import dialogs.file.OutputDataHandler1;

/**
 * Class for structure.
 * 
 * @author Murat
 * 
 */
public class Structure implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The tolerance for checking activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** Vector for storing nodes. */
	private Vector<Node> nodes_ = new Vector<Node>();

	/** Vector for storing elements. */
	private Vector<Element> elements_ = new Vector<Element>();

	/** Array for storing available dofs of the structure. */
	private int[] dofs_ = new int[6];

	/** Number of steps available. */
	private int steps_ = 0;

	/** Vector for storing the analysis information. */
	private Vector<Object> analysisInfo_ = new Vector<Object>();

	/**
	 * Appends node to structure.
	 * 
	 * @param node
	 *            The node to be appended.
	 */
	public void addNode(Node node) {
		nodes_.add(node);
	}

	/**
	 * Appends element to structure.
	 * 
	 * @param element
	 *            The element to be appended.
	 */
	public void addElement(Element element) {
		elements_.add(element);
	}

	/**
	 * Replaces the node at the given index with the new one.
	 * 
	 * @param index
	 *            The index of node to be replaced.
	 * @param node
	 *            The new node.
	 */
	public void setNode(int index, Node node) {
		nodes_.set(index, node);
	}

	/**
	 * Replaces the element at the given index with the new one.
	 * 
	 * @param index
	 *            The index of element to be replaced.
	 * @param element
	 *            The new element.
	 */
	public void setElement(int index, Element element) {
		elements_.set(index, element);
	}

	/**
	 * Sets available dofs to the structure.
	 * 
	 * @param dofs
	 *            Array storing the dofs of the structure. Dofs given as -1 are
	 *            considered as unavailable.
	 */
	public void setAvailableDofs(int[] dofs) {

		// check dimension of array
		if (dofs.length != 6)
			exceptionHandler("Illegal dimension for available dofs!");

		// set dofs
		dofs_ = dofs;
	}

	/**
	 * Writes output data object to given path.
	 * 
	 * @param path
	 *            The path to write the output data object.
	 * @param unknowns
	 *            The unknowns array of output data.
	 * @param steps
	 *            Number of steps.
	 * @return True if no problem occured during writing, False vice versa.
	 */
	public boolean setUnknowns(String path, double[][] unknowns, int steps) {
		steps_ = steps;
		return OutputDataHandler1.write(path, unknowns, steps);
	}

	/**
	 * Sets nodal unknowns and reaction forces to nodes depending on the step
	 * number given.
	 * 
	 * @param path
	 *            The path of output data.
	 * @param n
	 *            The step number of solution.
	 */
	public void setStep(String path, int n) {

		// read output data
		OutputData outputData = OutputDataHandler1.read(path);

		// no problem occurred with reading
		if (outputData != null) {

			// set step
			outputData.setStep(this, n);
		}
	}

	/**
	 * Sets unknowns to node for the given step number.
	 * 
	 * @param path
	 *            The path of output data.
	 * @param node
	 *            Node to be set.
	 * @param n
	 *            The step number.
	 */
	public void setStepToNode(String path, Node node, int n) {

		// read output data
		OutputData outputData = OutputDataHandler1.read(path);

		// no problem occurred with reading
		if (outputData != null) {

			// set step
			outputData.setStepToNode(node, n);
		}
	}

	/**
	 * Sets unknowns to element for the given step number.
	 * 
	 * @param path
	 *            The path of output data.
	 * @param e
	 *            Element to be set.
	 * @param n
	 *            The step number.
	 */
	public void setStepToElement(String path, Element e, int n) {

		// read output data
		OutputData outputData = OutputDataHandler1.read(path);

		// no problem occurred with reading
		if (outputData != null) {

			// set step
			outputData.setStepToElement(e, n);
		}
	}

	/**
	 * Sets analysis info to the structure.
	 * 
	 * @param info
	 *            Vector storing the analysis information.
	 */
	public void setAnalysisInfo(Vector<Object> info) {
		analysisInfo_ = info;
	}

	/**
	 * Returns true if no elements and nodes are valid.
	 * 
	 * @return True if no elements and nodes are valid.
	 */
	public boolean isEmpty() {

		// get number of nodes and elements
		int nn = getNumberOfNodes();
		int ne = getNumberOfElements();

		// empty
		if (nn + ne == 0)
			return true;
		else
			return false;
	}

	/**
	 * Inserts node to the structure at the given index.
	 * 
	 * @param index
	 *            The index of insertion.
	 * @param node
	 *            The node to be inserted.
	 */
	public void insertNode(int index, Node node) {
		nodes_.insertElementAt(node, index);
	}

	/**
	 * Inserts element to the structure at the given index.
	 * 
	 * @param index
	 *            The index of insertion.
	 * @param element
	 *            The element to be inserted.
	 */
	public void insertElement(int index, Element element) {
		elements_.insertElementAt(element, index);
	}

	/**
	 * Removes node from structure.
	 * 
	 * @param index
	 *            The index of node to be removed.
	 */
	public void removeNode(int index) {
		nodes_.remove(index);
	}

	/**
	 * Removes element from structure.
	 * 
	 * @param index
	 *            The index of element to be removed.
	 */
	public void removeElement(int index) {
		elements_.remove(index);
	}

	/**
	 * Checks model.
	 * 
	 * @param option
	 *            0 -> All checks performed, 1 -> # of nodes/elements, 2 ->
	 *            Duplicate nodes, 3 -> Duplicate elements, 4 -> Unused nodes, 5 ->
	 *            Material assigns, 6 -> Section assigns.
	 * 
	 * @return Null if no problem found, related message if problem occured.
	 */
	public String checkModel(int option) {

		// initialize error message
		String message = null;

		// check number of nodes and elements
		if (option == 0 || option == 1) {
			int nn = getNumberOfNodes();
			int ne = getNumberOfElements();
			if (nn == 0 || ne == 0) {
				message = "No nodes and/or elements have been created!";
				return message;
			}
		}

		// check duplicate nodes
		if (option == 0 || option == 2) {
			if (checkDuplicateNodes() == false) {
				message = "Duplicate nodes encountered in the model!";
				return message;
			}
		}

		// check duplicate elements
		if (option == 0 || option == 3) {
			if (checkDuplicateElements() == false) {
				message = "Duplicate elements encountered in the model!";
				return message;
			}
		}

		// check unused nodes
		if (option == 0 || option == 4) {
			if (checkUnusedNodes() == false) {
				message = "Unused nodes encountered in the model!";
				return message;
			}
		}

		// check for elements without material
		if (option == 0 || option == 5) {
			if (checkMaterialAssignments() == false) {
				message = "Elements without materials encountered in the model!";
				return message;
			}
		}

		// check for elements without section
		if (option == 0 || option == 6) {
			if (checkSectionAssignments() == false) {
				message = "Elements without sections encountered in the model!";
				return message;
			}
		}

		// no problem with the model
		return message;
	}

	/**
	 * Checks whether the given node is in structure or not.
	 * 
	 * @param node
	 *            The node to be checked.
	 * @return True if the given node is in structure, False vice versa.
	 */
	public boolean containsNode(Node node) {
		return nodes_.contains(node);
	}

	/**
	 * Checks whether the given element is in structure or not.
	 * 
	 * @param element
	 *            The element to be checked.
	 * @return True if the given element is in structure, False vice versa.
	 */
	public boolean containsElement(Element element) {
		return elements_.contains(element);
	}

	/**
	 * Returns the node at the specified position.
	 * 
	 * @param index
	 *            The position of node.
	 * @return The demanded node.
	 */
	public Node getNode(int index) {
		return nodes_.get(index);
	}

	/**
	 * Returns the element at the specified position.
	 * 
	 * @param index
	 *            The position of element.
	 * @return The demanded element.
	 */
	public Element getElement(int index) {
		return elements_.get(index);
	}

	/**
	 * Returns the index of given node.
	 * 
	 * @param node
	 *            The node to be searched.
	 * @return The index of given node.
	 */
	public int indexOfNode(Node node) {
		return nodes_.indexOf(node);
	}

	/**
	 * Returns the index of given element.
	 * 
	 * @param element
	 *            The element to be searched.
	 * @return The index of given element.
	 */
	public int indexOfElement(Element element) {
		return elements_.indexOf(element);
	}

	/**
	 * Returns available dofs of the structure.
	 * 
	 * @return Array storing the available dofs of the structure.
	 */
	public int[] getAvailableDofs() {
		return dofs_;
	}

	/**
	 * Returns the number of nodes of structure.
	 * 
	 * @return The number of nodes.
	 */
	public int getNumberOfNodes() {
		return nodes_.size();
	}

	/**
	 * Returns the number of elements of structure.
	 * 
	 * @return The number of elements.
	 */
	public int getNumberOfElements() {
		return elements_.size();
	}

	/**
	 * Returns number of steps of the solution.
	 * 
	 * @return Number of steps.
	 */
	public int getNumberOfSteps() {
		return steps_;
	}

	/**
	 * Returns the volume of structure.
	 * 
	 * @return The volume of structure.
	 */
	public double getVolume() {

		// compute volume of structure
		double volume = 0.0;
		for (int i = 0; i < elements_.size(); i++)
			volume += elements_.get(i).getVolume();

		// return
		return volume;
	}

	/**
	 * Returns the mass of structure.
	 * 
	 * @return The mass of structure.
	 */
	public double getMass() {

		// compute mass of structure
		double mass = 0.0;
		for (int i = 0; i < elements_.size(); i++)
			mass += elements_.get(i).getMass();

		// return
		return mass;
	}

	/**
	 * Returns the weight of structure.
	 * 
	 * @return The weight of structure.
	 */
	public double getWeight() {

		// compute weight of structure
		double weight = 0.0;
		for (int i = 0; i < elements_.size(); i++)
			weight += elements_.get(i).getWeight();

		// return
		return weight;
	}

	/**
	 * Returns analysis information of the structure.
	 * 
	 * @return Vector storing the analysis information. The information sequence
	 *         of the vector depends on the analysis type.
	 */
	public Vector<Object> getAnalysisInfo() {
		return analysisInfo_;
	}

	/**
	 * Initializes structure by deleting calculated displacements and reaction
	 * forces from nodes.
	 * 
	 */
	public void initialize() {

		// loop over nodes
		for (int i = 0; i < getNumberOfNodes(); i++) {

			// get node
			Node node = getNode(i);

			// set unknown
			node.setUnknown(null);

			// set reaction force
			node.setReactionForce(null);
		}
	}

	/**
	 * Enumerates nodal and element degrees of freedom and returns total number
	 * of equations.
	 * 
	 * @param bCases
	 *            The boundary cases of analysis.
	 * @param bScales
	 *            The scaling factor of boundary cases.
	 * @return The number of equations.
	 */
	protected int enumerateDofs(Vector<BoundaryCase> bCases, double[] bScales) {

		// set equation number index
		int eqn = 0;

		// enumerate nodal degrees of freedom
		for (int i = 0; i < nodes_.size(); i++) {
			nodes_.get(i).setBoundaryCases(bCases, bScales);
			nodes_.get(i).setAvailableDofs(dofs_);
			eqn = nodes_.get(i).enumerateDofs(eqn);
		}

		// enumerate element degrees of freedom
		for (int i = 0; i < elements_.size(); i++) {
			elements_.get(i).setBoundaryCases(bCases, bScales);
			elements_.get(i).enumerateDofs();
		}

		// return number of equations
		return eqn;
	}

	/**
	 * Checks whether any duplicate nodes exist.
	 * 
	 * @return True if there exists duplicate nodes, False vice versa.
	 */
	private boolean checkDuplicateNodes() {

		// loop over nodes
		for (int i = 0; i < getNumberOfNodes() - 1; i++) {

			// get position of base node
			Node node1 = getNode(i);
			DVec pos1 = node1.getPosition();

			// loop over other nodes
			for (int j = i + 1; j < getNumberOfNodes(); j++) {

				// get target node and its position
				Node node2 = getNode(j);
				DVec pos2 = node2.getPosition();

				// check positions
				DVec dif = pos1.subtract(pos2);
				if (dif.l2Norm() <= tolerance_)
					return false;
			}
		}

		// no duplicate nodes
		return true;
	}

	/**
	 * Checks whether any duplicate elements exist.
	 * 
	 * @return True if there exists duplicate elements, False vice versa.
	 */
	private boolean checkDuplicateElements() {

		// loop over elements
		for (int i = 0; i < getNumberOfElements() - 1; i++) {

			// get type and nodes of base element
			int type1 = getElement(i).getType();
			Node[] nodes = getElement(i).getNodes();

			// loop over other elements
			for (int j = i + 1; j < getNumberOfElements(); j++) {

				// get target element and its type
				Element e = getElement(j);
				int type2 = e.getType();

				// check for same type
				if (type1 == type2) {

					// check for identical nodes
					int m = 0;
					for (int k = 0; k < nodes.length; k++) {
						for (int l = 0; l < nodes.length; l++) {
							if (nodes[k].equals(e.getNodes()[l]))
								m++;
						}
					}

					// identical nodes
					if (m == nodes.length)
						return false;
				}
			}
		}

		// no duplicate elements
		return true;
	}

	/**
	 * Checks whether any unused nodes exist.
	 * 
	 * @return True if there exists unused nodes, False vice versa.
	 */
	private boolean checkUnusedNodes() {

		// loop over nodes
		for (int i = 0; i < getNumberOfNodes(); i++) {

			// get node to be checked
			Node node = getNode(i);
			int m = 0;

			// loop over elements
			for (int j = 0; j < getNumberOfElements(); j++) {

				// get element and its nodes
				Element e = getElement(j);
				Node[] nodes = e.getNodes();

				// loop over nodes of element
				for (int k = 0; k < nodes.length; k++) {

					// element contains node
					if (nodes[k].equals(node)) {
						m++;
						break;
					}
				}

				// check node's connection
				if (m > 0)
					break;
			}

			// check node's connection
			if (m == 0)
				return false;
		}

		// no unused nodes
		return true;
	}

	/**
	 * Checks if there is any element without material.
	 * 
	 * @return True if there exists elements without material, False vice versa.
	 */
	private boolean checkMaterialAssignments() {

		// loop over elements
		for (int i = 0; i < getNumberOfElements(); i++) {

			// get element
			Element e = getElement(i);

			// check if it has material
			if (e.getMaterial() == null)
				return false;
		}

		// no elements without material
		return true;
	}

	/**
	 * Checks if there is any element without section.
	 * 
	 * @return True if there exists elements without section, False vice versa.
	 */
	private boolean checkSectionAssignments() {

		// loop over elements
		for (int i = 0; i < getNumberOfElements(); i++) {

			// get element
			Element e = getElement(i);

			// check dimension
			if (e.getDimension() != ElementLibrary.threeDimensional_) {

				// check if it has section
				if (e.getSection() == null)
					return false;
			}
		}

		// no elements without sections
		return true;
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
