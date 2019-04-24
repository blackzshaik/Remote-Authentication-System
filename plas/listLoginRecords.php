<?php
include 'dbConnect.php';


if($_SERVER["REQUEST_METHOD"]== "POST"){
$logRecord =array();
$username = mysqli_real_escape_string($connect,$_POST['username']);
$deviceid = mysqli_real_escape_string($connect,$_POST['deviceid']);

$sql = "SELECT * FROM loginrecords WHERE username ='$username' AND deviceid ='$deviceid'"  ;
$result =mysqli_query($connect,$sql);
$row = mysqli_num_rows($result);

if($row > 0)
{

  $logRecord["r"] = array();

  while ($data = mysqli_fetch_assoc($result)) {
    $record = array();

    $record["username"] =  mysqli_real_escape_string($connect,$data["username"]);
    $record["email"] = mysqli_real_escape_string($connect,$data["email"]);
    $record["auth"] = mysqli_real_escape_string($connect,$data["auth"]);
    $record["time"] = mysqli_real_escape_string($connect,$data["ltime"]);

    array_push($logRecord["r"],$record);
  }
}else{
                    echo " ".$sql." ".$connect->error;
  }
  echo json_encode($logRecord);
}


 ?>
