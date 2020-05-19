package objekte;

import java.util.Comparator;

public class Auftraggeber {

//	Attribute
	private String name;
	private String kundennr;

	public Auftraggeber(String name, String kundennr) {
	super();
	this.name = name;
	this.kundennr = kundennr;
	

	}

	public String getName() {
		return name;
	}

	public String getKundennr() {
		return kundennr;
	}

	@Override
	public String toString() {
		return " [Name = " + name + ", Kundennr = " + kundennr + "]";
	}

}

class sortiereKundenName implements Comparator<Auftraggeber> {

	@Override
	public int compare(Auftraggeber o1, Auftraggeber o2) {

		return o1.getName().compareTo(o2.getName());
	}
}

class sortiereKundenNr implements Comparator<Auftraggeber> {

	@Override
	public int compare(Auftraggeber o1, Auftraggeber o2) {

		return o1.getKundennr().compareTo(o2.getKundennr());
	}
}
