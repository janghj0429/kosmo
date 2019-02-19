import java.util.Scanner;

class PhoneInfo
{
	String name;
	String phoneNumber;
	
	public PhoneInfo(String name, String num)
	{
		this.name = name;
		phoneNumber=num;
	}
	
	public void showPhoneInfo()
	{
		System.out.println("name : "+name);
		System.out.println("phone : "+phoneNumber);
	}
}

class PhoneUnivInfo extends PhoneInfo
{
	String major;
	int year;
	
	public PhoneUnivInfo(String name, String num, String major, int year)
	{
		super(name, num);
		this.major = major;
		this.year = year;
	}
	
	public void showPhoneInfo()
	{
		super.showPhoneInfo();
		System.out.println("major : " + major);
		System.out.println("year : " + year);
	}
}

class PhoneCompanyInfo extends PhoneInfo
{
	String company;
	
	public PhoneCompanyInfo(String name, String num, String company)
	{
		super(name, num);
		this.company = company;
	}
	
	public void showPhoneInfo()
	{
		super.showPhoneInfo();
		System.out.println("company : "+company);
	}
}

class PhoneBookManager
{
	final int MAX_CNT=100;
	PhoneInfo[]infoStorage=new PhoneInfo[MAX_CNT];
	int curCnt=0;
	
	private PhoneInfo readFriendInfo()
	{
		System.out.print("이름 : ");
		String name=MenuViewer.keyboard.nextLine();
		System.out.print("전화번호 : ");
		String phone=MenuViewer.keyboard.nextLine();
		return new PhoneInfo(name, phone);
	}
	
	private PhoneInfo readUnivFriendInfo()
	{
		System.out.print("이름 : ");
		String name=MenuViewer.keyboard.nextLine();
		System.out.print("전화번호 : ");
		String phone=MenuViewer.keyboard.nextLine();
		System.out.print("전공 : ");
		String major=MenuViewer.keyboard.nextLine();
		System.out.print("학년 : ");
		int year=MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		return new PhoneUnivInfo(name, phone, major, year);
	}
	
	private PhoneInfo readCompanyFriendInfo()
	{
		System.out.print("이름 : ");
		String name=MenuViewer.keyboard.nextLine();
		System.out.print("전화번호 : ");
		String phone=MenuViewer.keyboard.nextLine();
		System.out.print("회사 : ");
		String company=MenuViewer.keyboard.nextLine();
		return new PhoneCompanyInfo(name, phone, company);
	}
	
	public void inputData()
	{
		System.out.println("데이터 입력을 시작합니다.");
		System.out.println("1. 일반, 2. 대박, 3.회사");
		System.out.print("선택 >> ");
		int choice = MenuViewer.keyboard.nextInt();
		MenuViewer.keyboard.nextLine();
		PhoneInfo info = null;
		
		switch(choice)
		{
		case 1:
			info = readFriendInfo();
			break;
		case 2:
			info = readUnivFriendInfo();
			break;
		case 3:
			info = readCompanyFriendInfo();
			break;
		}
		
		infoStorage[curCnt++] = info;
		System.out.println("데이터 입력이 완료되었습니다. \n");
	}
	
	public void searchData()
	{
		System.out.println("데이터 검색을 시작합니다..");
		
		System.out.print("이름 : ");
		String name=MenuViewer.keyboard.nextLine();
		
		int dataIdx=search(name);
		if(dataIdx<0)
		{
			System.out.println("해당하는 데이터가 존재하지 않습니다. \n");
		}
		else
		{
			infoStorage[dataIdx].showPhoneInfo();
			System.out.println("데이터 검색이 완료되었습니다. \n");
		}
	}
	
	public void deleteDate()
	{
		System.out.println("데이터 삭제를 시작합니다..");
		
		System.out.print("이름 : ");
		String name=MenuViewer.keyboard.nextLine();
		
		int dataIdx=search(name);
		if(dataIdx<0)
		{
			System.out.println("해당하는 데이터가 존재하지 않습니다. \n");
		}
		else
		{
			for(int idx=dataIdx; idx<curCnt-1; idx++)
				infoStorage[idx]=infoStorage[idx+1];
			
			curCnt--;
			System.out.println("데이터 삭제가 완료되었습니다. \n");
		}
	}
	
	private int search(String name)
	{
		for(int idx=0; idx<curCnt; idx++)
		{
			PhoneInfo curInfo = infoStorage[idx];
			if(name.compareTo(curInfo.name)==0)
				return idx;
			
		}
		return -1;
	}	
}

class MenuViewer
{
	public static Scanner keyboard = new Scanner(System.in);
	
	public static void showMenu()
	{
		System.out.println("선택하세요...");
		System.out.println("1. 데이터 입력");
		System.out.println("2. 데이터 검색");
		System.out.println("3. 데이터 삭제");
		System.out.println("4. 프로그램 종료");
		System.out.print("선택 : ");
	}
}

public class PhoneBookVer04 {

	public static void main(String[] args) {
		PhoneBookManager manager=new PhoneBookManager();
		int choice;
		
		while(true)
		{
			MenuViewer.showMenu();
			choice=MenuViewer.keyboard.nextInt();
			MenuViewer.keyboard.nextLine();
			
			switch(choice)
			{
			case 1:
				manager.inputData();
				break;
			case 2:
				manager.searchData();
				break;
			case 3:
				manager.deleteDate();
				break;
			case 4:
				System.out.println("프로그램을 종료합니다.");
				return;
			}
		}

	}

}
