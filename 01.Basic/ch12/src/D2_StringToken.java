import java.util.StringTokenizer;

class D2_StringToken {

	public static void main(String[] args) {
		StringTokenizer v = new StringTokenizer("a b c");
		StringTokenizer n = new StringTokenizer("1 2 3");
		
		String s = v.nextToken();
		int num = Integer.parseInt(n.nextToken());
		int sum = num;
		
		while (v.hasMoreTokens())
		{
			s = s + " + " + (v.nextToken());
			sum = sum + Integer.parseInt(n.nextToken());
		}
		
		System.out.println( s + " = " + sum);

	}

}
