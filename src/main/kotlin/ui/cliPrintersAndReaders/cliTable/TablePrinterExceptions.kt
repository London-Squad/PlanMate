package ui.cliPrintersAndReaders.cliTable

open class InvalidTableInput(message: String="data have different rows with different number of cells"): Exception(message)
class InvalidDataShapeException() : InvalidTableInput("data should not have different rows with different number of cells")
class InvalidHeaderLengthException() : InvalidTableInput("headers size is not equal to the number of columns")
class InvalidColumnWidthLengthException() : InvalidTableInput("columnsWidth size is not equal to the number of cells in each row of data")
class InvalidColumnWidthValuesException() : InvalidTableInput("columnsWidth values should be more than 10")