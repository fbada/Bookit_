package com.bookit.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {        "pretty",
                "json:target/cucumber.json",
                "html:target/cucumber-reports.html",
                "me.jvt.cucumber.report.PrettyReports:target/cucumber",
                "rerun:target/rerun.text"},

        features = "src/test/resources/features",
        glue = "com/bookit/step_definitions",
        dryRun = false,
        publish = true,
        tags = ""
)
public class CukesRunner {


}
