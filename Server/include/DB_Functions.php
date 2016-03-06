<?php
//this class contains functions to manipulatin the database

class DB_Functions {
	
	private $conn;
	//private $table_name;
	
	

	// constructor
	function __construct() {
		require_once 'DB_Connect.php';
		$db = new DB_Connect();
		$this->conn = $db->connect();
		//$table_name = "user_test";
	}

		//destructor

	function __destructor() {
	}

	/*
	* Storing new user
	*returns user details
	*/

    public function storeUser($name, $email, $password, $sex, $phonenumber, $dateofbirth, $weight, $height, $typeofdiabetes, $address, $country, $city, $zipcode) {
		
		$hash = $this->hashAlgo($password);
    	$encrypted_password = $hash["encrypted"]; //encrypted password
    	$salt = $hash["salt"]; // the salt associated with the password

    	$query = "INSERT INTO user_info(name, email, password, salt, sex, phone_number, date_of_birth, weight, height, diabetes_type, address, country, city, zipcode) values($1, $2, $3, $4)";

    	$vals = array($name,$email,$encrypted_password, $salt, $sex, $phonenumber, $dateofbirth, $weight, $height, $typeofdiabetes, $address, $country, $city, $zipcode);
    	
    	$result = pg_query_params($this->conn,$query,$vals);
    

    	//check for successful store

    	if($result)
    	{
    		$stmt = "Select * from user_info where email = $1";
    		$eml = array($email);
    		$check = pg_query_params($this->conn,$stmt,$eml);
    		$user = pg_fetch_assoc($check);
    		return $user;
    	}
    	else 
    		return false;
    }
    
	
	/*
	*this function gets a user my email and password
	@param -email and password of the user
	*/

	public function getUserByEmailAndPassword($email, $password)
	{
		$query = "Select name,email,salt,created_at,password from user_info where email = $1";
		$val = array($email);
		$result = pg_query_params($this->conn, $query, $val);
			
		//while ($row = pg_fetch_assoc($result)) {
		//error_log("123".$row);
		

		// fetch the row as an associative array
		$user = pg_fetch_assoc($result);
			//while ($row = pg_fetch_assoc($result)) {
		//error_log("user".$row);

		$user_entered_password = $this->checkhashAlgo($user['salt'],$password);
		
		//echo $user_entered_password;
		//echo $user['password']."<br\>";
		error_log("111".$user_entered_password);
		error_log("222".$user['password']);
		//print_r($user_entered_password);
		if($result && (strcmp($user_entered_password,$user['password']) == 0))
		{	
			//$user = pg_fetch_assoc($result);
			//return "validated";
			error_log($user);
			return $user;
		}
		else
			return NULL;


	}	

	/**
	*check for the existence of the user
	* @param email
	*/
	 public function isUserExists($email)
	 {
	 	$query = "Select * from user_info where email=$1";
	 	$val = array($email);
	 	$result = pg_query_params($this->conn, $query, $val);

	 	if(pg_num_rows($result) > 0)
	 	{
	 		return true;
	 	}
	 	else
	 	{
	 		return false;
	 	}
	 }


	 /*
	 *this algorithm hashes the password together with the salt and returns the result
	 * @param password
	 */
	 public function hashAlgo($password)
	 {
	 	$salt = sha1(rand());
	 	$salt = substr($salt,0,10);
	 	$encrypted = base64_encode(sha1($password . $salt, true) . $salt);
	 	$hash = array("salt" => $salt, "encrypted"=>$encrypted);
	 	return $hash;

	 }

	 /*
	 * decrypting the password
	 * @param salt, password
	 * returns the hash string
     */

     public function checkhashAlgo($salt, $password)
     {
     	$hash = base64_encode(sha1($password . $salt, true) . $salt);
     	return $hash;
     }
	
}


//-------------//test codes
	//$db_fun = new DB_Functions();
	//$db_fun->storeUser('Dan','Dan','Dan');
	//$db_fun->storeUser('Dan1','Dan1','Dan1');
	//echo $db_fun->isUserExists('Dan');
	//print_r($db_fun->getUserByEmailAndPassword('Dan','Dan'));

?>
