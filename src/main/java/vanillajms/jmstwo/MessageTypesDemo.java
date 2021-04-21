package vanillajms.jmstwo;

import javax.jms.BytesMessage;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;


public class MessageTypesDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		Queue myQueue = (Queue) context.lookup("queue/myQueue");
		
		try (
				ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext("admin", "admin");
				
			) {
			
			JMSProducer producer = jmsContext.createProducer();
			
			BytesMessage byteMessage = jmsContext.createBytesMessage();
			byteMessage.writeUTF("Test Message");
			byteMessage.writeLong(123l);
			producer.send(myQueue, byteMessage);
			
			
			
			JMSConsumer consumer = jmsContext.createConsumer(myQueue);
			
				
			BytesMessage recievedMessage = (BytesMessage) consumer.receive(5000);
			System.out.println(recievedMessage.readUTF());
			System.out.println(recievedMessage.readLong());
			
			
			
		}
	}

}
