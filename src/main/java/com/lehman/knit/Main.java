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
import java.util.Arrays;

/**
 * The main entry point class implements the normal main
 * function as well as the Mojo execute function for
 * maven plugin support.
 */
@Mojo(name = "knit", defaultPhase = LifecyclePhase.COMPILE)
public class Main extends AbstractMojo {
    /**
     * This isn't used but there in case it's needed later. It provides
     * the maven project information to the execute() function.
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    /**
     * Maven config value generate.
     * Flag to run the doc parser/generator. This is the on/off switch for the maven plugin..
     */
    @Parameter(property = "generate")
    boolean generate = true;

    /**
     * Maven config value files.
     * A list of DW files to parse.
     */
    @Parameter(property = "files")
    String[] files;

    /**
     * Maven config value directories.
     * A list of DW directories to parse.
     */
    @Parameter(property = "directories")
    String[] directories = new String[]{ "src/main/resources/dw" };

    /**
     * Maven config value singleOutputFile.
     * Flag to switch between files for each module and a single output file.
     */
    @Parameter(property = "singleOutputFile")
    boolean singleOutputFile = true;

    /**
     * Maven config value outputFile.
     * The output file to write to when singleOutputFile == true.
     */
    @Parameter(property = "outputFile")
    String outputFile = "target/knit-doc.md";

    /**
     * Maven config value outputHeaderText.
     * If set this will be output in the document at
     * the top of the document as is. Set as a CDATA element.
     */
    @Parameter(property = "outputHeaderText")
    String outputHeaderText = "";

    /**
     * Maven config value writeHeaderTable.
     * If set to true this will write a table towards
     * the top of the doc that links to each module below. This
     * works in conjunction with headerTableModuleList.
     */
    @Parameter(property = "writeHeaderTable")
    boolean writeHeaderTable = false;

    /**
     * Maven config value headerTableModuleList.
     * If writeHeaderTable is set to true then this list
     * can be provided to provide the specified order of modules
     * in the header table.
     */
    @Parameter(property = "moduleList")
    String[] moduleList = new String[0];

    /**
     * Accessor to set the directories. So as to not overwrite the initial value, this checks
     * to see if the provided list is > 0 before replacing.
     * @param Directories is an array of strings with the list of directories to parse.
     */
    public void setDirectories(String[] Directories) { if (Directories.length > 0) { this.directories = Directories; } }

    /**
     * Accessor to set the files.
     * @param Files is an array of strings with the list of files to parse.
     */
    public void setFiles(String[] Files) { this.files = Files; }

    /**
     * Accessor to set the module list.
     * @param ModuleList is an array of strings with the module list specified.
     */
    public void setModuleList(String[] ModuleList) { this.moduleList = ModuleList; }

    /**
     * Main entry point of the application. This is currently just for testing.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //knitParser parser = new knitParser();
        //dwFile f = parser.parseFile("dw/test.dw");

        ArrayList<dwFile> parsedFiles = new ArrayList<dwFile>();
        Main.parseDirectory("dw", "dw", parsedFiles);

        dwDocWriter writer = new markdownDwDocWriterImpl();
        String doc = writer.writeDoc(parsedFiles);
        System.out.println(doc);
    }

    /**
     * Parses a DW directory with the provided arguments.
     * @param rootDirName is a String with the root directory to parse.
     * @param dirName is a String with the directory name.
     * @param parsedFiles is an ArrayList of dwFile objects to store the parsed results.
     * @throws Exception
     */
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

    /**
     * The entry point of the maven plugin.
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.printAbout();
        System.out.println("Running Knit doc generator ...");

        if (this.generate) {
            if (!this.singleOutputFile) {
                System.err.println("Error: knit-maven-plugin <singleOutputFile> is set to false but only single file is currently implemented.");
                System.exit(1);
            }

            if (this.files.length > 0 || this.directories.length > 0) {
                this.writeDwFile();
            } else {
                System.err.println("Error: knit-maven-plugin <srcFiles> or <srcDirectories> aren't specified.");
                System.exit(1);
            }
        } else {
            System.out.println("Info: knit-maven-plugin skipping doc generation. (generate=false)");
        }
    }

    /**
     * Writes the dataweave doc file.
     */
    private void writeDwFile() {
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
            String doc = "";

            // If header text is set.
            if (!this.outputHeaderText.equals("")) {
                doc += this.outputHeaderText + "\n\n";
            }

            // If write header table is set.
            if (this.writeHeaderTable) {
                doc += writer.writeHeaderTable(parsedFiles, Arrays.asList(this.moduleList));
            }

            // Write the doc.
            doc += writer.writeDoc(parsedFiles, Arrays.asList(this.moduleList));

            // Output to file.
            util.write(this.outputFile, doc, false);
            System.out.println("Document has been written to '" + this.outputFile + "'.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: knit-maven-plugin parse failed.");
            System.exit(1);
        }
    }

    /**
     * Prints the about text to standard output.
     */
    private void printAbout() {
        String out = "";
        out += " __  __     __   __     __     ______      _____     ______     ______                            \n" +
                "/\\ \\/ /    /\\ \"-.\\ \\   /\\ \\   /\\__  _\\    /\\  __-.  /\\  __ \\   /\\  ___\\                           \n" +
                "\\ \\  _\"-.  \\ \\ \\-.  \\  \\ \\ \\  \\/_/\\ \\/    \\ \\ \\/\\ \\ \\ \\ \\/\\ \\  \\ \\ \\____                          \n" +
                " \\ \\_\\ \\_\\  \\ \\_\\\\\"\\_\\  \\ \\_\\    \\ \\_\\     \\ \\____-  \\ \\_____\\  \\ \\_____\\                         \n" +
                "  \\/_/\\/_/   \\/_/ \\/_/   \\/_/     \\/_/      \\/____/   \\/_____/   \\/_____/                         \n" +
                "                                                                                                  \n" +
                " ______     ______     __   __     ______     ______     ______     ______   ______     ______    \n" +
                "/\\  ___\\   /\\  ___\\   /\\ \"-.\\ \\   /\\  ___\\   /\\  == \\   /\\  __ \\   /\\__  _\\ /\\  __ \\   /\\  == \\   \n" +
                "\\ \\ \\__ \\  \\ \\  __\\   \\ \\ \\-.  \\  \\ \\  __\\   \\ \\  __<   \\ \\  __ \\  \\/_/\\ \\/ \\ \\ \\/\\ \\  \\ \\  __<   \n" +
                " \\ \\_____\\  \\ \\_____\\  \\ \\_\\\\\"\\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_\\ \\_\\    \\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\ \n" +
                "  \\/_____/   \\/_____/   \\/_/ \\/_/   \\/_____/   \\/_/ /_/   \\/_/\\/_/     \\/_/   \\/_____/   \\/_/ /_/ \n" +
                "                                                                                                  \n";
        out += "Knit 1.0 - DataWeave Document Generator\n";
        out += "Written By Austin Lehman\n";
        out += "austin@rosevillecode.com\n";
        out += "Copyright 2020 Roseville Code Inc.\n";
        System.out.println(out);
    }
}