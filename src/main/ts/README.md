# Bioinformatics project

Implementation of research paper [_LCSk_++: Practical similarity metric for long strings](https://arxiv.org/pdf/1407.2407.pdf).

[Bioinformatics course website](https://www.fer.unizg.hr/en/course/bio)

## Getting Started

### 1. Prerequisites

#### Node

Installing Node.js via package manager.
Example for Debian/Ubuntu:

```
curl -sL https://deb.nodesource.com/setup_11.x | sudo -E bash -
sudo apt-get install -y nodejs
```

Other distributions: https://nodejs.org/en/download/package-manager/

#### Yarn

Installing Yarn dependency manager via package manager
Example for Debian/Ubuntu:

1. Update sources list

```
curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | sudo apt-key add -
echo "deb https://dl.yarnpkg.com/debian/ stable main" | sudo tee /etc/apt/sources.list.d/yarn.list
```

2. Install Yarn

```
sudo apt-get update && sudo apt-get install yarn
```

Other distributions: https://yarnpkg.com/lang/en/docs/install/

### 2. Compile & Run

1. Clone or download this repository
2. Run `yarn install` in root directory of TypeScript project
3. Run `yarn build` to compile
4. Run `node src/main.js [path to fasta file] [k]`

### 3. Test

1. Run `yarn run-tests` to run all tests
2. Results are output to `/results`
