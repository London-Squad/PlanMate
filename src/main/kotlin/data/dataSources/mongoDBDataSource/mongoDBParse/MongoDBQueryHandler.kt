package data.dataSources.mongoDBDataSource.mongoDBParse

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import logic.exceptions.NotFoundException
import logic.exceptions.RetrievingDataFailureException
import logic.exceptions.StoringDataFailureException
import org.bson.Document
import org.bson.conversions.Bson

class MongoDBQueryHandler(
    private val collection: MongoCollection<Document>
) {

    fun fetchFromCollection(filters: Bson): List<Document> {
        return try {
             collection.find(filters).toList()
        } catch (e: MongoException) {
            throw RetrievingDataFailureException("Failed to retrieve data: ${e.message}")
        }
    }

    fun insertToCollection(document: Document){
        try {
            collection.insertOne(document)
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to add data: ${e.message}")
        }
    }

    fun updateCollection(field: String, newValue: String, filters: Bson){
        try {
            val result = collection.updateOne(filters, Updates.set(field, newValue))
            if (result.matchedCount.toInt() == 0) {
                throw NotFoundException("Entity was not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to update entity: ${e.message}")
        }
    }

    fun softDeleteFromCollection(filters: Bson){
        try {
            val result = collection.updateOne(filters, Updates.set(MongoDBParse.IS_DELETED_FIELD, true)
            )
            if (result.matchedCount.toInt() == 0) {
                throw NotFoundException("Entity was not found")
            }
        } catch (e: MongoException) {
            throw StoringDataFailureException("Failed to delete entity: ${e.message}")
        }
    }
}