## Scala implementation
### Compile
'scalac -d bin -cp src/main/scala/*'
### Run
'scala -classpath bin/ LCSkPlusPlus path k outputDirectoryPath'

The first argument can be a single FASTA file or a folder with one or multiple FASTA files.
The last argument is an output directory path. It is created if it does not exist.

For long sequences you can get 'java.lang.OutOfMemoryError: GC overhead limit exceeded'. Too small heap size causes JVM to call garbage collector frequently, which consumes most of CPU time and slows program. In that case '-J-Xmx' JVM parameter can be used (for this implementation and our data 3 GB is sufficient).