
/*
 *mongo_reorganise_data.js
 * Creates Registered and Deregistered collections and populates with corresponding _ids.  Removes BN_STATUS field from busNames collection. 
 */


conn = new Mongo();
db = conn.getDB("ASIC");

// arrange structure so that date_info is stored together (ie nested)

db.busNames.find().forEach(function (d) {
    db.busNames.update(
        {
        _id : d._id
        }, 
        {
            $set : 
                {'date_info.BN_REG_DT' : d.BN_REG_DT, 'date_info.BN_CANCEL_DT' : d.BN_CANCEL_DT, 'date_info.BN_REVIEW_DT' : d.BN_REVIEW_DT},
            $unset : {
                'BN_REG_DT' : "", 
                'BN_CANCEL_DT' : "", 
                'BN_REVIEW_DT' : ""
            }
        }
    )});     

// arrange structure so that state_info is stored together (ie nested)
/*
db.busNames.find().forEach(function (d) {
    db.busNames.update({
        _id : d._id
        }, {
        $set : 
            {'state_info.BN_STATE_NUM' : d.BN_STATE_NUM, 'date_info.BN_STATE_OF_REG' : d.BN_STATE_OF_REG, 'date_info.BN_REVIEW_DT'}
        },
        $unset : {
            BN_STATE_NUM : "", 
            BN_STATE_OF_REG : "" 
        })
    });     
*/



/*
db.createCollection("Registered");
db.createCollection("Deregistered");

// Insert _ids into Registered and Deregistered tables and remove BN_STATUS from busNames collection

db.busNames.find({"BN_STATUS":"Registered"}).forEach(function(doc){db.Registered.insert({"_id":doc._id})});
db.busNames.find({"BN_STATUS":"Deregistered"}).forEach(function(doc){db.Deregistered.insert({"_id":doc._id})});
db.busNames.update({},{$unset: {"BN_STATUS":""}}, {multi: true})
*/
