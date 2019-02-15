import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class test {

	public static void main(String[] args) {
		//StringTokenizer v= new StringTokenizer("aa bbbb cc");
//		Scanner s = new Scanner(System.in);
//		String z = s.nextLine();
//		StringTokenizer v = new StringTokenizer(z);
//		String order = v.nextToken();
//		String name = v.nextToken();
//		String chat = v.nextToken();
//		
//		System.out.println(order +" "+ name +" "+ chat);
//		String b = "/ab cde";
//		StringTokenizer v=new StringTokenizer(b.substring(1));
//		String order = v.nextToken();
//		System.out.println(order);
//		
//		String a = "abbb bbbbbbb c";
//		int begin = a.indexOf(" ") + 1;
//		int end = a.indexOf(" ",begin);
//		
//		String c="6";
//		int d=Integer.parseInt(c);
//		
//		System.out.printf("%d, %d" ,begin ,end);
//		
//		String[]arr= {"aaa","bbb","ccc"};
//		String title= "room";
//		
//		Map<String, String[]> chatUserMap;
//		chatUserMap = new HashMap<String, String[]>();
//		chatUserMap.put(title, arr);
//		Iterator<String> itr = chatUserMap.keySet().iterator();
//		while (itr.hasNext()) {
//	        String key = (String) itr.next();
//	        System.out.print("key="+key);
//	        System.out.println(" value="+chatUserMap.get(key));
//		}
//		
		Map<String, String>map = new HashMap<String,String>();
		map.put("A", "a");
	    map.put("B", "b");
	    map.put("C", "c");
	    
	    Map<String, Integer>mapa = new HashMap<String,Integer>();
	    mapa.put("x", 1);
	    mapa.put("y", 2);
	    mapa.put("z", 3);
	    
	    int a = 1;
	    String title = "b";
	   
	    	Iterator<String> itr = map.keySet().iterator();
	    	while(itr.hasNext())
	    	{
	    		if(map.get(itr.next()).equals(title))
	    		{
	    			
	    			map.remove(itr.next());
	    			
	    		}
	    	}
	    
	    
	    
//	    while(itr.hasNext())
//	    {	    	
//	    	String name = map.get(itr.next());
//	    	if(map.containsValue(title))
//	    	{
//	    		String rn = map.get(itr.next());
//	    		System.out.println(rn);
//	    		//System.out.print(name + "\t");
//	    	}else {
//	    		System.out.println("???");
//	    	}
//	    }    
	    Set keyset = map.keySet();
	    System.out.println("Key set values are" + keyset);
	    
	    
//	    Iterator<String>itr = mapa.keySet().iterator();
//	    int pw = 1;
//	    String name = "A";
//	    int dd = mapa.get(name);
//	    if(pw==mapa.get(name))
//	    {
//	    	System.out.println("맞다");
//	    }
//	    else {
//	    	System.out.println("npo");
//	    }
//	    
//	    Map<String, Integer>mapaa = new HashMap<String,Integer>();
//	    if(mapaa.size() > 0) {
//			Set<String> keyset = mapaa.keySet();
//			System.out.println(keyset);
//		}else {
//			System.out.println("방이 없습니다.");
//		}
	    
//
//	    Iterator<String>str=map.keySet().iterator();
//    
//	    int i = 0;
//	    while(str.hasNext())
//	    {
//	    	String comp = "c";
//	    	String nameaa = map.get(str.next());
//	    	System.out.println("ㅡㅡㅡㅡ");
//	    	if(comp.equals(nameaa))
//	    	{
//	    		System.out.println("ㅡㅡㅡㅡ");
//	    		i=i+1;
//	    		System.out.printf("%d", i);
//	    	}else {
//	    		System.out.println("지나감");
//	    	}
//	    		
//	    }
//	    
		
	}

}
