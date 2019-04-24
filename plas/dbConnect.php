<?php
$connect  = mysqli_connect("localhost","root","","plas");

if(mysqli_connect_errno())
{
  echo "Failed To Connect To Database".mysqli_connect_errno;
}
?>
