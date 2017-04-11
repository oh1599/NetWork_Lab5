import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class PerClientThread extends Thread
{
   static List<PrintWriter> list = Collections
         .synchronizedList(new ArrayList<PrintWriter>());

   Socket socket;

   PrintWriter writer;

   public PerClientThread(Socket socket)
   {
      this.socket = socket;

      try
      {
         writer = new PrintWriter(socket.getOutputStream());
         list.add(writer);
// (#3) add new client stream into list

      } catch (Exception e)
      {
         System.out.println(e.getMessage());
      }
   }

   public void run()
   {
      try
      {
         BufferedReader reader = new BufferedReader(new InputStreamReader(
               socket.getInputStream()));
         while (true)
         {
            String str = reader.readLine();
            if (str == null)
            {
               break;
            }
            sendAll(str);
         }
      } catch (Exception e)
      {
         System.out.println(e.getMessage());
      } finally
      {
         list.remove(writer);
// (#4) remove disconnected client from list

         try
         {
            socket.close();
         } catch (Exception ignored)
         {
         }
      }
   }

   private void sendAll(String str)
   {

// (#5) broadcast arriving message to all clients
      for(PrintWriter writer : list)
      {
         writer.println(str);
         writer.flush();
      }

   }

}