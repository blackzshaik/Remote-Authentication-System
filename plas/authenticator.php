<?php
include 'dbConnect.php';


if($_SERVER["REQUEST_METHOD"]== "POST")
{

$username = mysqli_real_escape_string($connect,$_POST['username']);
$ltime = mysqli_real_escape_string($connect,$_POST['ltime']);
$deviceid = mysqli_real_escape_string($connect,$_POST['deviceid']);

$sql = "UPDATE loginrecords SET auth=1 WHERE username='".$username."' AND ltime='".$ltime."' ";
$res = mysqli_query($connect,$sql);
if($res)
{
  $res  = array();
  $result["status"] = "Sucessfull";

echo json_encode($result);

}else{
  echo json_encode("Update Failed");
}
}
 ?>
