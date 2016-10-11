package gov.llnl.packr.maven.plugin;

import com.badlogicgames.packr.Packr;
import com.badlogicgames.packr.PackrConfig;
import java.io.File;
import java.io.IOException;
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
     * Defines the platform target for which to build an installer package. One
     * of "Windows32", "Windows64", "Linux32", "Linux64", "MacOS"
     */
    @Parameter(property = "packr.platform")
    private PackrConfig.Platform platform;

    /**
     * ZIP file location to an Oracle JDK build containing a JRE.
     */
    @Parameter(property = "packr.jdk")
    public String jdk;

    /**
     * Name of the native executable, without extension such as ".exe"
     */
    @Parameter(property = "packr.executable")
    private String executable;

    /**
     * list of file locations of the JAR files to package
     */
    @Parameter(property = "packr.classpath")
    private List<String> classpath;

    /**
     * The fully qualified name of the main class, using dots to delimit package
     * names
     */
    @Parameter(property = "packr.mainClass")
    private String mainClass;

    /**
     * List of arguments for the JVM, without leading dashes, e.g. "Xmx1G"
     */
    @Parameter(property = "packr.vmArgs")
    private List<String> vmArgs;

    /**
     * Minimize the JRE by removing directories and files as specified by an
     * additional config file. Comes with a 'soft' and 'hard' configurations out
     * of the box.
     */
    @Parameter(property = "packr.minimizeJre")
    private String minimizeJre;

    /**
     * list of files and directories to be packaged next to the native
     * executable.
     */
    @Parameter(property = "packr.resources")
    private List<File> resources;

    /**
     * The output directory.
     */
    @Parameter(property = "packr.outDir")
    private File outDir;

    /**
     * Location of an AppBundle icon resource (.icns file) Note: This is for OS
     * X targets (see {@link Platform#MacOS} ).
     *
     * @see Platform
     */
    @Parameter(property = "packr.iconResource")
    private File iconResource;

    /**
     * The bundle identifier of your Java application, e.g. "com.my.app". This
     * is for OS X targets (see {@link Platform#MacOS} ).
     */
    @Parameter(property = "packr.bundleIdentifier")
    private String bundleIdentifier;

    /**
     * You can put all the command line arguments into a JSON file. The JSON, if
     * provided, takes precedence over the other configuration values.
     *
     * Typically, you either specify JSON or you specify the configuration
     * parameters manually.
     */
    @Parameter(property = "packr.jsonConfig")
    private File jsonConfig;

    /**
     * Prints more status information during processing, which can be useful for
     * debugging.
     */
    @Parameter(property = "packr.verbose", defaultValue = "true")
    private boolean verbose;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Starting Packr plugin...\n" + this.toString());

        PackrConfig config = null;
        if (this.jsonConfig != null && this.jsonConfig.exists()) {
            //get values from JSON
            getLog().info("Using values from JSON configuration file.");
            PackrJSONCommandLine commandline = new PackrJSONCommandLine(jsonConfig);
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
            throw new MojoExecutionException("Invalid JDK path provided. This path should be a ZIP file of a JDK directory.");
        }

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
        return "PackrMojo{" + "platform=" + platform + ", jdk=" + jdk + ", executable=" + executable + ", classpath=" + classpath + ", mainClass=" + mainClass + ", vmArgs=" + vmArgs + ", minimizeJre=" + minimizeJre + ", resources=" + resources + ", outDir=" + outDir + ", iconResource=" + iconResource + ", bundleIdentifier=" + bundleIdentifier + ", jsonConfig=" + jsonConfig + ", verbose=" + verbose + '}';
    }

}
