import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;



@RestController
public class VotingController 

{
	ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.ssZ");
	SimpleDateFormat dateFor = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	Random r = new Random( System.currentTimeMillis() );
    //String expDate;
	ArrayList<Moderator> moderatorlist = new ArrayList<Moderator>();
	ArrayList<Poll> polllist = new ArrayList<Poll>();
	ArrayList<Integer> results = new ArrayList<Integer>();
	@ResponseBody @RequestMapping(value="/api/v1/moderators" , method = RequestMethod.POST, produces ="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Moderator createModerator(@Validated(Moderator.ModeratorValidator.class) @RequestBody  Moderator moderator)
	{
		Calendar create = Calendar.getInstance();
		int id =  (1 + r.nextInt(2)) * 100000 + r.nextInt(100000);
		moderator.setId(id);
		moderator.setCreated_at(dateFormat.format(create.getTime()));
		//moderatorlist.add(moderator);
		mongoOperation.save(moderator);
		return moderator;
	}

	//2. View Moderator
	@RequestMapping(value="/api/v1//moderators/{moderator_id}",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getmoderator(@PathVariable Integer moderator_id)
	{
		Moderator moderator = mongoOperation.findOne(new Query(Criteria.where("mod_id").is(moderator_id)), Moderator.class);
		return moderator;
	}

	//3. Update Moderator
	@ResponseBody @RequestMapping(value="/api/v1/moderators/{moderator_id}" , method = RequestMethod.PUT, produces ="application/json")
	public  Object updateModerator(@Validated(Moderator.ModeratorEditValidator.class)@PathVariable("moderator_id") int moderator_id ,@RequestBody  Moderator moderator){
		Calendar update = Calendar.getInstance();
		Moderator moderator1 = mongoOperation.findOne(new Query(Criteria.where("mod_id").is(moderator_id)), Moderator.class);
		moderator1.setEmail(moderator.getEmail());
		moderator1.setPassword(moderator.getPassword());

		mongoOperation.save(moderator1);
		return mongoOperation.findOne(new Query(Criteria.where("mod_id").is(moderator_id)), Moderator.class);

	}

	//4. Create Poll
	@RequestMapping(value="/api/v1/moderators/{moderator_id}/polls",method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Object createpoll(@Valid @RequestBody Poll poll,@PathVariable Integer moderator_id) 
	{
		Moderator moderator1 = mongoOperation.findOne(new Query(Criteria.where("mod_id").is(moderator_id)), Moderator.class);
		if(moderator1 != null)
		{
			int id =  (1 + r.nextInt(2)) * 100000000 + r.nextInt(100000000);
			String pid = Integer.toString(id, 36);
			poll.setId(pid);
			poll.setMod_id(moderator_id);
            poll.setmod_email(moderator1.getEmail());
            //expDate= poll.getExpired_at();
			dateFor.setTimeZone(TimeZone.getTimeZone("GMT"));
			try{
            poll.setExpired_date(dateFor.parse(poll.getExpired_at()));
			}
			catch(Exception e){
				System.out.println(e);
			}

            poll.setPoll_sent(false);
            mongoOperation.save(poll);

			//return mongoOperation.findOne(new Query(Criteria.where("mod_id").is(moderator_id).and("poll_id").is(pid)), Poll.class);
		
			Poll poll1 =  mongoOperation.findOne(new Query(Criteria.where("mod_id").is(moderator_id).and("poll_id").is(pid)), Poll.class);
		    return poll1;
		 }else return "Moderator not found";
		
	}

	//5. View Poll without result
	@RequestMapping(value="/api/v1//polls/{poll_id}",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getpollwithoutresult(@PathVariable String poll_id)
	{	
		Poll poll = mongoOperation.findOne(new Query(Criteria.where("poll_id").is(poll_id)), Poll.class);
		if (poll != null){
			return poll;
		}else return "Poll not found";
	}


	//6. View Poll with result
	@RequestMapping(value="/api/v1//moderators/{moderator_id}/polls/{poll_id}",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object getpollwithresult(@PathVariable Integer moderator_id,@PathVariable String poll_id)
	{

		Poll poll = mongoOperation.findOne(new Query(Criteria.where("poll_id").is(poll_id).and("mod_id").is(moderator_id)), Poll.class);
		if (poll != null){
			if (poll.getResults().isEmpty()){
				results = new ArrayList<Integer>();
				for (int i=0; i<poll.getChoice().size(); i++){
					results.add(0);
				}
				poll.setResults(results);
			}
			mongoOperation.save(poll);
            return mongoOperation.findOne(new Query(Criteria.where("poll_id").is(poll_id)), Poll.class);
		}else return " poll not found";
	}

	//7. List All Polls
	@RequestMapping(value="/api/v1/moderators/{moderator_id}/polls",method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<Poll> getpoll(@PathVariable Integer moderator_id)
	{
		List<Poll> poll1 = mongoOperation.find(new Query(Criteria.where("mod_id").is(moderator_id)),Poll.class);
        return poll1;
	}

	//8. Delete Poll
	@RequestMapping(value="/api/v1/moderators/{moderator_id}/polls/{poll_id}",method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public Object deletepoll(@PathVariable Integer moderator_id,@PathVariable String poll_id)
	{

		Poll poll1 = mongoOperation.findOne(new Query(Criteria.where("poll_id").is(poll_id).and("mod_id").is(moderator_id)), Poll.class);

		if(poll1 != null)
		{
			mongoOperation.remove(poll1);
			return "Poll deleted";
		}else return "Poll not found";
	}

	//9. Vote Poll
	@RequestMapping(value="/api/v1/polls/{poll_id}",method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void votepoll(@Valid @PathVariable String poll_id, @RequestParam  int choice)
	{
		Poll poll1 = mongoOperation.findOne(new Query(Criteria.where("poll_id").is(poll_id)), Poll.class);
		if(poll1 != null)
		{
			List res = poll1.getResults();
			Integer val=Integer.parseInt(res.get(choice).toString());
			val++;
			res.set(choice,val);
			poll1.setResults(res);
			mongoOperation.save(poll1);
		}

	}

}

