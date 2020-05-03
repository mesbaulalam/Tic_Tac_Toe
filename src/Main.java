/**
 * This is the Main Class. From here, we create an instance of the client class, where the constructor is called which sets up the GUI and tries to connect to the server
 * @author Mesbaul
 */
public class Main {
    /**
     * This is the sole method of the Main Class which calls the entire client sided program.
     * @param args Command line arguments
     * @throws Exception Exception handling for calling sockets
     */
	public static void main(String[] args) throws Exception {
        Client client = new Client("localhost", 22222);
        client.play();
    }
}
