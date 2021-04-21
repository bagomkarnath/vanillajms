package vanillajms.jmstwo;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class JMSContextDemo {

	public static void main(String[] args) throws NamingException {

		InitialContext context = new InitialContext();
		Queue myQueue = (Queue) context.lookup("queue/myQueue");
		
		try (
				ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext("admin", "admin");
				
			) {
			
			jmsContext.createProducer().send(myQueue, "2.0 message");
			
			String msg = jmsContext.createConsumer(myQueue).receiveBody(String.class);
			
			System.out.println("Message recieved : " + msg);
			
			
		}
		
	}

}
