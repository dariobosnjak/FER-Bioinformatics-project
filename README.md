# FER-Bioinformatics-project
https://www.fer.unizg.hr/predmet/bio


## Scala implementation
### Compile
'scalac -d bin -cp src/main/scala/*'
### Run
'scala -classpath bin/ LCSkPlusPlus path k outputDirectoryPath'

The first argument can be a single FASTA file or a folder with one or multiple FASTA files.
The last argument is an output directory path. It is created if it does not exist.
