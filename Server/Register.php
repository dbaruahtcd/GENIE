<?php

	//$req_dump = print_r($_REQUEST, TRUE);
	//$fp = fopen('logs/request.log', 'a');
	//error_log($req_dump);
	//fwrite($fp, $req_dump);
	//fclose($fp); 
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();
	//print_r($_POST);
	//json response array
	$response = array("error"=>FALSE);


if(isset($_POST['fullname']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['sex']) && isset($_POST['phonenumber']) && isset($_POST['dateofbirth']) && isset($_POST['weight']) && isset($_POST['height']) && isset($_POST['typeofdiabetes']) && isset($_POST['address']) && isset($_POST['country']) && isset($_POST['city']) && isset($_POST['zipcode']))
{
    //receiving the post params
    $name = $_POST['fullname'];
    $email = $_POST['email'];
    $password = $_POST['password'];
	
	// personal details
	$sex = $_POST['sex'];
	$phonenumber = $_POST['phonenumber'];
	$dateofbirth = $_POST['dateofbirth'];
	$weight = $_POST['weight'];
	$height = $_POST['height'];
	$typeofdiabetes = $_POST['typeofdiabetes'];
	$address = $_POST['address'];
	$country = $_POST['country'];
	$city = $_POST['city'];
	$zipcode = $_POST['zipcode'];
	// to handle the update of the user info fields
	$user_id = $_POST['id'];
		
    if(isset($_POST['id']))
    {
    	
    	$old_email = $db->getemail($user_id);
    
    	$user_check = $db->getUserByEmailAndPassword($old_email, $password);
	    	
    	if($user_check !== NULL)
    	{
	    	$user = $db->updateUser($name, $email, $password,$sex,$phonenumber, $dateofbirth, $weight, $height, $typeofdiabetes, $address, $country, $city, $zipcode,$user_id);
	    	@error_log("Line 48 - update user info");
	    	if($user)
	        {
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
	            // unable to store user details
	            $response["error"] = TRUE;
	            $response["error_msg"] = "Error occured during updating the user info";
	            echo json_encode($response);
	        }
	    }
	    
	    else
	    	{
		    	$response["error"]=FALSE;
		    	$response["error_msg"] = "The password does not exists";
		    	echo json_encode($response);
		    }


    }

    //checking if the user already exists
    elseif($db->isUserExists($email))
    {
        //user already exists
        $response["error"] = TRUE;
        $response["error_msg"] = "User already exists with the email". $email;
        echo json_encode($response);
    }
    else 
    {
        //create a new user
        $user = $db->storeUser($name, $email, $password,$sex,$phonenumber, $dateofbirth, $weight, $height, $typeofdiabetes, $address, $country, $city, $zipcode);
		@error_log("Line 47 - $user");
		$response = NULL;
		if($user)
        {
            
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
            // unable to store user details
            $response["error"] = TRUE;
            $response["error_msg"] = "Error occured during registration";
            echo json_encode($response);
        }
    }
}
else
{
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}


?>