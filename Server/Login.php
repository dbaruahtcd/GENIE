<?php
	
	$req_dump = print_r($_REQUEST, TRUE);
	//$fp = fopen('logs/request.log', 'a');
	error_log($req_dump);
	//fwrite($fp, $req_dump);
	//fclose($fp); 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	error_log($_POST);
	$response = array("error" => FALSE);

	if(isset($_POST['email']) && isset($_POST['password']))
	{
		//receiving the post params
		$email = $_POST['email'];
		$password = $_POST['password'];

		// get the user by email and password
		error_log($email.$password);
		$user = $db->getUserByEmailAndPassword($email, $password);

		if($user !== NULL)
		{
			//the user is found
				$response["error"]=FALSE;
            	$response["user"]["name"] = $user["name"];
            	$response["user"]["email"] = $user["email"];
				$response["user"]["id"] = $user["user_id"];
				$response["user"]["sex"] = $user["sex"];
				$response["user"]["phonenumber"] = $user["phone_number"];
				$response["user"]["dateofbirth"] = $user["date_of_birth"];
				$response["user"]["weight"] = $user["weight"];
				$response["user"]["height"] = $user["height"];
				$response["user"]["typeofdiabetes"] = $user["diabetes_type"];
				$response["user"]["address"] = $user["address"];
				$response["user"]["country"] = $user["country"];
				$response["user"]["city"] = $user["city"];
				$response["user"]["zipcode"] = $user["zipcode"];
            $response["user"]["created_at"] = $user["created_at"];
            echo json_encode($response);
		}
		else
		{
			//user not found with the provided credentials
			$response["error"] = TRUE;
			$response["error_msg"] = "Login credentials are wrong. Please try again!";
			echo json_encode($response);
		}
	}
	else
	{
		//required params are missing
		$response["error"] =TRUE;
		$response["error_msg"] = "Required parameter email or password is missing!";
		echo json_encode($response);
	}


?>
