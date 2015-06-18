import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import com.mongodb.*;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class KafkaClient {
    ApplicationContext context= new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    MongoOperations mongoOperations= (MongoOperations)context.getBean("mongoTemplate");

    @Scheduled(fixedRate = 3000)
    public void KafkaThread()
    {
        long events = 22;
        Random rnd = new Random();
		StringBuffer sb= new StringBuffer();
        
        Properties props = new Properties();
        
		props.put("metadata.broker.list", "54.149.84.25:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class","polling.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<String, String>(config);

        Calendar cal= Calendar.getInstance();

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        TimeZone tz = TimeZone.getTimeZone("GMT");
        cal.setTimeZone(tz);
        Date d2=cal.getTime();
        
        System.out.println(d2);
        
        //List<Poll> poll1 = mongoOperations.find(new Query(Criteria.where("poll_sent").is(false).and("expired_date").lt(d2)), Poll.class);
        //List<Poll> poll1 = mongoOperations.find(new Query(Criteria.where("poll_sent").is(false)), Poll.class);
        List<Poll> poll1 = mongoOperations.find(new Query(new Criteria().andOperator(Criteria.where("expired_date").lte(d2), Criteria.where("poll_sent").is(false))), Poll.class);
		//System.out.println(poll1.size());
		Iterator<Poll> pollIterator = poll1.iterator();

        while (pollIterator.hasNext()) {
			Poll poll=pollIterator.next();
            if (poll != null) {
                poll.setPoll_sent(true);
                System.out.println("expired at " + d2);
                mongoOperations.save(poll);
                System.out.println("Found!!!");
				
				for (int i = 0; i< poll.getChoice().size(); i++)
				{
				if (i == (poll.getChoice().size()-1)){
				sb.append(poll.getChoice().get(i)+"="+poll.getResults().get(i));
				}else{
				sb.append(poll.getChoice().get(i)+"="+poll.getResults().get(i)+",");
				}
				}
				 
	           // KeyedMessage<String, String> data = new KeyedMessage<String, String>("cmpe273-topic", poll.getmod_email()+":009437064:Poll Result ["+poll.getChoice().get(0)+"="+poll.getResults().get(0)+","+poll.getChoice().get(1)+"="+poll.getResults().get(1)+"]" );
			   KeyedMessage<String, String> data = new KeyedMessage<String, String>("cmpe273-topic", poll.getmod_email()+":009437064:Poll Result ["+sb.toString()+"]" );
                System.out.println(data);
                producer.send(data);
                sb.delete(0, sb.length());
            }
        }
        

    }

}