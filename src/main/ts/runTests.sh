#/bin/sh

gulp build-js

for f in $(find ../../../data -type d \( -path ../../../data/results \) -prune -o -name '*.txt' -print)
do
    for k in 10 20 30
    do
        node src/main.js $f $k
    done
done