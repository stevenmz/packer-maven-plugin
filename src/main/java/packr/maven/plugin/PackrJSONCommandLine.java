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

import com.badlogicgames.packr.PackrCommandLine;
import java.io.File;
import java.util.List;

/**
 * This class is only used to provide a JSON configuration to the existing Packr
 * infrastructure.
 *
 * @author Steven Magana-Zook (maganazook1@llnl.gov)
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
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public List<String> classpath() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public File config() {
        return jsonConfig;
    }

    @Override
    public String executable() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public boolean help() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public File iconResource() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public boolean isConfig() {
        return true;
    }

    @Override
    public String jdk() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public String mainClass() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public String minimizeJre() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public File outDir() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public String platform() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public List<File> resources() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public boolean verbose() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

    @Override
    public List<String> vmArgs() {
        throw new UnsupportedOperationException("No plan to implement.");
    }

}
