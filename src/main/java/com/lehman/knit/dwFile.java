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

import java.io.File;
import java.util.ArrayList;

/**
 * Class models a dataweave file.
 */
public class dwFile {
    /**
     * The DW file name. (myModule)
     */
    public String name = "";

    /**
     * The DW filename. (myModule.dwl)
     */
    public String fileName = "";

    /**
     * The DW file name and path. (dw/test/myModule.dwl)
     */
    public String fileNameAndPath = "";

    /**
     * The DW module path as a list of strings. { "dw", "test", "myModule" }
     */
    public ArrayList<String> modulePath = new ArrayList<String>();

    /**
     * The module/file comment string.
     */
    private String commentString = "";

    /**
     * The module/file comment as a dwComment object.
     */
    private dwComment comment = new dwComment();

    /**
     * List of module/file functions.
     */
    public ArrayList<dwFunction> functions = new ArrayList<dwFunction>();

    /**
     * List of module/file variables.
     */
    public ArrayList<dwVariable> variables = new ArrayList<dwVariable>();

    /**
     * Default constructor.
     */
    public dwFile() { }

    /**
     * Constructor with provided file name and path.
     * @param FileNameAndPath is a String with the file name and path.
     */
    public dwFile(String FileNameAndPath) {
        this.fileNameAndPath = FileNameAndPath;
        this.fileName = (new File(FileNameAndPath)).getName();
        this.name = fileName.substring(0, fileName.length()-4);
        String pathStr = FileNameAndPath.substring(0, FileNameAndPath.length() - 4);
        this.modulePath = util.fromArray(pathStr.split(File.separator));
    }

    /**
     * Gets the DW file name. This is the module name of the file. For instance
     * if the module is dw/myModule.dwl then myModule will be returned.
     * @return A String with the DW file name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the DW file name. This is the module name of the file. For instance
     * if the module is dw/myModule.dwl then myModule is what should be provided.
     * @param name is a String with the DW file name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the DW file name. This is the actual name such as myModule.dwl.
     * @return A String with the DW file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the DW file name. This is the actual name such as myModule.dwl.
     * @param fileName is a String with the DW file name.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the DW file with name and path.
     * @return A String with the DW file name and path.
     */
    public String getFileNameAndPath() {
        return fileNameAndPath;
    }

    /**
     * Sets the DW file with name and path.
     * @param fileNameAndPath is a String with the DW file name and path.
     */
    public void setFileNameAndPath(String fileNameAndPath) {
        this.fileNameAndPath = fileNameAndPath;
    }

    /**
     * This returns a list of Strings that make up the module path
     * from the start of the root module directory.
     * @return An ArrayList of Strings with the module path parts.
     */
    public ArrayList<String> getModulePath() {
        return modulePath;
    }

    /**
     * Sets the module paths that make up the module path from
     * the start of the root module directory.
     * @param modulePath is an ArrayList of Strings with the module path.
     */
    public void setModulePath(ArrayList<String> modulePath) {
        this.modulePath = modulePath;
    }

    /**
     * Gets the module/file comment string.
     * @return A String with the module comment string.
     */
    public String getCommentString() {
        return commentString;
    }

    /**
     * Sets the module/file comment string.
     * @param commentString is a String with the module comment.
     */
    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    /**
     * Gets the module comment object.
     * @return A dwComment object.
     */
    public dwComment getComment() {
        return comment;
    }

    /**
     * Sets the module comment object.
     * @param comment is a dwComment object of the module.
     */
    public void setComment(dwComment comment) {
        this.comment = comment;
    }

    /**
     * Gets the list of functions of this module/file.
     * @return An ArrayList of dwFunction objects with the functions of this module.
     */
    public ArrayList<dwFunction> getFunctions() {
        return functions;
    }

    /**
     * Sets the list of functions of this module/file.
     * @param functions is an ArrayList of dwFunction objects to set.
     */
    public void setFunctions(ArrayList<dwFunction> functions) {
        this.functions = functions;
    }

    /**
     * Gets a list of variables of the module/file.
     * @return An ArrayList of dwVariable objects of the module.
     */
    public ArrayList<dwVariable> getVariables() {
        return variables;
    }

    /**
     * Sets a list of variables of the module/file.
     * @param variables is an ArrayList of dwVariable objects of the module.
     */
    public void setVariables(ArrayList<dwVariable> variables) {
        this.variables = variables;
    }

    /**
     * Helper toString function.
     * @return A String with the object representation.
     */
    @Override
    public String toString() {
        String rstr = "";
        rstr += "fileName: " + this.fileName + "\n";
        rstr += "fileNameAndPath: " + this.fileNameAndPath + "\n";
        rstr += "modulePath: " + util.join("::", this.modulePath) + "\n";
        return rstr;
    }
}
