# Scala implementation
## Installation
1. Create a bin directory
`mkdir bin`
2. Compile
`scalac -d bin -cp src/main/scala/*.scala`
3. Run
`scala -classpath bin/ LCSkPlusPlus <path> <k> <outputDirectoryPath>`
    * Arguments:
        * `<path>` can be a single FASTA file or a folder with one or multiple FASTA files
        * `<k>` is a natural number
        * `<outputDirectoryPath>` is an output directory path. It is created if it does not exist

NB - for long sequences you can get `java.lang.OutOfMemoryError: GC overhead limit exceeded` error. Too small heap size causes JVM to call garbage collector frequently, which consumes most of the CPU time and slows the program. In that case `-J-Xmx` JVM parameter can be used (for this implementation and our data 3 GB is sufficient).

## Test
1. Open terminal in the project root directory
2. Rum test example: `scala -classpath bin/ LCSkPlusPlus data/demo/sequence.fasta 10 data/demo/`
3. View results in `data/demo`