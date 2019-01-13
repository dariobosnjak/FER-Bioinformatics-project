# Bioinformatics project
Implementation of research paper [*LCSk*++: Practical similarity metric for long strings](https://arxiv.org/pdf/1407.2407.pdf).

[Bioinformatics course website](https://www.fer.unizg.hr/en/course/bio)

## Getting Started

### 1. Prerequisites
#### Java 8 
1. Open a terminal
2. Add the PPA repository: `sudo add-apt-repository ppa:openjdk-r/ppa`
3. Update the package list: `sudo apt-get update`
4. Install Java 8: `sudo apt-get install openjdk-8-jdk`
5. Update the links in `/etc/alternatives` by selecting java-8-openjdk: 
    * `sudo update-alternatives --config java` 
    * `sudo update-alternatives --config javac`
    * Validate Java version: `java -version` and `javac -version`
6. Set the `JAVA_HOME` variable:
    * Type `update-alternatives --list java` and copy the **path to the directory** where Java 8 is installed (e.g. `/usr/lib/jvm/java-8-openjdk-amd64`)
    * Type `sudo vim /etc/profile` and append lines:
        * `export JAVA_HOME="<paste the path>"`
        * `export PATH=$JAVA_HOME/bin:$PATH`
    * Save the file
7. Reboot to apply changes
8. Validate the `JAVA_HOME` variable: `which java` and `which javac`

### 2. Compile & Run
1. Clone or download this repository
2. Open a terminal in `FER-Bioinformatics-project` directory
3. Run command `mkdir bin`
4. Compile the code with the following command:
`javac -d bin -cp src/main/java/ src/main/java/lcsk/LCSKPlusPlus.java`
5. Run the code with the following command:
`java -classpath bin/ lcsk.LCSKPlusPlus <input_file> <k>`
    * arguments:
        * `<input_file>` can be a single FASTA file or a folder with one or multiple FASTA files
        * `<k>` must be a positive integer
6. View results in `FER-Bioinformatics-project/data/results/java` directory

### 3. Test
1. Open a terminal in `FER-Bioinformatics-project` directory
2. Run: `java -classpath bin/ lcsk.LCSKPlusPlus data/demo/sequence.fasta 10`
3. View results in `FER-Bioinformatics-project/data/results/java` directory

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
