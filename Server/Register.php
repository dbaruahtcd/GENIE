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
	$address = $_POST['typeofdiabetes'];
	$country = $_POST['country'];
	$city = $_POST['city'];
	$zipcode = $_POST['zipcode'];
	

    //checking if the user already exists
    if($db->isUserExists($email))
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
        if($user)
        {
            $response["error"]=FALSE;
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
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