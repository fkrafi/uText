package com.therap.javafest.utext.lib;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

public class NoteComparator implements Comparator<Note> {
	private int method;
	public static final int SEARCH_COMPARE = 1;
	public static final int DEFAULT_COMPARE = 2;

	public NoteComparator(int method) {
		this.method = method;
	}

	public int compare(Note n1, Note n2) {
		if (method == 1) {
			return searchCompare(n1, n2);
		}
		return defaultCompare(n1, n2);
	}

	private int defaultCompare(Note n1, Note n2) {
		Date date1 = new Date((Timestamp.valueOf(n1.getDateTime())).getTime());
		Date date2 = new Date((Timestamp.valueOf(n1.getDateTime()).getTime()));
		return date1.compareTo(date2);
	}

	private int searchCompare(Note n1, Note n2) {
		Integer count1 = Integer.valueOf(n1.getCount());
		Integer count2 = Integer.valueOf(n2.getCount());
		Date date1 = new Date((Timestamp.valueOf(n1.getDateTime())).getTime());
		Date date2 = new Date((Timestamp.valueOf(n1.getDateTime()).getTime()));
		Integer type1 = Integer.valueOf(n1.getType());
		Integer type2 = Integer.valueOf(n2.getType());
		Integer imp1 = Integer.valueOf(n1.getImportant());
		Integer imp2 = Integer.valueOf(n2.getImportant());
		if (count1.equals(count2)) {
			if (n1.getType() == Note.REMINDER && n2.getType() == Note.REMINDER) {
				Date rdate1 = StringToDate(n1.getRDateTime());
				Date rdate2 = StringToDate(n1.getRDateTime());
				if (rdate1.equals(rdate2)) {
					return imp2.compareTo(imp1);
				}
				return rdate2.compareTo(rdate1);
			}
			if (type1.equals(type2)) {
				if (date1.equals(date2)) {
					return imp2.compareTo(imp1);
				}
				return date1.compareTo(date2);
			}
			return type2.compareTo(type1);
		}
		return count2.compareTo(count1);
	}

	public Date StringToDate(String rDateTime) {
		String tokens[] = rDateTime.split(" ");
		int year = Integer.valueOf(tokens[3].trim());
		int month = Integer.valueOf(tokens[2].trim());
		int date = Integer.valueOf(tokens[1].trim());
		String time[] = tokens[4].split(":");
		int hrs = (Integer.valueOf(time[0].trim()) + (tokens[5].equals("PM") ? 12
				: 0)) % 24;
		int min = Integer.valueOf(time[1].trim());
		return new Date(year, month, date, hrs, min);
	}
}
