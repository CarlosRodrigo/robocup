@echo off
cd tools
title Servidor e Monitor da ROBOCUP 2D - Inicializacao
echo.
echo Este script inicia o servidor e o monitor (visualizador grafico) da RoboCup.
echo.
echo INICIANDO O SIMULADOR...
echo.
cd rcssserver-14.0.3-win
start rcssserver
cd ..

echo Simulador iniciado em nova janela!

rem timeout /t 1 /nobreak

:sw2
echo Iniciando o monitor "Soccer Window 2"
cd soccerwindow2-5.0.0-win
start SoccerWindow2
exit
