package gov.llnl.packr.maven.plugin;

import com.badlogicgames.packr.PackrConfig;
import java.io.File;
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
    public File jdk;

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
        getLog().info("Hello, world.");
    }

}
