// test cases to cover the handling the users connection to the backend  

<?php
require_once 'PHPUnit/Autoload.php';
require_once '../include/DB_Connect.php';

class DB_Function_Test extends PHPUnit_Framework_TestCase
{
    private $db1 ;
    private $func;
    
    function __construct()
    {
        $this->db1 = new DB_Connect();
        $this->func = new DB_Functions();
    }    
    
    
    //testing the getEmail() function
    public function testGetEmail()
    {
        $expected = "dan@tcd.ie";
        $actual = $this->func->getEmail();
        $this->assertEquals($expected, $actual);
        
    }

    //testing the storeUser() function
    public function testStoreUser()
    {

        $expected = "{error : false { name : "Dan", age : "25", blood_glucose : "120"}}";
        $actual = $this->func->storeUser();
        $this->JsonStringEqualsJsonStringTest($expected, $actual);
    }

    //testing the updateUser() function
    public function testUpdateUser()
    {
        $user='dan';
        $id = 12;
        $expected = "{"error" : false ,{ "name" : "Dan", "age" : "25", "blood_glucose" : "120" , "user_id" : "123" , "address" : "ireland"}}";
        $actual = $this->func->updateUser($user, $id);
        $this->JsonStringEqualsJsonStringTest($expected, $actual);
    }

    //testing the isUserExists() function
    public function testIsUserExists()
    {
        $expected = TRUE;
        $actual = $this->func->isUserExists();
        $this->assertEquals($actual, $expected);
    }

   
    //testing the connect function
    public function testConnect()
    {
        
        $expected = $this->db1->connect();
        $res = "Opened database successfully";
        $this->assertEquals($expected , $res);
    }

}

$test = new DB_Function_Test();
$test->testConnect();

?>
