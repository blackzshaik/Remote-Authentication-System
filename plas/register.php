<?php

include '../dbConnect.php';

$res["r"] = array();

if($_SERVER["REQUEST_METHOD"]== "POST")
{
$ttime = date('H:i:s');
$name =  mysqli_real_escape_string($connect,$_POST['name']);
$email= mysqli_real_escape_string($connect,$_POST['email']);
$username = mysqli_real_escape_string($connect,$_POST['username']);
  //to check whether register through website or mobile
if(isset($_POST['skey'])){
// register through mobile
$skey = mysqli_real_escape_string($connect,$_POST['skey']);
$deviceid = mysqli_real_escape_string($connect,$_POST['deviceid']);
}else{
  //register through web portal
$skey = mysqli_real_escape_string($connect,hash('sha256',$username." ".$email." ".$ttime,0));
$deviceid = mysqli_real_escape_string($connect,"Device Not Verified");

}

$sql = "INSERT INTO registration (name,email,username,skey,deviceid) VALUES ('".$name."',
                                                                    '".$email."',
                                                                    '".$username."',
                                                                    '".$skey."',
                                                                    '".$deviceid."')";

if($connect->query($sql) == TRUE)
{
  $result = array();

  $result["reason"] = "Successful";
  $result["message"] = "Registration Sucessfull, Now you can use your device to login next time";

  array_push($res["r"],$result);
   if($deviceid == "Device Not Verified")
   {
     header ("Location:loginWithMobile.php?token=".$skey);
   }else{
     echo json_encode($res);

   }

}else{
  $result = array();

  $result["reason"] = "Failed";
  $result["message"] = " ".$connect->error;
  //echo " ".$sql."<br>".$connect->error;
  array_push($res["r"],$result);
  echo json_encode($res);
}
}




?>
