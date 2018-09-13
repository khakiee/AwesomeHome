<?php
$f = fopen('buffer/door','r');
$door_read = fread($f,10);

if($_GET['num']==0 && $door_read[0]==1){
	$in = fopen('buffer/input','r');
	$temp = fopen('buffer/temp_default','w');
	$de = fopen('buffer/default','r');

	$temp_de = fread($de,10);
	$temp_write = fread($in,10);

	fclose($in);
	
	//write in_file
	$in = fopen('buffer/input','w');
	fwrite($in,$temp_de);
	fclose($in);
	
	fwrite($temp,$temp_write);
	
	fclose($in);
	fclose($temp);
	fclose($de);
	fclose($in_read);
}
else if($_GET['num']==1 && $door_read[0]==0){
	$temp = fopen('buffer/temp_default','r');
	$in = fopen('buffer/input','w');

	$temp_temp = fread($temp,10);
	fwrite($in,$temp_temp);
	
	fclose($in);
	fclose($temp);
}

fclose($f);

$f = fopen('buffer/door','w');
fwrite($f,$_GET['num']);

fclose($f);

echo $_GET['num'];
echo 'ok';
?>
