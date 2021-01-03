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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "knit", defaultPhase = LifecyclePhase.COMPILE)
public class Main extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "generate")
    boolean generate = true;

    @Parameter(property = "files")
    String[] files;

    @Parameter(property = "directories")
    String[] directories = new String[]{ "src/main/resources/dw" };

    @Parameter(property = "singleOutputFile")
    boolean singleOutputFile = true;

    @Parameter(property = "outputFile")
    String outputFile = "target/knit-doc.md";

    public void setDirectories(String[] Directories) { if (Directories.length > 0) { this.directories = Directories; } }
    public void setFiles(String[] Files) { this.files = Files; }

    public static void main(String[] args) throws Exception {
        //knitParser parser = new knitParser();
        //dwFile f = parser.parseFile("dw/test.dw");

        ArrayList<dwFile> parsedFiles = new ArrayList<dwFile>();
        Main.parseDirectory("dw", "dw", parsedFiles);

        dwDocWriter writer = new markdownDwDocWriterImpl();
        String doc = writer.writeDoc(parsedFiles);
        System.out.println(doc);
    }

    public static void parseDirectory(String rootDirName, String dirName, ArrayList<dwFile> parsedFiles) throws Exception {
        knitParser parser = new knitParser();
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            for (String name : dir.list()) {
                String relName = dirName + "/" + name;
                File f = new File(relName);
                if (f.isFile() && relName.endsWith(".dwl")) {
                    parsedFiles.add(parser.parseFile(rootDirName, relName));
                } else if (f.isDirectory()) {
                    parseDirectory(rootDirName, relName, parsedFiles);
                }
            }
        } else {
            throw new Exception("Provided directory '" + dirName + "' isn't a directory.");
        }
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        this.printAbout();
        System.out.println("Running Knit doc generator ...");

        if (this.generate) {
            if (!this.singleOutputFile) {
                System.err.println("Error: knit-maven-plugin <singleOutputFile> is set to false but only single file is currently implemented.");
                System.exit(1);
            }

            if (this.files.length > 0 || this.directories.length > 0) {
                ArrayList<dwFile> parsedFiles = new ArrayList<dwFile>();

                try {
                    // Parse directories
                    for (String dir : this.directories) {
                        Main.parseDirectory(dir, dir, parsedFiles);
                    }

                    // Parse files
                    knitParser parser = new knitParser();
                    for (String fname : this.files) {
                        File f = new File(fname);
                        parsedFiles.add(parser.parseFile(f.getParent(), fname));
                    }

                    // Create the doc writer and write the doc.
                    dwDocWriter writer = new markdownDwDocWriterImpl();
                    String doc = writer.writeDoc(parsedFiles);
                    util.write(this.outputFile, doc, false);
                    System.out.println("Document has been written to '" + this.outputFile + "'.");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error: knit-maven-plugin parse failed.");
                    System.exit(1);
                }


            } else {
                System.err.println("Error: knit-maven-plugin <srcFiles> or <srcDirectories> aren't specified.");
                System.exit(1);
            }
        } else {
            System.out.println("Info: knit-maven-plugin skipping doc generation. (generate=false)");
        }
    }

    private void printAbout() {
        String out = "";
        out += "**************************************************\n";
        out += "* Knit 1.0 - DataWeave Document Generator\n";
        out += "* Written By Austin Lehman\n";
        out += "* austin@rosevillecode.com\n";
        out += "* Copyright 2020 Roseville Code Inc.\n";
        out += "**************************************************\n";
        System.out.println(out);
    }
}