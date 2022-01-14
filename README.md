# SRCCodeTest
The data json is an array of objects.
I have used JsonParser of Gson to parse the json and stored the product values in m_products map with key as product code.
The logic for calculating the combination that best suits the given purchased quantity input is present in "calculateOutputUnits".
In "ProcessInput" method we iteratively call calulateOutputUnits method for each packagingOptions unit. ("count" values of packagingOptions array of a product), with the highest count value first.(P)
In "calulateOutputUnits", we check if the purchased quantity is a multiple of the packageUnit, and if yes, we break out of the method and return the value to outputs.
If not, we check if purchased quantity is less than the packageUnit, if yes, we break out of the method and return 0 to the output.
If the purchased quantity is not a multiple and not less than packageUnit, we calculate the remaining quantity of items and iterate with the rest of the packageUnits by recursively calling "calulateOutputUnits" to check if a combination with the initially chosen packageunit(P) is valid.
We return the output value as a map which has the valid combinations of the package units for the given purchased qty.
If a combination with a packageUnit is not possible, we send -1 as the value to the output with that packageUnit as key.

We then iterate through output list to print the output string.
