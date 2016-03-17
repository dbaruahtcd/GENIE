<?php
	
	require_once 'include/DB_Reports.php';
	$activity_header="";
	$response = array("error"=>FALSE);
	$get_reports = new DB_Reports();
	
	if(!empty($_POST['user_id']))
	{
		$activity_header = $_POST['report_type'];
		$user_id = $_POST['user_id'];
		
		
		switch($activity_header) 
		{
			case "blood_info" :
				
				@error_log("inside the blood info type");
				
				$glucose = $get_reports->getGlucoseInfo($user_id);
				echo $glucose;
				break;
			
			case "activity_info":
			
				@error_log("inside the activity info type");
				
				$blood = $get_reports->getActivityInfo($user_id);
				echo $blood;
				break;
				
			case "food_info" :
				
				@error_log("Inside the food info type");
				$food = $get_reports->getFoodInfo($user_id);
				echo $food;
				break;
			
			default :
				$response["error"]=TRUE;
				$response["error_msg"] = "The sent report type doesn't match the provided ones ";
				echo json_encode($response);
		}
			
		
	}
	else
	{
		$response["error"]=TRUE;
		$response["error_msg"]="The post fields are empty. Please send the request with the user_id fields";
		echo json_encode($response);
		
	}
	
?>