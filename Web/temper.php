<?php
	$temper = $_POST['temper'];
	$f = fopen("./buffer/temper","w");
	fwrite($f,$temper);
	fclose($f);
	echo $temper;

?>
