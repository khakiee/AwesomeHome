<?php
	echo "it is index2.php page";
	$file = fopen("output","w+");
	$b1=$_GET['b1'];
	$b2=$_GET['b2'];
	$b3=$_GET['b3'];
	$b4=$_GET['b4'];
	$item = $b1.$b2.$b3.$b4;
	fwrite($file,($item));
	fclose($file);
	echo $item;
?>
