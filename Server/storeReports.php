<?php
	
	require_once 'include/DB_Reports.php';
	$activity_Header = "";
	$response = array("error"=>FALSE);
	$store_report = new DB_Reports();
	
	if(!empty($_POST['user_id']))
	{
		$activity_header = $_POST['report_type'];
		$user_id = $_POST['user_id'];

				
		switch($activity_header)
		{
			case "activity" :
				@error_log("Inside activity info type");
				$activity = $store_report->storeActivityInfo();
				echo $activity;
				break;
			
			case "blood" :

				
				$glucose_test_date = $_POST['glucose_test_date'];
				$is_fasting = $_POST['is_fasting'] ;
				$glucose_level = $_POST['glucose_level'] ; 
				$notes = $_POST['notes'] ;
				$is_delete = $_POST['isDelete'];
				$test_id = $_POST['test_id'];

				@error_log("Inside the blood glucose type");

				if(($is_delete != '0') && ($is_delete != '1'))
				{
					$blood = $store_report->storeGlucoseInfo($user_id, $glucose_test_date, $is_fasting, $glucose_level, $notes);
					echo $blood;
				}
				else
				{
					if($is_delete == 1)
					{
						$blood = $store_report->deleteGlucoseInfo($test_id);
						echo $blood;
					}
					else if($is_delete == 0)
					{
						$blood = $store_report->updateGlucoseInfo($test_id,$glucose_level,$notes, $is_fasting,$glucose_test_date);
						echo $blood;
					}
				}

				break;
			
			case "food" :
				@error_log("Inside the food info type");
				$food = $store_report->storeFoodInfo();
				echo $food;
				break;
			
			default :
				@error_log("Inside the default switch case");
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