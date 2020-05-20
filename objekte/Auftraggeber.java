package objekte;

import java.util.Comparator;

public class Auftraggeber {

//	Attribute
	private String name;
	private String kundenNummer;

	public Auftraggeber(String name, String kundenNummer) {
	super();
	this.name = name;
	this.kundenNummer = kundenNummer;
	

	}

	public String getName() {
		return name;
	}

	public String getKundenNummer() {
		return kundenNummer;
	}

	@Override
	public String toString() {
		return " [Name = " + name + ", Kundennr = " + kundenNummer + "]";
	}

}


// BeispielComperator  -> nicht verwirren lassen
class sortiereKundenName implements Comparator<Auftraggeber> {

	@Override
	public int compare(Auftraggeber o1, Auftraggeber o2) {

		return o1.getName().compareTo(o2.getName());
	}
}

class sortiereKundenNr implements Comparator<Auftraggeber> {

	@Override
	public int compare(Auftraggeber o1, Auftraggeber o2) {

		return o1.getKundenNummer().compareTo(o2.getKundenNummer());
	}
}
