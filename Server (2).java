import java.io.*;
import java.net.*;

public class Server {

  public final static int SOCKET_PORT = 4444;  // you may change this
  public final static String
  FILE = "fileshag.txt"; 

  public final static String FILE_TO_SEND = "file.txt";
  public final static int FILE_SIZE = 6022386;

  public static void main (String [] args ) throws IOException {
   	
	int bytesRead;
    	int current = 0;
    	FileOutputStream fos = null;
    	BufferedOutputStream bos = null;

 	FileInputStream fis = null;
    	BufferedInputStream bis = null;
    	OutputStream os = null;

    	ServerSocket servsock = null;
    	Socket sock = null;
      	servsock = new ServerSocket(SOCKET_PORT);
       	System.out.println("Waiting for client...");
        sock = servsock.accept();
        System.out.println("Accept this connection");
	int ch;
        
	BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
	
	BufferedReader br1=null;
	
do{	
System.out.println("1. Receiving Encrypted File \t2. Sending Decrypted File\t3.  exit\n");
	System.out.println("Enter Your Choice");
	ch=Integer.parseInt(br.readLine());
try{	
switch(ch)
	{

		case 1: 
			try {
			byte [] mybytearray  = new byte [FILE_SIZE];
      			InputStream is = sock.getInputStream();
      			fos = new FileOutputStream(FILE);
      			bos = new BufferedOutputStream(fos);
      			bytesRead = is.read(mybytearray,0,mybytearray.length);
      			current = bytesRead;

      			do {
         			bytesRead =
            			is.read(mybytearray, current, (mybytearray.length-current));
         			if(bytesRead >= 0) current += bytesRead;
      			} while(bytesRead > -1);

      			bos.write(mybytearray, 0 , current);
      			bos.flush();
      			System.out.println("File " + FILE
          		+ " downloaded (" + current + " bytes read)");
    			}

   			finally {
      				if (fos != null) fos.close();
      				if (bos != null) bos.close();
      				if (sock != null) sock.close();
      				//if (servsock != null) servsock.close();
     				}
			System.out.println("\nContent of fileshag.txt...");
			br1 = new BufferedReader(new FileReader("fileshag.txt"));
    			for (String line; (line = br1.readLine()) != null;) {
        		System.out.print(line);
        		}
			System.out.println("\n");
			break;

		case 2: 
			
        		try {
			File myFile = new File (FILE_TO_SEND);
          		byte [] mybytearray  = new byte [(int)myFile.length()];
          		fis = new FileInputStream(myFile);
          		bis = new BufferedInputStream(fis);
          		bis.read(mybytearray,0,mybytearray.length);
          		os = sock.getOutputStream();
          		System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
          		os.write(mybytearray,0,mybytearray.length);
          		os.flush();
          		}
        		finally {
          		if (bis != null) bis.close();
          		if (os != null) os.close();
          		if (sock!=null) sock.close();
          		//if (servsock != null) servsock.close();	 
        		}
			System.out.println("\nContent of file.txt...");
			br1 = new BufferedReader(new FileReader("fileshag.txt"));
    			for (String line; (line = br1.readLine()) != null;) {
        		System.out.print(line);
        		}
			System.out.println("\n");
			break;


	}}
finally{
servsock.close();
}
}while(ch!=3);
        System.out.println("\nDone.");
     
  }
}

