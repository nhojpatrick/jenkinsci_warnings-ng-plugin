package io.jenkins.plugins.analysis.warnings;

import org.kohsuke.stapler.DataBoundConstructor;
import org.jenkinsci.Symbol;
import hudson.Extension;

import io.jenkins.plugins.analysis.core.model.AnalysisModelParser;

/**
 * Provides a parser and customized messages for the clang-analyzer.
 *
 * @author Andrey Danin
 */
public class ClangAnalyzer extends AnalysisModelParser {
    private static final long serialVersionUID = 1L;
    private static final String ID = "clang-analyzer";

    /** Creates a new instance of {@link ClangAnalyzer}. */
    @DataBoundConstructor
    public ClangAnalyzer() {
        super();
        // empty constructor required for stapler
    }

    /** Descriptor for this static analysis tool. */
    @Symbol("clangAnalyzer")
    @Extension
    public static class Descriptor extends AnalysisModelParserDescriptor {
        /** Creates the descriptor instance. */
        public Descriptor() {
            super(ID);
        }
    }
}
