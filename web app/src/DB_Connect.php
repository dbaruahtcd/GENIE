<?php
  
  class DB_Connect
  {
    private $db; 
    public function connect()
    {
      //$url       = "pgsql";
      $host        = "host=ec2-54-83-3-38.compute-1.amazonaws.com";
      $port        = "port=5432";
      $dbname      = "dbname=dfaje2dc0u5r2t";
      $credentials = "user=lodiyhiqoqvhkj password=OYxjiZcGZEMf8jYLnUe9vcOOAs";
      $db = pg_connect( "$host $port $dbname $credentials"  );
      if(!$db){
       echo "Error : Unable to open database\n";
       header('Location:404.html');
      } else {
       //echo "Opened database successfully\n";
      }
      return $db;
    }
  }
    
      
  
?>