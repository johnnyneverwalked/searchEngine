<?php
	include 'address.php';
	
	echo "Crawling... <br>";
	
	$url = escapeshellarg($_POST['url']);
	$max = $_POST['maxCrawl'];
	
	exec("java -jar dist/SearchEngine.jar crawl $url $max ",$output);
	
	foreach ($output as $line)
		echo $line."<br>";
	
	
		
	echo "<br> <a href='".$address."index.php'>Back</a>";

?>