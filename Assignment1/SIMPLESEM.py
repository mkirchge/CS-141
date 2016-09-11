# Maximillian Kirchgesner (ID: 27238431)
# CS 141 Concepts of Programming Languages
# 1X1 Programming Languages
# SIMPLESEM Interpreter

import sys

def open_file():
    print ""
    print "Program"
    f = open(sys.argv[1], "r")
    contents = f.readlines()
    f.close()
    for i in contents:
        program(i)

def program(line):
    stmt(line)

def stmt(line):
    print "Statement"
    if line[:3] == "set":
        setparse(line)
    elif line[:5] == "jumpt":
        jumptparse(line)
    elif line[:4] == "jump":
        jumpparse(line)
    elif line[:4] == "halt":
        return
    else:
        print "Error parsing line"

def setparse(line):
    print "Set"
    line = line[4:]
    line = line.replace(" ", "")
    line = line.replace("\r", "")
    line = line.replace("\n", "")
    tokens = line.split(',')
    if len(tokens) == 0:
        print "SetError1"
    if tokens[0] != "write":
        expressionparse(tokens[0])
    if len(tokens) < 2:
        print "SetError2"
    if tokens[1] != "read":
        expressionparse(tokens[1])
    if len(tokens) > 2:
        print "SetError3"
    

def jumpparse(line):
    print "Jump"
    line = line[5:]
    line = line.replace(" ", "")
    line = line.replace("\r", "")
    line = line.replace("\n", "")
    expressionparse(line)

def jumptparse(line):
    print "Jumpt"
    line = line.replace("jumpt ", "")
    line = line.replace(" ", "")
    line = line.replace("\r", "")
    line = line.replace("\n", "")
    tokens = line.split(',')
    if len(tokens) < 2:
        print "JumptError"
    else:
        expressionparse(tokens[0])
        newLine = tokens[1]
        if newLine[4] == "!" or newLine[4] == "=" or newLine[4] == ">" or newLine[4] == "<":
            if newLine[4] == "=":
                tk2 = newLine.split("==")
                expressionparse(tk2[0])
                expressionparse(tk2[1])
            if newLine[4] == "!":
                tk2 = newLine.split("!=")
                expressionparse(tk2[0])
                expressionparse(tk2[1])
            if newLine[4] == ">":
                tk2 = newLine.split(">")
                expressionparse(tk2[0])
                expressionparse(tk2[1])
            if newLine[4] == "<":
                tk2 = newLine.split("<")
                expressionparse(tk2[0])
                expressionparse(tk2[1])
            if newLine[4:5] == ">=":
                tk2 = newLine.split(">=")
                expressionparse(tk2[0])
                expressionparse(tk2[1])
            if newLine[4:5] == "<=":
                tk2 = newLine.split("<=")
                expressionparse(tk2[0])
                expressionparse(tk2[1])
            

def expressionparse(line):
    print "Expr"
    if len(line) > 4:
        if line[4] == "+":
            tokens = line.split("+")
            termparse(tokens[0])
            termparse(tokens[1])
        if line[4] == "-":
            tokens = line.split("-")
            termparse(tokens[0])
            termparse(tokens[1])
    else:
        termparse(line)

def termparse(line):
    print "Term"
    if '*' in line:
        tokens = line.split('*')
        factorparse(tokens[0])
        factorparse(tokens[1])
    elif '/' in line:
        tokens = line.split('/')
        factorparse(tokens[0])
        factorparse(tokens[1])
    elif '%' in line:
        tokens = line.split('%')
        factorparse(tokens[0])
        factorparse(tokens[1])
    else:
        factorparse(line)

def factorparse(line):
    print "Factor"
    if len(line) == 0:
        print "FactorError0"
    if line == "0" or line == "1" or line == "2" or line == "3" or line == "4" or line == "5" or line == "6" or line == "7" or line == "8" or line == "9":
        print "Number"
    if line[0] == "D" or line[0] == "(":
        if line[0] == "D":
            line = line.replace("D", "")
            line = line.replace("[", "")
            line = line.replace("]", "")
            expressionparse(line)
        elif line[0] == "(":
            line = line.replace("(", "")
            line = line.replace(")", "")
            expressionparse(line)
        else:
            print "FactorError1"

if __name__ == '__main__':
    open_file()
