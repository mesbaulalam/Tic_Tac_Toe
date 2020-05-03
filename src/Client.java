import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

/**
 * This is the client class which is responsible for setting up the GUI, establishing a connection to the server and outputting messages to the user.
 * @author Mesbaul
 */
public class Client {

    static JFrame frame = new JFrame("Tic Tac Toe");
    static private JLabel message;

    private Grid[] box = new Grid[9];
    private Grid current;

    static JMenuItem instruct = new JMenuItem("Instructions");
    static JMenuItem exit = new JMenuItem("Exit");

    String name;
    static JButton submitButton = new JButton("Submit");
    static JTextField field = new JTextField("",40);
    
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private static boolean ready = false;

    /**
     * A public getter for the message variable which is used to communicate with players
     * @return message
     */
    public static JLabel getMessage() {
        return message;
    }

    /**
     * A public getter for the textfield in which players enter their names
     * @return field
     */
    public static JTextField getField() {
        return field;
    }

    /**
     * A public getter for the instruction menu item in which players receive game instructions
     * @return instruct
     */
    public static JMenuItem getInstruct() {
        return instruct;
    }

    /**
     * A public getter for the exit menu item by which players can close the game
     * @return exit
     */
    public static JMenuItem getExit() {
        return exit;
    }

    /**
     * A public getter for the main JFrame of the GUI
     * @return frame
     */
    public static JFrame getFrame() {
        return frame;
    }

    /**
     * A public getter for the JButton in which players submit their names
     * @return submitButton
     */
    public static JButton getSubmitButton() {
        return submitButton;
    }

    /**
     * The constructor of the class which establishes a connection with the server, creates all the GUI components and adds ActionListeners and MouseListeners to all the Interactive GUI components.
     * We call the constructor of the Design class in order to craft the GUI.
     * @param ip The ip address of the Socket
     * @param port The port number of the socket
     * @throws Exception Exception due to Socket connection
     */
    public Client(String ip, int port) throws Exception {

        socket = new Socket(ip, port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        message = new JLabel("Enter your player name...");
        message.setBackground(Color.lightGray);
        frame.add(message, BorderLayout.NORTH);
        new Design();
        for (int i = 0; i < box.length; i++) {
            final int j = i;
            box[i] = new Grid();
            box[i].addMouseListener(new boxListener(j));
            Design.getBoardPanel().add(box[i]);
        }
        submitButton.addActionListener(new submitListener());
        instruct.addActionListener(new InstrListener());
        exit.addActionListener(new ExitListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(617, 735);
        frame.setVisible(true);
        frame.setResizable(false);
        
    }

    class boxListener implements MouseListener {

        int j;

        public boxListener(int i) {
            j = i;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(submitButton.isEnabled()==true) {
                message.setText("Please Enter Your Name First!");
            }else if(ready==false) {
                message.setText("Please wait for your opponent to join!");
            }
            else {
                current = box[j];
                out.println("MOVE " + j);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }
    
    class submitListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent event)
    	{
    		String name = field.getText();
    		if(name.length()==0) {
    		    message.setText("Please Enter a Valid Name!");
            }
    		else {
                frame.setTitle("Tic Tac Toe-Player: " + name);
                message.setText("WELCOME " + name);
                submitButton.setEnabled(false);
                field.setEnabled(false);
                out.println("lol");
            }
    	}
    }
    
    class InstrListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent event)
    	{
    		String mes = "Some information about the game:\nCriteria for a valid move:\n- The move is not occupied by any mark.\n- The move is made in the player's turn.\n- The move is made within the 3 x 3 box.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n- Player 1 wins.\n- Player 2 wins.\n- Draw.";
            JOptionPane.showMessageDialog(frame, mes);
    	}
    }
    
    class ExitListener implements ActionListener
    {
    	public void actionPerformed(ActionEvent event)
    	{
    		System.exit(1);
    	}
    }

    /**
     * This is the method which serves as the main communication hub between the client and the server. The client program paints the TicTacToe board according to instructions from the server, and also displays win, lose and draw messages depending on what the server says.
     * All of this is done through a input stream, where different types of incoming messages determine different instructions.
     */
    public void play() {
        try {
            String received = in.nextLine();
            char mark = received.charAt(0);
            char mark2 = 'O';

            if(mark == 'O')
                mark2 = 'X';

            while (in.hasNextLine()) {
                received = in.nextLine();
                if (received.startsWith("VALID MOVE")) {
                    message.setText("Valid move, wait for your opponent.");
                    current.setText(mark);
                    current.repaint();
                } else if (received.startsWith("OPPONENT MOVED")) {
                    int index = Integer.valueOf(received.substring(15));
                    box[index].setText(mark2);
                    box[index].repaint();
                    message.setText("Your opponent has moved, now is your turn.");
                } else if (received.startsWith("WARNING")) {
                    message.setText(received.substring(8));
                } else if (received.startsWith("WIN")) {
                    JOptionPane.showMessageDialog(frame, "Congratulations. You win.");
                    break;
                } else if (received.startsWith("LOST")) {
                    JOptionPane.showMessageDialog(frame, "You lose.");
                    break;
                } else if (received.startsWith("TIE")) {
                    JOptionPane.showMessageDialog(frame, "Draw.");
                    break;
                } else if (received.startsWith("PLAYER LEFT"))
                {
                    JOptionPane.showMessageDialog(frame, "Game Ends. One of the players left!");
                    break;
                } else if(received.startsWith("START")) {
                    ready = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(1);
    }
}