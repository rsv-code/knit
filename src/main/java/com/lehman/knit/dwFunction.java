/*
 * Copyright 2020 Austin Lehman (austin@rosevillecode.com)
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

public class dwFunction {
    private String commentString = "";
    private dwComment comment = new dwComment();
    private String name = "";
    private ArrayList<dwArgument> arguments = new ArrayList<dwArgument>();

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    public dwComment getComment() {
        return comment;
    }

    public void setComment(dwComment comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<dwArgument> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<dwArgument> arguments) {
        this.arguments = arguments;
    }
}
