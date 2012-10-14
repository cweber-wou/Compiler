import java.io.*;
/*  This is an example of reading a TEXT file one character at a time.
The read() method returns an integer that must be cast to a  character. When the
end of the file is reached, the read()   method returns a -1.  */

class StreamReader
{
	private char current;
	private char peek;
	private int line;
	private String fileName;
	private File cFile;
	private FileReader cReader;


public StreamReader()
{

}

public StreamReader(String inFile)
{
	fileName=inFile;
	line=1;
	current=' ';
	peek=' ';
	openFile();


}

private void openFile()
{
 cFile=new File (fileName);
 
 try {
	cReader=new FileReader (cFile);
	current = (char) cReader.read();
	 peek= (char) cReader.read();
	 if (current == '\n') line++;
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 
 

}

public void close()
{
 try {
	cReader.close();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 
}

public char getCurent()
{
  return current;
}

public char getPeek()
{

 return peek;
}
public  char next()
{
 current = peek;
 try {
	peek=(char) cReader.read();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 if (current == '\n') line++;
 return current;
}

public void advance ()
{//issue if you advance over a newline char it will not count the new line
	if (peek == '\n') line++;
	try {
	current = (char) cReader.read();
	peek=(char) cReader.read();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 
 if (current =='\n') line++;
}

public int getLine()
{
return line;
}
public String getFilename()
{
return fileName;
}

public static void main( String [] args ) throws IOException
	{

	  StreamReader x = new StreamReader("notes");
		System.out.println("Current: "+x.getCurent());
		System.out.println("Peek: "+x.getPeek());
		for (int i = 0; i < 1000; i++) {
			System.out.println(i+":"+x.next());
			}
		System.out.println(x.getPeek());
		System.out.println("Skip a few");
		x.advance();
		x.advance();
		System.out.println("Current: "+x.getCurent());
		System.out.println("Line Number: "+x.getLine());
		System.out.println("Filename: "+x.getFilename());
		x.close();


	}
}
