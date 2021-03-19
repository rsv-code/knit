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

/**
 * Enum that stores annotation key words.
 */
public enum KnitKeyWord {
    // Param
    P("p"),
    PARAM("param"),

    // Return
    R("r"),
    RETURN("return"),

    // Table
    TBL("tbl"),
    TABLE("table"),

    // Row
    ROW("row");

    private String val;

    KnitKeyWord(String Val) {
        this.val = Val;
    }

    public String getVal() {
        return val;
    }
}
