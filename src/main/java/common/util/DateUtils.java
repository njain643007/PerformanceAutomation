package common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static String generateCurrentDateTimeString(String format) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now).toString();
	}

	public String getFutureDate(String date_format, int days) {
		DateFormat dateFormat = new SimpleDateFormat(date_format);
		Date myDate = new Date(System.currentTimeMillis());
//		System.out.println("result is "+ dateFormat.format(myDate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.DATE, (days));
		String toDate = (dateFormat.format(cal.getTime()));
		return toDate;
	}

	public String getPastDate(String date_format, int days) {
		DateFormat dateFormat = new SimpleDateFormat(date_format);
		Date myDate = new Date(System.currentTimeMillis());
//		System.out.println("result is "+ dateFormat.format(myDate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.DATE, -(days));
		String toDate = (dateFormat.format(cal.getTime()));
		return toDate;
	}

	public String getPastDateByYears(String date_format, int years) {
		String expiredate = LocalDate.now().minusYears(years).format(DateTimeFormatter.ofPattern(date_format));
		return expiredate;
	}

	public static String changeStringDateFormat(String old_format, String date, String new_format)
			throws ParseException {
		SimpleDateFormat input = new SimpleDateFormat(old_format);
		Date dateValue = input.parse(date);
		SimpleDateFormat output = new SimpleDateFormat(new_format);
		System.out.println("" + output.format(dateValue) + " real date " + date);
		return output.format(dateValue);
	}

	public static int compareDate(String format, String firstDate, String secondDate) throws ParseException {
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdformat.parse(firstDate);
		Date date2 = sdformat.parse(secondDate);
		return date1.compareTo(date2);
	}

	

	public static void main(String[] args) throws ParseException {
		// System.out.println("20/Jan/2022");
		System.out.println(new DateUtils().getPastDate("yyyy-MM-dd", 365));
	}

	public static int getNumericMonth(String dt) throws ParseException {
		Date date = new SimpleDateFormat("dd/MMM/yyyy").parse(dt);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int m = calendar.get(Calendar.MONTH) + 1;
		return m;
	}

	public static boolean isEighteenYearOld(String data) throws ParseException {
		Date date = new SimpleDateFormat("dd/MMM/yyyy").parse(data);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 18);
		return calendar.getTime().after(date);
	}

	public static String getLastDateOfCurrMonth() {
		Calendar cal = Calendar.getInstance();
		int res = cal.getActualMaximum(Calendar.DATE);
		return String.valueOf(res);

	}

}
