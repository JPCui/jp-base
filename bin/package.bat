@echo off
rem /**
rem  * Copyright &copy; 2016-2017 https://github.com/jpcui All rights reserved.
rem  *
rem  * Author: Cui
rem  */
title %cd%
echo.
echo [��Ϣ] ���  - package
echo.

rem pause
rem echo.

cd %~dp0
cd..

call mvn clean compile package deploy -Dmaven.test.skip=true

cd bin
pause