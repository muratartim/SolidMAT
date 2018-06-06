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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.Vector;

/**
 * Class for Contour panel for displaying assignment contour on Contour Scalor.
 * 
 * @author Murat
 * 
 */
public class ContourPanel1 extends JPanel {

	private static final long serialVersionUID = 1L;

	private GridBagLayout layout1_;

	private GridBagConstraints constraints1_;

	/**
	 * Creates contour panel for displaying assignments.
	 * 
	 * @param type
	 *            The type of panel.
	 * @param values
	 *            The values to be displayed together with contour.
	 */
	public ContourPanel1(int type, Object[] values) {

		// build layout manager and set it to panel
		layout1_ = new GridBagLayout();
		setLayout(layout1_);

		// set border to panels
		setBorder(BorderFactory.createTitledBorder(""));

		// set background to white
		setBackground(Color.WHITE);

		// build gridbag constraints, make components extend in both directions
		// and determine gaps between components
		constraints1_ = new GridBagConstraints();
		constraints1_.fill = GridBagConstraints.BOTH;
		constraints1_.insets = new Insets(5, 5, 5, 5);

		// get names and colors of assignment
		Vector names = (Vector) values[0];
		Vector colors = (Vector) values[1];

		// build sub-panels and labels
		JPanel[] panels = new JPanel[names.size()];
		JLabel[] labels = new JLabel[names.size()];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel();
			panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));
			panels[i].setBackground((Color) colors.get(i));
			panels[i].add(Box.createRigidArea(new Dimension(15, 5)));
			labels[i] = new JLabel((String) names.get(i));
		}

		// add sub-panels and labels to panel
		for (int i = 0; i < panels.length; i++) {
			addComponent(this, panels[i], i, 0, 1, 1);
			addComponent(this, labels[i], i, 1, 1, 1);
		}
	}

	/**
	 * Sets gridbag layout constraints and adds components to layout.
	 * 
	 * @param component
	 *            Component to be added into layout.
	 * @param row
	 *            Y coordinate of the upper-left corner of the component.
	 * @param column
	 *            X coordinate of the upper-left corner of the component.
	 * @param width
	 *            Cell number captured by the component's width.
	 * @param height
	 *            Cell number captured by the component's height.
	 */
	private void addComponent(JPanel panel, Component component, int row,
			int column, int width, int height) {

		// set gridx and gridy
		constraints1_.gridx = column;
		constraints1_.gridy = row;

		// set gridwidth and gridheight
		constraints1_.gridwidth = width;
		constraints1_.gridheight = height;

		// set constraints and add component to panel1
		layout1_.setConstraints(component, constraints1_);
		panel.add(component);
	}
}
