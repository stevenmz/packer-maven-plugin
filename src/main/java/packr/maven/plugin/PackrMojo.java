/*
 * packr-maven-plugin
 *
 * Copyright (C) 2016 Lawrence Livermore National Laboratory
 * http://www.llnl.gov
 * All Rights Reserved
 *
 * This work was performed under the auspices of the U.S. Department of Energy by Lawrence Livermore National Laboratory under Contract DE-AC52-07NA27344.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package packr.maven.plugin;

import com.badlogicgames.packr.Packr;
import com.badlogicgames.packr.PackrConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * A maven plugin mojo to pack an application based on options passed or via
 * JSON configuration files.
 *
 * @author Steven Magana-Zook (maganazook1@llnl.gov)
 */
@Mojo(name = "packr", defaultPhase = LifecyclePhase.INSTALL)
public class PackrMojo extends AbstractMojo {

    /**
     * The bundle identifier of your Java application, e.g. "com.my.app". This
     * is for OS X targets (see {@link Platform#MacOS} ).
     */
    @Parameter(property = "bundleIdentifier")
    private String bundleIdentifier;

    /**
     * list of file locations of the JAR files to package
     */
    @Parameter(property = "classpath")
    private List<String> classpath;

    /**
     * Name of the native executable, without extension such as ".exe"
     */
    @Parameter(property = "executable")
    private String executable;

    /**
     * Location of an AppBundle icon resource (.icns file) Note: This is for OS
     * X targets (see {@link Platform#MacOS} ).
     *
     * @see Platform
     */
    @Parameter(property = "iconResource")
    private File iconResource;
    /**
     * ZIP file location to an Oracle JDK build containing a JRE.
     */
    @Parameter(property = "jdk")
    private String jdk;
    /**
     * You can put all the command line arguments into a JSON file. The JSON, if
     * provided, takes precedence over the other configuration values.
     *
     * Typically, you either specify JSON or you specify the configuration
     * parameters manually.
     */
    @Parameter(property = "jsonConfig")
    private File jsonConfig;

    /**
     * The fully qualified name of the main class, using dots to delimit package
     * names
     */
    @Parameter(property = "mainClass")
    private String mainClass;

    /**
     * Minimize the JRE by removing directories and files as specified by an
     * additional config file. Comes with a 'soft' and 'hard' configurations out
     * of the box.
     */
    @Parameter(property = "minimizeJre")
    private String minimizeJre;

    /**
     * The output directory.
     */
    @Parameter(property = "outDir")
    private File outDir;

    /**
     * Defines the platform target for which to build an installer package. One
     * of "Windows32", "Windows64", "Linux32", "Linux64", "MacOS"
     */
    @Parameter(property = "platform")
    private PackrConfig.Platform platform;

    /**
     * list of files and directories to be packaged next to the native
     * executable.
     */
    @Parameter(property = "resources")
    private List<File> resources;

    /**
     * Prints more status information during processing, which can be useful for
     * debugging.
     */
    @Parameter(property = "verbose", defaultValue = "true")
    private boolean verbose;
    /**
     * List of arguments for the JVM, without leading dashes, e.g. "Xmx1G"
     */
    @Parameter(property = "vmArgs")
    private List<String> vmArgs;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Starting packr-maven-plugin...");

        // toString outputs the fields as a json object. Can be useful for debugging.
        if (this.verbose) {
            getLog().info(this.toString());
        }

        PackrConfig config = null;
        if (this.jsonConfig != null && this.jsonConfig.exists()) {
            //get values from JSON
            getLog().info("Using values from JSON configuration file.");
            PackrJSONCommandLine commandline = new PackrJSONCommandLine(this.jsonConfig);
            try {
                config = new PackrConfig(commandline);
            } catch (IOException ex) {
                getLog().error("Error creating installation bundle!", ex);
                return;
            }
        } else {
            config = new PackrConfig();
            config.platform = this.platform;
            config.jdk = this.jdk;
            config.executable = this.executable;
            config.classpath = this.classpath;
            config.mainClass = this.mainClass;
            config.vmArgs = this.vmArgs;
            config.minimizeJre = this.minimizeJre;
            config.outDir = this.outDir;
        }

        // Try to catch some conditions that will make Packr throw exceptions
        File fleJdk = new File(config.jdk);
        if (config.jdk == null || !fleJdk.exists() || fleJdk.isDirectory()) {
            String errorMsg = "Invalid JDK path provided. This path should be a ZIP file of a JDK directory.";
            getLog().error(errorMsg);
            throw new MojoExecutionException(errorMsg);
        }

        // See if the user specified any non-existent locations to add to the classpath
        List<String> validClasspathEntries = new ArrayList<>();
        for (String cpEntry : classpath) {
            File fleEntry = new File(cpEntry);
            if (!fleEntry.exists()) {
                getLog().warn(cpEntry + " is not a valid location. Entry will be removed from list of classpath entries.");
            } else {
                validClasspathEntries.add(cpEntry);
            }
        }
        classpath.clear();
        classpath.addAll(validClasspathEntries);
        validClasspathEntries = null;

        if (this.mainClass.contains(".") == false) {
            throw new MojoExecutionException("Main class must contain at least one '.'. Please provide the fully qualified path to the main class.");
        }

        // Build the installation bundle
        try {
            new Packr().pack(config);
            getLog().info("Successfully wrote the application installer!");
        } catch (Exception e) {
            getLog().error("Error creating installation bundle!", e);
        }
    }

    @Override
    public String toString() {
        return "PackrMojo{" + "platform=" + platform
                + ", jdk=" + jdk
                + ", executable=" + executable
                + ", classpath=" + classpath
                + ", mainClass=" + mainClass
                + ", vmArgs=" + vmArgs
                + ", minimizeJre=" + minimizeJre
                + ", resources=" + resources
                + ", outDir=" + outDir
                + ", iconResource=" + iconResource
                + ", bundleIdentifier=" + bundleIdentifier
                + ", jsonConfig=" + jsonConfig
                + ", verbose=" + verbose + '}';
    }

}
