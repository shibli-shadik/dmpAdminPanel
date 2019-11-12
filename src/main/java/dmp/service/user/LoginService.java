package dmp.service.user;

public interface LoginService 
{
    boolean validateLoginInfo(String email, String password);
}
