package Database
import com.mongodb.client.MongoClients

class DatabaseUtil {
    // Ustvari povezavo do MongoDB strežnika
    val client = MongoClients.create("mongodb://localhost:27017")
    // Izberi bazo in kolekcijo
    val database = client.getDatabase("kaput")

}