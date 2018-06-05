package twoDim;

import data.Group;
import element.*;

import java.util.Vector;

import node.Node;

import matrix.DVec;

import analysis.Structure;

/**
 * Class for Meshing quad area elements.
 * 
 * @author Murat
 * 
 */
public class MeshQuadElement {

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** The structure of the element to be meshed. */
	private Structure structure_;

	/** The element groups. */
	private Vector<Group> groups_;

	/**
	 * Creates quad area element mesher.
	 * 
	 * @param s
	 *            The structure of the element to be meshed.
	 * @param groups
	 *            Vector storing the element groups.
	 */
	public MeshQuadElement(Structure s, Vector<Group> groups) {
		structure_ = s;
		groups_ = groups;
	}

	/**
	 * Meshes quad area elements into same type elements as the original one.
	 * 
	 * @param e
	 *            Two dimensional quad element to be meshed. Element should be a
	 *            quad element and is not checked. It should be checked before
	 *            assigned to function!!
	 * @param m
	 *            The number of divisions in local eps1 direction. This should
	 *            be at least 1 and is not checked. It should be checked before
	 *            assigned to function!!
	 * @param n
	 *            The number of divisions in local eps2 direction. This should
	 *            be at least 1 and is not checked. It should be checked before
	 *            assigned to function!!
	 */
	public void meshInto(Element2D e, int m, int n) {

		// create internal stationary nodes
		Vector<Node> nodes = createInternalNodes(e, m, n);

		// create new elements
		createElements(e, nodes, m, n);
	}

	/**
	 * Creates internal nodes and adds them to both structure and nodes array
	 * for the element to be meshed.
	 * 
	 * @param e
	 *            The element to be meshed.
	 * @param m
	 *            The number of divisions in local eps1 direction.
	 * @param n
	 *            The number of divisions in local eps2 direction.
	 * @return Vector storing the nodes after meshing.
	 */
	private Vector<Node> createInternalNodes(Element2D e, int m, int n) {

		// get element nodes
		Node[] elNodes = e.getNodes();

		// get corner nodes' position vectors
		DVec pos0 = elNodes[0].getPosition();
		DVec pos1 = elNodes[1].getPosition();
		DVec pos2 = elNodes[2].getPosition();
		DVec pos3 = elNodes[3].getPosition();

		// compute positions of points lying between 1 and 2
		DVec[] pos12 = new DVec[n + 1];
		DVec dis1 = pos2.subtract(pos1);
		for (int i = 0; i < pos12.length; i++) {
			double s1 = i;
			pos12[i] = pos1.add(dis1.scale(s1 / n));
		}

		// compute distance between corners 3 and 0
		dis1 = pos3.subtract(pos0);

		// initialize positions array
		DVec[] pos = new DVec[(m + 1) * (n + 1)];

		// loop over divisions in eps2 direction
		int k = 0;
		for (int i = 0; i < n + 1; i++) {

			// loop over divisions in eps1 direction
			for (int j = 0; j < m + 1; j++) {
				double s1 = j;
				double s2 = i;
				pos[k] = pos0.add(dis1.scale(s2 / n));
				DVec dis2 = pos12[i].subtract(pos[k]);
				pos[k] = pos[k].add(dis2.scale(s1 / m));
				k++;
			}
		}

		// create nodes array
		Vector<Node> nodes = new Vector<Node>();

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
	 *            Element to be meshed.
	 * @param nodes
	 *            The vector storing the nodes created after meshing.
	 * @param m
	 *            The number of divisions in local eps1 direction.
	 * @param n
	 *            The number of divisions in local eps2 direction.
	 */
	private void createElements(Element2D e, Vector<Node> nodes, int m, int n) {

		// get element type and number of nodes
		int type = e.getType();
		int nn = e.getNodes().length;

		// create elements array
		Vector<Element2D> elements = new Vector<Element2D>();

		// loop over divisions in eps2 direction
		for (int i = 0; i < n; i++) {

			// loop over divisions in eps1 direction
			for (int j = 0; j < m; j++) {

				// create element nodes array and set corner nodes
				Node[] elNodes = new Node[nn];
				elNodes[0] = nodes.get((m + 1) * i + j);
				elNodes[1] = nodes.get((m + 1) * i + j + 1);
				elNodes[2] = nodes.get((m + 1) * (i + 1) + j + 1);
				elNodes[3] = nodes.get((m + 1) * (i + 1) + j);

				// create array for storing positions of additional nodes
				DVec[] pos = new DVec[0];

				// eight noded
				if (nn == 8) {

					// compute positions of additional nodes
					pos = new DVec[4];
					pos[0] = elNodes[0].getPosition().add(
							elNodes[1].getPosition()).scale(0.5);
					pos[1] = elNodes[1].getPosition().add(
							elNodes[2].getPosition()).scale(0.5);
					pos[2] = elNodes[2].getPosition().add(
							elNodes[3].getPosition()).scale(0.5);
					pos[3] = elNodes[3].getPosition().add(
							elNodes[0].getPosition()).scale(0.5);
				}

				// twelve noded
				else if (nn == 12) {

					// get positions of corner nodes
					DVec pos0 = elNodes[0].getPosition();
					DVec pos1 = elNodes[1].getPosition();
					DVec pos2 = elNodes[2].getPosition();
					DVec pos3 = elNodes[3].getPosition();

					// compute positions of additional nodes
					pos = new DVec[8];
					pos[0] = pos0.add(pos1.subtract(pos0).scale(1.0 / 3.0));
					pos[1] = pos0.add(pos1.subtract(pos0).scale(2.0 / 3.0));
					pos[2] = pos1.add(pos2.subtract(pos1).scale(1.0 / 3.0));
					pos[3] = pos1.add(pos2.subtract(pos1).scale(2.0 / 3.0));
					pos[4] = pos2.add(pos3.subtract(pos2).scale(1.0 / 3.0));
					pos[5] = pos2.add(pos3.subtract(pos2).scale(2.0 / 3.0));
					pos[6] = pos3.add(pos0.subtract(pos3).scale(1.0 / 3.0));
					pos[7] = pos3.add(pos0.subtract(pos3).scale(2.0 / 3.0));
				}

				// create additional nodes
				for (int k = 0; k < pos.length; k++) {

					// check if there is any node at that coordinate
					elNodes[4 + k] = checkCoordinates(pos[k]);

					// if there is no, create and add internal node to
					// structure
					if (elNodes[4 + k] == null) {
						elNodes[4 + k] = new Node(pos[k].get(0), pos[k].get(1),
								pos[k].get(2));

						// add node to structure
						structure_.addNode(elNodes[4 + k]);
					}
				}

				// element3
				if (type == ElementLibrary.element3_)
					elements.add(new Element3(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3]));

				// element4
				else if (type == ElementLibrary.element4_)
					elements.add(new Element4(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7]));

				// element7
				else if (type == ElementLibrary.element7_)
					elements.add(new Element7(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7], elNodes[8], elNodes[9],
							elNodes[10], elNodes[11]));

				// element8
				else if (type == ElementLibrary.element8_)
					elements.add(new Element8(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3]));

				// element9
				else if (type == ElementLibrary.element9_)
					elements.add(new Element9(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7]));

				// element10
				else if (type == ElementLibrary.element10_)
					elements.add(new Element10(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7], elNodes[8], elNodes[9],
							elNodes[10], elNodes[11]));

				// element16
				else if (type == ElementLibrary.element16_)
					elements.add(new Element16(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3]));

				// element17
				else if (type == ElementLibrary.element17_)
					elements.add(new Element17(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7]));

				// element19
				else if (type == ElementLibrary.element19_)
					elements.add(new Element19(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3]));

				// element20
				else if (type == ElementLibrary.element20_)
					elements.add(new Element20(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7]));

				// element24
				else if (type == ElementLibrary.element24_)
					elements.add(new Element24(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3]));

				// element25
				else if (type == ElementLibrary.element25_)
					elements.add(new Element25(elNodes[0], elNodes[1],
							elNodes[2], elNodes[3], elNodes[4], elNodes[5],
							elNodes[6], elNodes[7]));
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
	 * Passes element properties to newly created element.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The newly created elements after meshing.
	 */
	private void passElementProperties(Element2D e1, Vector<Element2D> e2) {

		// loop over elements
		for (int i = 0; i < e2.size(); i++) {

			// material
			if (e1.getMaterial() != null)
				e2.get(i).setMaterial(e1.getMaterial());

			// section
			if (e1.getSection() != null)
				e2.get(i).setSection(e1.getSection());

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

			// radii of curvatures
			int typ = e1.getType();
			if (typ == ElementLibrary.element19_
					|| typ == ElementLibrary.element20_)
				e2.get(i).setParameters(e1.getParameters());
		}
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
	private void replaceInGroups(Element2D e, Vector<Element2D> elements) {

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
	private void replaceInStructure(Element2D e, Vector<Element2D> elements) {

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
	 *            The element to be meshed.
	 */
	private void removeNodes(Element2D e) {

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
