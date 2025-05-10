package logic.planeMateException

open class StoringDataFailureException(message: String) : PlaneMateException(message)

class FileIOException(message: String) : StoringDataFailureException(message)