<?php
//registered through webportal and later verify device
include '../dbConnect.php';
$res["r"] = array();

if($_SERVER["REQUEST_METHOD"]== "POST")
{
$username = mysqli_real_escape_string($connect,$_POST['username']);
$email = mysqli_real_escape_string($connect,$_POST['email']);
$skey = mysqli_real_escape_string($connect,$_POST['skey']);
$deviceid = mysqli_real_escape_string($connect,$_POST['deviceid']);

$newSkey = mysqli_real_escape_string($connect,hash('sha256',$username." ".$email,0));

$sql = "SELECT * FROM registration WHERE username='$username' AND skey = '$skey' AND sessionstatus = 0";
$result =mysqli_query($connect,$sql);
$row = mysqli_num_rows($result);
#$row = 1;
#echo "  ".$result;
#echo " ".$sql;
if($row == 1)
{
  $sql = "UPDATE registration SET deviceid='".$deviceid."', skey = '".$newSkey."',sessionstatus = 1
          WHERE username='".$username."' AND skey='".$skey."'";
  if(mysqli_query($connect,$sql))
  {
    $result = array();

    $result["reason"] = "Successful";
    $result["message"] = "Registration Sucessfull, Now you can use your device to login next time";
      array_push($res["r"],$result);
    echo json_encode($res);
  }else{
    $result["reason"] = "Failed";
    $result["message"] = "Please contact admin";
    array_push($res["r"],$result);
    echo json_encode($res);
  }

}


}
$result["reason"] = "Working";
$result["message"] = "Testing temp remove after this";
  array_push($res["r"],$result);
echo json_encode($res);
?>
