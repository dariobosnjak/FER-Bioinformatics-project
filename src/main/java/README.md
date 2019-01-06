# Bioinformatics project
Implementation of research paper [*LCSk*++: Practical similarity metric for long strings](https://arxiv.org/pdf/1407.2407.pdf).

[Bioinformatics course website](https://www.fer.unizg.hr/en/course/bio)

## Getting Started

### Prerequisites
* Java 8

### Installing
1. Clone or download this repository
2. Open terminal in `FER-Bioinformatics-project` directory
3. Create directory `FER-Bioinformatics-project/bin`
4. Compile code with the following command:
`javac -d bin -cp src/main/java/ src/main/java/lcsk/LCSKPlusPlus.java`
5. Run code with the following command:
`java -classpath bin/ lcsk.LCSKPlusPlus <input_file> <k>`
    * arguments:
        * `<input_file>` can be a single FASTA file or a folder with one or multiple FASTA files
        * `<k>` must be a positive integer
6. View results in `FER-Bioinformatics-project\data\results\java` directory

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
