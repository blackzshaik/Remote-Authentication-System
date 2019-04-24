<?php
include 'dbConnect.php';
include 'session.php';
$pid = $_GET['preid'];
if(isset($_SESSION['name']))
{
header ("Location:profile.php?user=".$_SESSION['name']."");
}else{

}
if($_SERVER["REQUEST_METHOD"]== "POST")
{
$username = mysqli_real_escape_string($connect,$_POST['email']);
$password = mysqli_real_escape_string($connect,$_POST['password']);

$sql = "SELECT * FROM registration WHERE username='$username'";
$result =mysqli_query($connect,$sql);
$row = mysqli_num_rows($result);
#$row = 1;
#echo "  ".$result;
#echo " ".$sql;
if($row == 1)
{

$tNow = date('H:i:s');
//$_SESSION['name'] = $username;

while($data = mysqli_fetch_assoc($result))
  {
    $usernamea = mysqli_real_escape_string($connect,$data["username"]);
    $emaila = mysqli_real_escape_string($connect,$data["email"]);
    $deviceida =  mysqli_real_escape_string($connect,$data["deviceid"]);
  }

  if($deviceida == "Device Not Verified"){
    echo "Device Not Verified";
  }else{
    $skey = 0;
    $sql1 = "INSERT INTO loginrecords (username,email,deviceid,auth,ltime) VALUES ('".$usernamea."',
                                                                        '".$emaila."',
                                                                        '".$deviceida."',
                                                                        '".$skey."',
                                                                        '".$tNow."')";

                if($connect->query($sql1) == TRUE)
                    {

                              sleep(10);
                              $sql3 = "SELECT * FROM loginrecords WHERE username='$username' AND auth = 1 AND ltime = '$tNow' " ;
                              $result1 =mysqli_query($connect,$sql3);
                              $row1 = mysqli_num_rows($result1);
                              #$row = 1;
                              #echo "  ".$result;
                              #echo " ".$sql;
                              if($row1 == 1)
                              {
                                echo "<div>Login Sucessfull </div>";
                              }else{
                                echo "<div>Authentication Failed. Please try again</div>";
                              }
                  }else{
                  echo " ".$sql."<br>".$connect->error;
                        }

  }

}else{
  $logerror = 'Hey '.$username.'! Your username or password is incorrect .<br>Please check your details .For Help
  <a href="help.php">Click Here</a>';
}
}

function waitForAuth($tNow){
  echo "Function Call";

}

?>

<!DOCTYPE HTML>
<html lang="en">
<head>
<title>PLAS- Password Less Authentication System</title>
<meta charset="utf-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<meta name="viewport" content="width=device-width,initial-scale=1">
<style>
.navbar{
  background-color:#FFB914;
}
</style>
</head>
<body>
  <nav class="navbar navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <a class="navbar-brand" id="h"  style="color:black;font-size:24px">PLAS! - Password Less Authentication System</a>
      </div>
      <ul class="nav navbar-nav navbar-right">

            <?php
            if(isset($_SESSION['name']))
            {
            echo 'Redirecting you to your profile';

            }
            ?></a></span></li>
      </ul>


</div>
    </nav>
<div class="container">
    <div class="alert alert-info alert-dismissable">
    <span style="font-size:15px">
	PLAS - Password Less Authentication System (Remote Login), is a system that will help you to authenticate to your online profile using your android device, for additional security we use finger print authentication in mobile phone.
</span>
    </div>
    <?php
    if($logerror)
    {
    echo '<div class="alert alert-danger alert-dismissable">'.$logerror.'<a href="#" class="close" data-dismiss="alert" aria-label="close">Close &times;</a></div>';
    }
    ?>
    <div class="panel panel-default">

<div class="panel-body" align="center">
      <ul class="nav nav-tabs" style="font-size:20px" >
        <li class="active" ><a href="#login">Login</a></li>
        <li><a href="#signup">Sign Up</a></li>
      </ul>
      <div class="tab-content">

    <div id="login" class="tab-pane fade in active">
      <span>Enter Your Email Id!</span><br>

    <form action="" method="post" style="font-size:18px">
    Username:<br>
    <input type="text" class="form-control" name="email"><br>
    <br>
    <input class="btn btn-primary" type="submit" value="Submit">
  </form>

    </div>

    <div id="signup" class="tab-pane fade ">
      <span>Enter Your Details To Vote Now !</span>

<form action="scripts/register.php" method="post" style="font-size:18px">
    Name:<br>
    <input type="text" class="form-control" name="name"><br>
    Email:<br>
    <input type="text" class="form-control" name="email"><br>
    Username:<br>
    <input type="text" class="form-control" name="username"><br>

    <button class="btn btn-primary" type="submit" value="signup" >Sign Up</button><br>
    <span style="color:red;font-size:12px">*All fields are required</span>
  </form>
    </div>

  </div>
      </div>
    </div>
</div>


    <div class="container-fluid" style="background-color:#FFB914; padding:12px; 0px">
      <img  src="img/cmnt.png" width="64" height="64">
      <strong>PLAS</strong>
        <span style="background-color:#ffb914"><a style="color:#fff"  href="#">About</a></span>
                <span style="background-color:#ffb914"><a style="color:#fff"  href="#">Privacy Policy</a></span>
        <span style="background-color:#ffb914"><a style="color:#fff"  href="#">FAQ</a></span>
        <span style="background-color:#ffb914"><a style="color:#fff"  href="#">Help?</a></span>
                <span style="background-color:#ffb914"><a style="color:#fff"  href="#">Terms And Conditions</a></span>
        <span style="background-color:#ffb914"><a style="color:#fff"  href="#">Support</a></span>
    </div>
    <div class="panel-footer" align="center">
    &copy; 2019 PLAS-Password Less Authentication System | All Rights Reserved
    </div>

    <script>
    $(document).ready(function(){
      $(".nav-tabs a").click(function(){
        $(this).tab('show');
      });
    });
    </script>
</body>
</html>
