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
import java.util.List;

/**
 * Markdown implementation of the dwDocWriter interface. This class
 * provides support for writing docs in Markdown format.
 */
public class MarkdownDwDocWriterImpl implements DwDocWriter {
    /**
     * Writes a Markdown formatted document with the provided dwFile object and
     * returns a String with the result.
     * @param file is a dwFile object to write.
     * @return A String with the doc contents.
     */
    @Override
    public String writeDoc(DwFile file) {
        String ret = "# " + file.name + "\n";
        ret += "###### " + Util.join("::", file.modulePath) + "\n";
        if (!file.getComment().getText().equals("")) {
            ret += file.getComment().getText() + "\n";
        }
        ret += "\n";

        String vars = this.writeVariables(file);
        if (!vars.equals("")) {
            ret += "### Variables\n";
            ret += vars + "\n";
        }

        String functs = this.writeFunctions(file);
        if (!functs.equals("")) {
            ret += "### Functions\n";
            ret += functs + "\n";
        }

        return ret;
    }

    /**
     * Writes a Markdown formatted document with the provided list of dwFile objects and
     * returns a String with the result.
     * @param files is a List of dwFile objects to write.
     * @return A String with the doc contents.
     */
    @Override
    public String writeDoc(List<DwFile> files) {
        return this.writeDoc(files, new ArrayList());
    }

    /**
     * Writes a doc with the provided dwFile list and moduleNameList.
     * @param files is a List of dwFile objects to write.
     * @param moduleNameList is an optional list of module names that can
     * be provided to specify the order of modules.
     * @return A String with the document text.
     */
    @Override
    public String writeDoc(List<DwFile> files, List<String> moduleNameList) {
        String ret = "";

        // Go through the module list first and add them in order.
        for (String modName : moduleNameList) {
            DwFile modFile = this.getFileByModuleName(files, modName);
            if (modFile != null) {
                ret += this.writeDoc(modFile) + "\n";
            } else {
                System.err.println("Warning: Module name '" + modName + "' was supplied in moduleNameList but was not found parsed file list.");
            }
        }

        // Iterate the rest.
        for (DwFile dwf : files) {
            if (!moduleNameList.contains(dwf.getName())) {
                ret += this.writeDoc(dwf) + "\n";
            }
        }

        return ret;
    }

    /**
     * Writes a header table with the provided dwFile list. This
     * table will link to each module further down in the document.
     * @param files is a List of dwFile objects to write.
     * @return A String with the header table text.
     */
    @Override
    public String writeHeaderTable(List<DwFile> files) {
        return this.writeHeaderTable(files, new ArrayList());
    }

    /**
     * Writes a header table with the provided dwFile list. This
     * table will link to each module further down in the document.
     * @param files is a List of dwFile objects to write.
     * @param moduleNameList is an optional list of module names that can
     * be provided to specify the order of modules in the table.
     * @return A String with the header table text.
     */
    @Override
    public String writeHeaderTable(List<DwFile> files, List<String> moduleNameList) {
        String ret = "";
        ret += "| Module | Description |\n";
        ret += "|-|-|\n";

        // Go through the module list first and add them in order.
        for (String modName : moduleNameList) {
            DwFile modFile = this.getFileByModuleName(files, modName);
            if (modFile != null) {
                ret += "| [" + modFile.getName() + "](#" + modFile.getName() + ") | " + Util.stripNewLines(modFile.getComment().getText()) + " |\n";
            } else {
                System.err.println("Warning: Module name '" + modName + "' was supplied in moduleNameList but was not found parsed file list.");
            }
        }

        // Iterate the rest.
        for (DwFile dwf : files) {
            if (!moduleNameList.contains(dwf.getName())) {
                ret += "| [" + dwf.getName() + "](#" + dwf.getName() + ") | " + Util.stripNewLines(dwf.getComment().getText()) + " |\n";
            }
        }

        ret += "\n";

        return ret;
    }

    private DwFile getFileByModuleName(List<DwFile> files, String name) {
        DwFile ret = null;
        for(DwFile dwf : files) {
            if (dwf.getName().equals(name)) {
                ret = dwf;
                break;
            }
        }
        return ret;
    }

    /**
     * Writes the variables section with the provided dwFile
     * object.
     * @param file is the dwObject file to write variables for.
     * @return A String with the variables section.
     */
    private String writeVariables(DwFile file) {
        String ret = "";

        for(DwVariable var : file.getVariables()) {
            ret += "__var__ `" + var.getName() + "`\n";
            ret += "> " + var.getComment().getText().replaceAll("\n", "  \n") + "\n\n";
        }

        return ret;
    }

    /**
     * Writes teh functions section with the provided dwFile
     * object.
     * @param file is the dwObject file to write functions for.
     * @return A String with the functions section.
     */
    private String writeFunctions(DwFile file) {
        String ret = "";

        for(DwFunction fun : file.getFunctions()) {
            ret += "__fun__ `" + fun.getName() + "` ( " + this.writeFunctArgs(fun) + ")\n\n";
            ret += this.writeFunctAnnotations(fun) + "\n";
            ret += "> " + Util.stripNewLines(fun.getComment().getText()) + "\n\n";
            if (fun.getTable() != null) {
                ret += writeAnnotationTable(fun.getTable()) + "\n\n";
            }
        }

        return ret;
    }

    /**
     * Writes the function args with the provided dwFunction object.
     * @param fun is a dwFunction object to write the args for.
     * @return A String with the function args.
     */
    private String writeFunctArgs(DwFunction fun) {
        String ret = "";

        for (int i = 0; i < fun.getArguments().size(); i++) {
            if (i > 0) ret += ", ";
            DwArgument arg = fun.getArguments().get(i);
            if (arg.getDatatype().equals("")) {
                if (!arg.getName().equals("")) {
                    ret += "__" + arg.getName() + "__";
                }
            } else {
                ret += "__" + arg.getName() + "__:_" + arg.getDatatype() + "_";
            }
        }

        return ret;
    }

    /**
     * Writes the function annotations with the provided dwFunction object.
     * @param fun is a dwFunction object to write annotations for.
     * @return A String with the annotations section.
     */
    private String writeFunctAnnotations(DwFunction fun) {
        String ret = "";

        DwCommentAnnotation retAnn = null;
        for (DwCommentAnnotation ann : fun.getComment().getAnnotations()) {
            if (ann.getName().toLowerCase().equals(KnitKeyWord.R) || ann.getName().toLowerCase().equals(KnitKeyWord.RETURN)) {
                retAnn = ann;
            } else if (ann.getName().toLowerCase().equals(KnitKeyWord.P) || ann.getName().toLowerCase().equals(KnitKeyWord.PARAM)) {
                ret += "__param__ `" + ann.getKey() + "` " + Util.stripNewLines(ann.getValue()) + "  \n";
            }
        }

        if (retAnn != null) {
            ret += "__return__ " + Util.stripNewLines(retAnn.getValue()) + "  \n";
        }

        if (!ret.equals("")) {
            ret = "> " + ret + "> ";
        }

        return ret;
    }

    /**
     * Writes the annotation table to string.
     * @param tbl is an annotationTable object to write.
     * @return A String with the annotation table.
     */
    private String writeAnnotationTable(AnnotationTable tbl) {
        String ret = "";
        ret += "> | " + Util.join(" | ", tbl.getColumns()) + " | \n";
        // divider
        ret += "> | ";
        for (int i = 0; i < tbl.getColumns().size(); i++) {
            ret += "-|";
        }
        ret += "\n";
        for (AnnotationRow row : tbl.getRows()) {
            ret += "> | " + Util.join(" | ", row.getFields()) + " | \n";
        }
        return ret;
    }
}
