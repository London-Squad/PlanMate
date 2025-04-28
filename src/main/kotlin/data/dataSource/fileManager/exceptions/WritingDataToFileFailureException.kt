package data.dataSource.fileManager.exceptions

import logic.exceptions.StoringDataFailureException

class WritingDataToFileFailureException(messages: String) : StoringDataFailureException(messages)