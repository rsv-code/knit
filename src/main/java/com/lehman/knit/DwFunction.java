/*
 * Copyright 2020 Roseville Code Inc. (austin@rosevillecode.com)
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
 * Class models a dataweave function.
 */
public class DwFunction extends AnnotationTableImpl {
    /**
     * The comment string.
     */
    private String commentString = "";

    /**
     * The comment object.
     */
    private DwComment comment = new DwComment();

    /**
     * The function name.
     */
    private String name = "";

    /**
     * The argument list.
     */
    private ArrayList<DwArgument> arguments = new ArrayList<DwArgument>();

    /**
     * Gets the comment string of the function.
     * @return A String with the comment of the function.
     */
    public String getCommentString() {
        return commentString;
    }

    /**
     * Sets the comment string of the function.
     * @param commentString is a String to set as the comment of the function.
     */
    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    /**
     * Gets the comment object of the function.
     * @return A dwComment object with the function comment.
     */
    public DwComment getComment() {
        return comment;
    }

    /**
     * Sets the comment object of the function.
     * @param comment is a dwComment object to set for the function.
     */
    public void setComment(DwComment comment) {
        this.comment = comment;
    }

    /**
     * Gets the function name.
     * @return A String with the function name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the function.
     * @param name is a String with the function name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the argument list.
     * @return An ArrayList of dwArgument objects with the argument list.
     */
    public ArrayList<DwArgument> getArguments() {
        return arguments;
    }

    /**
     * Sets the argument list.
     * @param arguments is an ArrayList of dwArguments to set as the argument list.
     */
    public void setArguments(ArrayList<DwArgument> arguments) {
        this.arguments = arguments;
    }
}
