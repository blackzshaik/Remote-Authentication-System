<?php
//Add mail function

if(isset($_GET['token']))
{
  $token = $_GET['token'];
  echo "<div>This is your token please use in the time of register through your device</div></br>";
  echo '<div><b>'.$token.'</b></div>';
  echo "</br><h4>Important:</h4>Do Not Share This Code With Anyone";

}else{
  echo "Are you lost?, I think you're not supposed to be here";
}
 ?>
