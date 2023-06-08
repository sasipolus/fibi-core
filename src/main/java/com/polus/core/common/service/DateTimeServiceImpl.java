package com.polus.core.common.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polus.core.constants.Constants;

@Transactional
@Service(value = "dateTimeService")
public class DateTimeServiceImpl implements DateTimeService, InitializingBean {

	protected static Logger logger = LogManager.getLogger(DateTimeServiceImpl.class.getName());

	/**
	 * Default date/time formats
	 */
	private static final String STRING_TO_DATE_FORMATS = "MM/dd/yyyy hh:mm a;MM/dd/yy;MM/dd/yyyy;MM-dd-yy;MM-dd-yyyy;MMddyy;MMMM dd;yyyy;MM/dd/yy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM-dd-yy HH:mm:ss;MMddyy HH:mm:ss;MMMM dd HH:mm:ss;yyyy HH:mm:ss";
	private static final String STRING_TO_TIME_FORMATS = "hh:mm aa";
	private static final String STRING_TO_TIMESTAMP_FORMATS = "MM/dd/yyyy hh:mm a;MM/dd/yy;MM/dd/yyyy;MM-dd-yy;MMddyy;MMMM dd;yyyy;MM/dd/yy HH:mm:ss;MM/dd/yyyy HH:mm:ss;MM-dd-yy HH:mm:ss;MMddyy HH:mm:ss;MMMM dd HH:mm:ss;yyyy HH:mm:ss";
	private static final String DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE = "MM/dd/yyyy";
	private static final String TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE = "hh:mm aa";
	private static final String TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE = "MM/dd/yyyy hh:mm a";
	private static final String DATE_TO_STRING_FORMAT_FOR_FILE_NAME = "yyyyMMdd";
	private static final String TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME = "yyyyMMdd-HH-mm-ss-S";

	protected String[] stringToDateFormats;
	protected String[] stringToTimeFormats;
	protected String[] stringToTimestampFormats;
	protected String dateToStringFormatForUserInterface;
	protected String timeToStringFormatForUserInterface;
	protected String timestampToStringFormatForUserInterface;
	protected String dateToStringFormatForFileName;
	protected String timestampToStringFormatForFileName;

	@Override
	public String toDateString(Date date) {
		return toString(date, dateToStringFormatForUserInterface);
	}

	@Override
	public String toTimeString(Time time) {
		return toString(time, timeToStringFormatForUserInterface);
	}

	@Override
	public String toDateTimeString(Date date) {
		return toString(date, timestampToStringFormatForUserInterface);
	}

	@Override
	public String toString(Date date, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		dateFormat.setLenient(false);
		return dateFormat.format(date);
	}

	@Override
	public Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getTime();
	}

	@Override
	public Timestamp getCurrentTimestamp() {
		return new java.sql.Timestamp(getCurrentDate().getTime());
	}

	@Override
	public java.sql.Date getCurrentSqlDate() {
		return new java.sql.Date(getCurrentDate().getTime());
	}

	@Override
	public java.sql.Date getCurrentSqlDateMidnight() {
		return java.sql.Date.valueOf(getCurrentSqlDate().toString());
	}

	@Override
	public Calendar getCurrentCalendar() {
		return getCalendar(getCurrentDate());
	}

	@Override
	public Calendar getCalendar(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("invalid (null) date");
		}

		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(date);

		return currentCalendar;
	}

	@Override
	public Date convertToDate(String dateString) throws ParseException {
		return parseAgainstFormatArray(dateString, stringToDateFormats);
	}

	@Override
	public Date convertToDateTime(String dateTimeString) throws ParseException {
		if (StringUtils.isBlank(dateTimeString)) {
			throw new IllegalArgumentException("invalid (blank) date/time string");
		}
		return parseAgainstFormatArray(dateTimeString, stringToTimestampFormats);
	}

	@Override
	public Timestamp convertToSqlTimestamp(String timeString) throws ParseException {
		if (!StringUtils.isBlank(timeString)) {
			return new java.sql.Timestamp(convertToDateTime(timeString).getTime());
		}
		return null;
	}

	@Override
	public java.sql.Date convertToSqlDate(String dateString) throws ParseException {
		if (StringUtils.isBlank(dateString)) {
			throw new IllegalArgumentException("invalid (blank) dateString");
		}
		Date date = parseAgainstFormatArray(dateString, stringToDateFormats);
		return new java.sql.Date(date.getTime());
	}

	@Override
	public Time convertToSqlTime(String timeString) throws ParseException {
		if (StringUtils.isBlank(timeString)) {
			throw new IllegalArgumentException("invalid (blank) dateString");
		}
		Date date = parseAgainstFormatArray(timeString, stringToTimeFormats);
		return new java.sql.Time(date.getTime());
	}

	@Override
	public java.sql.Date convertToSqlDate(Timestamp timestamp) throws ParseException {
		return new java.sql.Date(timestamp.getTime());
	}

	@Override
	public int dateDiff(Date startDate, Date endDate, boolean inclusive) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);

		int startDateOffset = -(startDateCalendar.get(Calendar.ZONE_OFFSET) + startDateCalendar.get(Calendar.DST_OFFSET)) / (60 * 1000);

		int endDateOffset = -(endDateCalendar.get(Calendar.ZONE_OFFSET) + endDateCalendar.get(Calendar.DST_OFFSET)) / (60 * 1000);

		if (startDateOffset > endDateOffset) {
			startDateCalendar.add(Calendar.MINUTE, endDateOffset - startDateOffset);
		}

		if (inclusive) {
			startDateCalendar.add(Calendar.DATE, -1);
		}

		int dateDiff = Integer.parseInt(DurationFormatUtils.formatDuration(endDateCalendar.getTimeInMillis() - startDateCalendar.getTimeInMillis(), "d", true));

		return dateDiff;
	}

	@Override
	public String toDateStringForFilename(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateToStringFormatForFileName);
		return dateFormat.format(date);
	}

	@Override
	public String toDateTimeStringForFilename(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(timestampToStringFormatForFileName);
		return dateFormat.format(date);
	}

	protected Date parseAgainstFormatArray(String dateString, String[] formats) throws ParseException {
		dateString = dateString.trim();
		StringBuffer exceptionMessage = new StringBuffer("Date or date/time string '").append(dateString)
				.append("' could not be converted using any of the accepted formats: ");
		for (String dateFormatString : formats) {
			try {
				return parse(dateString, dateFormatString);
			} catch (ParseException e) {
				exceptionMessage.append(dateFormatString).append(" (error offset=").append(e.getErrorOffset())
						.append("),");
			}
		}
		throw new ParseException(exceptionMessage.toString().substring(0, exceptionMessage.length() - 1), 0);
	}

	protected Date parse(String dateString, String pattern) throws ParseException {
		if (!StringUtils.isBlank(dateString)) {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			dateFormat.setLenient(false);
			ParsePosition parsePosition = new ParsePosition(0);
			Date testDate = dateFormat.parse(dateString, parsePosition);

			// Ensure that the entire date String can be parsed by the current
			// format.
			if (testDate == null) {
				throw new ParseException("The date that you provided is invalid.", parsePosition.getErrorIndex());
			} else if (parsePosition.getIndex() != dateString.length()) {
				throw new ParseException("The date that you provided is invalid.", parsePosition.getIndex());
			}

			// Ensure that the date's year lies between 1000 and 9999, to help
			// prevent database-related date errors.
			Calendar testCalendar = Calendar.getInstance();
			testCalendar.setLenient(false);
			testCalendar.setTime(testDate);
			if (testCalendar.get(Calendar.YEAR) < 1000 || testCalendar.get(Calendar.YEAR) > 9999) {
				throw new ParseException("The date that you provided is not between the years 1000 and 9999.", -1);
			}

			if (testCalendar.get(Calendar.YEAR) == 1970 && !pattern.contains("y".toLowerCase())) {
				Calendar curCalendar = Calendar.getInstance();
				curCalendar.setTime(new java.util.Date());
				testCalendar.set(Calendar.YEAR, curCalendar.get(Calendar.YEAR));
				testDate = testCalendar.getTime();
			}

			return testDate;
		}
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (stringToDateFormats == null) {
			stringToDateFormats = loadAndValidateFormats(Constants.STRING_TO_DATE_FORMATS, STRING_TO_DATE_FORMATS);
		}

		if (stringToTimeFormats == null) {
			stringToTimeFormats = loadAndValidateFormats(Constants.STRING_TO_TIME_FORMATS, STRING_TO_TIME_FORMATS);
		}

		if (stringToTimestampFormats == null) {
			stringToTimestampFormats = loadAndValidateFormats(Constants.STRING_TO_TIMESTAMP_FORMATS,
					STRING_TO_TIMESTAMP_FORMATS);
		}

		if (dateToStringFormatForUserInterface == null) {
			dateToStringFormatForUserInterface = loadAndValidateFormat(
					Constants.DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE, DATE_TO_STRING_FORMAT_FOR_USER_INTERFACE);
		}

		if (timeToStringFormatForUserInterface == null) {
			timeToStringFormatForUserInterface = loadAndValidateFormat(
					Constants.TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE, TIME_TO_STRING_FORMAT_FOR_USER_INTERFACE);
		}

		if (timestampToStringFormatForUserInterface == null) {
			timestampToStringFormatForUserInterface = loadAndValidateFormat(
					Constants.TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE,
					TIMESTAMP_TO_STRING_FORMAT_FOR_USER_INTERFACE);
		}

		if (dateToStringFormatForFileName == null) {
			dateToStringFormatForFileName = loadAndValidateFormat(Constants.DATE_TO_STRING_FORMAT_FOR_FILE_NAME,
					DATE_TO_STRING_FORMAT_FOR_FILE_NAME);
		}

		if (timestampToStringFormatForFileName == null) {
			timestampToStringFormatForFileName = loadAndValidateFormat(
					Constants.TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME, TIMESTAMP_TO_STRING_FORMAT_FOR_FILE_NAME);
		}
	}

	/**
	 * Loads a particular date format from the config, using a default for
	 * fallback, and validates the format.
	 * 
	 * @param property
	 *            the config property
	 * @param deflt
	 *            the default value
	 * @return the validated config value or default value
	 */
	private String loadAndValidateFormat(String property, String deflt) {
		String format = loadFormat(property, deflt);
		// construct new SDF to make sure it's properly formatted
		new SimpleDateFormat(format);
		return format;
	}

	/**
	 * Loads a particular date format from the config, using a default for
	 * fallback
	 * 
	 * @param property
	 *            the config property
	 * @param deflt
	 *            the default value
	 * @return the config value or default value
	 */
	private String loadFormat(String property, String deflt) {
		/*
		 * String format =
		 * ConfigContext.getCurrentContextConfig().getProperty(property); if
		 * (StringUtils.isBlank(format)) { format = deflt; } return format;
		 */
		return "";
	}

	/**
	 * Loads a format string list from the config or default and validates each
	 * entry
	 * 
	 * @param property
	 *            the config property
	 * @param deflt
	 *            the default value
	 * @return string array of valid date/time formats
	 */
	private String[] loadAndValidateFormats(String property, String deflt) {
		List<String> dateFormatParams = loadFormats(property, deflt);

		String[] validFormats = new String[dateFormatParams.size()];

		for (int i = 0; i < dateFormatParams.size(); i++) {
			String dateFormatParam = dateFormatParams.get(i);
			if (StringUtils.isBlank(dateFormatParam)) {
				throw new IllegalArgumentException(
						"Core/All/" + property + " parameter contains a blank semi-colon delimited substring");
			} else {
				// try to create a new SimpleDateFormat to try to detect illegal
				// patterns
				new SimpleDateFormat(dateFormatParam);
				validFormats[i] = dateFormatParam;
			}
		}

		return validFormats;
	}

	/**
	 * Loads a format string list from config or default
	 * 
	 * @param property
	 *            the config property
	 * @param deflt
	 *            the default value
	 * @return the config or default value
	 */
	private List<String> loadFormats(String property, String deflt) {
		return parseConfigValues(loadFormat(property, deflt));
	}

	/**
	 *
	 * The dateTime config vars are ';' seperated. This method should probably
	 * be on the config interface.
	 *
	 * @param configValue
	 * @return
	 */
	private List<String> parseConfigValues(String configValue) {
		if (configValue == null || "".equals(configValue)) {
			return Collections.emptyList();
		}
		return Arrays.asList(configValue.split(";"));
	}

	@Override
	public Timestamp stringToTimestamp(String date) throws ParseException {
		Date parseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		return new Timestamp(parseDate.getTime());
	}

	@Override
	public Timestamp getCurrentDateWithZeroTime(Timestamp currentTimestamp) {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(currentTimestamp); // compute start of the day for the timestamp
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return new Timestamp(cal.getTime().getTime());
	}

	@Override
	public Timestamp stringToTimestampWithTime(String date) throws ParseException {
		if (date.length() > 19) {
			date = date.substring(0, 19);
	    }
		Date parseDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
		return new Timestamp(parseDate.getTime());
	}

}
