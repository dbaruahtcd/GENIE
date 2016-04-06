<?php
if(!$do_login) exit;
 
// declare post fields
 
$post_username = trim($_POST['email']);
$post_password = trim($_POST['password']);
 
$post_autologin = $_POST['autologin'];

$dbFn = new DB_Functions();

$credArr = $dbFn->getUserByEmailAndPassword($post_username, $post_password);
$cookie_check_usr = $credArr['email'];
$cookie_check_pswd = $credArr['password'];
 
	if(!is_null($credArr))
	{
			// Autologin Requested?
			 
			if($post_autologin == 1)
			    {
			    $password_hash = md5($cookie_check_pswd); // will result in a 32 characters hash
			 
			    setcookie ($cookie_name, 'usr='.$cookie_check_usr.'&hash='.$password_hash, time() + $cookie_time);
			    
			   }
			 
			header("Location: index.php");
			exit;
	}
	else
	{
	$login_error = true;
	}
?>