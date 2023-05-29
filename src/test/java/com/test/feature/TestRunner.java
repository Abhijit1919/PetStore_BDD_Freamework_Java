package com.test.feature;


import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
		features = "src/test/resources/feature/Users/Login/Pet/feature",
		plugin = {"pretty"}
		)
public class TestRunner {
}
