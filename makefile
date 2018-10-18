###########################################################################
#
# Makefile for make(BCB)
#
###########################################################################

###########################################################################
FILENAME=GLSample
CLASSNAME=GLSample
PROJECT_NAME=egl
APP_NAME="EGL 3D Sample"

APPS_DIR=d:\ezplus\apps
PROJECT_DIR=$(APPS_DIR)\$(PROJECT_NAME)
RETRO_CLASSNAME=GLSample
APP_VERSION=1.0


APP_PROFILE=MIDP-1.0
APP_CONFIGURATION=CLDC-1.0
APP_DATASIZE=512
APP_COPYRIGHT=ON

###########################################################################


RESDIR=.\res
SRCDIR=.\cv_src
# CHINESE = ON
# N60 = OFF
BANK=.\bin\bank





!if $d(N40II)
	KIND=N40II
	PHONE_TYPE=_N40II
	BINDIR=.\bin\N40II
	SNDDIR=.\res\midi40
	#SNDDIR=.\res\tone
	PNGDIR=.\res\png120
	ICONDIR = .\res\I16
	STUBPATH=$(J2ME_HOME)\lib\classes.zip
	
	COLOR_BUFFER_TYPE = 1
	# CHINESE = OFF
	# EMU=\home\wingser\programs\Nokia\Devices\Nokia_6230_MIDP_Concept_SDK_Beta_0_2\bin\emulator
!endif

!if $d(N60C)
	KIND=N60C
	PHONE_TYPE=_N60C
	BINDIR=.\bin\N60C
	SNDDIR=.\res\midi60
	PNGDIR=.\res\png176
	ICONDIR = .\res\I28
	STUBPATH=$(J2ME_HOME)\lib\classes.zip
	
	COLOR_BUFFER_TYPE = 0
	
	# N60 = ON
	
	# EMU=\home\wingser\programs\Nokia\Devices\Series_60_MIDP_Concept_SDK_Beta_0_3_1_Nokia_edition\bin\emulator
!endif


!if $d(GM)
	KIND=GM
	#CHINESE = OFF
	PHONE_TYPE=_GM
	BINDIR=.\bin\GM
	SNDDIR=.\res\midimoto
	PNGDIR=.\res\png176
	ICONDIR = .\res\I15
	STUBPATH=$(WTK_HOME)\lib\midpapi.zip	
	
	STUBPATH=$(WTK_HOME)\lib\midpapi20.jar;$(WTK_HOME)\lib\cldcapi10.jar
	
	COLOR_BUFFER_TYPE = 0
	
	# EMU=\home\wingser\programs\sonyericsson\wtk22\bin\emulator
	# DEVICE= -Xdevice:SonyEricsson_K700
!endif


!if $d(GL)
	KIND=GL
	#CHINESE = OFF
	PHONE_TYPE=_GL
	BINDIR=.\bin\GL
	SNDDIR=.\res\midi_common
	PNGDIR=.\res\png240
	ICONDIR = .\res\I24
	STUBPATH=$(WTK_HOME)\lib\midpapi20.jar;$(WTK_HOME)\lib\cldcapi10.jar
	
	COLOR_BUFFER_TYPE = 0
	
	# EMU=\home\wingser\programs\wtk22\bin\emulator
	# DEVICE= -Xdevice:DefaultColorPhone
!endif



!if $d(GS)
	KIND=GS
	#CHINESE = OFF
	PHONE_TYPE=_GS
	BINDIR=.\bin\GS
	SNDDIR=.\res\midi_common
	PNGDIR=.\res\png120
	ICONDIR = .\res\I16
	STUBPATH=$(WTK_HOME)\lib\midpapi20.jar;$(WTK_HOME)\lib\cldcapi10.jar
	
	COLOR_BUFFER_TYPE = 0
	
	# EMU=\home\wingser\programs\sonyericsson\wtk22\bin\emulator
	# DEVICE= -Xdevice:SonyEricsson_K500
!endif

!if $d(MS)
	KIND=MS
	PHONE_TYPE=_MS
	BINDIR=.\bin\MS
	SNDDIR=.\res\midi_common
	PNGDIR=.\res\png120
	ICONDIR = .\res\I15
	STUBPATH=$(WTK_HOME)\lib\midpapi20.jar;$(WTK_HOME)\lib\cldcapi10.jar
	
	COLOR_BUFFER_TYPE = 0
	
	# EMU=\home\wingser\programs\sonyericsson\wtk22\bin\emulator
	# DEVICE= -Xdevice:SonyEricsson_K300
!endif



APP_VENDER="SYJAY"
#APP_NAME="DDR"

# !if "$(CHINESE)" == "OFF"
	MFDIR=.
	GENJAD = genjad.pl		
# !else	
	# MFDIR=.\emf
	# GENJAD=genjads.pl
# !endif



###########################################################################
J2ME_HOME=d:\J2me
JAVA_HOME=d:\jdk1.4.2
EZPLUS_HOME=d:\ezplus
WTK_HOME=d:\WTK22
EMULATOR_EXE=$(EZPLUS_HOME)\Emu3\EMUJAVA.EXE
###########################################################################
JAVAC=$(JAVA_HOME)\bin\javac.exe
JAVA=$(JAVA_HOME)\bin\java.exe
JAR=$(JAVA_HOME)\bin\jar.exe
PREVER=$(J2ME_HOME)\bin\preverify.exe
ZIP=$(J2ME_HOME)\bin\zip.exe
# RETROGUARD=$(JAVA) -classpath $(EZPLUS_HOME)\retroguard-v1.1\retroguard.jar;$(STUBPATH) RetroGuard 
PERL=d:\Perl\bin\JPerl.exe
CPP=$(J2ME_HOME)\bin\cpp32.exe

JARFILE=$(BINDIR)\$(FILENAME)$(PHONE_TYPE).jar
JARFILE_BANK=$(BANK)\$(FILENAME)$(PHONE_TYPE).jar

JADFILE=$(BINDIR)\$(FILENAME)$(PHONE_TYPE).jad
JADFILE_BANK=$(BANK)\$(FILENAME)$(PHONE_TYPE).jad

MFFILE=$(MFDIR)\$(FILENAME).mf
# KJXFILE=$(BINDIR)\$(FILENAME)$(PHONE_TYPE).kjx
#KJXFILE=$(BINDIR)\$(CLASSNAME).kjx
#KJXFILECRC=$(BINDIR)\$(FILENAME)$(PHONE_TYPE).kjx
# HDMLFILE=$(BINDIR)\dnld.hdml
###########################################################################
TMPCLASSESDIR=.\tmpclasses
CLASSESDIR=.\classes
###########################################################################
JAVACFLAGS= -g:none -d $(TMPCLASSESDIR) -bootclasspath $(STUBPATH) \
	-classpath $(TMPCLASSESDIR);$(CLASSESDIR);$(SRCDIR)


create:	convert compile retroguard preverify pack genjad

convert:
	@echo Convert ...... $(CLASSNAME).java
	@$(CPP)  -D$(KIND)=1 -o$(SRCDIR)\$(CLASSNAME).java $(PROJECT_DIR)\src\$(CLASSNAME).java -P

	@echo proprocessing GL library...
	@$(CPP)  -DCOLOR_BUFFER_TYPE=$(COLOR_BUFFER_TYPE) -o$(SRCDIR)\com\syjay\egl\GL.java $(PROJECT_DIR)\src\GL\GL.java -P 
	@$(CPP)  -DCOLOR_BUFFER_TYPE=$(COLOR_BUFFER_TYPE) -o$(SRCDIR)\com\syjay\egl\GLU.java $(PROJECT_DIR)\src\GL\GLU.java  -P 
	@$(CPP)  -DCOLOR_BUFFER_TYPE=$(COLOR_BUFFER_TYPE) -o$(SRCDIR)\com\syjay\egl\MathFP.java $(PROJECT_DIR)\src\GL\MathFP.java  -P 
	@$(CPP)  -DCOLOR_BUFFER_TYPE=$(COLOR_BUFFER_TYPE) -o$(SRCDIR)\com\syjay\egl\Renderer.java $(PROJECT_DIR)\src\GL\Renderer.java  -P
compile:
	@echo Compile ...... $(CLASSNAME).java
	@rmdir /s /q  $(CLASSESDIR)
	@rmdir /s /q  $(TMPCLASSESDIR)
	@mkdir $(TMPCLASSESDIR)
	@mkdir $(CLASSESDIR)
	@echo $(JAVAC) $(JAVACFLAGS) $(SRCDIR)\$(CLASSNAME).java
	@$(JAVAC) $(JAVACFLAGS) $(SRCDIR)\$(CLASSNAME).java
	@copy $(TMPCLASSESDIR)\com\syjay\egl\*.* .\compiled\com\syjay\egl
retroguard:
	@echo proguard ... $(CLASSNAME).class
	@echo $(PREVER) -d $(TMPCLASSESDIR) -classpath $(STUBPATH) $(TMPCLASSESDIR)
	@$(PREVER) -d $(TMPCLASSESDIR) -classpath $(STUBPATH) $(TMPCLASSESDIR)
	
	
	@echo $(ZIP) -ujJ9q $(TMPCLASSESDIR)\tmp.jar $(TMPCLASSESDIR)\*.*
	
	@cd $(TMPCLASSESDIR)
	@$(ZIP) -uJ9qr .\tmp.jar .\*
	@cd ..
	
	
	
	@$(JAVA) -jar d:\ezplus\proguard\proguard38.jar -libraryjars $(STUBPATH) -injars $(TMPCLASSESDIR)\tmp.jar -outjar  $(TMPCLASSESDIR)\tmp2.jar -keep public class $(RETRO_CLASSNAME) -printseeds -printusage -printmapping -verbose -dump -defaultpackage '' > proguard.log
	
	
	@$(JAR) -xf $(TMPCLASSESDIR)\tmp2.jar
	@del $(TMPCLASSESDIR)\*.class
	@del $(TMPCLASSESDIR)\*.jar
	@move *.class $(TMPCLASSESDIR)
	@rmdir /q /s $(TMPCLASSESDIR)\com
	#@rmdir /s /q  META-INF


preverify:
	@echo Byte check ... $(FILENAME).class
	@$(PREVER) -d $(CLASSESDIR) -classpath $(STUBPATH) $(TMPCLASSESDIR)

pack:
	@echo Packing ...... $(JARFILE)

	@$(JAR) cmf $(MFFILE) $(JARFILE)
	@$(JAR) cmf $(MFFILE) $(JARFILE_BANK)
	@del $(RESDIR)\*.* /q
	@copy  $(PNGDIR)\*.* $(RESDIR)
	@copy  $(SNDDIR)\*.* $(RESDIR)
	@copy  $(ICONDIR)\*.* $(RESDIR)
	@del $(RESDIR)\Thumbs.db
	
	@$(ZIP) -ujJ9q $(JARFILE)  $(RESDIR)\*.*
	@cd $(CLASSESDIR)
	@$(ZIP) -uJ9qr ..\$(JARFILE)  .\*
	@cd ..
	#@$(ZIP) -ujJ9q $(JARFILE_BANK) $(CLASSESDIR)\*.* $(RESDIR)\*.*
	#@rmdir /s /q  $(CLASSESDIR)
	#@rmdir /s /q  $(TMPCLASSESDIR)
	@echo pack ok
genjad:
	@echo Generate jad file ...... $(JADFILE)
	@$(PERL) $(GENJAD) $(JARFILE) $(FILENAME)$(PHONE_TYPE).jar $(APP_NAME) $(APP_VERSION) $(APP_VENDER) $(APP_VENDER2) $(RETRO_CLASSNAME) $(APP_PROFILE) $(APP_CONFIGURATION) $(APP_DATASIZE) $(APP_COPYRIGHT) 	> $(JADFILE)
	@copy $(JADFILE) $(JADFILE_BANK)       
	@copy $(JARFILE) $(JARFILE_BANK)
emu:
	@$(EMU) -Xdescriptor:$(JADFILE) $(DEVICE)

document:
	@javadoc -locale en_US -public .\src\com\syjay\egl\*.java -d doc\
