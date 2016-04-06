<?php 
session_start();
    //include('includes/config.php');
include_once '../src/DB_Connect.php';
    //require_once 'config.php';
include_once '../src/DB_Functions.php';

    //$dbFnObj; // connection resource
    if(is_null(@$dbFnObj)){
       $dbFnObj = new DB_Functions();
         //$connRes = $connObj->connect();
   }

   
   if(isset($_POST['register'])){
      $_SESSION['fname'] = $_POST['fname'];
      $_SESSION['lname'] = $_POST['lname'];
      $_SESSION['dob'] = $_POST['dob'];
      $_SESSION['sex'] = $_POST['sex'];
      $_SESSION['email'] = $_POST['email'];
      $_SESSION['phno'] = $_POST['phno'];
      $_SESSION['qualif'] = $_POST['qualif'];
      $_SESSION['address'] = $_POST['address'];
      $_SESSION['city'] = $_POST['city'];
      $_SESSION['country'] = $_POST['country'];
      $_SESSION['zip'] = $_POST['zip'];
      //$_SESSION['password'] = $_POST['password'];
      //$_SESSION['confirm_password'] = $_POST['confirm_password'];

      /*if(strlen($_POST['name']) < 3){
        header("Location:register.php?err=" . urlencode("The name must be at least 3 characters long"));
        exit();
    }*/
    if ($_POST['password'] != $_POST['confirm_password']) {
        header("Location:register.php?err=" . urlencode("The Password and Confirm Password does not match"));
        exit();
    }

    elseif (strlen($_POST['password']) < 5) {
        header("Location:register.php?err=" . urlencode("The Password should be atleast 5 characters"));
        //header("Location:index.php");
        exit();
    }
    elseif ($dbFnObj->isUserExists($_POST['email'])){
        header("Location:register.php?err=" . urlencode("Email id is already in use. Please use another one"));
        exit();
    }
    else {
     $fname = trim($_POST['fname']);
     $lname = trim($_POST['lname']);
     $dateofbirth = trim($_POST['dob']);
     $sex = trim($_POST['sex']);
     $email = trim($_POST['email']);
     $password = trim($_POST['password']);
     $phonenumber = trim($_POST['phno']);
     $qual = trim($_POST['qualif']);
     $address = trim($_POST['address']);
     $city = trim($_POST['city']);
     $country = trim($_POST['country']);
     $zipcode = trim($_POST['zip']);

     $insertOK = $dbFnObj->storeDoc($fname, $lname, $qual, $phonenumber, $email, $city, $zipcode, $country, $address, $password, $sex, $dateofbirth);
     if ($insertOK) {
        header("Location:index.php");
    }else{
        header("Location:register.php?err=" . urlencode("Registration is incomplete, Please try again."));
    }
        //$db -> query($query);
        //header("Location:index.php");
}
}


?>


<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Genie :: Homepage</title>

    <!-- Bootstrap Core CSS -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="../css/agency.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="../font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
    <link href='https://fonts.googleapis.com/css?family=Kaushan+Script' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Droid+Serif:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700' rel='stylesheet' type='text/css'>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

    </head>

    <body id="page-top" class="index">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header page-scroll">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand page-scroll" href="../index.html">Genie</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="hidden">
                            <a href="#page-top"></a>
                        </li>
                        <li>
                            <a class="page-scroll" href="login.php">Login</a>
                        </li>

                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container-fluid -->
        </nav>

        <!-- Header -->
        <header>
            <div class="container">
                <div class="intro-text-form">

                    <div class="row">
                        <div class="col-md-4 col-md-offset-4">
                            <div class="login-panel panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title">Please Register</h3>
                                </div>
                                <?php if(isset($_GET['err'])) { ?>
                                <div class="alert alert-danger"> <?php echo $_GET['err']; ?> </div>
                                <?php } ?>
                                <?php if(isset($_GET['loggedout'])) { ?>
                                <div class="alert alert-success"> <?php echo $_GET['loggedout']; ?> </div>
                                <?php } ?>
                                <div class="panel-body">
                                    <form role="form" method="post" >
                                        <fieldset>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="First Name" name="fname" type="text" value="<?php echo @$_SESSION['fname'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Last Name" name="lname" type="text" value="<?php echo @$_SESSION['lname'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Date of Birth" name="dob" type="text" value="<?php echo @$_SESSION['dob'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Gender" name="sex" type="text" value="<?php echo @$_SESSION['sex'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="E-mail" name="email" type="email" value="<?php echo @$_SESSION['email'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Password" name="password" type="password" value="" required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Confirm Password" name="confirm_password" type="password" value="" required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Phone Number" name="phno" type="text" value="<?php echo @$_SESSION['phno'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Qualification" name="qualif" type="text" value="<?php echo @$_SESSION['qualif'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Address" name="address" type="text" value="<?php echo @$_SESSION['address'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="City" name="city" type="text" value="<?php echo @$_SESSION['city'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Country" name="country" type="text" value="<?php echo @$_SESSION['country'];?>" autofocus required>
                                            </div>
                                            <div class="form-group">
                                                <input class="form-control" placeholder="Zip Code" name="zip" type="text" value="<?php echo @$_SESSION['zip'];?>" autofocus required>
                                            </div>
                                <!-- <div class="checkbox">
                                    <label>
                                        <input name="remember" type="checkbox" value="Remember Me">Remember Me
                                    </label>
                                </div> -->
                                <!-- Change this to a button or input when using this as a form -->
                                <button type="submit" name="register" class="btn btn-lg btn-success btn-block">Register</button>
                                
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>    

    </div>

</div>

</header>



<footer>
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <span class="copyright">Copyright &copy; Genie 2016</span>
            </div>
            <div class="col-md-4">
                <ul class="list-inline social-buttons">
                    <li><a href="#"><i class="fa fa-twitter"></i></a>
                    </li>
                    <li><a href="#"><i class="fa fa-facebook"></i></a>
                    </li>
                </ul>
            </div>
            <div class="col-md-4">
                <ul class="list-inline quicklinks">
                    <li><a href="#">Privacy Policy</a>
                    </li>
                    <li><a href="#">Terms of Use</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</footer>


<!-- jQuery -->
<script src="../js/jquery.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="../js/bootstrap.min.js"></script>

<!-- Plugin JavaScript -->
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
<script src="../js/classie.js"></script>
<script src="../js/cbpAnimatedHeader.js"></script>

<!-- Contact Form JavaScript -->
<script src="../js/jqBootstrapValidation.js"></script>
<script src="../js/contact_me.js"></script>

<!-- Custom Theme JavaScript -->
<script src="../js/agency.js"></script>


<!-- jQuery -->
<script src="../bower_components/jquery/dist/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="../bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="../bower_components/metisMenu/dist/metisMenu.min.js"></script>

<!-- Custom Theme JavaScript -->
<script src="../dist/js/sb-admin-2.js"></script>

</body>

</html>