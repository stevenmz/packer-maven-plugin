# packr-maven-plugin

## About
This tool integrates the [Packr](https://github.com/libgdx/packr) project's ability to create installer packages for Java applications 
into the [Maven](https://maven.apache.org/) build process as a maven build plugin. 

The advantages of integrating the use of Packr into the Maven build, rather than using the Packr api or command line functionality, are that:
* POM files are typically included in source control.
* Developers already know to look in Maven POM files to see how the project build infrastruture workspace.
* Defining installer parameters in the POM makes for ease of use (over scripting the command line version of Packr).
* Not having a seperate project to build for installers:
   * You get to reuse an existing workspace saving disk space and eliminating a redundant build 
   * Fewer projects to track and maintain in automated build systems like Jenkins

This project currently builds against the latest version (v2.0) of Packr.

This work was performed under the auspices of the U.S. Department of Energy by Lawrence Livermore National Laboratory under Contract DE-AC52-07NA27344.

## Usage

Users can choose to define all of the necessary configuration parameters in the Maven POM file, or they can reference OS-specific Packr configurations 
in JSON formats. Examples of both workflows are provided below.

### Specifying Parameters in the POM
For this use-case, we will instruct the plugin to create an OS X application bundle (.app directory). 
You will need to create the JDK zip file yourself. The easiest way to do that is to browse to an installed JDK directory (i.e. 
/Library/Java/JavaVirtualMachines/jdk1.8.0_101.jdk/Contents/Home/ on a Mac) and zip the contents of that directory.

Keep in mind that the directories below are relative to the location of the POM file.

```xml
<build>
    <plugins>
        <!-- Create an OS X app bundle using the packr-maven-plugin -->
        <plugin>
            <groupId>gov.llnl</groupId>                
            <artifactId>packr-maven-plugin</artifactId>
            <version>1.0</version>
            <executions>
                <execution>
                    <!-- Have this plugin run after the JAR is created -->
                    <phase>install</phase>
                    <goals>
                        <goal>packr</goal>
                    </goals>
                    <configuration>
                        <!-- Valid platform values are: MacOS, Linux32, Linux64, Windows32, Windows64 -->
                        <platform>MacOS</platform>
                        <!-- Change this JDK path to match your system and version -->
                        <jdk>/Library/java/jdk1_8.0_101_OSX.zip</jdk>
                        <!-- This is the name of the resulting executable inside the installer package -->
                        <executable>OurApp</executable>
                        <!-- Things to include on the classpath when the executable (JRE) runs. -->
                        <classpath>
                            <param>target/our_app.jar</param>
                        </classpath>
                        <!-- Fully qualified class name. Must contain at least one period. -->
                        <mainClass>gov.llnl.OurApp</mainClass>
                        <!-- Arguments to the JRE without the leading dash -->
                        <vmArgs>
                            <param>Xms500m</param>
                            <param>Xmx1024m</param>
                        </vmArgs>
                        <!-- These resource will be placed alongside your jar in the resulting install package -->
                        <resources>
                            <param>src/main/resources/</param>
                        </resources>
                        <!-- For OS X make sure to include the .app -->
                        <outDir>target/OurSoftwareName.app</outDir>
                        <verbose>true</verbose>
                        <!-- OS X only: Path to the icon set to use for the application bundle -->
                        <iconResource>src/main/resources/myIcon.icns</iconResource>
                        <!-- OS X only: Unique bundle identifier -->
                        <bundleIdentifier>gov.llnl.OurApp</bundleIdentifier>
                    </configuration>
            </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Specifying Parameters in a JSON file
If you want to instead specify a JSON file, then your POM gets shorter but you will need to of course create an extra file for each platform you target.

```xml
<build>
    <plugins>
        <!-- Create an OS X app bundle using the packr-maven-plugin -->
        <plugin>
            <groupId>gov.llnl</groupId>                
            <artifactId>packr-maven-plugin</artifactId>
            <version>1.0</version>
            <executions>
                <execution>
                    <!-- Have this plugin run after the JAR is created -->
                    <phase>install</phase>
                    <goals>
                        <goal>packr</goal>
                    </goals>
                    <configuration>
                        <jsonConfig>src/main/resources/installers/MacOS.json</jsonConfig>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
You would then create this JSON file at the location referenced in the POM. The sample values below match what was defined in the first POM example.
Notice that the platform element has different valid values than the POM does. For JSON, the valid values are: mac, linux32, linux64, windows32, windows64. 

```json
{
    "platform": "mac",
    "jdk": "/Library/java/jdk1_8.0_101_OSX.zip",
    "executable": "OurApp",
    "classpath": [
        "target/our_app.jar"
    ],
    "mainclass": "gov.llnl.OurApp",
    "vmargs": [
       "Xms500m",
       "Xmx1024m"
    ],
    "resources": [
        "src/main/resources"
    ],
    "output": "target/OurSoftwareName.app",
    "verbose":"true",
    "iconResource":"src/main/resources/myIcon.icns",
    "bundleIdentifier":"gov.llnl.OurApp"
}
```

## License
Copyright 2016 Lawrence Livermore National Laboratory

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Contributions
Any and all contributions are welcome! Fork this project, and send a pull request of your changes.