
package officepro;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Task1{

    public void job(ServerSocket ss,String seq) throws IOException{
        
        Socket s=ss.accept();
        //System.out.println("Client Connected");
        String nc=seq+".txt";
        String sz;

        OutputStreamWriter os=new OutputStreamWriter(s.getOutputStream());
        PrintWriter ot=new PrintWriter(os);
        ot.println(nc);
        ot.flush();
        System.out.println("FILE REQUEST sent from server");
        
        ///////******** rcving size /////
        
        BufferedReader br1=new BufferedReader(new InputStreamReader(s.getInputStream()));
        sz=br1.readLine();
        
        
        //******* Receiving File *********//////
        
        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
        File f1=new File("/Users/Tashfiq/Desktop/copy/"+nc);
        List<String> list = new ArrayList<String>();

        
        
        try{
        
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/Tashfiq/Desktop/copy/"+nc)));
         String str;

         String tmp1=f1.getName().replace(".txt", "");
        
         
       while((str=br.readLine())!=null){
           
           bw.write(str);
           list.add(str);          
       }
       
       bw.flush();
       
       if(br.readLine()==null)
           System.out.println("FILE Read Successfull");
       
       
           }
           
           catch(IOException ioe){
              System.out.println("Error in ftp");

           }
        
        //********    LOG   **********
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String date1=dateFormat.format(date);
        System.out.println("File name: "+ f1.getName()+"Size: "+f1.length()+" Bytes Date: "+date1 );
        
        if(Long.toString(f1.length()) == null ? sz == null : Long.toString(f1.length()).equals(sz)){
            System.out.println("Whole File is SuccessFully Copied");
            
            
              //******* Database ***********

             String value=list.get(list.size()-1);
        String lastword = value.substring(value.lastIndexOf(" ")+1);
         Connection myconobj=null;
        String sql = "INSERT INTO TASHFIQ.SERVERINFO(FILE_NAME,FILE_SIZE,UPLOAD_TIME,LAST_WORD) VALUES (?, ?,?,?)";
        String sql1 = "SELECT * FROM SERVERINFO";

        try{
             myconobj=DriverManager.getConnection("jdbc:derby://localhost:1527/MyDataBase", "tashfiq","12345");
             
             PreparedStatement statement = myconobj.prepareStatement(sql);
                        statement.setString(1,f1.getName());
                        Double a=(double)(f1.length());
                       statement.setDouble(2, a);
                       statement.setString(3,date1);
                       statement.setString(4,lastword);
                       int rowsInserted = statement.executeUpdate();
                  if (rowsInserted > 0) {
                   System.out.println("A new user was inserted successfully!");               
                 } 
                  
     ///********* Retreive From Database   ******////
             String file_name=null ;
             Double file_size ;
            String  date_value1;
           String last_word ;
           
            Statement statement1 = myconobj.createStatement();
             ResultSet result = statement1.executeQuery(sql1);
             while (result.next()){
            file_name = result.getString(1);
           file_size = result.getDouble(2);
           date_value1 = result.getString(3);
           last_word = result.getString(4);
              //System.out.println(file_name);
             //System.out.println(date_value1);
   
           }
            
            String nm=file_name;
             
             
             
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    

        }
        
        else if(f1.length()==0){
            
           System.out.println("File Not found");

            
        }
        else{
            
            System.out.println("Whole File is SuccessFully Copied");
            
        }
        
        
        
        
        
    } 
    
    

}

public class newproject {

    
    public static void main(String[] args) throws IOException {
        
         
        System.out.println("Server is started");
                ServerSocket ss=new ServerSocket(6666);
                System.out.println("Server is waiting for client ");


        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date2 = new Date();
        String date3=dateFormat.format(date2);
        //String name=date3.substring(0,8);
        //System.out.println(name);     
        
        int i=0;
        String seq;
        Task1 t1=new Task1();
        
        
         try {
        while (true) {
            
            String name=date3.substring(0,8);
    
            seq=name+String.format("%04d", i++);
            System.out.println("***********   File name: "+seq+".txt    ********");

            t1.job(ss, seq);
            System.out.println("File name: "+seq+".txt is copied");

            

            Thread.sleep(60 * 1000);
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        
        
    }
    
}
