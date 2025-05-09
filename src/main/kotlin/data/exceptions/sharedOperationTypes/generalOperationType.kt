package data.exceptions.sharedOperationTypes

enum class GeneralOperationType { RETRIEVAL, MODIFICATION,CONNECTION,PARSING,CLOSING}
enum class MongoOperationName {
    RETRIEVE_PROJECTS,
    INSERT_PROJECT,
    UPDATE_PROJECT_TITLE,
    UPDATE_PROJECT_DESCRIPTION,
    DELETE_PROJECT,
    CONNECT_TO_MONGO,
    CLOSE_MONGO_CONNECTION
}
enum class CSVOperationName {
    READ_CSV_DATA,
    WRITE_CSV_DATA,
    PARSE_CSV_ROW,
    OPEN_CSV_FILE,
    CLOSE_CSV_FILE,
}

