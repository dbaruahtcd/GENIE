<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$response = array("error" => FALSE);

	if(isset($_POST['email']) && isset($_POST['password']))
	{
		//receiving the post params
		$email = $_POST['$email'];
		$password = $_POST['$password'];

		// get the user by email and password
		$user = $db->getUserByEmailAndPassword($email, $password);

		if($user != false)
		{
			//the user is found
			$response["error"] = FALSE;
			$response["user"]["name"] = $user["name"];
			$response["user"]["email"] = $user["email"];
			$response["user"]["created_at"]= $user["created_at"];
			echo json_encode($response);
		}
		else
		{
			//user not found with the provided credentials
			$response["error"] = TRUE;
			$responsep["error_msg"] = "Login credentials are wrong. Please try again!";
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
