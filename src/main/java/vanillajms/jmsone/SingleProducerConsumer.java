package vanillajms.jmsone;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * Needed Java 1.5 and jndi
 */
public class SingleProducerConsumer {
	
	public static void main(String[] args) {
		InitialContext initialContext = null;
		Connection connection = null;
		Session session = null;
		try {
			initialContext = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
			connection = factory.createConnection();
			session = connection.createSession();
			Queue queue = (Queue) initialContext.lookup("queue/myQueue");
			
			MessageProducer producer = session.createProducer(queue);
			TextMessage message = session.createTextMessage("Text Message from Producer");
			
			producer.send(message);
			System.out.println("Message sent");
			
			MessageConsumer consumer = session.createConsumer(queue);
			connection.start();
			TextMessage messageRecieved = (TextMessage) consumer.receive(5000);
			String msg = messageRecieved.getText();
			System.out.println("Message recieved : " + msg);
			
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (initialContext != null) {
				try {
					initialContext.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
