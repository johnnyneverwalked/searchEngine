<?php

	$p='1thisisaphrasequery1';
	
	
	$query = str_replace('"',$p,$_POST['query']);
	$query = escapeshellarg($query);
	
	if(strcmp($_POST['model'],"BM")===0)
		$model="true";
	else
		$model="false";
	
	$results = $_POST['results'];
	
	exec("java -jar dist/SearchEngine.jar search $query $model $results",$output);
	$i=0;
	echo "<p class='bold italic'>---Query for $results documents ~ ".$output[count($output)-1]."---</p><br>";
	echo "<ol>";
	while($i<count($output)-1){
		echo "<li><p class='bold'>".$output[$i]."</p><br>";
		if(strncmp('uploads',$output[$i+1],7)===0){
			echo "<a href='readFile.php?filename=".$output[$i+1]."'>".$output[$i+1]."</a>";
		}
		else{
			echo "<a href='".$output[$i+1]."'>".$output[$i+1]."</a>";
		}
		echo "</li><br>";
		$i+=2;
	}
	echo "</ol>";
?>