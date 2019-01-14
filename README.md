# LCSk++ implementation in PYTHON

## Python Installtion 
#### Windows:
###### Step 1: Download the Python 3 Installer
###### Step 2: Run the Installer
###### Step 3: Add Python to the PATH Environmental Variable:
  1. click on the **Start Menu** and right click **My Computer** C

  2. click on **Advanced System Settings**

  3. click on **Environment Variables**

  4. find the **PATH** variable and click **Edit**, then paste the Python path to the end of that string

  5. Save your changes
#### Linux: 
There is a very good chance your Linux distribution has Python installed already, but it probably won’t be the latest version, and it may be Python 2 instead of Python 3 wich is requiered.To find out what version(s) you have, open a terminal window and try the following commands:
* python --version
* python2 --version
* python3 --version
If not installed, install Python 3 using this command in shell:
  * $ sudo apt-get update
  * $ sudo apt-get install python3.6



## Code running 
1. Clone or download repository

2. Open terminal in Fer-Bioinformatics-project directory

3. Run the code in command prompt with the following command: `python main.py <k> <input_file>`
  * input arguments:
    1. `<k>` positive integer
    2. `<input_file>` text file with sequences to analyise
4. When execution is finished the results will be saved to text file with name `input_file-k=<k>`
 
 
 
 
