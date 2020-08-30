<?php include 'address.php';?>
<!DOCTYPE html>
<html lang ="en">

	<head>
		<meta charset="utf-8">
		<title>Search Engine</title>
		<meta name="Search Engine" content="search a local database">
		<link rel="stylesheet" type="text/css" <?php echo"href='".$address."central.css'"?>>
	</head>
	
	<body>
		<main>
			
			<h2><a <?php echo"href='".$address."index.php'"?> title="Go to Gooble Home" ><img src="gooble-small.png" alt="gooble logo"></a></h2>
			
			<?php include 'search.php';?>


		</main>
	</body>