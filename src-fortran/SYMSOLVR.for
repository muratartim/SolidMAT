	PROGRAM SYMMSOL
	IMPLICIT REAL*8(A-H,O-Z)
	COMMON /FILES/ ND1,ND2
	DATA ND1,ND2/1,2/
C
C	READ PARAMETERS
C
	CALL READPARAM (NRM,NCM,NEQNS,NBW)
C
C	START PROCESS
C
	CALL START (NRM,NCM,NEQNS,NBW)
	END PROGRAM SYMMSOL
	SUBROUTINE READPARAM (NRM,NCM,NEQNS,NBW)
C ......................................................................
C .																	 .
C .	P R O G R A M													 .
C .		TO READ VARIABLE PARAMETERS FROM INPUT FILE  				 .
C .																	 .
C ......................................................................
	IMPLICIT REAL*8(A-H,O-Z)
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
      READ (ND2,*) NRM
	READ (ND2,*) NCM
	READ (ND2,*) NEQNS
	READ (ND2,*) NBW
C
	RETURN
C
  100	FORMAT ('*** ERROR : CANNOT OPEN INPUT FILE! ***')
	END
	SUBROUTINE START (NRM,NCM,NEQNS,NBW)
C ......................................................................
C .																	 .
C .	P R O G R A M													 .
C .		TO READ MATRICES AND START SOLVER  							 .
C .																	 .
C ......................................................................
C
	IMPLICIT REAL*8(A-H,O-Z)
	DIMENSION BAND(NEQNS,NBW),RHS(NEQNS)
	COMMON /FILES/ ND1,ND2
C
C	READ MATRICES/VECTORS BAND, RHS
C
	READ (ND2,*)
      DO 10 I=1,NEQNS
	DO 10 J=1,NBW
   10	READ (ND2,*) BAND(I,J)
	READ (ND2,*)
	DO 20 I=1,NEQNS
   20	READ (ND2,*) RHS(I)
	CLOSE (ND2, STATUS='DELETE')
C
C	WRITE STIFFNESS MATRIX
C
	CLOSE (ND1, STATUS='DELETE')
	OPEN (UNIT=ND1,FILE='OUTPUT.txt') 
      WRITE (ND1,120)
	DO 30 I=1,NEQNS
	DO 30 J=1,NBW
   30	WRITE (ND1,*) BAND(I,J)
C
C	START SYMSOLVR
C
	CALL SYMSOLVR(NRM,NCM,NEQNS,NBW,BAND,RHS,0)
C
C	WRITE OUTPUT FILE
C
      WRITE (ND1,100)
	WRITE (ND1,110)
	WRITE (ND1,*) NEQNS
	WRITE (ND1,130)
	DO 40 I=1,NEQNS
   40	WRITE (ND1,*) RHS(I)
	CLOSE (ND1)
C
	RETURN
C
  100	FORMAT ('*** SUCCESSFULL EXIT ***')
  110	FORMAT ('# OF EQUATIONS')
  120	FORMAT ('M A T R I X  -  A')
  130	FORMAT ('D I S P L A C E M E N T S')
	END
      SUBROUTINE SYMSOLVR(NRM,NCM,NEQNS,NBW,BAND,RHS,IRES)
C ......................................................................
C .	The subroutine solves a banded, symmetric, system of algebraic	 .
C .	equations [BAND]{U}={RHS} using Gauss elimination method: The	 .
C .	coefficient matrix is input as BAND(NEQNS,NBW) and the column	 .
C .	vector is input as RHS(NEQNS), where NEQNS is the actual number  .
C .	of equations and NBW is the half band width. The true dimensions .
C .	of the matrix [BAND] in the calling program, are NRM by NCM. When.
C .	IRES is greater than zero, the right hand elimination is skipped..
C ......................................................................
	IMPLICIT REAL*8(A-H,O-Z)
	DIMENSION BAND(NRM,NCM),RHS(NRM)
	MEQNS=NEQNS-1
	IF(IRES.LE.0) THEN
	DO 30 NPIV=1,MEQNS
	NPIVOT=NPIV+1
	LSTSUB=NPIV+NBW-1
	IF(LSTSUB.GT.NEQNS) THEN
	LSTSUB=NEQNS
	ENDIF
	DO 20 NROW=NPIVOT,LSTSUB
	NCOL=NROW-NPIV+1
	FACTOR=BAND(NPIV,NCOL)/BAND(NPIV,1)
	DO 10 NCOL=NROW,LSTSUB
	ICOL=NCOL-NROW+1
	JCOL=NCOL-NPIV+1
   10 BAND(NROW,ICOL)=BAND(NROW,ICOL)-FACTOR*BAND(NPIV,JCOL)
   20 RHS(NROW)=RHS(NROW)-FACTOR*RHS(NPIV)
   30 CONTINUE
	ELSE
   40 DO 60 NPIV=1,MEQNS
	NPIVOT=NPIV+1
	LSTSUB=NPIV+NBW-1
	IF(LSTSUB.GT.NEQNS) THEN
	LSTSUB=NEQNS
	ENDIF
	DO 50 NROW=NPIVOT,LSTSUB
	NCOL=NROW-NPIV+1
	FACTOR=BAND(NPIV,NCOL)/BAND(NPIV,1)
   50 RHS(NROW)=RHS(NROW)-FACTOR*RHS(NPIV)
   60 CONTINUE
	ENDIF
C
C	Back substitution
C
	DO 90 IJK=2,NEQNS
	NPIV=NEQNS-IJK+2
	RHS(NPIV)=RHS(NPIV)/BAND(NPIV,1)
	LSTSUB=NPIV-NBW+1
	IF(LSTSUB.LT.1) THEN
	LSTSUB=1
	ENDIF
	NPIVOT=NPIV-1
	DO 80 JKI=LSTSUB,NPIVOT
	NROW=NPIVOT-JKI+LSTSUB
	NCOL=NPIV-NROW+1
	FACTOR=BAND(NROW,NCOL)
   80 RHS(NROW)=RHS(NROW)-FACTOR*RHS(NPIV)
   90 CONTINUE
	RHS(1)=RHS(1)/BAND(1,1)
	RETURN
	END