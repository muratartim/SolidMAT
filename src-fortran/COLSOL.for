	PROGRAM CSOL
	IMPLICIT DOUBLE PRECISION (A-H,O-Z)
	COMMON /FILES/ ND1,ND2
	DATA ND1,ND2/1,2/
C
C	READ PARAMETERS
C
	CALL READPARAM (NN,NWK,NNM,KKK)
C
C	START PROCESS
C
	CALL START (NN,NWK,NNM,KKK)
	END PROGRAM CSOL
	SUBROUTINE READPARAM (NN,NWK,NNM,KKK)
C ......................................................................
C .																	 .
C .	P R O G R A M													 .
C .		TO READ VARIABLE PARAMETERS FROM INPUT FILE  				 .
C .																	 .
C ......................................................................
	IMPLICIT DOUBLE PRECISION (A-H,O-Z)
	COMMON /FILES/ ND1,ND2
C
C	OPEN OUTPUT AND INPUT FILES
C
	OPEN (UNIT=ND1,FILE='OUTPUT.txt',ACCESS='SEQUENTIAL')
	OPEN (UNIT=ND2,FILE='INPUT.txt',ACCESS='SEQUENTIAL',STATUS='OLD',
	&IOSTAT=L)
	IF (L.NE.0) THEN
	WRITE (ND1,100)
	STOP
	ENDIF
C
C	READ INPUT VARIABLES
C
	READ (ND2,*)
      READ (ND2,*) NN
	READ (ND2,*) NWK
	READ (ND2,*) NNM
	READ (ND2,*) KKK
C
	RETURN
C
  100	FORMAT ('*** ERROR : CANNOT OPEN INPUT FILE! ***')
	END
	SUBROUTINE START (NN,NWK,NNM,KKK)
C ......................................................................
C .																	 .
C .	P R O G R A M													 .
C .		TO READ MATRICES AND START SOLVER  							 .
C .																	 .
C ......................................................................
C
	IMPLICIT DOUBLE PRECISION (A-H,O-Z)
	DIMENSION A(NWK),V(NN),MAXA(NNM)
	COMMON /FILES/ ND1,ND2
C
C	READ MATRICES/VECTORS A, V AND MAXA
C
	READ (ND2,*)
      DO 10 I=1,NWK
   10	READ (ND2,*) A(I)
	READ (ND2,*)
	DO 20 I=1,NN
   20	READ (ND2,*) V(I)
	READ (ND2,*)
      DO 30 I=1,NNM
   30	READ (ND2,*) MAXA(I)
	CLOSE (ND2, STATUS='DELETE')
C
C	START COLSOL
C
	CALL COLSOL (A,V,MAXA,NN,NWK,NNM,KKK,ND1)
C
C	WRITE OUTPUT FILE
C
	CLOSE (ND1, STATUS='DELETE')
	OPEN (UNIT=ND1,FILE='OUTPUT.txt')
      WRITE (ND1,100)
	WRITE (ND1,110)
	WRITE (ND1,*) NN
	WRITE (ND1,120)
	DO 40 I=1,NWK
   40	WRITE (ND1,*) A(I)
	IF (KKK.EQ.2) THEN
	WRITE (ND1,130)
	DO 50 I=1,NN
   50	WRITE (ND1,*) V(I)
	ENDIF
	CLOSE (ND1)
C
	RETURN
C
  100	FORMAT ('*** SUCCESSFULL EXIT ***')
  110	FORMAT ('# OF EQUATIONS')
  120	FORMAT ('M A T R I X  -  A')
  130	FORMAT ('D I S P L A C E M E N T S')
	END
      SUBROUTINE COLSOL (A,V,MAXA,NN,NWK,NNM,KKK,IOUT)
C ......................................................................
C .																	 .
C .	P R O G R A M													 .
C .		TO SOLVE FINITE ELEMENT STATIC EQUILIBRIUM EQUATIONS IN		 .
C .		CORE, USING COMPACTED STORAGE AND COLUMN REDUCTION SCHEME	 .
C .																	 .
C . - - INPUT VARIABLES - -											 .
C .		A(NWK)		= STIFFNESS MATRIX STORED IN COMPACTED FORM		 .
C .		V(NN)		= RIGHT-HAND-SIDE LOAD VECTOR					 .
C .		MAXA(NNM)	= VECTOR CONTAINING ADRESSES OF DIAGONAL		 .
C .					  ELEMENTS OF STIFFNESS MATRIX IN A				 .
C .		NN			= NUMBER OF EQUATIONS							 .
C .		NWK			= NUMBER OF ELEMENTS BELOW SKYLINE OF MATRIX	 .
C .		NNM			= NN + 1										 .
C .		KKK			= INPUT FLAG									 .
C .			EQ.1	  TRIANGULARIZATION OF STIFFNESS MATRIX			 .
C .			EQ.2	  REDUCTION AND BACK-SUBSTITUTION OF LOAD VECTOR .
C .		IOUT		= UNIT NUMBER USED FOR OUTPUT					 .
C .																	 .
C . - - OUTPUT - -													 .
C .		A(NWK)		= D AND L - FACTORS OF STIFFNESS MATRIX			 .
C .		V(NN)		= DISPLACEMENT VECTOR							 .
C .																	 .
C ......................................................................
	IMPLICIT DOUBLE PRECISION (A-H,O-Z)
C ......................................................................
C .	THIS PROGRAM IS USED IN SINGLE PRECISION ARITHMETIC ON CRAY		 .
C .	EQUIPMENT AND DOUBLE PECISION ARITHMETIC ON IBM MACHINES,		 .
C .	ENGINEERING WORKSTATIONS AND PCS. DEACTIVATE ABOVE LINE FOR		 .
C .	SINGLE PRECISION ARITHMETIC.									 .
C ......................................................................
	DIMENSION A(NWK),V(NN),MAXA(NNM)
C
C	PERFORM L*D*L(T) FACTORIZATION OF STIFFNESS MATRIX
C
	IF (KKK-2) 40,150,150
   40 DO 140 N=1,NN
	KN=MAXA(N)
	KL=KN + 1
	KU=MAXA(N+1) - 1
	KH=KU - KL
	IF (KH) 110,90,50
   50 K=N - KH
	IC=0
	KLT=KU
	DO 80 J=1,KH
	IC=IC + 1
	KLT=KLT - 1
	KI=MAXA(K)
	ND=MAXA(K+1) - KI - 1
	IF (ND) 80,80,60
   60 KK=MIN0(IC,ND)
	C=0.
	DO 70 L=1,KK
   70 C=C + A(KI+L)*A(KLT+L)
	A(KLT)=A(KLT) - C
   80 K=K + 1
   90 K=N
      B=0.
	DO 100 KK=KL,KU
	K=K - 1
	KI=MAXA(K)
	C=A(KK)/A(KI)
	B=B + C*A(KK)
  100 A(KK)=C
	A(KN)=A(KN) - B
  110 IF (A(KN)) 120,120,140
  120 WRITE (IOUT,2000) N,A(KN)
	GOTO 800
  140 CONTINUE
	GOTO 900
C
C	REDUCE RIGHT-HAND-SIDE LOAD VECTOR
C
  150 DO 180 N=1,NN
	KL=MAXA(N) + 1
	KU=MAXA(N+1) - 1
	IF (KU-KL) 180,160,160
  160 K=N
	C=0.
	DO 170 KK=KL,KU
	K=K - 1
  170 C=C + A(KK)*V(K)
	V(N)=V(N) - C
  180 CONTINUE
C
C	BACK-SUBSTITUTE
C
	DO 200 N=1,NN
	K=MAXA(N)
  200 V(N)=V(N)/A(K)
	IF (NN.EQ.1) GOTO 900
	N=NN
	DO 230 L=2,NN
	KL=MAXA(N) + 1
	KU=MAXA(N+1) - 1
	IF (KU-KL) 230,210,210
  210 K=N
	DO 220 KK=KL,KU
	K=K - 1
  220 V(K)=V(K) - A(KK)*V(N)
  230 N=N - 1
	GOTO 900
C
  800 STOP
  900	RETURN
C
 2000 FORMAT (//' STOP - STIFFNESS MATRIX NOT POSITIVE DEFINITE',//,
	&		  ' NONPOSITIVE PIVOT FOR EQUATION ',I8,//,
	&		  ' PIVOT = ',E20.12)
	END