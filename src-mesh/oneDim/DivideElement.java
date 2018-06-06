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
package oneDim;

import java.util.Vector;

import data.Group;

import node.Node;

import matrix.DVec;

import analysis.Structure;

import element.*;

/**
 * Class for Dividing line elements.
 * 
 * @author Murat
 * 
 */
public class DivideElement {

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** The structure of the element to be divided. */
	private Structure structure_;

	/** The element groups. */
	private Vector<Group> groups_;

	/**
	 * Creates line element divider.
	 * 
	 * @param s
	 *            The structure of the element to be divided.
	 * @param groups
	 *            Vector storing the element groups.
	 */
	public DivideElement(Structure s, Vector<Group> groups) {
		structure_ = s;
		groups_ = groups;
	}

	/**
	 * Divides line element into equally lengthened elements.
	 * 
	 * @param e
	 *            One dimensional element to be divided.
	 * @param n
	 *            The number of divisions. This should be at least 1 and is not
	 *            checked. It should be checked before assigned to function!!
	 */
	public void divideInto(Element1D e, int n) {

		// create internal stationary nodes
		Vector<Node> nodes = createInternalNodes(e, n);

		// create new elements
		createElements(e, nodes);
	}

	/**
	 * Creates internal nodes and adds them to both structure and nodes array
	 * for the element to be divivded.
	 * 
	 * @param e
	 *            The element to be divided.
	 * @param n
	 *            The number of divisions.
	 * @return Vector storing the nodes after division.
	 */
	private Vector<Node> createInternalNodes(Element1D e, int n) {

		// get element nodes
		Node[] elNodes = e.getNodes();
		int nn = elNodes.length;

		// get end nodes' position vectors
		DVec posI = elNodes[0].getPosition();
		DVec posJ = elNodes[nn - 1].getPosition();
		DVec posDif = posJ.subtract(posI);

		// compute position vectors of internal stationary points
		DVec[] posInt = new DVec[(nn - 1) * n - 1];
		for (int i = 0; i < posInt.length; i++)
			posInt[i] = posI.add(posDif.scale((i + 1.0) / ((nn - 1.0) * n)));

		// create nodes array and add initial node to it
		Vector<Node> nodes = new Vector<Node>();
		nodes.add(elNodes[0]);

		// create internal nodes array
		Node[] intNodes = new Node[posInt.length];

		// loop over internal nodes
		for (int i = 0; i < intNodes.length; i++) {

			// check if there is any node at that coordinate
			intNodes[i] = checkCoordinates(posInt[i]);

			// if there is no, create and add internal node to structure
			if (intNodes[i] == null) {
				intNodes[i] = new Node(posInt[i].get(0), posInt[i].get(1),
						posInt[i].get(2));

				// add node to structure
				structure_.addNode(intNodes[i]);
			}

			// add to nodes vector
			nodes.add(intNodes[i]);
		}

		// add the final node to nodes array
		nodes.add(elNodes[elNodes.length - 1]);

		// return nodes vector
		return nodes;
	}

	/**
	 * Checks whether given coordinates are occupied by a node of the structure
	 * or not.
	 * 
	 * @param pos
	 *            The coordinates to be checked.
	 * @return True if no nodes are at the given coordinates, False vice versa.
	 */
	private Node checkCoordinates(DVec pos) {

		// loop over nodes of structure
		for (int i = 0; i < structure_.getNumberOfNodes(); i++) {

			// get nodal position vector
			Node node = structure_.getNode(i);
			DVec pos1 = node.getPosition();

			// check coordinates
			if (pos.subtract(pos1).l2Norm() <= tolerance_)
				return node;
		}

		// no node exists at the same coordinates
		return null;
	}

	/**
	 * Creates elements from the given element.
	 * 
	 * @param e
	 *            Element to be divivded.
	 * @param nodes
	 *            The vector storing the nodes created after division.
	 */
	private void createElements(Element1D e, Vector<Node> nodes) {

		// get element type
		int type = e.getType();

		// create elements array
		Vector<Element1D> elements = new Vector<Element1D>();

		// determine number of divisions
		int n = 0;
		if (e.getNodes().length == 2)
			n = nodes.size() - 1;
		else if (e.getNodes().length == 3)
			n = (nodes.size() - 1) / 2;
		else if (e.getNodes().length == 4)
			n = (nodes.size() - 1) / 3;

		// loop over number of divisions
		for (int i = 0; i < n; i++) {

			// element0
			if (type == ElementLibrary.element0_)
				elements.add(new Element0(nodes.get(i), nodes.get(i + 1)));

			// element1
			else if (type == ElementLibrary.element1_)
				elements.add(new Element1(nodes.get(2 * i), nodes
						.get(2 * i + 1), nodes.get(2 * i + 2)));

			// element2
			else if (type == ElementLibrary.element2_)
				elements.add(new Element2(nodes.get(3 * i), nodes
						.get(3 * i + 1), nodes.get(3 * i + 2), nodes
						.get(3 * i + 3)));

			// element13
			else if (type == ElementLibrary.element13_)
				elements.add(new Element13(nodes.get(i), nodes.get(i + 1)));

			// element14
			else if (type == ElementLibrary.element14_)
				elements.add(new Element14(nodes.get(2 * i), nodes
						.get(2 * i + 1), nodes.get(2 * i + 2)));

			// element15
			else if (type == ElementLibrary.element15_)
				elements.add(new Element15(nodes.get(3 * i), nodes
						.get(3 * i + 1), nodes.get(3 * i + 2), nodes
						.get(3 * i + 3)));

			// element27
			else if (type == ElementLibrary.element27_)
				elements.add(new Element27(nodes.get(i), nodes.get(i + 1)));

			// element28
			else if (type == ElementLibrary.element28_)
				elements.add(new Element28(nodes.get(2 * i), nodes
						.get(2 * i + 1), nodes.get(2 * i + 2)));

			// element29
			else if (type == ElementLibrary.element29_)
				elements.add(new Element29(nodes.get(3 * i), nodes
						.get(3 * i + 1), nodes.get(3 * i + 2), nodes
						.get(3 * i + 3)));

			// element32
			else if (type == ElementLibrary.element32_)
				elements.add(new Element32(nodes.get(i), nodes.get(i + 1)));

			// element33
			else if (type == ElementLibrary.element33_)
				elements.add(new Element33(nodes.get(2 * i), nodes
						.get(2 * i + 1), nodes.get(2 * i + 2)));

			// element34
			else if (type == ElementLibrary.element34_)
				elements.add(new Element34(nodes.get(3 * i), nodes
						.get(3 * i + 1), nodes.get(3 * i + 2), nodes
						.get(3 * i + 3)));

			// element35
			else if (type == ElementLibrary.element35_)
				elements.add(new Element35(nodes.get(i), nodes.get(i + 1)));

			// element36
			else if (type == ElementLibrary.element36_)
				elements.add(new Element36(nodes.get(2 * i), nodes
						.get(2 * i + 1), nodes.get(2 * i + 2)));

			// element37
			else if (type == ElementLibrary.element37_)
				elements.add(new Element37(nodes.get(3 * i), nodes
						.get(3 * i + 1), nodes.get(3 * i + 2), nodes
						.get(3 * i + 3)));

			// element38
			else if (type == ElementLibrary.element38_)
				elements.add(new Element38(nodes.get(i), nodes.get(i + 1)));

			// pass element properties to newly created elements
			passElementProperties(e, elements.get(i));
		}

		// replace the old element in groups
		replaceInGroups(e, elements);

		// replace the old element in structure
		replaceInStructure(e, elements);

		// remove old element's nodes from structure and groups
		removeNodes(e);
	}

	/**
	 * Passes element properties to newly created element.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The newly created element after division.
	 */
	private void passElementProperties(Element1D e1, Element1D e2) {

		// material
		if (e1.getMaterial() != null)
			e2.setMaterial(e1.getMaterial());

		// section
		if (e1.getSection() != null)
			e2.setSection(e1.getSection());

		// mechanical loads
		if (e1.getAllMechLoads() != null)
			e2.setMechLoads(e1.getAllMechLoads());

		// temperature loads
		e2.setTempLoads(e1.getAllTempLoads());

		// springs
		if (e1.getSprings() != null)
			e2.setSprings(e1.getSprings());

		// masses
		if (e1.getAdditionalMasses() != null)
			e2.setMasses(e1.getAdditionalMasses());

		// local axes
		if (e1.getLocalAxis() != null)
			e2.setLocalAxis(e1.getLocalAxis());

		// radius of curvature
		int typ = e1.getType();
		if (typ == ElementLibrary.element27_
				|| typ == ElementLibrary.element28_
				|| typ == ElementLibrary.element29_)
			e2.setParameters(e1.getParameters());
	}

	/**
	 * Replaces demanded element from related groups.
	 * 
	 * @param e
	 *            The element to be replaced.
	 * @param elements
	 *            Vector storing the newly created elements.
	 * 
	 */
	private void replaceInGroups(Element1D e, Vector<Element1D> elements) {

		// loop over groups
		for (int i = 0; i < groups_.size(); i++) {

			// get group
			Group group = groups_.get(i);

			// check if group contains element
			if (group.containsElement(e)) {

				// get the index of element in the group
				int index = group.indexOfElement(e);

				// remove the element from the group
				group.removeElement(e);

				// loop over newly created elements
				for (int j = 0; j < elements.size(); j++)
					group.insertElement(index + j, elements.get(j));
			}
		}
	}

	/**
	 * Replaces demanded element from the structure.
	 * 
	 * @param e
	 *            The element to be replaced.
	 * @param elements
	 *            Vector storing the newly created elements.
	 */
	private void replaceInStructure(Element1D e, Vector<Element1D> elements) {

		// get the index of element in the structure
		int index = structure_.indexOfElement(e);

		// remove the element from the structure
		structure_.removeElement(index);

		// loop over newly created elements
		for (int j = 0; j < elements.size(); j++)
			structure_.insertElement(index + j, elements.get(j));
	}

	/**
	 * Removes nodes of old element which are not connected to any other element
	 * from structure and groups.
	 * 
	 * @param e
	 *            The element to be divided.
	 */
	private void removeNodes(Element1D e) {

		// loop over nodes of old element
		for (int i = 0; i < e.getNodes().length; i++) {

			// get node
			Node node = e.getNodes()[i];

			// loop over elements of structure
			int m = 0;
			for (int j = 0; j < structure_.getNumberOfElements(); j++) {

				// get nodes of element
				Node[] nodes = structure_.getElement(j).getNodes();

				// loop over nodes of element
				for (int k = 0; k < nodes.length; k++) {
					if (node.equals(nodes[k])) {
						m++;
						break;
					}
				}
				if (m > 0)
					break;
			}

			// remove node if it is not connected
			if (m == 0) {

				// remove from structure
				int index = structure_.indexOfNode(node);
				structure_.removeNode(index);

				// remove from groups
				for (int j = 0; j < groups_.size(); j++) {

					// get group
					Group group = groups_.get(j);

					// check if group contains node
					if (group.containsNode(node))
						group.removeNode(node);
				}
			}
		}
	}
}
