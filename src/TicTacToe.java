import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This is the TicTacToe class which is the main implementation of the TicTacToe Game.Each player have their own socket connection and a designated opponent.
 * The player is assigned a symbol, then the game checks whether both opponents have hit the submit button or not, and then the game checks whether there is any illegal move, wrong turns or whether there is only one single player.
 * Finally, it checks whether 3x3 Tic Tac Toe is completely filled up without any winning combination which results in a tie.
 * @author Mesbaul
 */
public class TicTacToe {

    String p1 = "";
    String p2 = "";
    private Set<PrintWriter> writers = new HashSet<>();

    private Player[] box = new Player[9];

    Player current;

    private boolean win() {
        boolean win = (box[0] != null && box[0] == box[1] && box[0] == box[2])
            || (box[3] != null && box[3] == box[4] && box[3] == box[5])
            || (box[6] != null && box[6] == box[7] && box[6] == box[8])
            || (box[0] != null && box[0] == box[3] && box[0] == box[6])
            || (box[1] != null && box[1] == box[4] && box[1] == box[7])
            || (box[2] != null && box[2] == box[5] && box[2] == box[8])
            || (box[0] != null && box[0] == box[4] && box[0] == box[8])
            || (box[2] != null && box[2] == box[4] && box[2] == box[6]
        );
        return win;
    }

    private boolean tie()
    {
    	for(int i = 0; i < 9; i++)
    	{
    		if(box[i] == null)
    			return false;
    	}
    	return true;
    }

    private synchronized void move(int pos, Player player) {
        if (player != current) {
            throw new IllegalStateException("It is not your turn. Wait for your Opponent!");
        } else if (box[pos] != null) {
            throw new IllegalStateException("Space filled up. Please choose another box!");
        }
        box[pos] = current;
        current = current.player2;
    }

    class Player implements Runnable {
    	char symbol;
        Player player2;
        Socket socket;
        Scanner input;
        PrintWriter out;

        public Player(Socket socket, char symbol) {
            this.socket = socket;
            this.symbol = symbol;
        }

        @Override
        public void run() {
            try {
                players();
                read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (player2 != null && player2.out != null) {
                player2.out.println("PLAYER LEFT");
            }
        }

        private void players() throws IOException {
            input = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            writers.add(out);
            out.println(symbol);

            if (symbol == 'X') {
                current = this;
            } else {
                player2 = current;
                player2.player2 = this;
            }
        }

        private void read() {

            while (input.hasNextLine()) {
                String move = input.nextLine();
                if(move.startsWith("lol")) {
                    if (p1 == "") {
                        p1 = "done";
                    }else if (p2 == "") {
                        p2 = "done";
                        for(PrintWriter writer : writers) {
                            writer.println("START");
                        }
                    }
                }
                if (move.startsWith("MOVE")) {
                    mark(Integer.valueOf(move.substring(5)));
                }
            }
        }

        private void mark(int pos) {
            try {
                move(pos, this);
                out.println("VALID MOVE");
                player2.out.println("OPPONENT MOVED " + pos);
                if (win()) {
                    out.println("WIN");
                    player2.out.println("LOST");
                } else if (tie())
                {
                    out.println("TIE");
                    player2.out.println("TIE");
                }
            } catch (IllegalStateException e) {
                out.println("WARNING " + e.getMessage());
            }
        }
    }
}


