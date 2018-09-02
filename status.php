<?php
	$file = fopen('output','r');
	$text = fread($file,100);
	fclose($file);
	echo $text;
?>
