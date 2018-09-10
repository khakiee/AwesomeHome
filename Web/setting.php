<?php
$file = fopen('./buffer/default','w');
$b1 = $_GET['b1'];
$b2 = $_GET['b2'];
$b3 = $_GET['b3'];
$b4 = $_GET['b4'];

$item=$b1.$b2.$b3.$b4;

echo $item;
fwrite($file,$item);
fclose($file);
?>
