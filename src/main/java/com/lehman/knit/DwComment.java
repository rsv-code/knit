/*
 * Copyright 2021 Roseville Code Inc. (austin@rosevillecode.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.lehman.knit;

import java.util.ArrayList;

/**
 * Class models a comment.
 */
public class DwComment {
    /**
     * The text of the comment.
     */
    private String text = "";

    /**
     * A list of dwCommentAnnotation objects that are the annotations.
     */
    private ArrayList<DwCommentAnnotation> annotations = new ArrayList<DwCommentAnnotation>();

    /**
     * Gets the comment text.
     * @return A String with the comment text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the comment text.
     * @param text is a String with the comment text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets a list of the annotations for the comment.
     * @return An ArrayList of dwCommentAnnotation objects with the annotations.
     */
    public ArrayList<DwCommentAnnotation> getAnnotations() {
        return annotations;
    }

    /**
     * Sets the annotations.
     * @param annotations is an ArrayList of dwCommentAnnotation objects to set.
     */
    public void setAnnotations(ArrayList<DwCommentAnnotation> annotations) {
        this.annotations = annotations;
    }
}
