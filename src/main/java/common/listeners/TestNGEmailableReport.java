package common.listeners;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


import common.util.AWSHelper;

public class TestNGEmailableReport {

	protected String jenkinsJobName = System.getProperty("jobName");
	protected String jenkinsBuildNUmber = System.getProperty("buildNumber");
	protected String url = System.getProperty("url");


	// Reusable buffer
	private final StringBuilder writer = new StringBuilder();

	private String dReportTitle = "Performance Automation Report";
	private String dReportFileName = "emailable-report2.html";
	static String outputDirectory = "report.html";

	public StringBuilder sentReport() {
		try {
			writeDocumentStart();
			writeHead();
			writeBody();
			writeDocumentEnd();
		} catch (Exception e) {
		}
	
		return writer;
		
	}


	protected void writeReportTitle(String title) {
		writer.append("<center><h1>" + title + " - " + getCurrentDateTime() + "</h1></center>");
	}

	protected void writeDocumentStart() {
		writer.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		writer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	}

	protected void writeHead() {
		writer.append("<head>");
		writer.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
		writer.append("<title>Performance Testing Report</title>");
		writeStylesheet();
		writer.append("</head>");
	}

	protected void writeStylesheet() {
		writer.append(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\">");
		writer.append("<style type=\"text/css\">");
		writer.append("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show;border:1px solid black;}");
		writer.append("#summary {margin-top:30px}");
		writer.append("h1 {font-size:30px}");
		writer.append("body {width:100%;}");
		writer.append("th,td {padding: 8px solid black;border:1px solid black;}");
		writer.append("th {vertical-align:bottom}");
		writer.append("td {vertical-align:top}");
		writer.append("tr {border:1px solid black;}");
		writer.append("table a {font-weight:bold;color:#0D1EB6;}");
		writer.append(".easy-overview {margin-left: auto; margin-right: auto;} ");
		writer.append(".easy-test-overview tr:first-child {background-color:#D3D3D3}");
		writer.append(".stripe td {background-color: #E6EBF9}");
		writer.append(".num {text-align:right}");
		writer.append(".passedodd td {background-color: #3F3}");
		writer.append(".passedeven td {background-color: #0A0}");
		writer.append(".skippedodd td {background-color: #DDD}");
		writer.append(".skippedeven td {background-color: #CCC}");
		writer.append(".failedodd td,.attn {background-color: #F33}");
		writer.append(".failedeven td,.stripe .attn {background-color: #D00}");
		writer.append(".stacktrace {font-family:monospace}");
		writer.append(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
		writer.append(".invisible {display:none}");
		writer.append("td.none {border-style: none;}");
		writer.append("tr.none {border-style: none;}");
		writer.append("table.none {border-style: none;}");
		writer.append(
				"div.center{width: 40%;background-color: #3B9C9C;justify-content: center;padding-left:1em;padding-top:1em;}");
		writer.append("</style>");
	}

	protected void writeBody() {
		writer.append("<body>");
		writeReportTitle(dReportTitle);
		writeEnvDetails();
		writer.append("</body>");
	}

	protected void writeDocumentEnd() {
		writer.append("</html>");
	}


	protected void writeEnvDetails() {
		writer.append("<div class='center';'>");
		writer.append("<b>Environment Details </b>");
		writer.append("<table class = 'none'><tbody>");
		if (jenkinsJobName != null) {
			writer.append("<tr class = 'none'>");
			writer.append("<td class = 'none'>JOB_NAME</td><td class = 'none'>&nbsp;&nbsp;:&nbsp;&nbsp;</td>");
			writer.append("<td class = 'none'>" + jenkinsJobName + "</td>");
			writer.append("</tr>");
		}
		if (jenkinsBuildNUmber != null) {
			writer.append("<tr class = 'none'>");
			writer.append("<td class = 'none'>BUILD_NUMBER</td><td class = 'none'>&nbsp;&nbsp;:&nbsp;&nbsp;</td>");
			writer.append("<td class = 'none'>" + jenkinsBuildNUmber + "</td>");
			writer.append("</tr>");
		}
//		writer.append("<tr class = 'none'>");
//		writer.append("<td class = 'none'>Username</td><td class = 'none'>&nbsp;&nbsp;:&nbsp;&nbsp;</td>");
//		writer.append("<td class = 'none'>" + System.getProperty("user.name") + "</td>");
		if (jenkinsBuildNUmber == null) {
			try {
				System.setProperty("ReportPath",
						AWSHelper.getInstance().uploadFolderToS3Bucket(System.getProperty("user.dir")+"/TestReport/Dashboard"));
			} catch (Exception e) {
				e.printStackTrace();
				System.setProperty("ReportPath", "");
			}
			writer.append("<tr class = 'none'>");
			writer.append("<td class = 'none'>Report Path</td><td class = 'none'>&nbsp;&nbsp;:&nbsp;&nbsp;</td>");
			writer.append("<td class = 'none'>https://" + System.getProperty("ReportPath") + "</td>");
			writer.append("</tr>");
		}
		writer.append("</tbody></table>");
		writer.append("</div>");
	}

	

	/* Convert long type milliseconds to format hh:mm:ss */
	public String convertTimeToString(long miliSeconds) {
		int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
		int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
		int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
		return String.format("%02d:%02d:%02d", hrs, min, sec);
	}

	public static String getCurrentDateTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy:HH.mm.ss");
		return formatter.format(currentDate.getTime());
	}
	
	public static void main(String...strings) {
		System.out.println(
		new TestNGEmailableReport().sentReport());
		
	}

}
