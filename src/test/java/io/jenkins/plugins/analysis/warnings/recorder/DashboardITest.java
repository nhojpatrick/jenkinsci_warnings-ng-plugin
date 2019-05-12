package io.jenkins.plugins.analysis.warnings.recorder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;

import hudson.model.FreeStyleProject;
import hudson.model.Project;
import hudson.model.Result;
import hudson.model.Run;
import hudson.plugins.view.dashboard.Dashboard;

import io.jenkins.plugins.analysis.core.model.AnalysisResult;
import io.jenkins.plugins.analysis.core.portlets.IssuesTablePortlet;
import io.jenkins.plugins.analysis.core.testutil.IntegrationTestWithJenkinsPerSuite;

public class DashboardITest extends IntegrationTestWithJenkinsPerSuite {

    @Test
    public void firstTest() {
        //FreeStyleProject project = createFreeStyleProjectWithWorkspaceFiles("checkstyle-healthReport.xml");
        FreeStyleProject project = createFreeStyleProjectWithWorkspaceFiles("checkstyle-healthReport.xml",
                "eclipse.txt");
        enableEclipseWarnings(project);
        enableCheckStyleWarnings(project);

        configureDashboard("SuperTest", project);

        Run<?, ?> build = buildWithResult(project, Result.SUCCESS);
        List<AnalysisResult> results = getAnalysisResults(build);

        System.out.println(results);

        System.out.println("end");
    }

    private void configureDashboard(final String name, final Project... projects) {
        Dashboard dashboardView = new Dashboard(name);

        try {
            getJenkins().getInstance().addView(dashboardView);
        }
        catch (IOException exception) {
            throw new RuntimeException("Dashboard configuration failed", exception);
        }

        Arrays.stream(projects).forEach(project -> {
            try {
                dashboardView.doAddJobToView(project.getName());
            }
            catch (ServletException | IOException exception) {
                throw new RuntimeException("The project " + project.getName() + " couldn't be added to the dashboard",
                        exception);
            }
        });

        WebRequest request;
        try {
            request = new WebRequest(
                    new URL(getJenkins().getInstance().getRootUrl() + "view/" + name + "/configSubmit"),
                    HttpMethod.POST);
        }
        catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }

        request.setRequestBody("name=" + name
                + "&description=&statusFilter=&test0=on&includeRegex=&stapler-class=hudson.views.StatusColumn&%24class=hudson.views.StatusColumn&stapler-class=hudson.views.WeatherColumn&%24class=hudson.views.WeatherColumn&stapler-class=hudson.views.JobColumn&%24class=hudson.views.JobColumn&stapler-class=hudson.views.LastSuccessColumn&%24class=hudson.views.LastSuccessColumn&stapler-class=hudson.views.LastFailureColumn&%24class=hudson.views.LastFailureColumn&stapler-class=hudson.views.LastDurationColumn&%24class=hudson.views.LastDurationColumn&stapler-class=hudson.views.BuildButtonColumn&%24class=hudson.views.BuildButtonColumn&_.name=%23+Issues&_.id=&stapler-class=io.jenkins.plugins.analysis.core.columns.IssuesTotalColumn&%24class=io.jenkins.plugins.analysis.core.columns.IssuesTotalColumn&leftPortletWidth=50%25&rightPortletWidth=50%25&_.name=Static+analysis+issues+per+tool+and+job&_.id=&stapler-class=io.jenkins.plugins.analysis.core.portlets.IssuesTablePortlet&%24class=io.jenkins.plugins.analysis.core.portlets.IssuesTablePortlet&core%3Aapply=&Jenkins-Crumb=test&json=%7B%22name%22%3A+%22Test%22%2C+%22description%22%3A+%22%22%2C+%22filterQueue%22%3A+false%2C+%22filterExecutors%22%3A+false%2C+%22statusFilter%22%3A+%22%22%2C+%22recurse%22%3A+false%2C+%22test0%22%3A+%22true%22%2C+%22columns%22%3A+%5B%7B%22stapler-class%22%3A+%22hudson.views.StatusColumn%22%2C+%22%24class%22%3A+%22hudson.views.StatusColumn%22%7D%2C+%7B%22stapler-class%22%3A+%22hudson.views.WeatherColumn%22%2C+%22%24class%22%3A+%22hudson.views.WeatherColumn%22%7D%2C+%7B%22stapler-class%22%3A+%22hudson.views.JobColumn%22%2C+%22%24class%22%3A+%22hudson.views.JobColumn%22%7D%2C+%7B%22stapler-class%22%3A+%22hudson.views.LastSuccessColumn%22%2C+%22%24class%22%3A+%22hudson.views.LastSuccessColumn%22%7D%2C+%7B%22stapler-class%22%3A+%22hudson.views.LastFailureColumn%22%2C+%22%24class%22%3A+%22hudson.views.LastFailureColumn%22%7D%2C+%7B%22stapler-class%22%3A+%22hudson.views.LastDurationColumn%22%2C+%22%24class%22%3A+%22hudson.views.LastDurationColumn%22%7D%2C+%7B%22stapler-class%22%3A+%22hudson.views.BuildButtonColumn%22%2C+%22%24class%22%3A+%22hudson.views.BuildButtonColumn%22%7D%2C+%7B%22name%22%3A+%22%23+Issues%22%2C+%22selectTools%22%3A+false%2C+%22tools%22%3A+%7B%22id%22%3A+%22%22%7D%2C+%22stapler-class%22%3A+%22io.jenkins.plugins.analysis.core.columns.IssuesTotalColumn%22%2C+%22%24class%22%3A+%22io.jenkins.plugins.analysis.core.columns.IssuesTotalColumn%22%7D%5D%2C+%22includeStdJobList%22%3A+false%2C+%22topPortlet%22%3A+%7B%22name%22%3A+%22Static+analysis+issues+per+tool+and+job%22%2C+%22hideCleanJobs%22%3A+false%2C+%22showIcons%22%3A+false%2C+%22selectTools%22%3A+false%2C+%22tools%22%3A+%7B%22id%22%3A+%22%22%7D%2C+%22stapler-class%22%3A+%22io.jenkins.plugins.analysis.core.portlets.IssuesTablePortlet%22%2C+%22%24class%22%3A+%22io.jenkins.plugins.analysis.core.portlets.IssuesTablePortlet%22%7D%2C+%22core%3Aapply%22%3A+%22%22%2C+%22Jenkins-Crumb%22%3A+%22test%22%7D&Submit=OK");

        try {
            getWebClient(JavaScriptSupport.JS_ENABLED).getPage(request);
        }
        catch (IOException exception) {
            throw new RuntimeException("The setup of the IssuesTablePortlet failed" + exception);
        }

    }
}
