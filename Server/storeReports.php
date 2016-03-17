<?php
	
	require_once 'include/DB_Reports.php';
	$activity_Header = "";
	$response = array("error"=>FALSE);
	$store_report = new DB_Reports();
	
	if(!empty($_POST['user_id']))
	{
		$activity_header = $_POST['report_type'];
		$user_id = $_POST['user_id'];
		
		switch($report_type)
		{
			case "activity_info" :
				error_log("Inside activity info type");
				//gather the food information
				
				$food
				
				$store_report->storeActivityInfo();
				break;
			
			case "blood_glucose_info" :
				error_log("Inside the blood glucose type");
				$store_report->storeGlucoseInfo();
				break;
			
			case "food_info" :
				error_log("Inside the food info type");
				$store_report->storeFoodInfo();
				break;
			
			default :
				error_log("Inside the default switch case");
				$response["error"]=TRUE;
				$reponse["error_msg"]= "The post parameter doesn't match the checked parameters. Try Again!!";
				echo json_encode($response);
				
		}
	}
	else
	{
			$response["error"]=TRUE;
			$response["error_msg"] = "The post fields are empty. Please send the request with the user_id field";
			error_log("Inside the next else clause");
			echo json_encode($response);
			
	}
	
	
?>