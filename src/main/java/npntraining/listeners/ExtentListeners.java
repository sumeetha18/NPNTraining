package npntraining.listeners;


import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class ExtentListeners implements ITestListener{
	static Date d = new Date();
	static String filename = "Extent_" + d.toString().replace(":", "_").replace("","_") + ".png";
	
	private static ExtentReports extent = ExtentManager.createInstance(System.getProperty("user.dir") + "reports" + filename);
	public static ThreadLocal<ExtentTest> testreport = new ThreadLocal<ExtentTest>();
	
	public void onTestStart(ITestResult result) {
		ExtentTest test = extent.createTest(result.getTestClass().getName() +"@TestCase : "+result.getMethod().getMethodName());
		testreport.set(test);
	}
	
	public void onTestSuccess(ITestResult result) {
		String methodname = result.getMethod().getMethodName();
		String logText = "</b>"+"TEST CASE:-"+methodname.toUpperCase()+ "PASSED"+"</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		testreport.get().pass(m);	
	}
	
	public void onTestFailure(ITestResult result) {
		String exceptionMessage=Arrays.toString(result.getThrowable().getStackTrace());
		testreport.get().fail("<details>" + "<summary>" + "</b>" + "font color" + "red>" + "Exception occured:Click to see" + "</font>" + "</b>" + "<summary>" + exceptionMessage.replaceAll(",", "<br>") + "<details>"+ "\n");
	    try {
	    	ExtentManager.capturescreenshot();
	    	testreport.get().fail("<b>"  + "font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(ExtentManager.screenshotname).build());
	    	
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    }
	    String failureLog = "TEST CASE FAILED";
	    Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
	    testreport.get().log(Status.FAIL, m);
	}
	public void onTestSkipped(ITestResult result) {
		String methodname = result.getMethod().getMethodName();
		String logText = "</b>"+"TEST CASE:-"+methodname+ "SKIPPED"+"</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		testreport.get().skip(m);
	}
	public void onFinish(ITestContext context) {
		if(extent!=null) {
			extent.flush();
		}
	}
	//commit and push
	//added a new line
}
