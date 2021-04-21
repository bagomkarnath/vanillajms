package vanillajms.jmstwo;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class RequestReplyDemo {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext context = new InitialContext();
		Queue requestQueue = (Queue) context.lookup("queue/requestQueue");
		Queue replyQueue = (Queue) context.lookup("queue/replyQueue");
		
		try (
				ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
				JMSContext jmsContext = cf.createContext("admin", "admin");
				
			) {
			
			JMSProducer producer = jmsContext.createProducer();
			producer.setTimeToLive(2000);
			TextMessage message = jmsContext.createTextMessage("Text message with reply to");
			message.setJMSReplyTo(replyQueue);
			producer.send(requestQueue, message);
			
			JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
			TextMessage receiveMessage = (TextMessage) consumer.receive();
			
			System.out.println("Message recieved : " + receiveMessage.getText());
			
			JMSProducer replyProducer = jmsContext.createProducer();
			TextMessage replyMessage = jmsContext.createTextMessage("You are awesome");
			replyMessage.setJMSCorrelationID(receiveMessage.getJMSMessageID());
			replyProducer.send(receiveMessage.getJMSReplyTo(), replyMessage);
			
			
			JMSConsumer replyConsumer = jmsContext.createConsumer(requestQueue);
			TextMessage rmsg = (TextMessage) replyConsumer.receive();
			System.out.println("Message recieved : " + rmsg.getText());
			System.out.println("Corr ID : " + rmsg.getJMSCorrelationID());
			
		}
	}

}
	