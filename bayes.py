
"""
Jacob Gold

Written for Fundamentals of Artificial Intelligence
at Brandeis University, taught by James Pustejovsky

Sample network taken from Artificial Intelligence: A Modern Approach
by S. Russell and P. Norvig
Figure 14.2
"""


"""
Code which constructs a Bayesian network. Each node of the network has a conditional probability table,
where the probability of the event represented by the node occuring is based on the assignments of its
parents.

Queries are made using the BNWrapper class, so the network can be reset after each query is performed,
as the queries change the structure of the network.

The network with the alarm defined in the assignment is returned from the method alarmNetwork(). Queries
take the form BNWrapper.query(variable, [(v1, assignment1), (v2, assignment2)...]), where variable is
the variable being queried, and the second argument is a list of pairs of other variables and the values
they are being assigned (second argument is optional). The assignments are represented by + for true and
- for false. This method return a probability table, which can then be printed.

The query for part a can be printed by simply calling the method partA().

The query for part b can be printed by simply calling the method partB().

These functions are at the bottom of the document, and may be useful to reference to see how queries are
formatted.
"""

#A node of the Bayesian Network
class BNNode(object):

    #Constructs a node with a given name, parents, children, and probabilities as entries for the probability table
    def __init__(self, name, parent_list, child_list, probability_list):
        self.name = name
        self.parent_list = parent_list
        self.child_list = child_list
        self.probability_list = probability_list
        self.probability_table = makeProbabilityTable(parent_list, name)
        for i in range(len(self.probability_table)):
            self.probability_table[i] = (self.probability_table[i][0], probability_list[i])
        
    def __str__(self):
        return self.name

    #Returns a copy of the node
    def copyNode(self):
        return BNNode(self.name, copy(self.parent_list), copy(self.child_list), copy(self.probability_list))

#Returns a copy of a list
def copy(l):
    m = list()
    for i in l:
        m.append(i)
    return m

"""
Creates a probability table with a given set of variables, where each probability is 0.
The table is a list of pairs, where each pair consists of a list of positive or negative
variables (strings) and the joint probability of those variables.
"""
def makeProbabilityTable(parent_list, name = ""):
    p_list = parent_list[:]
    if name != "":
        p_list += [name]
    prefixes = ""
    table = list()
    for i in range(len(p_list)):
        prefixes += "+"
    length = pow(2, len(p_list))
    for i in range(length):
        variables = list()
        for i in range(len(p_list)):
            temp = prefixes[len(p_list) - 1 - i] + p_list[i]
            variables.append(temp)
        table.append([variables, 0])
        j = 0
        while j < len(prefixes) and prefixes[j] == "-":
            prefixes = prefixes[:j] + "+" + prefixes[j + 1:]
            j = j + 1
        prefixes = prefixes[:j] + "-" + prefixes[j + 1:]
    return table

#A Bayesian network used to calculate probabilities
class BayesianNetwork(object):

    #Stores the list of BNNodes in a dictionary for easy access
    def __init__(self, node_list):
        self.variables = dict()
        for node in node_list:
            self.variables[node.name] = node

    #Adds a node to the network
    def add(self, node):
        self.variables[node.name] = node

    """
    Joins the probabilities of two nodes. The argument direction will always be the same
    as parent or child, and specifies which one is having its probability table updated.
    """
    def join(self, parent, child, direction):
        parent_node = self.variables[parent]
        child_node = self.variables[child]
        s = set()
        s.update(parent_node.parent_list)
        s.update(child_node.parent_list)
        table = makeProbabilityTable(list(s), child)
        for k in range(len(table)):
            new_variable_list = table[k][0]
            for i in range(len(parent_node.probability_table)):
                p_variable_list = parent_node.probability_table[i][0]
                valid = True
                for v in p_variable_list:
                    if v not in new_variable_list:
                        valid = False
                if valid:
                    for j in range(len(child_node.probability_table)):
                        c_variable_list = child_node.probability_table[j][0]
                        valid = True
                        for v in c_variable_list:
                            if v not in new_variable_list:
                                valid = False
                        if valid:
                            temp = child_node.probability_table[j][1] * parent_node.probability_table[i][1]
                            table = table[:k] + [(table[k][0], temp)] + table[k+1:]
        self.variables[direction].probability_table = table
        self.variables[direction].parent_list = list(s)

    #Sums a variable out of the probability table of a given node
    def marginalize(self, name, variable):
        node = self.variables[name]
        l = node.parent_list
        if variable in l:
            l.remove(variable)
        if name in l:
            l.remove(name)
        table = makeProbabilityTable(l, name)
        for i in range(len(node.probability_table)):
            old_variable_list = node.probability_table[i][0]
            for j in range(len(table)):
                new_variable_list = table[j][0]
                valid = True
                for v in old_variable_list:
                    if v[1:] != variable: 
                        if v not in new_variable_list:
                            valid = False
                if valid:
                    temp = node.probability_table[i][1] + table[j][1]
                    table = table[:j] + [(table[j][0], temp)] + table[j+1:]
        node.probability_table = table

    #Assigns a variable to a specific value
    def assign(self, name, assignment):
        node = self.variables[name]
        node_value = assignment + name
        for i in range(len(node.probability_table)):
            variable_list = node.probability_table[i][0]
            if node_value not in variable_list:
                node.probability_table = node.probability_table[:i] + [(node.probability_table[i][0], 0)] + node.probability_table[i+1:]

    #Normalizes a variable (the variable will always be the last in the list of variables in its own probability table)
    def normalize(self, name):
        node = self.variables[name]
        i = 0
        while i < len(node.probability_table):
            pos_probability = node.probability_table[i][1]
            neg_probability = node.probability_table[i + 1][1]
            total = pos_probability + neg_probability
            pos = pos_probability / total
            neg = neg_probability / total
            node.probability_table = node.probability_table[:i] + [(node.probability_table[i][0], pos)] + [(node.probability_table[i + 1][0], neg)] + node.probability_table[i+2:]
            i = i + 2

    #Joins two nodes, updating the parent, then marginalizes the parent with respect to the child
    def joinUp(self, parent, child):
        self.join(parent, child, parent)
        self.marginalize(parent, child)

    #Joins two nodes, updating the child, then marginalizes the child with respect to the parent
    def joinDown(self, parent, child):
        self.join(parent, child, child)
        self.marginalize(child, parent)


#Stores a BayesianNetwork so it can be restored after each query
class BNWrapper(object):

    #Stores the BayesianNetwork
    def __init__(self, bayesian_network):
        self.bayesian_network = bayesian_network

    #(Deep-ish) Copies the dictionary of the BayesianNetwork so it can be used to reset the network after a query
    def genTemp(self):
        temp = dict()
        for key in self.bayesian_network.variables:
            temp[key] = self.bayesian_network.variables[key].copyNode()
        return temp

    #Returns the probability table of the given variable, with some optional evidence
    def query(self, variable, evidence = []):
        if evidence == []:
            return self.simpleQuery(variable)
        temp = self.genTemp()
        s = set()
        l = list()
        for pair in evidence:
            self.bayesian_network.assign(pair[0], pair[1])
        self.joinUp(variable, [])
        self.bayesian_network.normalize(variable)
        value = self.bayesian_network.variables[variable].probability_table
        self.bayesian_network.variables = temp
        return value
            
                

    #Returns the probability table of the given variable (no optional evidence)
    def simpleQuery(self, variable):
        temp = self.genTemp()
        self.joinDown(variable)
        value = self.bayesian_network.variables[variable].probability_table
        self.bayesian_network.variables = temp
        return value

    """
    Performs a series of joins and marginalizations downwards on all ancestors of variable,
    except those in the avoid list.
    """
    def joinDown(self, variable, avoid = []):
        node = self.bayesian_network.variables[variable]
        for p in node.parent_list:
            if p not in avoid:
                self.joinDown(p)
                self.bayesian_network.joinDown(p, variable)

    """
    Performs a series of joins and marginalizations upwards on all descendants of variable,
    after those variables have been joined and marginalized with all of their ancestors not
    in the path being joined upwards. Useful for updating based on Bayes Rule.
    """
    def joinUp(self, variable, path):
        node = self.bayesian_network.variables[variable]
        self.joinDown(variable, path)
        path.append(variable)
        for c in node.child_list:
            self.joinUp(c, path)
            self.bayesian_network.joinUp(variable, c)

#Explicit test case for part a
def test():
    a = BNNode("Alarm", ["Burglary", "Earthquake"], ["JohnCalls", "MaryCalls"], [.95, .05, .94, .06, .29, .71, .001, .999])
    b = BNNode("Burglary", [], ["Alarm"], [.001, .999])
    e = BNNode("Earthquake", [], ["Alarm"], [.002, .998])
    j = BNNode("JohnCalls", ["Alarm"], [], [.9, .1, .05, .95])
    m = BNNode("MaryCalls", ["Alarm"], [], [.7, .3, .01, .99])
    l = [a, b, e, j, m]
    bn = BayesianNetwork(l)
    bn.assign("JohnCalls", "+")
    bn.assign("MaryCalls", "+")
    print "JOIN JOHNCALLS"
    bn.join("Alarm", "JohnCalls", "Alarm")
    print a.probability_table
    bn.marginalize("Alarm", "JohnCalls")
    print a.probability_table
    print "JOIN MARYCALLS"
    bn.join("Alarm", "MaryCalls", "Alarm")
    print a.probability_table
    bn.marginalize("Alarm", "MaryCalls")
    print ""
    print a.probability_table
    print ""
    print "JOIN EARTHQUAKE"
    bn.join("Earthquake", "Alarm", "Alarm")
    print a.probability_table
    print "MARGINALIZE"
    bn.marginalize("Alarm", "Earthquake")
    print a.probability_table
    print "JOIN BURGLARY"
    bn.join("Burglary", "Alarm", "Burglary")
    print b.probability_table
    bn.marginalize("Burglary", "Alarm")
    print ""
    print b.probability_table
    print ""
    bn.normalize("Burglary")
    print b.probability_table

def alarmNetwork():
    a = BNNode("Alarm", ["Burglary", "Earthquake"], ["JohnCalls", "MaryCalls"], [.95, .05, .94, .06, .29, .71, .001, .999])
    b = BNNode("Burglary", [], ["Alarm"], [.001, .999])
    e = BNNode("Earthquake", [], ["Alarm"], [.002, .998])
    j = BNNode("JohnCalls", ["Alarm"], [], [.9, .1, .05, .95])
    m = BNNode("MaryCalls", ["Alarm"], [], [.7, .3, .01, .99])
    l = [a, b, e, j, m]
    bn = BayesianNetwork(l)
    bnwrapper = BNWrapper(bn)
    return bnwrapper

def partA():
    x = alarmNetwork()
    print x.query("Burglary", [("JohnCalls", "+"), ("MaryCalls", "+")])
    
def partB():
    x = alarmNetwork()
    print x.query("Earthquake", [("JohnCalls", "+")])
