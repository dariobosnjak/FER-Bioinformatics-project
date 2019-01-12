# Scala implementation
## Installation
### Prerequisites
1. Install Java 8:
    1. Add the PPA repository: `sudo add-apt-repository ppa:openjdk-r/ppa`
    2. Update the package list: `sudo apt-get update`
    3. Install Java 8: `sudo apt-get install openjdk-8-jdk`
    4. Update the links in `/etc/alternatives` by selecting java-8-openjdk: 
        * `sudo update-alternatives --config java` 
        * `sudo update-alternatives --config javac`
        * Validate Java version: `java -version` and `javac -version`
    5. Set the `JAVA_HOME` variable:
        * Type `update-alternatives --list java` and copy the **path to the directory** where Java 8 is installed (e.g. `/usr/lib/jvm/java-8-openjdk-amd64`)
        * Type `sudo vim /etc/profile` and append lines:
            * `export JAVA_HOME="<paste the path>"`
            * `export PATH=$JAVA_HOME/bin:$PATH`
        * Save the file
    6. Reboot or type `source /etc/profile` to apply changes
    7. Validate the `JAVA_HOME` variable: `which java` and `which javac`

2. Install Scala 2.12.8:
    1. Download the installer: `sudo wget https://downloads.lightbend.com/scala/2.12.8/scala-2.12.8.deb`
    2. Install the `scala-2.12.8.deb` file: `sudo dpkg -i scala-2.12.8.deb`

### Compilation and execution
1. Create a bin directory:
`mkdir bin`
2. Compile:
`scalac -d bin -cp src/main/scala/*.scala`
3. Run:
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