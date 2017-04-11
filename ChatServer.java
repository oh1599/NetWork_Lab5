import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatServer extends JFrame implements ActionListener {
	BufferedReader in = null;
	BufferedWriter out = null;
	ServerSocket listener = null;
	Socket socket = null;
	JTextField serverMessage;
	JScrollPane spane;
	Receiver clientMessage;
	public ChatServer() {
		setTitle("���� ä�� â"); // ������ Ÿ��Ʋ
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //������ ���� ��ư(X)�� Ŭ���ϸ� ���α׷� ����
		setLayout(new BorderLayout()); //BorderLayout ��ġ�������� ���
		clientMessage = new Receiver(); // Ŭ���̾�Ʈ���� ���� �޽����� ����� ���۳�Ʈ
		clientMessage.setEditable(false); // ���� �Ұ�
		Thread t = new Thread(clientMessage); // Ŭ���̾�Ʈ���� �޽��� ������ ���� ������ ����
		serverMessage = new JTextField();
		serverMessage.addActionListener(this);
		spane = new JScrollPane(clientMessage); // �� �ؽ�Ʈ�� ����  ScrollPane���� ǥ��
		add(spane,BorderLayout.CENTER);
		add(serverMessage,BorderLayout.SOUTH);
		setSize(400, 200); // �� 400 �ȼ�, ���� 200 �ȼ��� ũ��� ������ ũ�� ����
		setVisible(true); // �������� ȭ�鿡 ��Ÿ������ ����
		
		System.out.println(System.getProperty("file.encoding"));
		
		try {
			setupConnection();
		} catch (IOException e) {
			handleError(e.getMessage());
		}
		t.start();
	}
	private void setupConnection() throws IOException {
		listener = new ServerSocket(9999); // ���� ���� ����
		socket = listener.accept(); // Ŭ���̾�Ʈ�κ��� ���� ��û ���
		System.out.println("�����");
		in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Ŭ���̾�Ʈ���� ��� ��Ʈ��
//		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")); // Ŭ���̾�Ʈ�κ����� �Է� ��Ʈ��
//		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")); // Ŭ���̾�Ʈ���� ��� ��Ʈ��
	}
	public static void main(String[] args) {
		ChatServer frame = new ChatServer();
	}

	private static void handleError(String string) {
		System.out.println(string);
		System.exit(1);
	}
	
	private class Receiver extends JTextArea implements Runnable {
		@Override
		public void run() {
			String inputMessage=null;
			while (true) {
				try {
					inputMessage = in.readLine(); // Ŭ���̾�Ʈ���� �� ���� ���ڿ� ����
//				String convert = new String(inputMessage.getBytes("MS949"), "MS949");
//				String str = convert+"\n";
//				clientMessage.append("\n" + str);
				clientMessage.append("\n" + inputMessage);
				int pos = clientMessage.getText().length();
				clientMessage.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
				} catch (IOException e) {
					handleError(e.getMessage());
				} 
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == serverMessage) {
			String outputMessage = serverMessage.getText(); // �ؽ�Ʈ �ʵ忡�� ���ڿ� ����
			try {
//				String str = "server>" + outputMessage+"\n";
//				String convert = new String(str.getBytes("UTF-8"), "UTF-8");
//				out.write(convert); // ���ڿ� ����
				String str=("server>" + outputMessage+"\n");
				out.write(str); // ���ڿ� ����
				out.flush();
				clientMessage.append("\nserver>" + outputMessage);
				int pos = clientMessage.getText().length();
				clientMessage.setCaretPosition(pos); // caret �������� ���� ���������� �̵�
				serverMessage.setText(null); // �Է�â�� ���ڿ� ����
			} catch (IOException e1) {
				handleError(e1.getMessage());
			} 
		}
	}
}
