package WDI.Resolution;

import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;

public class Erg {
	private double precision;
	private double recall;
	private double f1;
	private double weight1;
	private double weight2;
	private Comparator c1;
	private Comparator c2;

	public Erg(double precision, double recall, double f1, double weight1, double weight2,Comparator c1, Comparator c2) {
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
		this.weight1 = weight1;
		this.weight2 = weight2;
		this.setC1(c1);
		this.setC2(c2);
	}

	public String toString() {
		return "Precision: " + this.precision + "  Recall: " + this.recall + " f1: " + this.f1 + " weight1: "
				+ this.weight1 + " weight2: " + this.weight2+ " C1: "+c1.toString()+" C2: "+c2.toString();
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getF1() {
		return f1;
	}

	public void setF1(double f1) {
		this.f1 = f1;
	}

	public double getWeight1() {
		return weight1;
	}

	public void setWeight1(double weight1) {
		this.weight1 = weight1;
	}

	public double getWeight2() {
		return weight2;
	}

	public void setWeight2(double weight2) {
		this.weight2 = weight2;
	}

	public Comparator getC1() {
		return c1;
	}

	public void setC1(Comparator c1) {
		this.c1 = c1;
	}

	public Comparator getC2() {
		return c2;
	}

	public void setC2(Comparator c2) {
		this.c2 = c2;
	}
}
