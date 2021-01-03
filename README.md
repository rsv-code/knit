![Knit Logo](knit.png)

# Knit - Dataweave Document Generator

Written by Austin Lehman

Knit is a Maven plugin that generates documentation from source code 
comments in DataWeave files. 

Here's how it works. You write comments like this in your dwl files.

```
/**
 * Maps a color object to a result color object.
 * @p data is an input color object.
 * @r a result color object.
 */
fun mapColor(data) = {
...
```
 
Then you add the plugin to your pom like this.

```
<plugin>
    <groupId>com.lehman</groupId>
    <artifactId>knit-maven-plugin</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <goals>
                <goal>knit</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Then when you build your project it will generate a knit-doc.md file in the 
target directory. It's that simple.


# Install

To install the plugin just clone the repo and then install with maven.
```
$ git clone git@github.com:rsv-code/knit.git
$ cd knit
$ mvn clean install
```

Done and done, that's all you need to use in your project. Just add it to 
the pom.xml and you're golden.


# Usage

Since this is a Maven plugin all you have to do is reference it in the 
pom.xml of the project you want to use it in so long as you already 
locally installed the plugin.

```
<plugin>
    <groupId>com.lehman</groupId>
    <artifactId>knit-maven-plugin</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <goals>
                <goal>knit</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!-- Generate docs ... run the plugin? -->
        <generate>true</generate>
        
        <!-- Make a single output file. -->
        <singleOutputFile>true</singleOutputFile>
        
        <!-- Direcories to look for files to parse. By default src/main/resources/dw is set by the plugin.
        <directories>
            <dir>src/main/resources/dw</dir>
        </directories>
        -->
        
        <!-- Individual files can be listed to parse.
        <files>
            <file>test1.dwl</file>
            <file>test2.dwl</file>
        </files>
         -->
         
         <!-- Specify the output file to save to. By default it saves to target/knit-doc.md.
         <outputFile>target/knit-doc.md</outputFile>
         -->
    </configuration>
</plugin>
```

The configuration section is optional, if not specified it will use 
the default values which should work for most cases.

- **generate** - A flag to run or not to run the Knit doc generator. Set 
  to false if you want it to skip generation.
- **singleOutputFile** - A flag to specify if it should generate a single 
  output file or a file for each module. Currently only single output file 
  is supported.
- **directories** - A list of directories to look for .dwl files. If not 
  specified it will look in rc/main/resources/dw. If specified it will look 
  at just those directories you set.
- **files** - A list of files parse.
- **outputFile** - A string with the output file to write to. By deafult this 
  writes to target/knit-doc.md.


# Comments

There are 3 comment blocks that can be used to generate docs and they are 
module, variable, and function.

Module level documentation is set at the very begining of the module before 
the %dw declaration and starting with /** like this.
```
/**
 * This module supports color realted functions.
 */

%dw 2.0
...
```

Variable documentation is implemented by writing a comment block above the 
variable declaration.
```
/**
 * First name of the author.
 */
var first = "Austin"
```

Finally, documentation of a function is accomplished by writing a comment block 
above the function. In function comments you can set annotations to define 
parameters and return values by using @p and @r respectively. 
```
/**
 * Maps a color object to a result color object.
 * @p data is an input color object.
 * @r a result color object.
 */
fun mapColor(data) = {
...
```

# License
Copyright 2020 Roseville Code Inc. (austin@rosevillecode.com)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.