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

import java.util.List;

/**
 * The interface that models the Knit document writer.
 */
public interface DwDocWriter {
    /**
     * Sets the provided options for the doc writer.
     * @param writeHeaderTable is a boolean with true to write header table and false for not.
     * @param outputHeaderText is a String with the header text.
     * @param outputFooterText is a String with the footer text.
     */
    public void setOptions(
        boolean writeHeaderTable,
        String outputHeaderText,
        String outputFooterText
    );

    /**
     * Writes a doc with the provided dwFile list.
     * @param files is a List of dwFile objects to write.
     * @return A byte array with the document.
     */
    public byte[] writeDoc(List<DwFile> files);

    /**
     * Writes a doc with the provided dwFile list and moduleNameList.
     * @param files is a List of dwFile objects to write.
     * @param moduleNameList is an optional list of module names that can
     * be provided to specify the order of modules.
     * @return A byte array with the document.
     */
    public byte[] writeDoc(List<DwFile> files, List<String> moduleNameList);
}
