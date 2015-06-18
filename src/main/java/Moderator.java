import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;


@Document(collection = "moderators")
public class Moderator implements Serializable {

//public class Moderator{
	
	@Id
	private String id;
	
    private  Integer mod_id;
    //@NotEmpty
    @NotBlank(groups= {Moderator.ModeratorValidator.class} ,message =" Name must not be empty")
	private  String name;
    //@NotEmpty
    @NotBlank(groups= {Moderator.ModeratorValidator.class,Moderator.ModeratorEditValidator.class} ,message =" Email must not be empty")
    private  String email;
    //@NotEmpty
    @NotBlank(groups= {Moderator.ModeratorValidator.class,Moderator.ModeratorEditValidator.class} ,message =" Password must not be empty")
    private  String password;
    //@NotEmpty
    private  String created_at;
    
    
	public Moderator(Integer mod_id, String name, String email, String password,
			String created_at) {
		super();
		this.mod_id = mod_id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.created_at = created_at;
	}
	public Moderator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return mod_id;
	}
	public void setId(Integer mod_id) {
		this.mod_id = mod_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
    public interface ModeratorValidator{
		
	};
	public interface ModeratorEditValidator{
		
	};
    
    
}