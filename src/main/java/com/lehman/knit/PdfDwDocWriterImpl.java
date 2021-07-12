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

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF implementation of the dwDocWriter interface. This class
 * provides support for writing docs in PDF format.
 */
public class PdfDwDocWriterImpl implements DwDocWriter {
    private boolean writeHeaderTable = false;
    private String outputHeaderText = "";
    private String outputFooterText = "";

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
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // If header text is set.
        if (!this.outputHeaderText.equals("")) {
            doc.add(new Paragraph(this.outputHeaderText + System.lineSeparator() + System.lineSeparator()));
        }

        // If write header table is set.
        if (this.writeHeaderTable) {
            this.writeHeaderTable(doc, files, moduleNameList);
        }

        // Go through the module list first and add them in order.
        for (String modName : moduleNameList) {
            DwFile modFile = this.getFileByModuleName(files, modName);
            if (modFile != null) {
                this.writeDoc(doc, modFile);
            } else {
                System.err.println("Warning: Module name '" + modName + "' was supplied in moduleNameList but was not found parsed file list.");
            }
        }

        // Iterate the rest.
        for (DwFile dwf : files) {
            if (!moduleNameList.contains(dwf.getName())) {
                this.writeDoc(doc, dwf);
            }
        }

        // If footer text is set.
        if (!this.outputFooterText.equals("")) {
            doc.add(new Paragraph(this.outputFooterText + System.lineSeparator() + System.lineSeparator()));
        }

        doc.close();

        return out.toByteArray();
    }

    /**
     * Writes a Markdown formatted document with the provided dwFile object and
     * returns a String with the result.
     * @param doc is the PDF document to write to.
     * @param file is a dwFile object to write.
     * @return A String with the doc contents.
     */
    private void writeDoc(Document doc, DwFile file) {
        doc.add(new Paragraph(this.h1(file.name)));
        doc.add(new Paragraph(this.h6(Util.join("::", file.modulePath))));

        if (!file.getComment().getText().equals("")) {
            doc.add(new Paragraph(file.getComment().getText()));
            if (file.getTable() != null) {
                writeAnnotationTable(doc, file.getTable());
                doc.add(new Paragraph(System.lineSeparator()));
            }
        }

        this.writeVariables(doc, file);
        this.writeFunctions(doc, file);
    }

    /**
     * Writes a header table with the provided dwFile list. This
     * table will link to each module further down in the document.
     * @param doc is the PDF document to write to.
     * @param files is a List of dwFile objects to write.
     * @return A String with the header table text.
     */
    private void writeHeaderTable(Document doc, List<DwFile> files) {
        this.writeHeaderTable(doc, files, new ArrayList());
    }

    /**
     * Writes a header table with the provided dwFile list.
     * @param doc is the PDF document to write to.
     * @param files is a List of dwFile objects to write.
     * @param moduleNameList is an optional list of module names that can
     * be provided to specify the order of modules in the table.
     * @return A String with the header table text.
     */
    private void writeHeaderTable(Document doc, List<DwFile> files, List<String> moduleNameList) {
        Table table = new Table(2);
        table.addHeaderCell("Module");
        table.addHeaderCell("Description");

        // Go through the module list first and add them in order.
        for (String modName : moduleNameList) {
            DwFile modFile = this.getFileByModuleName(files, modName);
            if (modFile != null) {
                table.addCell(modFile.getName());
                table.addCell(Util.stripNewLines(modFile.getComment().getText()));
            } else {
                System.err.println("Warning: Module name '" + modName + "' was supplied in moduleNameList but was not found parsed file list.");
            }
        }

        // Iterate the rest.
        for (DwFile dwf : files) {
            if (!moduleNameList.contains(dwf.getName())) {
                table.addCell(dwf.getName());
                table.addCell(Util.stripNewLines(dwf.getComment().getText()));
            }
        }

        doc.add(table);
    }

    /**
     * Gets the file with the provided module name.
     * @param files is the list of files to search.
     * @param name is a String with the file name to look for.
     * @return A DwFile with the result or null if not found.
     */
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
     * @param doc is the PDF document to write to.
     * @param file is the dwObject file to write variables for.
     * @return A String with the variables section.
     */
    private void writeVariables(Document doc, DwFile file) {
        if (file.getVariables().size() > 0) {
            doc.add(new Paragraph(this.h3("Variables")));

            for (DwVariable var : file.getVariables()) {
                Paragraph p = new Paragraph();
                p.add((new Text("var ")).setBold());
                p.add((new Text(var.getName() + System.lineSeparator())).setItalic());
                doc.add(p);

                Paragraph ptext = new Paragraph();
                ptext.setMarginLeft(30);
                ptext.add(var.getComment().getText().replaceAll(System.lineSeparator(), "  " + System.lineSeparator()) + System.lineSeparator() + System.lineSeparator());
                doc.add(ptext);
                if (var.getTable() != null) {
                    writeAnnotationTable(doc, var.getTable());
                }
            }
        }
    }

    /**
     * Writes teh functions section with the provided dwFile
     * object.
     * @param doc is the PDF document to write to.
     * @param file is the dwObject file to write functions for.
     * @return A String with the functions section.
     */
    private void writeFunctions(Document doc, DwFile file) {
        if (file.getFunctions().size() > 0) {
            doc.add(new Paragraph(this.h3("Functions")));

            for (DwFunction fun : file.getFunctions()) {
                Paragraph p = new Paragraph();
                p.add((new Text("fun ")).setBold());
                p.add((new Text(fun.getName())).setItalic());
                p.add(new Text(" ( "));
                this.writeFunctArgs(p, fun);
                p.add(" ) " + System.lineSeparator() + System.lineSeparator());
                doc.add(p);

                this.writeFunctAnnotations(doc, fun);

                Paragraph ptext = new Paragraph();
                ptext.setMarginLeft(30);
                ptext.add(Util.stripNewLines(fun.getComment().getText()) + System.lineSeparator() + System.lineSeparator());
                doc.add(ptext);

                if (fun.getTable() != null) {
                    writeAnnotationTable(doc, fun.getTable());
                    doc.add(new Paragraph(System.lineSeparator()));
                }
            }
        }
    }

    /**
     * Writes the function args with the provided dwFunction object.
     * @param fun is a dwFunction object to write the args for.
     * @return A String with the function args.
     */
    private void writeFunctArgs(Paragraph p, DwFunction fun) {
        for (int i = 0; i < fun.getArguments().size(); i++) {
            if (i > 0) p.add(new Text(", "));
            DwArgument arg = fun.getArguments().get(i);
            if (arg.getDatatype().equals("")) {
                if (!arg.getName().equals("")) {
                    p.add((new Text(arg.getName())).setBold());
                }
            } else {
                p.add((new Text(arg.getName())).setBold());
                p.add(new Text(":"));
                p.add((new Text(arg.getDatatype())).setItalic());
            }
        }
    }

    /**
     * Writes the function annotations with the provided dwFunction object.
     * @param doc is the PDF document to write to.
     * @param fun is a dwFunction object to write annotations for.
     * @return A String with the annotations section.
     */
    private void writeFunctAnnotations(Document doc, DwFunction fun) {
        Paragraph p = new Paragraph();

        DwCommentAnnotation retAnn = null;
        for (DwCommentAnnotation ann : fun.getComment().getAnnotations()) {
            if (ann.getName().toLowerCase().equals(KnitKeyWord.R.getVal()) || ann.getName().toLowerCase().equals(KnitKeyWord.RETURN.getVal())) {
                retAnn = ann;
            } else if (ann.getName().toLowerCase().equals(KnitKeyWord.P.getVal()) || ann.getName().toLowerCase().equals(KnitKeyWord.PARAM.getVal())) {
                p.add((new Text("param ")).setBold());
                p.add((new Text(ann.getKey() + " ")).setItalic());
                p.add(new Text(Util.stripNewLines(ann.getValue()) + "  " + System.lineSeparator()));
            }
        }

        if (retAnn != null) {
            p.add((new Text("return ")).setBold());
            p.add(new Text(Util.stripNewLines(retAnn.getValue()) + "  " + System.lineSeparator()));
        }

        doc.add(p);
    }

    /**
     * Writes the annotation table to string.
     * @param doc is the PDF document to write to.
     * @param tbl is an annotationTable object to write.
     * @return A String with the annotation table.
     */
    private void writeAnnotationTable(Document doc, AnnotationTable tbl) {
        Table table = new Table(tbl.getColumns().size());

        for (String col : tbl.getColumns()) {
            table.addHeaderCell(col);
        }

        for (AnnotationRow row : tbl.getRows()) {
            for (String field : row.getFields()) {
                table.addCell(field);
            }
        }

        doc.add(table);
    }

    /**
     * Creates a large H1 text section with the provided text.
     * @param text is a String with the text.
     * @return A Text object with the header text.
     */
    private Text h1(String text) {
        return new Text(text)
            .setFontSize(36)
            .setBold()
        ;
    }

    /**
     * Creates a medium H3 text section with the provided text.
     * @param text is a String with the text.
     * @return A Text object with the header text.
     */
    private Text h3(String text) {
        return new Text(text)
            .setFontSize(24)
        ;
    }

    /**
     * Creates a small H6 text section with the provided text.
     * @param text is a String with the text.
     * @return A Text object with the header text.
     */
    private Text h6(String text) {
        return new Text(text)
            .setFontSize(16)
        ;
    }

    /**
     * Creates a plan text section with the provided text.
     * @param text is a String with the text.
     * @return A Text object with the provided text.
     */
    private Text text(String text) {
        return new Text(text)
            .setFontSize(12)
            .setBold()
        ;
    }
}
