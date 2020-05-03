import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is the Server class which establishes a connection with the client. It creates two seperate threads for two players giving them each X and O's
 * @author Mesbaul
 */
public class Server {

    /**
     * This is the main method which creates a serverSocket and establishes a connection and creates the two respective threads.
     * @param args Command Line Arguments
     * @throws Exception Exception due to Socket Connection
     */
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(22222);
        ExecutorService t = Executors.newFixedThreadPool(200);
        while (true) {
            TicTacToe tt = new TicTacToe();
            t.execute(tt.new Player(ss.accept(), 'X'));
            t.execute(tt.new Player(ss.accept(), 'O'));
        }
    }
}