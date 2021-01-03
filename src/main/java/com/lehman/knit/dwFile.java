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

public class dwFile {
    public String name = "";
    public String fileName = "";
    public String fileNameAndPath = "";
    public ArrayList<String> modulePath = new ArrayList<String>();

    private String commentString = "";
    private dwComment comment = new dwComment();

    public ArrayList<dwFunction> functions = new ArrayList<dwFunction>();
    public ArrayList<dwVariable> variables = new ArrayList<dwVariable>();

    public dwFile() { };

    public dwFile(String FileNameAndPath) {
        this.fileNameAndPath = FileNameAndPath;
        this.fileName = (new File(FileNameAndPath)).getName();
        this.name = fileName.substring(0, fileName.length()-4);
        String pathStr = FileNameAndPath.substring(0, FileNameAndPath.length() - 4);
        this.modulePath = util.fromArray(pathStr.split(File.separator));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameAndPath() {
        return fileNameAndPath;
    }

    public void setFileNameAndPath(String fileNameAndPath) {
        this.fileNameAndPath = fileNameAndPath;
    }

    public ArrayList<String> getModulePath() {
        return modulePath;
    }

    public void setModulePath(ArrayList<String> modulePath) {
        this.modulePath = modulePath;
    }

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

    public ArrayList<dwFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<dwFunction> functions) {
        this.functions = functions;
    }

    public ArrayList<dwVariable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<dwVariable> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        String rstr = "";
        rstr += "fileName: " + this.fileName + "\n";
        rstr += "fileNameAndPath: " + this.fileNameAndPath + "\n";
        rstr += "modulePath: " + util.join("::", this.modulePath) + "\n";
        return rstr;
    }
}
