<?php
	$file = fopen('buffer/default','r');
	$text = fread($file,100);
	fclose($file);
	echo $text;
?>
