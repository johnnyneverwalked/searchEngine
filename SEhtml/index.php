<!DOCTYPE html>
<html lang ="en">

	<head>
		<meta charset="utf-8">
		<title>Search Engine</title>
		<meta name="Search Engine" content="search a local database">
		<link rel="stylesheet" type="text/css" href="central.css">
	</head>
	
	<body>
		<main>
			
			<div id="upperdiv">
				<p class="fright italic">By John Stamtsidis</p>
				
				<form id="fileupload" action="php/upload.php" method="post" enctype="multipart/form-data">
					<fieldset>
					<legend>Upload txt files:</legend>
						<input type="file" multiple="multiple" name="fileToUpload[]" id="fileToUpload">
						<input type="submit" name="upload" value="Upload">
					</fieldset>
				</form>
				
				<form id="crawlform" action="php/crawl.php" method="post">
					<fieldset>
					<legend>Use the Crawler:</legend>
						<input type="url" required id="url" name="url" placeholder="Enter starting url">
						<label for="maxCrawl">Stop after:</label>
						<input id="maxCrawl" type="number" name="maxCrawl" min="1" value="1">
						<Button type="submit">Crawl</Button>
					</fieldset>
				</form>
			</div>
			
			<h1><img src="gooble.png" alt="gooble logo"></h1>
			
			<div>
				<form method="post" action="php/showResults.php">
						
					<input id="searchbar" type="text" name="query" required placeholder="Enter a query">
					<p>
						<label for="results">Results:</label>
						<input id="results" type="number" name="results" min="1" value="1">
						<input id="VSM" checked="checked" type="radio" name="model" value="VSM">
						<label for="VSM">Vector Space</label>
						<input id="BM" type="radio" name="model" value="BM">
						<label for="BM">Boolean</label>
					</p>
					<p>
						<button type="submit">Search</button>
						<button type="reset">Reset</button>
					</p>
				</form>
			</div>
			
		</main>
	</body>