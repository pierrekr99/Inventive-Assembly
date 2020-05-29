package test;

import java.util.ArrayList;

public class compi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> liste = new ArrayList<>();
		liste.add("Andi");
		liste.add("Moritz");
		liste.add("AAAndi");
		liste.add("MMMoritz");
		liste.add("Felix");
		liste.add("Feeeeeelix");
		
		liste.sort((o1,o2) -> {
			return o1.compareTo(o2) *-1;
		});
		
		liste.forEach(System.out::println);
	}

}
