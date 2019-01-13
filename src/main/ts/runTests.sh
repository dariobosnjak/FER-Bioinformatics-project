#/bin/sh

gulp build-js

for f in $(find ../../../data -type d \( -path ../../../data/results \) -prune -o -name '*.txt' -print)
do
    node src/main.js $f
done