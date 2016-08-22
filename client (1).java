import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Client {

  public final static int SOCKET_PORT = 4444;      // you may change this
  public final static String SERVER = "127.0.0.1";  // localhost
  public final static String FILE = "ecp.txt"; 
  public final static String FILE_TO_RECEIVED = "file.txt"; 
  public final static int FILE_SIZE = 6022386; 
public static void main (String [] args ) throws IOException {
      	
	Socket sock = null;
       	sock = new Socket(SERVER, SOCKET_PORT);
      	System.out.println("Connecting to server...");
	int ch;
 	 
    	FileInputStream fis = null;
    	BufferedInputStream bis = null;
    	OutputStream os = null;
 int bytesRead;
    	int current = 0;
    	FileOutputStream fos = null;
    	BufferedOutputStream bos = null;
BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
      	
	BufferedReader br1=null;

	do{
	System.out.println("1. Send File \t2. Receive  File\t3.  exit\n");
	System.out.println("Enter Your Choice");
	ch=Integer.parseInt(br.readLine());
try{
switch(ch)
	{

	case 1: // Encrypting and Sending File
		try 
		{
			String key = "squirrel123"; // needs to be at least 8 characters for DES

			FileInputStream fis1 = new FileInputStream("shag.txt");
			FileOutputStream fos1 = new FileOutputStream("ecp.txt");
			encrypt(key, fis1, fos1);

		} 
                catch (Throwable e) 
		{
			e.printStackTrace();
		}

		System.out.println("Content of shag.txt...");
		br1 = new BufferedReader(new FileReader("shag.txt"));
    		for (String line; (line = br1.readLine()) != null;) {
        	System.out.print(line);
        	}
		System.out.println("\n");
		System.out.println("Content of ecp.txt...");
		br1 = new BufferedReader(new FileReader("ecp.txt"));
    		for (String line; (line = br1.readLine()) != null;) {
        	System.out.print(line);
        	}
		System.out.println("\n");

		try 
		{
	  		File myFile = new File (FILE);

          		byte [] mybytearray  = new byte [(int)myFile.length()];

          		fis = new FileInputStream(myFile);
          		bis = new BufferedInputStream(fis);
          		bis.read(mybytearray,0,mybytearray.length);
          		os = sock.getOutputStream();
          		System.out.println("Sending " + FILE + "(" + mybytearray.length + " bytes)");
          		os.write(mybytearray,0,mybytearray.length);
          		os.flush();
          			
        	}
        	finally 
		{
          		if (bis != null) bis.close();
          		if (os != null) os.close();
          		//if (sock!=null) sock.close();
        	}
		System.out.println("Encrypted File Sent to server...\n");
		break;


case 2:	//Receive and Decrypting file
		try 
		{
			byte [] mybytearray  = new byte [FILE_SIZE];
     	 		InputStream is = sock.getInputStream();
      			fos = new FileOutputStream(FILE_TO_RECEIVED);
      			bos = new BufferedOutputStream(fos);
      			bytesRead = is.read(mybytearray,0,mybytearray.length);
      			current = bytesRead;

      			do 
			{
         			bytesRead=is.read(mybytearray, current, (mybytearray.length-current));
         			if(bytesRead >= 0) current += bytesRead;
      			} while(bytesRead > -1);

      			bos.write(mybytearray, 0 , current);
      			bos.flush();
      			System.out.println("File " + FILE_TO_RECEIVED
         		 + " downloaded (" + current + " bytes read)");
    		}
    		finally 
		{
      			if (fos != null) fos.close();
      			if (bos != null) bos.close();
      			//if (sock != null) sock.close();
    		}
		//Decryption
		try 
		{
			String key = "squirrel123"; // needs to be at least 8 characters for DES

			FileInputStream fis2 = new FileInputStream("file.txt");
			FileOutputStream fos2 = new FileOutputStream("dec.txt");
			decrypt(key, fis2, fos2);
		} 
		catch (Throwable e) 
		{
			e.printStackTrace();
		}

		System.out.println("Content of file.txt...");
		br1 = new BufferedReader(new FileReader("file.txt"));
    		for (String line; (line = br1.readLine()) != null;) {
        	System.out.print(line);
        	}
		System.out.println("\n");
		System.out.println("Content of decrypted.txt...");
		br1 = new BufferedReader(new FileReader("dec.txt"));
    		for (String line; (line = br1.readLine()) != null;) {
        	System.out.print(line);
        	}
		System.out.println("\n");

		break;

	default:System.out.println("Invalid Choice");
		break;

	}}
finally{
sock.close();
}
}while(ch!=3);
	System.out.println("Done.");
    
  }


 public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
  encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
  }

  public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
  encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
  }

  public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {
  DESKeySpec dks = new DESKeySpec(key.getBytes());
  SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
  SecretKey desKey = skf.generateSecret(dks);
  Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

  	if (mode == Cipher.ENCRYPT_MODE) 
  	{
		cipher.init(Cipher.ENCRYPT_MODE, desKey);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		doCopy(cis, os);
  	} 
  	else if (mode == Cipher.DECRYPT_MODE) 
  	{
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		CipherOutputStream cos = new CipherOutputStream(os, cipher);
		doCopy(is, cos);
  	}
  }

  public static void doCopy(InputStream is, OutputStream os) throws IOException {
  byte[] bytes = new byte[64];
  int numBytes;
	
  	while ((numBytes = is.read(bytes)) != -1) 
  	{
  		os.write(bytes, 0, numBytes);
  	}
  os.flush();
  os.close();
  is.close();
  }

}
