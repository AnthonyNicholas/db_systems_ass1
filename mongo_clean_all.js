conn = new Mongo();
db = conn.getDB("ASIC");
db.regCode.drop();
db.RegCode.drop();
db.busNames.drop();
db.Registered.drop();
db.Deregistered.drop();




