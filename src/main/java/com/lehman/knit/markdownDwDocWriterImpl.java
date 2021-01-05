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

import java.util.List;

/**
 * Markdown implementation of the dwDocWriter interface. This class
 * provides support for writing docs in Markdown format.
 */
public class markdownDwDocWriterImpl implements dwDocWriter {
    /**
     * Writes a Markdown formatted document with the provided dwFile object and
     * returns a String with the result.
     * @param file is a dwFile object to write.
     * @return A String with the doc contents.
     */
    @Override
    public String writeDoc(dwFile file) {
        String ret = "# " + file.name + "\n";
        ret += "###### " + util.join("::", file.modulePath) + "\n";
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
    public String writeDoc(List<dwFile> files) {
        String ret = "";

        for(dwFile f : files) {
            ret += this.writeDoc(f) + "\n";
        }
        return ret;
    }

    /**
     * Writes the variables section with the provided dwFile
     * object.
     * @param file is the dwObject file to write variables for.
     * @return A String with the variables section.
     */
    private String writeVariables(dwFile file) {
        String ret = "";

        for(dwVariable var : file.getVariables()) {
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
    private String writeFunctions(dwFile file) {
        String ret = "";

        for(dwFunction fun : file.getFunctions()) {
            ret += "__fun__ `" + fun.getName() + "` ( " + this.writeFunctArgs(fun) + ")\n\n";
            ret += this.writeFunctAnnotations(fun) + "\n";
            ret += "> " + fun.getComment().getText().replaceAll("\n", "  \n") + "\n\n";
        }

        return ret;
    }

    /**
     * Writes the function args with the provided dwFunction object.
     * @param fun is a dwFunction object to write the args for.
     * @return A String with the function args.
     */
    private String writeFunctArgs(dwFunction fun) {
        String ret = "";

        for (int i = 0; i < fun.getArguments().size(); i++) {
            if (i > 0) ret += ", ";
            dwArgument arg = fun.getArguments().get(i);
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
    private String writeFunctAnnotations(dwFunction fun) {
        String ret = "";

        dwCommentAnnotation retAnn = null;
        for (dwCommentAnnotation ann : fun.getComment().getAnnotations()) {
            if (ann.getName().toLowerCase().equals("r")) {
                retAnn = ann;
            } else if (ann.getName().toLowerCase().equals("p")) {
                ret += "__param__ `" + ann.getKey() + "` " + ann.getValue().replaceAll("\n", "  \n");
            }
        }

        if (retAnn != null) {
            ret += "__return__ " + retAnn.getValue().replaceAll("\n", "  \n");
        }

        if (!ret.equals("")) {
            ret = "> " + ret + "> ";
        }

        return ret;
    }
}
