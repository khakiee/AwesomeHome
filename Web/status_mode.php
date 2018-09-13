<?php
//this file change mode in home.html after read on door_file
	$door = fopen('./buffer/door','r');
	$value = fread($door,10);
	echo $value;

?>
