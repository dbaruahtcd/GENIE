<?php
if(!$do_login) exit;
 
// declare post fields
 
$post_username = trim($_POST['email']);
$post_password = trim($_POST['password']);
 
$post_autologin = $_POST['autologin'];

$dbFn = new DB_Functions();

$credArr = $dbFn->getUserByEmailAndPassword($post_username, $post_password);
$config_username = $credArr['email'];
$config_password = $credArr['password'];
 
	if(!is_null($credArr))
	{
			// Autologin Requested?
			 
			if($post_autologin == 1)
			    {
			    $password_hash = md5($config_password); // will result in a 32 characters hash
			 
			    setcookie ($cookie_name, 'usr='.$config_username.'&hash='.$password_hash, time() + $cookie_time);
			    
			   }
			 
			header("Location: index.php");
			exit;
	}
	else
	{
	$login_error = true;
	}
?>