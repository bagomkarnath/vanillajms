package vanillajms.jmsone;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/*
 * Needed Java 1.5 and jndi
 */
public class QueueBrowserDemo {
	
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
			TextMessage message1 = session.createTextMessage("Message 1");
			TextMessage message2 = session.createTextMessage("Message 2");
			
			producer.send(message1);
			producer.send(message2);
			System.out.println("Message sent");
			
			QueueBrowser browser = session.createBrowser(queue);
			
			Enumeration messagesEnum =  browser.getEnumeration();
			
			while (messagesEnum.hasMoreElements()) {
				TextMessage messageRecieved = (TextMessage) messagesEnum.nextElement();
				System.out.println("Message recieved : " + messageRecieved.getText());
			}
			
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
