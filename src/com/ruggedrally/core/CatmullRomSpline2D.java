/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.ruggedrally.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * Encapsulates a catmull rom spline with n control points, n >= 4. For more information on this type of spline see
 * http://www.mvps.org/directx/articles/catmull/.
 * 
 * @author badlogicgames@gmail.com
 * 
 */
public class CatmullRomSpline2D implements Serializable {
	private static final long serialVersionUID = -3290464799289771451L;
	private List<Vector2> controlPoints = new ArrayList<Vector2>();
	Vector2 T1 = new Vector2();
	Vector2 T2 = new Vector2();

	/**
	 * Adds a new control point
	 * 
	 * @param point the point
	 */
	public void add (Vector2 point) {
		controlPoints.add(point);
	}

	/**
	 * @return all control points
	 */
	public List<Vector2> getControlPoints () {
		return controlPoints;
	}

	/**
	 * Returns a path, between every two control points numPoints are generated and the control points themselves are added too.
	 * The first and the last controlpoint are omitted. if there's less than 4 controlpoints an empty path is returned.
	 * 
	 * @param numPoints number of points returned for a segment
	 * @return the path
	 */
	public List<Vector2> getPath (int numPoints) {
		ArrayList<Vector2> points = new ArrayList<Vector2>();

		if (controlPoints.size() < 4) return points;

		Vector2 T1 = new Vector2();
		Vector2 T2 = new Vector2();

		for (int i = 1; i <= controlPoints.size() - 3; i++) {
			points.add(controlPoints.get(i));
			float increment = 1.0f / (numPoints + 1);
			float t = increment;

			T1.set(controlPoints.get(i + 1)).sub(controlPoints.get(i - 1)).mul(0.5f);
			T2.set(controlPoints.get(i + 2)).sub(controlPoints.get(i)).mul(0.5f);

			for (int j = 0; j < numPoints; j++) {
				float h1 = 2 * t * t * t - 3 * t * t + 1; // calculate basis
				// function 1
				float h2 = -2 * t * t * t + 3 * t * t; // calculate basis
				// function 2
				float h3 = t * t * t - 2 * t * t + t; // calculate basis
				// function 3
				float h4 = t * t * t - t * t; // calculate basis function 4

				Vector2 point = new Vector2(controlPoints.get(i)).mul(h1);
				point.add(controlPoints.get(i + 1).tmp().mul(h2));
				point.add(T1.tmp().mul(h3));
				point.add(T2.tmp().mul(h4));
				points.add(point);
				t += increment;
			}
		}

		if (controlPoints.size() >= 4) points.add(controlPoints.get(controlPoints.size() - 2));

		return points;
	}

	/**
	 * Returns a path, between every two control points numPoints are generated and the control points themselves are added too.
	 * The first and the last controlpoint are omitted. if there's less than 4 controlpoints an empty path is returned.
	 * 
	 * @param points the array of Vector2 instances to store the path in
	 * @param numPoints number of points returned for a segment
	 */	
	public void getPath (Vector2[] points, int numPoints) {		
		int idx = 0;
		if (controlPoints.size() < 4) return;	

		for (int i = 1; i <= controlPoints.size() - 3; i++) {
			points[idx++].set(controlPoints.get(i));
			float increment = 1.0f / (numPoints + 1);
			float t = increment;

			T1.set(controlPoints.get(i + 1)).sub(controlPoints.get(i - 1)).mul(0.5f);
			T2.set(controlPoints.get(i + 2)).sub(controlPoints.get(i)).mul(0.5f);

			for (int j = 0; j < numPoints; j++) {
				float h1 = 2 * t * t * t - 3 * t * t + 1; // calculate basis
				// function 1
				float h2 = -2 * t * t * t + 3 * t * t; // calculate basis
				// function 2
				float h3 = t * t * t - 2 * t * t + t; // calculate basis
				// function 3
				float h4 = t * t * t - t * t; // calculate basis function 4

				Vector2 point = points[idx++].set(controlPoints.get(i)).mul(h1);
				point.add(controlPoints.get(i + 1).tmp().mul(h2));
				point.add(T1.tmp().mul(h3));
				point.add(T2.tmp().mul(h4));				
				t += increment;
			}
		}

		points[idx].set(controlPoints.get(controlPoints.size() - 2));	
	}

	/**
	 * Returns all tangents for the points in a path. Same semantics as getPath.
	 * 
	 * @param numPoints number of points returned for a segment
	 * @return the tangents of the points in the path
	 */
	public List<Vector2> getTangents (int numPoints) {
		ArrayList<Vector2> tangents = new ArrayList<Vector2>();

		if (controlPoints.size() < 4) return tangents;

		Vector2 T1 = new Vector2();
		Vector2 T2 = new Vector2();

		for (int i = 1; i <= controlPoints.size() - 3; i++) {
			float increment = 1.0f / (numPoints + 1);
			float t = increment;

			T1.set(controlPoints.get(i + 1)).sub(controlPoints.get(i - 1)).mul(0.5f);
			T2.set(controlPoints.get(i + 2)).sub(controlPoints.get(i)).mul(0.5f);

			tangents.add(new Vector2(T1).nor());

			for (int j = 0; j < numPoints; j++) {
				float h1 = 6 * t * t - 6 * t; // calculate basis function 1
				float h2 = -6 * t * t + 6 * t; // calculate basis function 2
				float h3 = 3 * t * t - 4 * t + 1; // calculate basis function 3
				float h4 = 3 * t * t - 2 * t; // calculate basis function 4

				Vector2 point = new Vector2(controlPoints.get(i)).mul(h1);
				point.add(controlPoints.get(i + 1).tmp().mul(h2));
				point.add(T1.tmp().mul(h3));
				point.add(T2.tmp().mul(h4));
				tangents.add(point.nor());
				t += increment;
			}
		}

		if (controlPoints.size() >= 4)
			tangents.add(T1.set(controlPoints.get(controlPoints.size() - 1)).sub(controlPoints.get(controlPoints.size() - 3))
				.mul(0.5f).cpy().nor());

		return tangents;
	}

	/**
	 * Returns all tangent's normals in 2D space for the points in a path. The controlpoints have to lie in the x/y plane for this
	 * to work. Same semantics as getPath.
	 * 
	 * @param numPoints number of points returned for a segment
	 * @return the tangents of the points in the path
	 */
	public List<Vector2> getTangentNormals2D (int numPoints) {
		ArrayList<Vector2> tangents = new ArrayList<Vector2>();

		if (controlPoints.size() < 4) return tangents;

		Vector2 T1 = new Vector2();
		Vector2 T2 = new Vector2();

		for (int i = 1; i <= controlPoints.size() - 3; i++) {
			float increment = 1.0f / (numPoints + 1);
			float t = increment;

			T1.set(controlPoints.get(i + 1)).sub(controlPoints.get(i - 1)).mul(0.5f);
			T2.set(controlPoints.get(i + 2)).sub(controlPoints.get(i)).mul(0.5f);

			Vector2 normal = new Vector2(T1).nor();
			float x = normal.x;
			normal.x = normal.y;
			normal.y = -x;
			tangents.add(normal);

			for (int j = 0; j < numPoints; j++) {
				float h1 = 6 * t * t - 6 * t; // calculate basis function 1
				float h2 = -6 * t * t + 6 * t; // calculate basis function 2
				float h3 = 3 * t * t - 4 * t + 1; // calculate basis function 3
				float h4 = 3 * t * t - 2 * t; // calculate basis function 4

				Vector2 point = new Vector2(controlPoints.get(i)).mul(h1);
				point.add(controlPoints.get(i + 1).tmp().mul(h2));
				point.add(T1.tmp().mul(h3));
				point.add(T2.tmp().mul(h4));
				point.nor();
				x = point.x;
				point.x = point.y;
				point.y = -x;
				tangents.add(point);
				t += increment;
			}
		}

		return tangents;
	}

}
