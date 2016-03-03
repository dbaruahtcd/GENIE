<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

//json response array
$response = array("error"=>FALSE);

if(isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']))
{
    //receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];

    //checking if the user already exists
    if($db->isUserExists($email))
    {
        //user already exists
        $response["error"] = TRUE;
        $response["error_msg"] = "User already exists with the email". $emaill;
        echo json_encode($response);
    }
    else 
    {
        //create a new user
        $user = $db->storeUser($name, $email, $password);
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