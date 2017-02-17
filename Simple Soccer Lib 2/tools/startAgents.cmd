@echo off
set JOGADORES=2
pushd %cd%
cd ..
cd bin
FOR /L %%G IN (1,1,%JOGADORES%) DO start java krislet.KrisletLauncher -team Yellow
rem FOR /L %%G IN (1,1,%JOGADORES%) DO start java krislet.KrisletLauncher -team Blue
start java xlet.XletLauncher -team Blue
popd