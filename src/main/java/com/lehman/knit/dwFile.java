package com.lehman.knit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class dwFile {
    public String fileName = "";
    public String fileNameAndPath = "";
    public ArrayList<String> modulePath = new ArrayList<String>();
    public ArrayList<dwFunction> functions = new ArrayList<dwFunction>();
    public ArrayList<dwVariable> variables = new ArrayList<dwVariable>();

    public dwFile() { };

    public dwFile(String FileNameAndPath) {
        this.fileNameAndPath = FileNameAndPath;
        this.fileName = (new File(FileNameAndPath)).getName();
        String pathStr = FileNameAndPath.substring(0, FileNameAndPath.length() - 3);
        System.out.println(pathStr);
        this.modulePath = util.fromArray(pathStr.split(File.separator));
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
