package applijms.common;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
 
/**
 * Hello world!
 */
public class SampleHelloWorld {
 
//    public static void main(String[] args) throws Exception {
//        thread(new HelloWorldProducer(), false);
//        thread(new HelloWorldConsumer(), false);
//        
//    }
// 
//    public static void thread(Runnable runnable, boolean daemon) {
//        Thread brokerThread = new Thread(runnable);
//        brokerThread.setDaemon(daemon);
//        brokerThread.start();
//    }
// 
//    public static class HelloWorldProducer implements Runnable {
//        public void run() {
//            try {
//                // Create a ConnectionFactory
//                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://127.0.0.1");
// 
//                // Create a Connection
//                Connection connection = connectionFactory.createConnection();
//                connection.start();
// 
//                // Create a Session
//                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//                
// 
//                // Create the destination (Topic or Queue)
//             //   Destination destination = session.createQueue("TEST.FOO");
//                Queue destination =  session.createQueue("TEST.FOO");
// 
//                // Create a MessageProducer from the Session to the Topic or Queue
//                MessageProducer producer = session.createProducer(destination);
//                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
// 
//                // Create a messages
//                String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
//                TextMessage message = session.createTextMessage(text);
// 
//                // Tell the producer to send the message
//                System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
//                producer.send(message);
//                producer.send(message);
//                
//                producer.send(message);
//                producer.send(message);
// 
//                // Clean up
//                session.close();
//               connection.close();
//            }
//            catch (Exception e) {
//                System.out.println("Caught: " + e);
//                e.printStackTrace();
//            }
//        }
//    }
// 
//    public static class HelloWorldConsumer implements Runnable, ExceptionListener {
//        public void run() {
//            try {
// 
//                // Create a ConnectionFactory
//                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://127.0.0.1");
// 
	
//                // Create a Connection
//                Connection connection = connectionFactory.createConnection();
//                connection.start();
// 
//                connection.setExceptionListener(this);
// 
//                // Create a Session
//                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
// 
//                // Create the destination (Topic or Queue)
//                Destination destination = session.createQueue("TEST.FOO");
// 
//                // Create a MessageConsumer from the Session to the Topic or Queue
//                MessageConsumer consumer = session.createConsumer(destination);
// 
//                Boolean done = false;
//              //  while(!done)// Wait for a message
//                //{
//                	Message message = consumer.receive(1000);
//                	
// 
//			        if (message instanceof TextMessage) {
//			            TextMessage textMessage = (TextMessage) message;
//			            String text = textMessage.getText();
//			            System.out.println("Received: " + text);
//			        } else {
//			            System.out.println("Received ELSE: " + message);
//			        }
//			       
//               // }
// 
//                consumer.close();
//                session.close();
//                connection.close();
//            } catch (Exception e) {
//                System.out.println("Caught: " + e);
//                e.printStackTrace();
//            }
//        }
// 
//        public synchronized void onException(JMSException ex) {
//            System.out.println("JMS Exception occured.  Shutting down client.");
//        }
//    }
}