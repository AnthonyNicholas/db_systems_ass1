
/*
 *mongo_reorganise_data.js
 * Creates Registered and Deregistered collections and populates with corresponding _ids.  Removes BN_STATUS field from busNames collection. 
 */


conn = new Mongo();
db = conn.getDB("ASIC");

// create new collections to store registration status

db.createCollection("Registered");
db.createCollection("Deregistered");

// Insert _ids into Registered and Deregistered tables and remove BN_STATUS from busNames collection

db.busNames.find({"BN_STATUS":"Registered"}).forEach(function(doc){db.Registered.insert({"_id":doc._id})});
db.busNames.find({"BN_STATUS":"Deregistered"}).forEach(function(doc){db.Deregistered.insert({"_id":doc._id})});
db.busNames.update({},{$unset: {"BN_STATUS":""}}, {multi: true})

