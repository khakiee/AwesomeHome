<?php
	$out = fopen('input','r');
        $text = fread($out,100);
        fclose($out);
	$file = fopen("input","w+");
	$b1=$_GET['b1'];
	$b2=$_GET['b2'];
	$b3=$_GET['b3'];
	$b4=$_GET['b4'];
	if($b4==NULL)
		$b4=$text{3};
	if($b3==NULL)
		$b3=$text{2};
	if($b2==NULL)
		$b2=$text{1};
	if($b1==NULL)
		$b1=$text{0};

	$item = $b1.$b2.$b3.$b4;
	fwrite($file,($item));
	fclose($file);
	echo $item;
?>
