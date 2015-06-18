import java.io.Serializable;
import java.util.*;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


@Document(collection = "polls")
public class Poll implements Serializable {

	@Id
	private String id;
	private  String poll_id;
	//private  String id;
	@JsonIgnore
	private  Integer mod_id;
    @NotEmpty
    private  String question;
    @NotEmpty
    //@DateTimeFormat(iso = ISO.DATE_TIME)
    private  String started_at;
    //@DateTimeFormat(iso = ISO.DATE_TIME)
    @NotEmpty
    private  String expired_at;
    @NotEmpty
    private  List<String> choice = new ArrayList<String> ();
    //@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonInclude(Include.NON_EMPTY)
    private  List<Integer> results = new ArrayList<Integer> ();

    @JsonIgnore
    private String mod_email;

    @JsonIgnore
    //@DateTimeFormat()
    private Date expired_date;

    @JsonIgnore
    private Boolean poll_sent;



    public Poll(Integer mod_id, String poll_id, String question, String started_at,
			String expired_at, List<String> choice,
		List<Integer> results, Date expired_date, Boolean poll_sent, String mod_email) {
		super();
		this.mod_id = mod_id;
		this.poll_id = poll_id;
		this.question = question;
		this.started_at = started_at;
		this.expired_at = expired_at;
		this.choice = choice;
		this.results = results;
		this.expired_date = expired_date;
        this.poll_sent = poll_sent;
        this.mod_email = mod_email;
	}
    public Poll() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public Integer getMod_id() {
		return mod_id;
	}
	public void setMod_id(Integer mod_id) {
		this.mod_id = mod_id;
	}
	public String getId() {
		return poll_id;
	}
	public void setId(String poll_id) {
		this.poll_id = poll_id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getStarted_at() {
		return started_at;
	}
	public void setStarted_at(String started_at) {
		this.started_at = started_at;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}
	public List<String> getChoice() {
		return choice;
	}
	public void setChoice(List<String> choice) {
		this.choice = choice;
	}
	public List<Integer> getResults() {
		return results;
	}
	public void setResults(List<Integer> results) {
		this.results = results;
	}
	public Date getExpired_date() {
		return expired_date;
	}
	public void setExpired_date(Date expired_date) {
		this.expired_date = expired_date;
	}
    public Boolean getPoll_sent() {
        return poll_sent;
    }
    public void setPoll_sent(Boolean poll_sent) {
        this.poll_sent = poll_sent;
    }
    public String getmod_email() {
        return mod_email;
    }
    public void setmod_email(String mod_email) {
        this.mod_email = mod_email;
    }
}