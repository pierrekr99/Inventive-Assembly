package objekte;

import java.util.Comparator;

class DateComparator implements Comparator<String> {

	boolean sortType = true;

	public DateComparator() {
		this(true);
	}

	public DateComparator(boolean sortType) {
		this.sortType = sortType;
	}

	public int compare(String datum1, String datum2) {
		String[] datumGetrennt1 = datum1.split("\\.");
		String[] datumGetrennt2 = datum2.split("\\.");
		if (datumGetrennt1.length != datumGetrennt2.length)
			throw new ClassCastException();
		String datumZusammengesetzt1 = datumGetrennt1[2] + datumGetrennt1[1] + datumGetrennt1[0];
		String datumZusammengesetzt2 = datumGetrennt2[2] + datumGetrennt2[1] + datumGetrennt2[0];
		if (!sortType)
			return datumZusammengesetzt1.compareTo(datumZusammengesetzt2) * -1;
		return datumZusammengesetzt1.compareTo(datumZusammengesetzt2);
	}
}