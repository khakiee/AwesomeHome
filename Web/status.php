<?php
	$file = fopen('buffer/input','r');
	$text = fread($file,100);
	fclose($file);
	echo $text;
?>
