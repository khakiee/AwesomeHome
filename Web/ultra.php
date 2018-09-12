<?php
$f = fopen('buffer/door','w');
$in = fopen('buffer/input','rw');
$de = fopen('buffer/default','rw');

$temp_in = fgets($in);
$temp_de = fgets($de);

fwrite($in,$temp_de);
fwrite($de,$temp_in);	

fwrite($f,$_GET['num']);

fclose($in);
fclose($de);
fclose($f);

echo $_GET['num'];
?>
