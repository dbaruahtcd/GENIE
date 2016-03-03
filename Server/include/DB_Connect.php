<?php
	
	// PHP code begins and ends with 
	//echo "My first php line" ; 
	//echo date("l");
	
	class DB_Connect
	{
		private $db; 
		public function connect()
		{
		  //$url		   = "pgsql";
		  $host        = "host=ec2-54-83-3-38.compute-1.amazonaws.com";
		  $port        = "port=5432";
		  $dbname      = "dbname=dfaje2dc0u5r2t";
		  $credentials = "user=lodiyhiqoqvhkj password=OYxjiZcGZEMf8jYLnUe9vcOOAs";

		  $db = pg_connect( " $url $host $port $dbname $credentials"  );
		  if(!$db){
			 echo "Error : Unable to open database\n";
		  } else {
			 echo "Opened database successfully\n";
		  }
		  return $db;
		}
	}
    
   
	//$db1 = new DB_Connect();
	//$db1->connect();
      
	
?>