<?php
	
	class DB_Reports
	{
		
		private $conn;
		
		function __construct()
		{
			require_once('DB_Connect.php');
			$db = new DB_Connect();
			$this->conn = $db->connect();
		}
		
		
		
		//retrieve email for all the activity types
		public function getEmail($user_id)
		{
			$query = "select email from user_info where user_id = $1";
			$val= array($user_id);
			$result = pg_query_params($this->conn,$query,$val);
			$row = pg_fetch_assoc($result);
			return $row['email'];
		}
		
		//stores the activity information of the user;
		public function storeFoodInfo($user_id, $food_type)
		{
			$query="INSERT INTO food_info(user_id, type_of_food) values
			($1, $2)";
			$val= array($user_id, $food_type);
			$result= pg_query_params($this->conn,$query,$val);
			
			if($result)
			{
				error_log("activity info inserted successfully");
				//$stmt="select * from food_info where e";
			}
		}
		
		// stores the blood glucose information of the user
		public function storeGlucoseInfo()
		{
			$query="INSERT INTO BLOOD_TEST_INFO(HAEMOGLOBIN_COUNT, PRE_FAST_BLOOD_SUGAR, POST_FAST_BLOOD_SUGAR, CHOLESTEROL,AHDL_CHOLESTEROL,LDL_CHOLESTEROL, USER_ID) values($1, $2, $3, $4, $5, $6, $7)";
			//$val=array($)
		}
		
		// stores the food intake information of the user
		
	
		//gets the blood glucose infomation
		
		public function getGlucoseInfo($user_id)
		{
			$val= array($user_id);
			$result_blood = array("error"=>"false");
			
			//getting the static blood information
			$query_pre = "SELECT user_id,blood_test_date,HAEMOGLOBIN_COUNT,CHOLESTEROL,AHDL_CHOLESTEROL, LDL_CHOLESTEROL FROM BLOOD_TEST_INFO where user_id=$1 and 
				blood_test_id = (SELECT MAX(BLOOD_TEST_ID) FROM BLOOD_TEST_INFO WHERE USER_ID = $1)";
			
			$result_pre = pg_query_params($this->conn,$query_pre,$val);
			
			//getting the current blood_glucose_info

			$query_curr = "SELECT pre_fast, post_fast, glucose_test_date FROM BLOOD_GLUCOSE_INFO 
							WHERE user_id = $1 ORDER BY glucose_test_date DESC";
			$result_curr = pg_query_params($this->conn,$query_curr,$val);
			

			if($result_curr && $result_pre)
			{
				$res_curr = pg_fetch_all($result_curr);
				$res_pre  = pg_fetch_assoc($result_pre);
				
				//creating the json format
				$result_blood["user_id"] = $res_pre["user_id"];
				$result_blood["blood_info"]["blood_test_date"] = $res_pre["blood_test_date"];
				$result_blood["blood_info"]["hg_count"] = $res_pre["haemoglobin_count"];
				$result_blood["blood_info"]["cholesterol"] = $res_pre["cholesterol"];
				$result_blood["blood_info"]["ahdl_cholesterol"] = $res_pre["ahdl_cholesterol"];
				$result_blood["blood_info"]["ldl_cholesterol"] = $res_pre["ldl_cholesterol"];
				$result_blood["glucose_test"] = $res_curr;

				return json_encode($result_blood);
			}
			else
			{
				$result_blood["error"] = TRUE;
				$result_blood["err_msg"] = "Test records for this particular user doesn't exist";
				return json_encode($result_blood);
			}
		}
		
		//fetches the activity info
		
		public function getActivityInfo($user_id)
		{
			$query = "SELECT ACTIVITY_TYPE,TIME_OF_ACTIVITY,HOURS_OF_SLEEP,CALORIES_BURNT,BLOOD_PRESSURE from ACTIVITY_INFO where user_id=$1";
			$val = array($user_id);
			$result = pg_query_params($this->conn,$query,$val);
			if($result)
			{
				$result_arr=pg_fetch_all($result);
				return json_encode($result_arr);
			}
		}
		
		
		//gets the food information 
		
		public function getFoodInfo($user_id)
		{
			$query = "SELECT TYPE_OF_FOOD, ESTIMATED_CALORIE from FOOD_INFO where user_id = $1";
			$val=array($user_id);
			$result = pg_query_params($this->conn, $query,$val);
			if($result)
			{
				$result_arr=pg_fetch_all($result);
				return json_encode($result_arr);
			
			}
		}
	}
	
	//$rep = new DB_Reports();
	//$mail = $rep->getEmail(104);
	//echo "The email is : $mail";
	
?>