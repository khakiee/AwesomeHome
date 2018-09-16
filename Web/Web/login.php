<!DOCTYPE html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>MySql-PHP 연결 테스트</title>
</head>
<body>
 
<?php
 
$id=$_GET['id'];
$password=$_GET['password'];

$db = mysqli_connect("localhost", "root", "wlsdn501", "home");
 
if($db){
}
else{
    echo "disconnect : 실패<br>";
}

$query = "SELECT * from user where id='".$id."' and password='".$password."'"; 
$result = mysqli_query($db, $query);
$data = mysqli_fetch_array($result);
if($data['id']){
	echo 'Hi '.$data['id'];
	echo "<script>location.href='home.html'</script>";
}
else{
	echo "<script>alert('no user');</script>";
	echo "<script>location.href='login.html'</script>";
}	
?>
 
</body>
</html>


