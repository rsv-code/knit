package com.lehman.knit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class knitParser {
    public knitParser() {}

    public dwFile parseFile(String fileName) throws IOException {
        dwFile ret = new dwFile(fileName);
        String fileStr = util.read(fileName);
        ret.setVariables(this.parseVariables(fileStr));
        ret.setFunctions(this.parseFunctions(fileStr));
        return ret;
    }

    private ArrayList<dwFunction> parseFunctions(String text) {
        ArrayList<dwFunction> ret = new ArrayList<dwFunction>();

        // Get functions sections.
        String funPatternStr = "(\\/\\*\\*[^\\/]+?\\*\\/\\s*fun\\s*\\w*\\(.*?\\))";
        Pattern r = Pattern.compile(funPatternStr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(text);
        while (m.find()) {
            for (int i = 0; i < m.groupCount(); i++) {
                ret.add(this.parseFunctionString(m.group(i).toString()));
            }
        }

        return ret;
    }

    private dwFunction parseFunctionString(String functionString) {
        dwFunction funct = new dwFunction();

        String funPatternStr = "\\/\\*\\*(.+?)\\*\\/\\s*fun\\s*(\\w*)\\((.*?)\\)";
        Pattern r = Pattern.compile(funPatternStr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(functionString);
        while (m.find()) {
            funct.setCommentString(this.parseCommentString(m.group(1).toString()).trim());
            funct.setName(m.group(2).toString());
            funct.setComment(this.parseComment(funct.getCommentString()));
            funct.setArguments(this.parseArguments(m.group(3).toString()));
        }

        return funct;
    }

    private String parseCommentString(String str) {
        String ret = "";

        for (String line : str.toString().split("\n")) {
            ret += line.replaceFirst("^\\s\\*\\s", "") + "\n";
        }

        return ret;
    }

    private dwComment parseComment(String str) {
        dwComment comment = new dwComment();

        String pstr = "(.*?)(^@.*)";

        Pattern r = Pattern.compile(pstr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(str);
        if (m.find()) {
            comment.setText(m.group(1).toString());
            String annStr = m.group(2).toString();
            comment.setAnnotations(this.parseAnnotations(annStr));
        } else {
            comment.setText(str);
        }

        return comment;
    }

    private ArrayList<dwCommentAnnotation> parseAnnotations(String str) {
        ArrayList<dwCommentAnnotation> ret = new ArrayList<dwCommentAnnotation>();
        String pstr = "^@(\\w+)\\s(.*?(?=@))";
        Pattern r = Pattern.compile(pstr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(str + "\n@");
        while (m.find()) {
            dwCommentAnnotation ann = new dwCommentAnnotation();
            ann.setName(m.group(1).toString());
            String kvStr = m.group(2).toString();
            if (ann.getName().equals("p")) {
                this.parseAnnotationValue(kvStr, ann);
            } else {
                ann.setValue(kvStr);
            }
            ret.add(ann);
        }
        return ret;
    }

    private void parseAnnotationValue(String str, dwCommentAnnotation ann) {
        String pstr = "(\\w+)\\s(.*)";
        Pattern r = Pattern.compile(pstr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(str);
        if (m.find()) {
            ann.setKey(m.group(1).toString());
            ann.setValue(m.group(2).toString());
        }
    }

    private ArrayList<dwArgument> parseArguments(String str) {
        ArrayList<dwArgument> args = new ArrayList<dwArgument>();
        String parts[] = str.split(",");
        for(String part : parts) {
            dwArgument arg = new dwArgument();
            if (part.contains(":")) {
                String argParts[] = part.split(":");

                arg.setName(argParts[0].trim());
                arg.setDatatype(argParts[1].trim());
            } else {
                arg.setName(part.trim());
            }
            args.add(arg);
        }
        return args;
    }

    private ArrayList<dwVariable> parseVariables(String text) {
        ArrayList<dwVariable> variables = new ArrayList<dwVariable>();

        // Get functions sections.
        String funPatternStr = "(\\/\\*\\*[^\\/]+?\\*\\/\\s*var\\s*\\w*)";
        Pattern r = Pattern.compile(funPatternStr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(text);
        while (m.find()) {
            for (int i = 0; i < m.groupCount(); i++) {
                variables.add(this.parseVariableString(m.group(i).toString()));
            }
        }

        return variables;
    }

    private dwVariable parseVariableString(String variableString) {
        dwVariable var = new dwVariable();

        String varPatternStr = "\\/\\*\\*(.+?)\\*\\/\\s*var\\s*(\\w*)";
        Pattern r = Pattern.compile(varPatternStr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher m = r.matcher(variableString);
        while (m.find()) {
            var.setCommentString(this.parseCommentString(m.group(1).toString()).trim());
            var.setName(m.group(2).toString());
            var.setComment(this.parseComment(var.getCommentString()));
        }

        return var;
    }
}
