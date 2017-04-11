import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

   /**
    * @param args
    */
   public static void main(String[] args)
   {
      ServerSocket serverSocket = null;
      try
      {
    	  // (#1) open serverSocket with port number 9999
         serverSocket = new ServerSocket(9999);
         while (true)
         {
        	 // (#2) accept new incoming client connection
            Socket socket = serverSocket.accept();
            System.out.println("client");
            Thread thread = new PerClientThread(socket);
            thread.start();
         }
      } catch (Exception e)
      {
         System.out.println(e.getMessage());
      }

   }

}