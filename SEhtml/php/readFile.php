<?php
	include 'address.php';	


	echo file_get_contents($_GET['filename']).' <br>';
	
	echo "<br> <a href='".$address."index.php'>Back</a>";

?>