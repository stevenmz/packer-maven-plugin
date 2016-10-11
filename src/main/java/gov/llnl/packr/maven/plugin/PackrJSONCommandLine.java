package gov.llnl.packr.maven.plugin;

import com.badlogicgames.packr.PackrCommandLine;
import java.io.File;
import java.util.List;

/**
 * This class is only used to provide a JSON configuration to the existing Packr
 * infrastructure.
 *
 * @author maganazook1 (maganazook1@llnl.gov)
 */
public class PackrJSONCommandLine implements PackrCommandLine {

    private final File jsonConfig;

    /**
     * Constructs a Packr command line options object that points to a JSON file
     * containing the configuration for assembling an installation package.
     *
     * @param jsonConfig A valid java.io.File object pointing to a JSON
     * configuration.
     */
    public PackrJSONCommandLine(File jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    @Override
    public String bundleIdentifier() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> classpath() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File config() {
        return jsonConfig;
    }

    @Override
    public String executable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean help() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File iconResource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isConfig() {
        return true;
    }

    @Override
    public String jdk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String mainClass() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String minimizeJre() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File outDir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String platform() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<File> resources() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean verbose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> vmArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
