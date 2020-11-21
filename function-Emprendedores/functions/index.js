
const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);
exports.sendAdminNotification = functions.database.ref('/Proyectos/{pushId}').onWrite((change, context) => {
    // var topic = 'News';
    // console.log("referencia",functions.database.ref('/News/{pushId}'))
    console.log("3")
    const news = change.after.val();
    console.log("4")
    console.log("new_", news)
    const payload = {
        notification: {
            title: `${news.categoria}`,
            body: `${news.nombre}`+" - "+`${news.descripcion}`,
            image:`${news.imagen}`,
        }
    }; 
    console.log("payload", payload)
    console.log("4")
    console.log("Valdemar Historia")

    return admin.messaging().sendToTopic("Proyectos", payload)
        .then(function (response) {
            console.log('Notification sent successfully:', response);
        })
        .catch(function (error) {
            console.log('Notification sent failed:', error);
        });
});


exports.sendsuscrito = functions.database.ref('/HistoriasDetalle/count/{pushId}').onWrite((change, context) => {
    // var topic = 'News';
    // console.log("referencia",functions.database.ref('/News/{pushId}'))
    console.log("3")
    const news =  JSON.stringify(change.after.val());
    console.log("4") 
    console.log("new_",news)
    const payload = {
        notification: {
            title: "suscritos"+`${news}`, 
        }
    }; 
    console.log("payload", payload)
    console.log("4")
    console.log("Valdemar Historia")

    return admin.messaging().sendToTopic("sendsuscrito", payload)
        .then(function (response) {
            console.log('Notification sent successfully:', response);
        })
        .catch(function (error) {
            console.log('Notification sent failed:', error);
        });
}); 


exports.sendsuscrito2 = functions.database.ref('/HistoriasDetalle/count_aceptados/{pushId}').onWrite((change, context) => {
    // var topic = 'News';
    // console.log("referencia",functions.database.ref('/News/{pushId}'))
    console.log("3")
    const news =  JSON.stringify(change.after.val());
    console.log("4") 
    console.log("new_",news)
    const payload = {
        notification: {
            title: "aceptados"+`${news}`, 
        }
    }; 
    console.log("payload", payload)
    console.log("4")
    console.log("Valdemar Historia")

    return admin.messaging().sendToTopic("sendsuscrito2", payload)
        .then(function (response) {
            console.log('Notification sent successfully:', response);
        })
        .catch(function (error) {
            console.log('Notification sent failed:', error);
        });
}); 