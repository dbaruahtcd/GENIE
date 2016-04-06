// tests the connection to heroku database
<?php
require_once 'PHPUnit/Autoload.php';
require_once '../include/DB_Connect.php';

class DB_Connect_Test extends PHPUnit_Framework_TestCase
{
    private $db1 ;
    
    function __construct()
    {
        $this->db1 = new DB_Connect();
    }    
    
    public function testConnect()
    {
        
        $expected = $this->db1->connect();
        $res = "Opened database successfully";
        if($this->assertEquals($expected , $res))
        
    }

}

$test = new DB_Connect_Test();
$test->testConnect();

?>
