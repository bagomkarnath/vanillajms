package vanillajms.jmstwo.messagestructure;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class MessagePriorityDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		Queue myQueue = (Queue) context.lookup("queue/myQueue");
		
		try (
				ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext("admin", "admin");
				
			) {
			
			JMSProducer producer = jmsContext.createProducer();
			
			String[] messages = new String[3];
			
			messages[0] = "Message One";
			messages[1] = "Message Two";
			messages[2] = "Message Three";
			
			producer.setPriority(3);
			producer.send(myQueue, messages[0]);
			
			producer.setPriority(1);
			producer.send(myQueue, messages[1]);
			
			producer.setPriority(9);
			producer.send(myQueue, messages[2]);
			
			producer.send(myQueue, "default priority");
			
			JMSConsumer consumer = jmsContext.createConsumer(myQueue);
			
			for (int i = 0; i < 3; i++) {
				//System.out.println("Message recieved : " + consumer.receiveBody(String.class));
				Message recievedMessage = consumer.receive();
				System.out.println("Priority : " + recievedMessage.getJMSPriority());
			}
			
			
			
			
		}
	}

}
