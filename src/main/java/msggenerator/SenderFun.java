package msggenerator;

import java.util.Date;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JOptionPane;
import javax.xml.crypto.Data;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SenderFun {

	public int send(String name, int count, int gap, String content, String mqip) {
		int successSendCount = 0;
		// ConnectionFactory ���ӹ�����JMS ������������
		ConnectionFactory connectionFactory;
		// Connection JMS �ͻ��˵�JMS Provider ������
		Connection connection = null;
		// Session һ�����ͻ������Ϣ���߳�
		Session session;
		// Destination ��Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭
		Destination destination;
		// MessageProducer��Ϣ������
		MessageProducer producer;
		// TextMessage message;
		// ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��jar
		connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://" + mqip + ":61616");
		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			// ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
			destination = session.createQueue(name);
			// �õ���Ϣ�����ߡ������ߡ�
			producer = session.createProducer(destination);
			// ���ò��־û����˴�ѧϰ��ʵ�ʸ�����Ŀ����
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage message = session.createTextMessage(content);
			producer.send(message);
			session.commit();
			
			for (int i = 1; i <= count; i++) {
				if (i % 1000 == 0) {
					try {
						if (null != connection)
							connection.close();
					} catch (Throwable ignore) {
					}
					connection = connectionFactory.createConnection();
					// ����
					connection.start();
				}
				// ��ȡ��������
				session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
				// ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
				destination = session.createQueue(name);
				// �õ���Ϣ�����ߡ������ߡ�
				producer = session.createProducer(destination);
				// ���ò��־û����˴�ѧϰ��ʵ�ʸ�����Ŀ����
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				// ������Ϣ���˴�д������Ŀ���ǲ��������߷�����ȡ
				sendMessage(session, producer, count, content, gap);
				session.commit();
				successSendCount++;
				try {
					if (null != session)
						session.close();
				} catch (Throwable ignore) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (Throwable ignore) {
			}
		}
		return successSendCount;
	}

	public static void sendMessage(Session session, MessageProducer producer, int count, String content, int gap)
			throws Exception {
		// String stamp=(new Date().getTime())/1000+"";
		// content=content.replaceAll("\"Stamp\":[0-9]{10}","\"Stamp\":"+
		// stamp);
		TextMessage message = session.createTextMessage(content);
		// ������Ϣ��Ŀ�ĵط�
		// System.out.println("������Ϣ��" + content);
		producer.send(message);
		Thread.sleep(gap);
	}

}
