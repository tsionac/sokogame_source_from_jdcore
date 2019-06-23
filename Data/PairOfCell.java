package Data;

//Object the contains 2 cells (pair of cells for use the undo function)
public class PairOfCell {

	private Cell a;
	private Cell b;

	public PairOfCell(Cell a, Cell b) {

		this.a = a;
		this.b = b;
	}

	public Cell getA() {
		return a;
	}

	public void setA(Cell a) {
		this.a = a;
	}

	public Cell getB() {
		return b;
	}

	public void setB(Cell b) {
		this.b = b;
	}
}