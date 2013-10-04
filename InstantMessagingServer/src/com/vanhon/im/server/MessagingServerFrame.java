package com.vanhon.im.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.vanhon.im.server.MessagingServer.OnStateChangedListener;

public class MessagingServerFrame extends JFrame implements ActionListener,OnStateChangedListener{

	private static final long serialVersionUID = 1L;

	private static final int SCREEN_WIDTH = 400;
	private static final int SCREEN_HEIGHT =300;
	
	private JButton startButton;
	private JButton stopButton;
	private JTextArea stateText;
	private JLabel portLabel;
	private JTextField portInput;
	
	private MessagingServer messagingServer;

	public MessagingServerFrame(String name) {
		super(name);
	}
		
	public void launch() {
		this.add(createPanel());
		this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JPanel createPanel() {
		
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        basic.setBorder(BorderFactory.createEmptyBorder(10, 0,10, 0));

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		stateText = new JTextArea("Server hasn't been started !",5,50);
		stateText.setEditable(false);
		stateText.setLineWrap(true);   	//auto newline
		stateText.setBackground(null);
		stateText.setMargin(new Insets(5, 5, 5, 5));
		
		top.setBorder(BorderFactory.createTitledBorder(null, "Server status", 0, 0, null, Color.BLUE));
		JScrollPane scrollPane = new JScrollPane(stateText);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		top.add(scrollPane,BorderLayout.CENTER);
		
		
		basic.add(top);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

        portLabel  = new JLabel("Port:");
        portInput  = new JTextField(10);
        portInput.setMaximumSize(new Dimension(80,30));
        portInput.setAlignmentY(CENTER_ALIGNMENT);
        
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		stopButton = new JButton("Stop");
		stopButton.addActionListener(this);

		bottom.add(portLabel);
		bottom.add(Box.createRigidArea(new Dimension(10, 0)));
		bottom.add(portInput);
		bottom.add(Box.createRigidArea(new Dimension(10, 0)));
        bottom.add(startButton);
        bottom.add(Box.createRigidArea(new Dimension(10, 0)));
        bottom.add(stopButton);

        basic.add(Box.createRigidArea(new Dimension(0,10)));
        basic.add(bottom);

		return basic;
	}
	
	
	public JButton getStartButton(){
		return this.startButton;
	}
	
	public JButton getStopButton(){
		return this.stopButton;
	}

	public static void main(String[] args) {
		MessagingServerFrame server = new MessagingServerFrame("Instant Messaging Server");
		server.launch();
	}


	
	public void startServer(){
		if(messagingServer == null){
			messagingServer = new MessagingServer();
			messagingServer.setOnStateChangedListener(this);
		}
		messagingServer.start();
		
	}
	
	public void stopServer(){
		if(messagingServer != null){
			messagingServer.stop();
		}
	}

	@Override
	public void onStart(boolean success) {
		if(success){
			stateText.setText("Server is running");
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
		}else{
			stateText.setText("Start server failure !");
		}
		
	}

	@Override
	public void onStop(boolean success) {
		if(success){
			stateText.setText("Stop server successful !");
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == startButton){
			startServer();
		}else if(obj == stopButton){
			stopServer();
		}
		
	}
}
