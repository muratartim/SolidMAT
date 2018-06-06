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

/**
 * Class for Contour panel for displaying result contour on Contour Scalor.
 * 
 * @author Murat
 * 
 */
public class ContourPanel2 extends JPanel {

	private static final long serialVersionUID = 1L;

	private GridBagLayout layout1_;

	private GridBagConstraints constraints1_;

	/** Mother dialog of this panel. */
	protected ContourScalor owner_;

	/**
	 * Creates contour panel for displaying results.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this panel.
	 * @param type
	 *            The type of panel.
	 * @param values
	 *            The values to be displayed together with contour.
	 */
	public ContourPanel2(ContourScalor owner, int type, Object[] values) {

		// set owner
		owner_ = owner;

		// result type contour panel
		if (type == ContourScalor.result_)
			createResultContour(values);

		// deformed type contour panel
		else if (type == ContourScalor.deformed_)
			createDeformedContour(values);
	}

	/**
	 * Creates result type contour panel.
	 * 
	 * @param values
	 *            The values to be displayed on the contour.
	 */
	private void createResultContour(Object[] values) {

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
		constraints1_.insets = new Insets(3, 3, 3, 3);

		// convert values to double values
		double val1 = Double.parseDouble((String) values[0]);
		double val5 = Double.parseDouble((String) values[1]);
		double val9 = Double.parseDouble((String) values[2]);
		double val3 = 0.5 * (val1 + val5);
		double val7 = 0.5 * (val5 + val9);
		double val2 = 0.5 * (val1 + val3);
		double val4 = 0.5 * (val3 + val5);
		double val6 = 0.5 * (val5 + val7);
		double val8 = 0.5 * (val7 + val9);

		// create formatted strings
		String[] vals = new String[9];
		vals[8] = owner_.owner1_.formatter_.format(val1);
		vals[7] = owner_.owner1_.formatter_.format(val2);
		vals[6] = owner_.owner1_.formatter_.format(val3);
		vals[5] = owner_.owner1_.formatter_.format(val4);
		vals[4] = owner_.owner1_.formatter_.format(val5);
		vals[3] = owner_.owner1_.formatter_.format(val6);
		vals[2] = owner_.owner1_.formatter_.format(val7);
		vals[1] = owner_.owner1_.formatter_.format(val8);
		vals[0] = owner_.owner1_.formatter_.format(val9);

		// build sub-panels and labels
		JPanel[] panels = new JPanel[9];
		JLabel[] labels = new JLabel[9];
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel();
			panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));
			panels[i].add(Box.createRigidArea(new Dimension(15, 5)));
			labels[i] = new JLabel(vals[i]);
			if (val1 == val5 && val5 == val9)
				panels[i].setBackground(Color.blue);
		}

		// set color to panels
		if (val1 != val5 && val5 != val9) {
			panels[8].setBackground(new Color(0, 0, 255));
			panels[7].setBackground(new Color(0, 127, 255));
			panels[6].setBackground(new Color(0, 255, 255));
			panels[5].setBackground(new Color(0, 255, 127));
			panels[4].setBackground(new Color(0, 255, 0));
			panels[3].setBackground(new Color(127, 255, 0));
			panels[2].setBackground(new Color(255, 255, 0));
			panels[1].setBackground(new Color(255, 127, 0));
			panels[0].setBackground(new Color(255, 0, 0));
		}

		// add sub-panels and labels to panel
		for (int i = 0; i < panels.length; i++) {
			addComponent(this, panels[i], i, 0, 1, 1);
			addComponent(this, labels[i], i, 1, 1, 1);
		}
	}

	/**
	 * Creates deformed type contour panel.
	 * 
	 * @param values
	 *            The values to be displayed on the contour.
	 */
	private void createDeformedContour(Object[] values) {

		// set background to white
		setBackground(Color.WHITE);

		// build layout manager and set it to panel
		layout1_ = new GridBagLayout();
		setLayout(layout1_);

		// set border to panel
		setBorder(BorderFactory.createTitledBorder(""));

		// build gridbag constraints, make components extend in both directions
		// and determine gaps between components
		constraints1_ = new GridBagConstraints();
		constraints1_.fill = GridBagConstraints.BOTH;
		constraints1_.insets = new Insets(5, 5, 5, 5);

		// create labels
		JLabel label1 = new JLabel((String) values[0]);

		// add labels to panel
		addComponent(this, label1, 0, 0, 1, 1);
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
