<?php
  
class DB_Connect
{

  private $db; 
  public function connect()
   {
      $host        = "host=127.0.0.1";
      $port        = "port=5432";
      $dbname      = "dbname=d8769lmj0di1e";
      $credentials = "user=odvorriftfuvhq password=Qnhig92_JpiuGjs6YecH-hiC0T";

      $db = pg_connect( "$host $port $dbname $credentials"  );
      if(!$db){
         echo "Error : Unable to open database\n";
      } else {
         echo "Opened database successfully\n";
      }
   }

   public function disconnect()
   {
      pg_connect($db);
   }

     
}



#$db1 = new DB_Connect();
#$db1->connect();
      

?>