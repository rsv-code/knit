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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HTMLDwDocWriterImpl implements DwDocWriter {
    private String br = "<br/>";
    private boolean writeHeaderTable = false;
    private String outputHeaderText = "";
    private String outputFooterText = "";

    // CSS file name
    private String cssFileName = "knit-style.css";

    /**
     * Gets the current CSS file name.
     * @return A String with the CSS file name.
     */
    public String getCssFileName() {
        return cssFileName;
    }

    /**
     * Sets the CSS file name.
     * @param cssFileName is a String with the CSS file name to set.
     */
    public void setCssFileName(String cssFileName) {
        this.cssFileName = cssFileName;
    }

    /**
     * Sets the provided options for the doc writer.
     * @param WriteHeaderTable is a boolean with True to write the header table and false to not.
     * @param OutputHeaderText is a String with the header text.
     * @param OutputFooterText is a String with the footer text.
     */
    @Override
    public void setOptions(boolean WriteHeaderTable, String OutputHeaderText, String OutputFooterText) {
        this.writeHeaderTable = WriteHeaderTable;
        this.outputHeaderText = OutputHeaderText;
        this.outputFooterText = OutputFooterText;
    }

    /**
     * Writes a Markdown formatted document with the provided list of dwFile objects and
     * returns a String with the result.
     * @param files is a List of dwFile objects to write.
     * @return A String with the doc contents.
     */
    @Override
    public byte[] writeDoc(List<DwFile> files) {
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
    public byte[] writeDoc(List<DwFile> files, List<String> moduleNameList) {
        String ret = "<html>" + System.lineSeparator();
        ret += "<head>" + System.lineSeparator();
        ret += "<title>Knit DW Documentation</title>" + System.lineSeparator();
        ret += "<link rel=\"stylesheet\" href=\"" + this.cssFileName + "\">" + System.lineSeparator();
        ret += "</head>" + System.lineSeparator();
        ret += "<body>" + System.lineSeparator();

        // If header text is set.
        if (!this.outputHeaderText.equals("")) {
            ret += "<div class=\"output-header-section\">" + this.outputHeaderText + "</div>" + this.br + System.lineSeparator();
        }

        // If write header table is set.
        if (this.writeHeaderTable) {
            ret += "<div class=\"output-header-table-section\">" + this.writeHeaderTable(files, moduleNameList) + "</div>" + this.br;
        }

        // Go through the module list first and add them in order.
        for (String modName : moduleNameList) {
            DwFile modFile = this.getFileByModuleName(files, modName);
            if (modFile != null) {
                ret += this.writeDoc(modFile) + this.br + System.lineSeparator();
            } else {
                System.err.println("Warning: Module name '" + modName + "' was supplied in moduleNameList but was not found parsed file list.");
            }
        }

        // Iterate the rest.
        for (DwFile dwf : files) {
            if (!moduleNameList.contains(dwf.getName())) {
                ret += this.writeDoc(dwf) + this.br + System.lineSeparator();
            }
        }

        // If footer text is set.
        if (!this.outputFooterText.equals("")) {
            ret += "<div class=\"output-footer-section\">" + this.outputFooterText + "</div>" + this.br + System.lineSeparator();
        }

        ret += "</body>" + System.lineSeparator();
        ret += "</html>" + System.lineSeparator();
        return ret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Writes a Markdown formatted document with the provided dwFile object and
     * returns a String with the result.
     * @param file is a dwFile object to write.
     * @return A String with the doc contents.
     */
    private String writeDoc(DwFile file) {
        String ret = "<h1 id=\"" + file.name + "\">" + file.name + "</h1>" + System.lineSeparator();
        ret += "<h3>" + Util.join("::", file.modulePath) + "</h3>" + System.lineSeparator();
        if (!file.getComment().getText().equals("")) {
            ret += file.getComment().getText() + this.br + System.lineSeparator();
            if (file.getTable() != null) {
                ret += writeAnnotationTable(file.getTable()) + this.br + System.lineSeparator();
            }
        }
        ret += this.br + System.lineSeparator();

        String vars = this.writeVariables(file);
        if (!vars.equals("")) {
            ret += "<h3>Variables</h3>" + System.lineSeparator();
            ret += vars + this.br + System.lineSeparator();
        }

        String functs = this.writeFunctions(file);
        if (!functs.equals("")) {
            ret += "<h3>Functions</h3>" + System.lineSeparator();
            ret += functs + this.br + System.lineSeparator();
        }

        return ret;
    }

    /**
     * Writes a header table with the provided dwFile list. This
     * table will link to each module further down in the document.
     * @param files is a List of dwFile objects to write.
     * @return A String with the header table text.
     */
    private String writeHeaderTable(List<DwFile> files) {
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
    private String writeHeaderTable(List<DwFile> files, List<String> moduleNameList) {
        String ret = "<table class=\"table-header\">";
        ret += "<tr class=\"table-header-header-row\"><th class=\"table-header-header-cell\">Module</th><th class=\"table-header-header-cell\">Description</th></tr>" + System.lineSeparator();

        // Go through the module list first and add them in order.
        for (String modName : moduleNameList) {
            DwFile modFile = this.getFileByModuleName(files, modName);
            if (modFile != null) {
                ret += "<tr class=\"table-header-row\"><td class=\"table-header-cell\"><a href=\"#" + modFile.getName() + "\">" + modFile.getName() + "</a></td><td class=\"table-header-cell\">" + Util.stripNewLines(modFile.getComment().getText()) + "</td></tr>" + System.lineSeparator();
            } else {
                System.err.println("Warning: Module name '" + modName + "' was supplied in moduleNameList but was not found parsed file list.");
            }
        }

        // Iterate the rest.
        for (DwFile dwf : files) {
            if (!moduleNameList.contains(dwf.getName())) {
                ret += "<tr class=\"table-header-row\"><td class=\"table-header-cell\"><a href=\"#" + dwf.getName() + "\">" + dwf.getName() + "</a></td><td class=\"table-header-cell\">" + Util.stripNewLines(dwf.getComment().getText()) + "</td></tr>" + System.lineSeparator();
            }
        }

        ret += "</table>" + System.lineSeparator();


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

        if (file.getVariables().size() > 0) {
            ret += "<div class=\"variable-section\">";
            for (DwVariable var : file.getVariables()) {
                ret += "<div class=\"variable-title-line\"><span class=\"variable-var\">var</span> <span class=\"variable-name\">" + var.getName() + "</span></div>" + this.br + System.lineSeparator();
                ret += "<div class=\"variable-body-section\">" + this.br + System.lineSeparator();
                ret += "<div class=\"variable-comment-text\">" + var.getComment().getText().replaceAll(System.lineSeparator(), this.br + System.lineSeparator()) + "</div>" + this.br + System.lineSeparator();
                if (var.getTable() != null) {
                    ret += writeAnnotationTable(var.getTable()) + this.br + System.lineSeparator();
                }
                ret += "</div>" + this.br + System.lineSeparator();
            }
            ret += "</div>" + System.lineSeparator();
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

        if (file.getFunctions().size() > 0) {
            ret += "<div class=\"function-section\">";
            for (DwFunction fun : file.getFunctions()) {
                ret += "<div class=\"function-title-line\"><span class=\"function-fun\">fun</span>  <span class=\"function-name\">" + fun.getName() + "</span> (<span class=\"function-args\">" + this.writeFunctArgs(fun) + "</span>)</div>" + this.br + System.lineSeparator();
                ret += "<div class=\"function-body-section\">" + this.br + System.lineSeparator();
                ret += this.writeFunctAnnotations(fun) + this.br + System.lineSeparator();
                ret += "<div class=\"function-comment-text\">" + Util.stripNewLines(fun.getComment().getText()) + "</div>" + this.br + System.lineSeparator();
                if (fun.getTable() != null) {
                    ret += writeAnnotationTable(fun.getTable()) + this.br + System.lineSeparator();
                }
                ret += "</div>" + this.br + System.lineSeparator();
            }
            ret += "</div>" + System.lineSeparator();
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
                    ret += "<span class=\"function-arg-name\">" + arg.getName() + "</span>";
                }
            } else {
                ret += "<span class=\"function-arg-name\">" + arg.getName() + "</span>:<span class=\"function-arg-datatype\">" + arg.getDatatype() + "</span>";
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
            if (ann.getName().toLowerCase().equals(KnitKeyWord.R.getVal()) || ann.getName().toLowerCase().equals(KnitKeyWord.RETURN.getVal())) {
                retAnn = ann;
            } else if (ann.getName().toLowerCase().equals(KnitKeyWord.P.getVal()) || ann.getName().toLowerCase().equals(KnitKeyWord.PARAM.getVal())) {
                ret += "<span class=\"function-ann-param\">param</span> <span class=\"function-ann-key\">" + ann.getKey() + "</span> <span class=\"function-ann-value\">" + Util.stripNewLines(ann.getValue()) + "</span>" + this.br + System.lineSeparator();
            }
        }

        if (retAnn != null) {
            ret += "<span class=\"function-ann-return\">return</span> <span class=\"function-ann-return-value\">" + Util.stripNewLines(retAnn.getValue()) + "</span>" + this.br + System.lineSeparator();
        }

        String retstr = "";
        if (!ret.equals("")) {
            retstr = "<div class=\"function-ann-section\">" + ret + "</div>";
        }

        return retstr;
    }

    /**
     * Writes the annotation table to string.
     * @param tbl is an annotationTable object to write.
     * @return A String with the annotation table.
     */
    private String writeAnnotationTable(AnnotationTable tbl) {
        String ret = "<table class=\"table-ann\">";
        ret += "<tr class=\"table-ann-header-row\"><th class=\"table-ann-header-cell\">" + Util.join("</th><th class=\"table-ann-header-cell\">", tbl.getColumns()) + "</th></tr>" + System.lineSeparator();
        for (AnnotationRow row : tbl.getRows()) {
            ret += "<tr class=\"table-ann-row\"><td class=\"table-ann-cell\">" + Util.join("</td><td class=\"table-ann-cell\">", row.getFields()) + "</td></tr>" + System.lineSeparator();
        }
        ret += "</table>";
        return ret;
    }
}
