package com.therap.javafest.utext.lib;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

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
		long date1 = (Timestamp.valueOf(n1.getDateTime())).getTime();
		long date2 = (Timestamp.valueOf(n1.getDateTime())).getTime();
		return (date1 < date2) ? 1 : ((date1 == date2) ? 0 : -1);
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
				Date rdate1 = NoteComparator.StringToDate(n1.getRDateTime());
				Date rdate2 = NoteComparator.StringToDate(n1.getRDateTime());
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

	public static Date StringToDate(String rDateTime) {
		String str[] = rDateTime.split(" ");
		return (new GregorianCalendar(Integer.parseInt(str[0]),
				Integer.parseInt(str[1]), Integer.parseInt(str[2]),
				Integer.parseInt(str[3]), Integer.parseInt(str[4]), 0))
				.getTime();
	}
}
