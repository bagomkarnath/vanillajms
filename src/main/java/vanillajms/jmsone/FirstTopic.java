package vanillajms.jmsone;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

/*
 * Needed Java 1.5 and jndi
 */
public class FirstTopic {

	public static void main(String[] args) throws Exception {

		InitialContext initialContext = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		Session session = connection.createSession();
		Topic topic = (Topic) initialContext.lookup("topic/myTopic");
		
		MessageProducer producer = session.createProducer(topic);
		
		
		MessageConsumer consumer1 = session.createConsumer(topic);
		MessageConsumer consumer2 = session.createConsumer(topic);
		
		TextMessage message = session.createTextMessage("Text Message from Producer");
		
		producer.send(message);
		
		connection.start();
		
		TextMessage msgRecieved1 =  (TextMessage) consumer1.receive(5000);
		
		System.out.println("Message recieved by consumer1 : " + msgRecieved1.getText());
		
		TextMessage msgRecieved2 =  (TextMessage) consumer2.receive(5000);
		
		System.out.println("Message recieved by consumer2 : " + msgRecieved2.getText());
		
		connection.close();
		initialContext.close();
		
	}

}
