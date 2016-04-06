//test class for  handling the activities of the user
<?php

require_once 'PHPUnit/Autoload.php';
require_once '../include/DB_Reports.php';

class DB_Reports_Test extends PHPUnit_Framework_TestCase
{
	private $report;
	private $db;
	
	function __constructor()
	{
		$this->db = new Connect();
		$this->report = new DB_Reports();
	}


	//testing the storeGlucoseInfo() function
	public function testStoreGlucoseInfo()
	{
		$user_id = 123;
		$actual = $this->report->storeGlucoseInfo($user_id);
		$expected = { "error" : "False" , "test_id" : "123"};
		JsonStringEqualsJsonStringTest($actual, $expected);
	}

	//testing the updateGlucoseInfo() function
	public function testUpdateGlucoseInfo()
	{
		$user_id = 123;dib
		$glucose_level = 123;
		$notes = "update";
		$test_date = "2016-03-03";
		$expected = {"error" : "false", "blood_test_id":"123"}
		$actual = $this->report->updateGlucoseInfo($user_id, $glucose_level,$notes, $test_date);
		JsonStringEqualsJsonStringTest($actual, $expected);
	}

	//testing the deleteGlucoseInfo function
	public function testDeleteGlucoseInfo()
	{
		$user_id = 123;
		$blood_test_id = 12;
		$expected = {"error" : "false", "blood_test_id" : "12"};
		$actual = $this->report->deleteGlucoseInfo($user_id, $blood_test_id);
		JsonStringEqualsJsonStringTest($actual, $expected);
	}

	//testing the getGlucoseInfo() function
	public function testGetGlucoseInfo()
	{
		$user_id = 123;
		$actual = $this->report->getGlucoseInfo($user_id);
		$expected = {"error" :"false" , "blood" : 12};
		JsonStringEqualsJsonStringTest($actual, $expected);
	}

	//test for the storeBloodInfo() function
	public function testStoreGlucoseInfo()
	{
		$user_id = 12;
		$haemoglobin_count = 122;
		$pre_fast_blood_sugar = 100; 
		$post_fast_blood_sugar = 130;
		$cholesterol = 24 ;
		$ahdl_cholesterol = 35 ;
		$ldl_cholesterol = 36;
		$actual = $this->report->storeBloodInfo($user_id,$haemoglobin_count,$pre_fast_blood_sugar, $post_fast_blood_sugar, $cholesterol
			, $ahdl_cholesterol,$ldl_cholesterol);
		$expected = {"user_id" = "12"};
		JsonStringEqualsJsonStringTest($actual, $expected);
	}

	//test for getting the food information of the user
	public function testGetFoodInfo()
	{
		$user_id = 12;
		$actual = $this->report->getFoodInfo($user_id);
		$expected = {"type_of_food":"calorie", "estimated_calorie":"202"};
		JsonStringEqualsJsonStringTest($expected,$actual);

	}

	//test for getting the activity information of the user
	public function testGetActivityInfo()
	{
		$user_id = 12;
		$actual = $this->report->getFoodInfo($user_id);
		$expected = {"activity_type":"average","time_of_activity":"2016-01-30","hours_of_sleep":"7","calories_burnt":"201",
		"blood_pressure":"120"};
		JsonStringEqualsJsonStringTest($actual,$expected);
	}

	//test for the function storeFoodInfo() function
	public function testStoreFoodInfo()
	{
		$user_id = 12;
		$food_type = "fibre";
		$expected = {"error":"false"};
		$actual = $this->report->storeFoodInfo($user_id,$food_type);
		JsonStringEqualsJsonStringTest($expected, $actual);
	}



?>