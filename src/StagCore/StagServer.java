package StagCore;

import StagExceptions.StagException;
import java.io.*;
import java.net.*;

/**
 * The sole purpose of this class is to pass commands from connected players to engine
 * and send players the output message.
 */
public class StagServer {

    private StagEngine gameEngine;

    public static void main(String[] args)
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            StagGame game = new StagGame();
            game.generateLocations(entityFilename);
            game.generateActions(actionFilename);
            gameEngine = new StagEngine(game);
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            //noinspection InfiniteLoopStatement
            while(true) acceptNextConnection(ss);
        } catch(IOException | StagException ioe) {
            ioe.printStackTrace();
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String line = in.readLine();
        try {
            gameEngine.processMessage(line);
            out.write(gameEngine.getReturnMessage() + "\n");
        }
        catch (StagException se){
            out.write("ERROR: An error occurred. " + se.getErrorMessage());
            se.printStackTrace();
        }
    }
}