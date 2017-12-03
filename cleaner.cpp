#include <iostream>
#include <vector>
#include <string>
#include <fstream>


std::string					instructions(const std::string& line)
{
    std::string				linedup;

    for (int i = 0; i < (int)line.length(); i++) {
        if (line[i] == '#')
            break ;
        linedup += line[i];
    }
    return (linedup);
}

std::vector<std::string>		start(const std::string& argv)
{
    std::ifstream				file;
    
    file.open(argv);
    if (file.fail()) {
        std::cout << "Error: could not open file!" << std::endl;
        return std::vector<std::string>();
    }
    
    std::string line;
    std::vector<std::string> stringarray;

    while (!file.eof()) {
        std::getline(file, line);
        line = instructions(line);
        if (!line.empty())
            stringarray.emplace_back(line); //use emplace_back and compile with -std=c++14
    }
    file.close();
    return stringarray;
}

int 							main(int argc, char **argv)
{

    if (argc > 1) {
		std::ofstream file;
		std::string name = (std::string)argv[1];
		std::vector<std::string> array;
		
		try {
			array = start(name);
			
			file.open(name);
			
			for (auto line: array) {
				file << line << std::endl;
			}
			file.close();
			return (0);
		}
		catch (...) {
			return (1);
		}
	}
}