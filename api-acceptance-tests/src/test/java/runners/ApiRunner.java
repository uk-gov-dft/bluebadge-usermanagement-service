package runners;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

import com.intuit.karate.junit4.Karate;

@RunWith(Karate.class)
@CucumberOptions(features = "classpath:features")
public class ApiRunner {}