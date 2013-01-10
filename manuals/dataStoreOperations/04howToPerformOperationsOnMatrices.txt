By now you should have already an idea how to perform CRUD operations on matrices: As usual we do GET, POST, PUT and DELETE requests and the commands are stored as static string constants in PsApi class.


To get a list of known matrices you do GET

{url}/PsApi.MATRIX


To get info about matrix with a certain matrixId you do GET

{url}/PsApi.MATRIX/{matrixId}

To delete matrix {matrixId} you do DELETE

{url}/PsApi.MATRIX/{matrixId}


=========================================================================
How do you create a new matrix? Well it is a little bit tricky. 

You have to provide two information about the matrix you want to create: matrix name and matrix type.

As of now we have three basic matrix types, they are 

PsApi.THROUGHPUT = throughput matrix
PsApi.LATENCY = latency matrix
PsApi.TRACEROUTE = traceroute matrix

You have to build a JSONObject for the matrix, it should contain two fierlds: matrix name and matrix type (type must be one of the three known types mentioned above). Then you perform a POST request

{url}/PsApi.MATRIX

with the JSONObject in the data part of the request. Your requested matrix will be created. You will get back full JSONObject of this matrix.

============================================================================


How to modify a matrix.


That depends what you mean by "modify".

There are things you cannot modify. For example if your matrix is of a given type you cannot change it to another. A latency matrix cannot be converted to throughput.

If you want to change attribute like name, then you build JSONObject of this matrix, change the name parameter to something else and put the string representation of JSONObject into data part of PUT request

{url}/PsApi.MATRIX/{matrixId}

and the name will be changed.


If you want to add or remove hosts to matrix, then things get a little bit more complex.

Adding a host to matrix is a complex operation: not only you have to add a new row and column but also create all required services which fill the new row and column. Therefore it is done using a high level command which has a form:

PUT request

{url}/PsApi.MATRIX/{matrixId}/PsApi.MATRIX_ADD_HOST_IDS

where the data part of the request contains a JSONArray object with id's of the hosts you want to put into the matrix.

To remove hosts from mattrix you do a PUT


{url}/PsApi.MATRIX/{matrixId}/PsApi.MATRIX_REMOVE_HOST_IDS

again, host ids come as JSONArray in data part of the request.

The PsApi class gives you commands to add host to a column or to a row only, they are


public static final String MATRIX_ADD_COLUMN_HOST_IDS="addcolumnhostids";
    public static final String MATRIX_REMOVE_COLUMN_HOST_IDS="removecolumnhostids";
    public static final String MATRIX_ADD_ROW_HOST_IDS="addrowhostids";
    public static final String MATRIX_REMOVE_ROW_HOST_IDS="removerowhostids";


however they are not implemented yet.
