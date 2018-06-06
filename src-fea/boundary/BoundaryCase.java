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
package boundary;

import java.io.Serializable;

/**
 * Class for storing data coming from Boundary Case Library menu.
 * 
 * @author Murat
 * 
 */
public class BoundaryCase implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The name of the boundary case object. */
	private String name_;

	/**
	 * Stores the given parameters into attributes.
	 * 
	 * @param name
	 *            The name of boundary case.
	 */
	public BoundaryCase(String name) {
		name_ = name;
	}

	/**
	 * Sets the name of boundary case.
	 * 
	 * @param name
	 *            The name of boundary case.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Returns the name of boundary case.
	 * 
	 * @return name_ The name of boundary case.
	 */
	public String getName() {
		return name_;
	}
}
