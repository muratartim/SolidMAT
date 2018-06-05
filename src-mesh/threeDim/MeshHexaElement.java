package threeDim;

import java.util.Vector;

import data.Group;

import matrix.DVec;
import node.Node;
import element.Element22;
import element.Element23;
import element.Element3D;
import element.ElementLibrary;

import analysis.Structure;

/**
 * Class for Meshing hexa solid elements.
 * 
 * @author Murat
 * 
 */
public class MeshHexaElement {

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** The structure of the element to be meshed. */
	private Structure structure_;

	/** The element groups. */
	private Vector<Group> groups_;

	/**
	 * Creates hexa solid element mesher.
	 * 
	 * @param s
	 *            The structure of the element to be meshed.
	 * @param groups
	 *            Vector storing the element groups.
	 */
	public MeshHexaElement(Structure s, Vector<Group> groups) {
		structure_ = s;
		groups_ = groups;
	}

	/**
	 * Meshes hexa solid elements into same type elements as the original one.
	 * 
	 * @param e
	 *            Three dimensional hexa element to be meshed. Element should be
	 *            a hexa element and is not checked. It should be checked before
	 *            assigned to function!!
	 * @param m
	 *            The number of divisions in local eps1 direction. This should
	 *            be at least 1 and is not checked. It should be checked before
	 *            assigned to function!!
	 * @param n
	 *            The number of divisions in local eps2 direction. This should
	 *            be at least 1 and is not checked. It should be checked before
	 *            assigned to function!!
	 * @param k
	 *            The number of divisions in local eps3 direction. This should
	 *            be at least 1 and is not checked. It should be checked before
	 *            assigned to function!!
	 */
	public void meshInto(Element3D e, int m, int n, int k) {

		// create stationary nodes
		Vector<Node> nodes = createNodes(e, m, n, k);

		// create new elements
		createElements(e, nodes, m, n, k);
	}

	/**
	 * Creates elements from the given element.
	 * 
	 * @param e
	 *            Element to be meshed.
	 * @param nodes
	 *            The vector storing the nodes created after meshing.
	 * @param m
	 *            The number of divisions in local eps1 direction.
	 * @param n
	 *            The number of divisions in local eps2 direction.
	 * @param k
	 *            The number of divisions in local eps3 direction.
	 */
	private void createElements(Element3D e, Vector<Node> nodes, int m, int n,
			int k) {

		// get element type and number of nodes
		int type = e.getType();
		int nn = e.getNodes().length;

		// create elements array
		Vector<Element3D> elements = new Vector<Element3D>();

		// loop over divisions in eps3 direction
		for (int i = 0; i < k; i++) {

			// loop over divisions in eps2 direction
			for (int j = 0; j < n; j++) {

				// loop over divisions in eps1 direction
				for (int l = 0; l < m; l++) {

					// compute node indices for vertex nodes
					int p0 = (m + 1) * (j + 1) + l + 1 + i * (m + 1) * (n + 1);
					int p1 = (m + 1) * (j + 1) + l + i * (m + 1) * (n + 1);
					int p2 = (m + 1) * j + l + i * (m + 1) * (n + 1);
					int p3 = (m + 1) * j + l + 1 + i * (m + 1) * (n + 1);
					int p4 = p0 + (m + 1) * (n + 1);
					int p5 = p1 + (m + 1) * (n + 1);
					int p6 = p2 + (m + 1) * (n + 1);
					int p7 = p3 + (m + 1) * (n + 1);

					// create element nodes array and set vertex nodes
					Node[] elNodes = new Node[nn];
					elNodes[0] = nodes.get(p0);
					elNodes[1] = nodes.get(p1);
					elNodes[2] = nodes.get(p2);
					elNodes[3] = nodes.get(p3);
					elNodes[4] = nodes.get(p4);
					elNodes[5] = nodes.get(p5);
					elNodes[6] = nodes.get(p6);
					elNodes[7] = nodes.get(p7);

					// create array for storing positions of additional nodes
					DVec[] pos = new DVec[0];

					// twenty noded
					if (nn == 20) {

						// compute positions of additional nodes
						pos = new DVec[12];
						pos[0] = elNodes[0].getPosition().add(
								elNodes[1].getPosition()).scale(0.5);
						pos[1] = elNodes[1].getPosition().add(
								elNodes[2].getPosition()).scale(0.5);
						pos[2] = elNodes[2].getPosition().add(
								elNodes[3].getPosition()).scale(0.5);
						pos[3] = elNodes[3].getPosition().add(
								elNodes[0].getPosition()).scale(0.5);
						pos[4] = elNodes[4].getPosition().add(
								elNodes[5].getPosition()).scale(0.5);
						pos[5] = elNodes[5].getPosition().add(
								elNodes[6].getPosition()).scale(0.5);
						pos[6] = elNodes[6].getPosition().add(
								elNodes[7].getPosition()).scale(0.5);
						pos[7] = elNodes[7].getPosition().add(
								elNodes[4].getPosition()).scale(0.5);
						pos[8] = elNodes[4].getPosition().add(
								elNodes[0].getPosition()).scale(0.5);
						pos[9] = elNodes[5].getPosition().add(
								elNodes[1].getPosition()).scale(0.5);
						pos[10] = elNodes[6].getPosition().add(
								elNodes[2].getPosition()).scale(0.5);
						pos[11] = elNodes[7].getPosition().add(
								elNodes[3].getPosition()).scale(0.5);
					}

					// create additional nodes
					for (int o = 0; o < pos.length; o++) {

						// check if there is any node at that coordinate
						elNodes[8 + o] = checkCoordinates(pos[o]);

						// if there is no, create and add internal node to
						// structure
						if (elNodes[8 + o] == null) {
							elNodes[8 + o] = new Node(pos[o].get(0), pos[o]
									.get(1), pos[o].get(2));

							// add node to structure
							structure_.addNode(elNodes[8 + o]);
						}
					}

					// element22
					if (type == ElementLibrary.element22_)
						elements.add(new Element22(elNodes[0], elNodes[1],
								elNodes[2], elNodes[3], elNodes[4], elNodes[5],
								elNodes[6], elNodes[7]));

					// element23
					else if (type == ElementLibrary.element23_)
						elements.add(new Element23(elNodes[0], elNodes[1],
								elNodes[2], elNodes[3], elNodes[4], elNodes[5],
								elNodes[6], elNodes[7], elNodes[8], elNodes[9],
								elNodes[10], elNodes[11], elNodes[12],
								elNodes[13], elNodes[14], elNodes[15],
								elNodes[16], elNodes[17], elNodes[18],
								elNodes[19]));
				}
			}
		}

		// pass element properties to newly created elements
		passElementProperties(e, elements);

		// replace the old element in groups
		replaceInGroups(e, elements);

		// replace the old element in structure
		replaceInStructure(e, elements);

		// remove old element's nodes from structure and groups
		removeNodes(e);
	}

	/**
	 * Removes nodes of old element which are not connected to any other element
	 * from structure and groups.
	 * 
	 * @param e
	 *            The element to be meshed.
	 */
	private void removeNodes(Element3D e) {

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

	/**
	 * Replaces demanded element from the structure.
	 * 
	 * @param e
	 *            The element to be replaced.
	 * @param elements
	 *            Vector storing the newly created elements.
	 */
	private void replaceInStructure(Element3D e, Vector<Element3D> elements) {

		// get the index of element in the structure
		int index = structure_.indexOfElement(e);

		// remove the element from the structure
		structure_.removeElement(index);

		// loop over newly created elements
		for (int j = 0; j < elements.size(); j++)
			structure_.insertElement(index + j, elements.get(j));
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
	private void replaceInGroups(Element3D e, Vector<Element3D> elements) {

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
	 * Passes element properties to newly created element.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The newly created elements after meshing.
	 */
	private void passElementProperties(Element3D e1, Vector<Element3D> e2) {

		// loop over elements
		for (int i = 0; i < e2.size(); i++) {

			// material
			if (e1.getMaterial() != null)
				e2.get(i).setMaterial(e1.getMaterial());

			// mechanical loads
			if (e1.getAllMechLoads() != null)
				e2.get(i).setMechLoads(e1.getAllMechLoads());

			// temperature loads
			e2.get(i).setTempLoads(e1.getAllTempLoads());

			// springs
			if (e1.getSprings() != null)
				e2.get(i).setSprings(e1.getSprings());

			// masses
			if (e1.getAdditionalMasses() != null)
				e2.get(i).setMasses(e1.getAdditionalMasses());
		}
	}

	/**
	 * Creates stationary nodes and adds them to both structure and nodes array
	 * for the element to be meshed.
	 * 
	 * @param e
	 *            The element to be meshed.
	 * @param m
	 *            The number of divisions in local eps1 direction.
	 * @param n
	 *            The number of divisions in local eps2 direction.
	 * @param k
	 *            The number of divisions in local eps3 direction.
	 * @return Vector storing the nodes after meshing.
	 */
	private Vector<Node> createNodes(Element3D e, int m, int n, int k) {

		// get element nodes
		Node[] elNodes = e.getNodes();

		// get vertex nodes' position vectors
		DVec pos0 = elNodes[0].getPosition();
		DVec pos1 = elNodes[1].getPosition();
		DVec pos2 = elNodes[2].getPosition();
		DVec pos3 = elNodes[3].getPosition();
		DVec pos4 = elNodes[4].getPosition();
		DVec pos5 = elNodes[5].getPosition();
		DVec pos6 = elNodes[6].getPosition();
		DVec pos7 = elNodes[7].getPosition();

		// compute positions of corner points
		DVec[] pos04 = computeCornerPositions(k, pos0, pos4);
		DVec[] pos15 = computeCornerPositions(k, pos1, pos5);
		DVec[] pos26 = computeCornerPositions(k, pos2, pos6);
		DVec[] pos37 = computeCornerPositions(k, pos3, pos7);

		// create nodes array
		Vector<Node> nodes = new Vector<Node>();

		// loop over divisions in eps3 direction
		for (int i = 0; i <= k; i++)
			create12PlaneNodes(m, n, pos04[i], pos15[i], pos26[i], pos37[i],
					nodes);

		// return nodes vector
		return nodes;
	}

	/**
	 * Computes positions of points lying between the given points in local eps3
	 * direction.
	 * 
	 * @param k
	 *            The number of divisions in local eps3 direction.
	 * @param p1
	 *            Position of the upper point.
	 * @param p2
	 *            Position of the lower point.
	 * @return Array storing the computed position vectors.
	 */
	private DVec[] computeCornerPositions(int k, DVec p1, DVec p2) {

		// create array for storing computed positions
		DVec[] pos = new DVec[k + 1];

		// compute positions of points lying between p2 and p1
		DVec dis = p1.subtract(p2);
		for (int i = 0; i < pos.length; i++) {
			double s1 = i;
			pos[i] = p1.subtract(dis.scale(s1 / k));
		}

		// return positions array
		return pos;
	}

	/**
	 * Creates nodes that lie on local 1-2 plane by the given corner position
	 * vectors.
	 * 
	 * @param m
	 *            The number of divisions in local eps1 direction.
	 * @param n
	 *            The number of divisions in local eps2 direction.
	 * @param pos0
	 *            Lower-right corner of the plane.
	 * @param pos1
	 *            Upper-right corner of the plane.
	 * @param pos2
	 *            Upper-left corner of the plane.
	 * @param pos3
	 *            Lower-left corner of the plane.
	 * @param nodes
	 *            Vector for adding the newly created nodes.
	 */
	private void create12PlaneNodes(int m, int n, DVec pos0, DVec pos1,
			DVec pos2, DVec pos3, Vector<Node> nodes) {

		// compute positions of points lying between 3 and 0
		DVec[] pos30 = new DVec[n + 1];
		DVec dis1 = pos0.subtract(pos3);
		for (int i = 0; i < pos30.length; i++) {
			double s1 = i;
			pos30[i] = pos3.add(dis1.scale(s1 / n));
		}

		// compute distance between corners 1 and 2
		dis1 = pos1.subtract(pos2);

		// initialize positions array
		DVec[] pos = new DVec[(m + 1) * (n + 1)];

		// loop over divisions in eps2 direction
		int k = 0;
		for (int i = 0; i < n + 1; i++) {

			// loop over divisions in eps1 direction
			for (int j = 0; j < m + 1; j++) {
				double s1 = j;
				double s2 = i;
				pos[k] = pos2.add(dis1.scale(s2 / n));
				DVec dis2 = pos30[i].subtract(pos[k]);
				pos[k] = pos[k].add(dis2.scale(s1 / m));
				k++;
			}
		}

		// create internal nodes array
		Node[] intNodes = new Node[pos.length];

		// loop over internal nodes
		for (int i = 0; i < intNodes.length; i++) {

			// check if there is any node at that coordinate
			intNodes[i] = checkCoordinates(pos[i]);

			// if there is no, create and add internal node to structure
			if (intNodes[i] == null) {
				intNodes[i] = new Node(pos[i].get(0), pos[i].get(1), pos[i]
						.get(2));

				// add node to structure
				structure_.addNode(intNodes[i]);
			}

			// add to nodes vector
			nodes.add(intNodes[i]);
		}
	}

	/**
	 * Checks whether given coordinates are occupied by a node of the structure
	 * or not.
	 * 
	 * @param pos
	 *            The coordinates to be checked.
	 * @return Null if no nodes are at the given coordinates, the occupying node
	 *         vice versa.
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
}
