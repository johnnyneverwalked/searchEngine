<?php
	
	include 'address.php';
	
	$target_dir = "uploads/";	
	
	if(count($_FILES['fileToUpload']['name']) > 0){
		$successcount=0;
		for ($i=0; $i<count($_FILES['fileToUpload']['name']); $i++) {
			
			$target_file = $target_dir.basename($_FILES["fileToUpload"]["name"][$i]);
			$uploadOk = 1;
			$fileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
			
			
			if(file_exists($target_file)){
				echo "A file with that name already exists in the directory ";
				$uploadOk = 0;
			}
			
			if($fileType != "txt"){
				echo "Please upload txt files only ";
				$uploadOk = 0;
			}
			
			if($uploadOk == 0){
				echo "-The file ". basename( $_FILES["fileToUpload"]["name"][$i]). " was not uploaded <br>";
			}else{
				if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"][$i], $target_file)) {
					echo "-The file ". basename( $_FILES["fileToUpload"]["name"][$i]). " has been uploaded. <br>";
					$successcount++;
				} else {
					echo "-Sorry, there was an error uploading ".basename( $_FILES["fileToUpload"]["name"][$i])."<br>" ;
				}
			}
			if($successcount>0){
				exec("java -jar dist/SearchEngine.jar index uploads");
			}
		}
	}else{
		echo "No files were given for upload <br>";
	}
	echo "<br> <a href='".$address."index.php'>Back</a>";

?>