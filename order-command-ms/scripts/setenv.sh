#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
export msname="ordercommandms"
export chart=$(ls ./chart/| grep $msname)
export kname="kc-ordercmd"
export ns="browncompute"