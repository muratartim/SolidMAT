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
package main;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * Class for Status bar of main frame.
 * 
 * @author Murat Artim
 * 
 */
public class Statusbar extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel label1_;

	/**
	 * Creates Statusbar for the main frame.
	 * 
	 */
	public Statusbar() {

		// build panel and set layout
		super(new FlowLayout(FlowLayout.LEFT, 27, 5));

		// set color
		setBackground(new Color(240, 240, 240));

		// build labels
		label1_ = new JLabel("Ready");

		// add components
		add(label1_);
	}

	/**
	 * Sets given text to label of Statusbar.
	 * 
	 * @param text
	 *            The text to be set.
	 */
	public void setText(String text) {

		// check if null
		if (text == null)
			label1_.setText("Ready");
		else
			label1_.setText(text);
	}

	/**
	 * Returns the text of statusbar.
	 * 
	 * @return The text of statusbar.
	 */
	public String getText() {
		return label1_.getText();
	}
}
