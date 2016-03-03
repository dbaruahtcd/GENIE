<?php
	require_once 'DB_Connect.php';
		
	$db = new DB_Connect();
	$conn = $db->connect();

	$qr = "insert into public.user_test(name,email) values('Babin','babin')";
    $result = pg_query($conn,$qr);
	echo $result;
?>