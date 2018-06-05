package data;

import java.io.Serializable;
import java.util.Vector;

import element.Element;
import node.Node;

/**
 * Class for storing Group model information.
 *
 * @author Murat
 *
 */
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The name of group. */
	private String name_;

	/** Vector for storing nodes of group. */
	private Vector<Node> nodes_ = new Vector<>();

	/** Vector for storing nodes of group. */
	private Vector<Element> elements_ = new Vector<>();

	/**
	 * Creates Group object.
	 *
	 * @param name
	 *            The name of group.
	 */
	public Group(String name) {
		name_ = name;
	}

	/**
	 * Sets name to group.
	 *
	 * @param name
	 *            The name to be set.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Sets nodes to group.
	 *
	 * @param nodes
	 *            The nodes vector to be set.
	 */
	public void setNodes(Vector<Node> nodes) {
		nodes_ = nodes;
	}

	/**
	 * Sets elements to group.
	 *
	 * @param elements
	 *            The elements vector to be set.
	 */
	public void setElements(Vector<Element> elements) {
		elements_ = elements;
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
	 * Adds given node to group.
	 *
	 * @param node
	 *            The node to be added.
	 */
	public void addNode(Node node) {
		nodes_.add(node);
	}

	/**
	 * Adds given element to group.
	 *
	 * @param element
	 *            The element to be added.
	 */
	public void addElement(Element element) {
		elements_.add(element);
	}

	/**
	 * Inserts given node to group at the given index.
	 *
	 * @param index
	 *            The index for insertion.
	 * @param node
	 *            The node to be inserted.
	 */
	public void insertNode(int index, Node node) {
		nodes_.insertElementAt(node, index);
	}

	/**
	 * Inserts given element to group at the given index.
	 *
	 * @param index
	 *            The index for insertion.
	 * @param element
	 *            The element to be inserted.
	 */
	public void insertElement(int index, Element element) {
		elements_.insertElementAt(element, index);
	}

	/**
	 * Removes given node from group.
	 *
	 * @param node
	 *            The node to be removed.
	 */
	public void removeNode(Node node) {
		nodes_.remove(node);
	}

	/**
	 * Removes given element from group.
	 *
	 * @param element
	 *            The element to be removed.
	 */
	public void removeElement(Element element) {
		elements_.remove(element);
	}

	/**
	 * Checks whether the given node is in group or not.
	 *
	 * @param node
	 *            The node to be checked.
	 * @return True if the given node is in group, False vice versa.
	 */
	public boolean containsNode(Node node) {
		return nodes_.contains(node);
	}

	/**
	 * Checks whether the given element is in group or not.
	 *
	 * @param element
	 *            The element to be checked.
	 * @return True if the given element is in group, False vice versa.
	 */
	public boolean containsElement(Element element) {
		return elements_.contains(element);
	}

	/**
	 * Returns the name of group.
	 *
	 * @return The name of group.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the nodes of group.
	 *
	 * @return The vector storing the nodes of group.
	 */
	public Vector<Node> getNodes() {
		return nodes_;
	}

	/**
	 * Returns the elements of group.
	 *
	 * @return The vector storing the elements of group.
	 */
	public Vector<Element> getElements() {
		return elements_;
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
}
