package eu.q_b.asn007.encore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import eu.q_b.asn007.lambda.Main;

public class UnZipper {
	
	 private final static int BUFFER = 10241024;  
	    /** 
	     * @param args 
	     */  
	    
	    public File recursiveUnzip(File inFile, File outFolder)  
	    {  
	        Main.getFramework().log("Extracting archive: " + inFile.getName() + " to " + outFolder.getName(), this.getClass());
	    	try  
	         {  
	              this.createFolder(outFolder, true);  
	              BufferedOutputStream out = null;  
	              ZipInputStream  in = new ZipInputStream((new FileInputStream(inFile)));  
	              ZipEntry entry;  
	              while((entry = in.getNextEntry()) != null)  
	              {  
	                   Main.getFramework().log("Processing entry: " + entry + ", extracting to " + outFolder.getPath() + File.separator + entry.getName() , this.getClass());  
	                   int count;  
	                   byte data[] = new byte[BUFFER];  
	                     

	                   File newFile = new File(outFolder.getPath() + File.separator + entry.getName());  
	                   Stack<File> pathStack = new Stack<File>();  
	                   File newNevigate = newFile.getParentFile();  
	                   while(newNevigate != null){  
	                       pathStack.push(newNevigate);  
	                       newNevigate = newNevigate.getParentFile();  
	                   }  
	 
	                   while(!pathStack.isEmpty()){  
	                       File createFile = pathStack.pop();  
	                       this.createFolder(createFile, true);  
	                   }  
	                   if(!entry.isDirectory()){  
	                         out = new BufferedOutputStream(  
	                                   new FileOutputStream(newFile),BUFFER);  
	                         while ((count = in.read(data,0,BUFFER)) != -1){  
	                              out.write(data,0,count);  
	                         }  
	                         this.cleanUp(out);
	                   }  
	              }  
	              this.cleanUp(in);  
	              return outFolder;  
	         }
	    	catch(Exception e)
	         {  
	              e.printStackTrace();  
	              return inFile;  
	         }  
	    }  
	      
	    public void removeAllZipFiles(File folder)
	    {  
	        String[] files = folder.list();
	        for(String file: files){  
	            File item = new File(folder.getPath() + File.separator + file);
	            
	           	if(item.exists() && item.getName().toLowerCase().endsWith(".zip"))
	            {
	            	Main.getFramework().log("Processing entry: " + item.getName(), this.getClass());
	                item.delete();   
	            }  
	        }  
	    }  
	      
	      
	    private void createFolder(File folder, boolean isDriectory)
	    {  
	        if(isDriectory)
	        {  
	            folder.mkdir();  
	        }  
	    }  
	      
	    private void cleanUp(InputStream in) throws Exception
	    {
	    
	         in.close();  
	    }  
	      
	    private void cleanUp(OutputStream out) throws Exception
	    {  
	         out.flush();  
	         out.close();  
	    }  

}
