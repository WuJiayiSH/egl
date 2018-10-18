$packageURL = $ARGV[1];
$packageURL =~ s/^.*\/(.*jar)$/$1/g;

($dev,$ino,$mode,$nlink,$uid,$gid,$rdev,$size,$atime,$mtime,$ctime,$blksize,$blocks) = stat($ARGV[0]);
($sec,$min,$hour,$mday,$mon,$year,$wday,$isdat)=localtime($ctime);

print "MIDlet-Name: EGL 3D Sample\n";
print "MIDlet-Version: 1.0\n";
print "MIDlet-Vendor: SYJAY\n";
print "MIDlet-Jar-URL: $ARGV[1]\n";
print "MIDlet-Jar-Size: $size\n";
print "MIDlet-1: EGL 3D Sample,icon.png,GLSample\n";
print "MicroEdition-Profile: MIDP-1.0\n";
print "MicroEdition-Configuration: CLDC-1.0\n";
print "MIDlet-Data-Size: 512\n";
print "MIDlet-Description: EGL 3D Sample\n";
print "Media-price: 0\n";
