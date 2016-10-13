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
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * A maven plugin mojo to pack an application based on options passed or via
 * JSON configuration files.
 *
 * @author Steven Magana-Zook (maganazook1@llnl.gov)
 */
@Mojo(name = "packr")
public class PackrMojo extends AbstractMojo {

    /**
     * The bundle identifier of your Java application, e.g. "com.my.app". This
     * is for OS X targets (see {@link Platform#MacOS} ).
     */
    @Parameter(property = "packr.bundleIdentifier")
    private String _bundleIdentifier;

    /**
     * list of file locations of the JAR files to package
     */
    @Parameter(property = "classpath")
    private List<String> _classpath;

    /**
     * Name of the native executable, without extension such as ".exe"
     */
    @Parameter(property = "executable")
    private String _executable;

    /**
     * Location of an AppBundle icon resource (.icns file) Note: This is for OS
     * X targets (see {@link Platform#MacOS} ).
     *
     * @see Platform
     */
    @Parameter(property = "packr.iconResource")
    private File _iconResource;
    /**
     * ZIP file location to an Oracle JDK build containing a JRE.
     */
    @Parameter(property = "jdk")
    private String _jdk;
    /**
     * You can put all the command line arguments into a JSON file. The JSON, if
     * provided, takes precedence over the other configuration values.
     *
     * Typically, you either specify JSON or you specify the configuration
     * parameters manually.
     */
    @Parameter(property = "packr.jsonConfig")
    private File _jsonConfig;

    /**
     * The fully qualified name of the main class, using dots to delimit package
     * names
     */
    @Parameter(property = "mainClass")
    private String _mainClass;

    /**
     * Minimize the JRE by removing directories and files as specified by an
     * additional config file. Comes with a 'soft' and 'hard' configurations out
     * of the box.
     */
    @Parameter(property = "minimizeJre")
    private String _minimizeJre;

    /**
     * The output directory.
     */
    @Parameter(property = "outDir")
    private File _outDir;

    /**
     * Defines the platform target for which to build an installer package. One
     * of "Windows32", "Windows64", "Linux32", "Linux64", "MacOS"
     */
    @Parameter(property = "platform")
    private PackrConfig.Platform _platform;

    /**
     * list of files and directories to be packaged next to the native
     * executable.
     */
    @Parameter(property = "resources")
    private List<File> _resources;

    /**
     * Prints more status information during processing, which can be useful for
     * debugging.
     */
    @Parameter(property = "packr.verbose", defaultValue = "true")
    private boolean _verbose;
    /**
     * List of arguments for the JVM, without leading dashes, e.g. "Xmx1G"
     */
    @Parameter(property = "vmArgs")
    private List<String> _vmArgs;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Starting packr-maven-plugin...");

        // toString outputs the fields as a json object. Can be useful for debugging.
        if (this._verbose) {
            getLog().info(this.toString());
        }

        PackrConfig config = null;
        if (this._jsonConfig != null && this._jsonConfig.exists()) {
            //get values from JSON
            getLog().info("Using values from JSON configuration file.");
            PackrJSONCommandLine commandline = new PackrJSONCommandLine(this._jsonConfig);
            try {
                config = new PackrConfig(commandline);
            } catch (IOException ex) {
                getLog().error("Error creating installation bundle!", ex);
                return;
            }
        } else {
            config = new PackrConfig();
            config.platform = this._platform;
            config.jdk = this._jdk;
            config.executable = this._executable;
            config.classpath = this._classpath;
            config.mainClass = this._mainClass;
            config.vmArgs = this._vmArgs;
            config.minimizeJre = this._minimizeJre;
            config.outDir = this._outDir;
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
        for (String cpEntry : _classpath) {
            File fleEntry = new File(cpEntry);
            if (!fleEntry.exists()) {
                getLog().warn(cpEntry + " is not a valid location. Entry will be removed from list of classpath entries.");
            } else {
                validClasspathEntries.add(cpEntry);
            }
        }
        _classpath.clear();
        _classpath.addAll(validClasspathEntries);
        validClasspathEntries = null;

        if (this._mainClass.contains(".") == false) {
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

    public void setBundleIdentifier(String _bundleIdentifier) {
        this._bundleIdentifier = _bundleIdentifier;
    }

    public void setClasspath(List<String> _classpath) {
        this._classpath = _classpath;
    }

    public void setExecutable(String _executable) {
        this._executable = _executable;
    }

    public void setIconResource(File _iconResource) {
        this._iconResource = _iconResource;
    }

    public void setJdk(String _jdk) {
        this._jdk = _jdk;
    }

    public void setJsonConfig(File _jsonConfig) {
        this._jsonConfig = _jsonConfig;
    }

    public void setMainClass(String _mainClass) {
        this._mainClass = _mainClass;
    }

    public void setMinimizeJre(String _minimizeJre) {
        this._minimizeJre = _minimizeJre;
    }

    public void setOutDir(File _outDir) {
        this._outDir = _outDir;
    }

    public void setPlatform(PackrConfig.Platform _platform) {
        this._platform = _platform;
    }

    public void setResources(List<File> _resources) {
        this._resources = _resources;
    }

    public void setVerbose(boolean _verbose) {
        this._verbose = _verbose;
    }

    public void setVmArgs(List<String> _vmArgs) {
        this._vmArgs = _vmArgs;
    }

    @Override
    public String toString() {
        return "PackrMojo{" + "platform=" + _platform
                + ", jdk=" + _jdk
                + ", executable=" + _executable
                + ", classpath=" + _classpath
                + ", mainClass=" + _mainClass
                + ", vmArgs=" + _vmArgs
                + ", minimizeJre=" + _minimizeJre
                + ", resources=" + _resources
                + ", outDir=" + _outDir
                + ", iconResource=" + _iconResource
                + ", bundleIdentifier=" + _bundleIdentifier
                + ", jsonConfig=" + _jsonConfig
                + ", verbose=" + _verbose + '}';
    }

}
