package common.reports;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import common.listeners.TestNGEmailableReport;
import common.util.PropertiesReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EmailReporter {

	public static Properties prop = PropertiesReader.getProperty(System.getProperty("user.dir") + "/config.properties");
	public static String docServiceBaseUrl;
	public static String docServiceToken;
	public static String docServiceApiKey;
//	public static void emailReport() {
//		String toJenkins = System.getProperty("email");
//		String ccJenkins = System.getProperty("ccEmail");
//		String jmxFile = System.getProperty("jmx_file");
//		String to = null;
//		String cc = null;
//		String fileName = null;
//		if (toJenkins != null) {
//			to = toJenkins;
//		} else {
//			to = prop.getProperty("to");
//		}
//		if (ccJenkins != null) {
//			cc = ccJenkins;
//		} else {
//			cc = prop.getProperty("cc");
//		}
//		String sendEmailReportFlag = prop.getProperty("sendEmailReportFlag");
//		if (sendEmailReportFlag.equalsIgnoreCase("Yes")) {
//			sendEmail(to, cc, );
//		} else {
//		}
//	}

	public static void sendEmail(String to, String cc, String fileName) {
		Response res = null;
		try {
			System.out.println("-------------------------------"+fileName);
            String mailSubject = "Performance Test Automation Report" + " - " + fileName.replace("= ","");

			String body = "{\n" + "    \"to\": \"{" + getEmailString(to.split(",")) + "}\",\n" + "    \"cc\": \"{"
					+ getEmailString(cc.split(",")) + "}\",\n" + "    \"template_name\": \""
					+ prop.getProperty("emailTemplate") + "\",\n" + "    \"template_variable\": \"{\\\"Report\\\":\\\""
					+ new TestNGEmailableReport().sentReport().toString().replaceAll("\"", "'").replaceAll("\t", "")
					+ "\\\"}\",\n" + "    \"reference_type\": \"1\",\n" + "    \"reference_id\": \"1\",\n"
					+ "    \"subject_variable\": \"{\\\"Subject\\\":\\\"" + mailSubject + "\\\"}\"\n" + "}";
			System.out.println(body);
			res = given().contentType("application/json").cookie("PHPSESSID", "1jhi61k8h6c6qi6meljb89iuo6").body(body)
					.when().post(prop.getProperty("emailApiEndPoint"));
			System.out.println("email api response " + res.asString());
			if (res.getBody().jsonPath().get("statusCode").equals(200)) {
				System.out.println("Sent mail to - " + to);
			} else {
				System.out.println("mail is not send to - " + to);
			}

		} catch (Exception e) {
			System.out.println("email api response " + res.asString());
			System.out.println("Error to send email - " + e.getMessage());
		}
	}

	private static String getEmailString(String[] emails) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < emails.length; i++) {
			String a = "\\\"" + emails[i] + "\\\":\\\"\\\"";
			if (i > 0) {
				str.append(",");
				str.append(a);
			} else {
				str.append(a);
			}
		}
		return str.toString();
	}

	private static String getExecutionTime() {
		LocalDateTime date = LocalDateTime.now();
		String time = date.format(DateTimeFormatter.ofPattern("d-MMM-uuuu hh:mm:ss"));
		return time;
	}
	
	public static String uploadExtentReport() {
		prop = PropertiesReader.getProperty("config.properties");
		docServiceBaseUrl = prop.get("docBaseUrl").toString();
		docServiceToken = prop.get("docToken").toString();
		docServiceApiKey = prop.get("docApiKey").toString();
		String UPLOAD_DOCUMENT = "/doc-service/v1/documents";
		String REGISTER_DOCUMENT = "/doc-service/v1/documents/register";
		String GET_DOCUMENT = "/doc-service/v1/documents";
		RestAssured.baseURI = docServiceBaseUrl;
		String authorizationToken = docServiceToken;
		String apiKey = docServiceApiKey;
	
		String filePath =   System.getProperty("user.dir")+"/TestReport/ExtentReport.html";
		String docOwnerUuid = "owner";
		Response response = given().header("Authorization", authorizationToken).header("x-api-key", apiKey)
				.multiPart("document", new File(filePath), "text/html").formParam("doc_owner_uuid", docOwnerUuid).when()
				.post(UPLOAD_DOCUMENT).then().extract().response();
		System.out.println(response.asPrettyString());
		String docId = response.then().extract().path("data.doc_id");
		String requestBody = "{\"doc_ids\":[\"" + docId + "\"]}";
		response = given().header("Authorization", authorizationToken).header("x-api-key", apiKey)
				.contentType(ContentType.JSON).body(requestBody).when().post(REGISTER_DOCUMENT).then().extract()
				.response();
		String docVertualId = response.then().extract().path("data.docs[0]");
		String reportUrl = docServiceBaseUrl + GET_DOCUMENT + "/" + docVertualId;
		System.out.println(reportUrl);
		return reportUrl;
	}
	

}
