<html>
<body>
	hello
<?php
	$servername = "localhost";
	$username = "root";
	$password = "wlsdn501";

	$conn = new mysqli($servername, $username, $password);
	
	$id=$_GET['userid'];
        $ps=$_GET['userpw'];

	$query = "select id,password from user where id= ".$id." and password=".$ps;
	
	
	$result = mysqli_query($query);
	
	if(!$result){
		$message  = 'Invalid query: ' . mysqli_error() . "\n";
   	 	$message .= 'Whole query: ' . $query;
    		die($message);
		echo ("fail");
	}
	while ($row = mysql_fetch_assoc($result)) {
		echo $row['id'];
	   	echo $row['password'];
	}

	if ($conn->connect_error) {
    		die("Connection failed: " . $conn->connect_error);
	}	 
	
	echo "<script>alert('".$id."');</script>";
	echo ($ps);
	mysqli_close($conn); 
?>
</body>
</html>
