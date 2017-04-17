package orgin;
public class Equation {
	private double a, b, c;

	public Equation(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public static void main(String[] args) throws Exception {
		//j*(j-1) = 40;  
		//j*j - j - 40 =0
		double[] results = new Equation(1, -1, -40).analyze();
		for(int i = 0; i < results.length; i++){
			System.out.println("j = " + results[i]);
		}
		
	}

	public double[] analyze() throws Exception {
		double delt = b * b - 4 * a * c;

		if (delt < 0) {
			throw new Exception("NO result");
		} else if (delt == 0) {
			double[] ary = { -b / (2 * a) };
			return ary;
		} else {
			double[] ary = new double[2];
			ary[0] = (-b + Math.sqrt(delt)) / (2 * a);
			ary[1] = (-b - Math.sqrt(delt)) / (2 * a);

			return ary;
		}

	}

} 