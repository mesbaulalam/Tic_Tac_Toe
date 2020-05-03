import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")

/**
 * This class implements the creation of X and O's in the tic tac toe board. It creates 9 white squares in the board which are the spaces for the X's and O's.
 * @author Mesbaul
 */
public class Grid extends JPanel {
    JLabel label = new JLabel();

    /**
     * This is the constructor of the class which creates the white squares for the X and O's
     */
    public Grid() {
        setBackground(Color.white);
        setLayout(new GridBagLayout());
        add(label);
    }

    /**
     * This method takes in a character which determines whether an X will be printed or O will be printed
     * @param text The symbol to be written in the Tic Tac Toe board i.e X or O
     */
    public void setText(char text) {

    	if(text=='O') {
    	    label.setIcon(new ImageIcon("o.png"));
        }
    	if(text=='X') {
    	    label.setIcon(new ImageIcon("x.png"));
        }
    }
}