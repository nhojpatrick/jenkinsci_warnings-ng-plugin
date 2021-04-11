package io.jenkins.plugins.analysis.warnings;

import org.kohsuke.stapler.DataBoundConstructor;
import org.jenkinsci.Symbol;
import hudson.Extension;

import io.jenkins.plugins.analysis.core.model.AnalysisModelParser;

/**
 * Provides a parser and customized messages for the AcuCobol Compiler.
 *
 * @author Ullrich Hafner
 */
public class AcuCobol extends AnalysisModelParser {
    private static final long serialVersionUID = 2333849052758654239L;
    private static final String ID = "acu-cobol";

    /** Creates a new instance of {@link AcuCobol}. */
    @DataBoundConstructor
    public AcuCobol() {
        super();
        // empty constructor required for stapler
    }

    /** Descriptor for this static analysis tool. */
    @Symbol("acuCobol")
    @Extension
    public static class Descriptor extends AnalysisModelParserDescriptor {
        /** Creates the descriptor instance. */
        public Descriptor() {
            super(ID);
        }
    }
}
