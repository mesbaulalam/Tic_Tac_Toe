import javax.swing.*;
import java.awt.*;

/**
 * This class is for the creation of the GUI. It creates the menubars along with the menuitems, creates the 3x3 tic tac toe board and creates the textfield to input user information
 * @author Mesbaul
 */
public class Design {

    JMenuBar menu;
    JMenu control, help;
    static JPanel boardPanel = new JPanel();
    JPanel name = new JPanel();

    /**
     * This is a public getter for the JPanel which holds the 3x3 tic tac toe board
     * @return boardPanel
     */
    public static JPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * This is the main method of the class which serves as the flow of the program. It calls the menu function to crqft the menu, sets up the 3x3 Tic Tac Toe board and its layout, and crafts the textfield and submit button.
     */
    public Design() {
        menu();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        Client.getFrame().getContentPane().add(Design.getBoardPanel(), BorderLayout.CENTER);
        sub();
    }

    private void menu() {
        menu = new JMenuBar();
        control  = new JMenu("Control");
        control.add(Client.getExit());
        help = new JMenu("Help");
        help.add(Client.getInstruct());
        menu.add(control);
        menu.add(help);
        Client.getFrame().setJMenuBar(menu);
    }

    private void sub() {
        name.add(Client.getField());
        name.add(Client.getSubmitButton());
        Client.getFrame().add(name, BorderLayout.SOUTH);
    }
}
