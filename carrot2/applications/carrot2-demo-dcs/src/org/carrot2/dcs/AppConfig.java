package org.carrot2.dcs;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.carrot2.dcs.cli.BatchApp;
import org.carrot2.dcs.http.DCSApp;

/**
 * Configuration options required for running {@link BatchApp} and {@link DCSApp} (in
 * embedded and Web-container modes).
 */
public final class AppConfig
{
    /**
     * An initialized instance of {@link ControllerContext}.
     */
    private final ControllerContext controllerContext;

    /**
     * An instance of {@link Logger} typically visible on the console (if application was
     * run from the console).
     */
    public final Logger consoleLogger;

    /**
     * Default values of certain processing options.
     * 
     * @see ProcessingOptionNames
     */
    private final Map processingDefaults;

    /**
     * 
     */
    public AppConfig(ControllerContext context, Logger consoleLogger,
        Map processingDefaults)
    {
        if (context == null || processingDefaults == null) throw new IllegalArgumentException();
        this.processingDefaults = Collections.unmodifiableMap(processingDefaults);
        this.controllerContext = context;
        this.consoleLogger = consoleLogger;
    }

    /**
     * 
     */
    public ControllerContext getControllerContext()
    {
        return controllerContext;
    }

    /**
     * 
     */
    public Map getProcessingDefaults()
    {
        return processingDefaults;
    }

    /**
     * 
     */
    public Logger getConsoleLogger() {
        return this.consoleLogger;
    }
}