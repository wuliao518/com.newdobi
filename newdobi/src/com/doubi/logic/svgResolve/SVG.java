package com.doubi.logic.svgResolve;

import java.util.List;

import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;

/*

 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
/**
 * Describes a vector Picture object, and optionally its bounds.
 *
 * @author Larva Labs, LLC
 */
public class SVG {

	/**
	 * The parsed Picture object.
	 */
	private Picture picture;

	/**
	 * These are the bounds for the SVG specified as a hidden "bounds" layer in
	 * the SVG.
	 */
	private RectF bounds;

	/**
	 * These are the estimated bounds of the SVG computed from the SVG elements
	 * while parsing. Note that this could be null if there was a failure to
	 * compute limits (ie. an empty SVG).
	 */
	private RectF limits = null;

	/**
	 * 返回svg对应的曲线
	 */
	private List<Path> path;

	/**
	 * Construct a new SVG.
	 * 
	 * @param picture
	 *            the parsed picture object.
	 * @param bounds
	 *            the bounds computed from the "bounds" layer in the SVG.
	 */
	SVG(Picture picture, RectF bounds, List<Path> path) {
		this.picture = picture;
		this.bounds = bounds;
		this.path = path;
	}

	/**
	 * Set the limits of the SVG, which are the estimated bounds computed by the
	 * parser.
	 * 
	 * @param limits
	 *            the bounds computed while parsing the SVG, may not be entirely
	 *            accurate.
	 */
	void setLimits(RectF limits) {
		this.limits = limits;
	}

	/**
	 * Create a picture drawable from the SVG.
	 * 
	 * @return the PictureDrawable.
	 */
	public PictureDrawable createPictureDrawable() {
		return new PictureDrawable(picture);
		// return new PictureDrawable(picture) {
		// @Override
		// public int getIntrinsicWidth() {
		// if (bounds != null) {
		// return (int) bounds.width();
		// } else if (limits != null) {
		// return (int) limits.width();
		// } else {
		// return -1;
		// }
		// }
		//
		// @Override
		// public int getIntrinsicHeight() {
		// if (bounds != null) {
		// return (int) bounds.height();
		// } else if (limits != null) {
		// return (int) limits.height();
		// } else {
		// return -1;
		// }
		// }
		// };
	}

	/**
	 * Get the parsed SVG picture data.
	 * 
	 * @return the picture.
	 */
	public Picture getPicture() {
		return picture;
	}

	/**
	 * Gets the bounding rectangle for the SVG, if one was specified.
	 * 
	 * @return rectangle representing the bounds.
	 */
	public RectF getBounds() {
		return bounds;
	}

	/**
	 * Gets the bounding rectangle for the SVG that was computed upon parsing.
	 * It may not be entirely accurate for certain curves or transformations,
	 * but is often better than nothing.
	 * 
	 * @return rectangle representing the computed bounds.
	 */
	public RectF getLimits() {
		return limits;
	}

	/**
	 * 获取曲线
	 * 
	 * @return
	 */
	public Path getPath() {
		Path repath = new Path();
		for (Path mpath : path) {
			repath.addPath(mpath);
		}
		return repath;
	}

}
