package db;
/** 로그인 정보를 저장하기 위한 싱글톤 클래스 */
public class UserInfo {
	private static UserInfo sharedInstance = new UserInfo();
	private UserInfo() {}
	
	public String userID, userName, address;
	public boolean isAdmin;
	
	public static UserInfo getInstance() { return sharedInstance; }
	
	public boolean validateUser() {
		if(userID != null && userName != null && address != null) return true;
		else return false;
	}
	
	public void clearUserInfo() {
		userID = null;
		userName = null;
		address = null;
		isAdmin = false;
	}
}
