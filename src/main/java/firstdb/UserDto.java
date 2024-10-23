package firstdb;

import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class UserDto {

    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phonenumber;
    private String role;

    public UserDto(String username, String password, String fullname, String email,String phonenumber, String role) {
        super();
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserDto [username=" + username + ", password=" + password + ", fullname=" +
                fullname + ", email=" + email + ",,phonenumber=" + phonenumber  + ", role=" + role + "]";
    }
}